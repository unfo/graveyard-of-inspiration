/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.servlets;

import java.io.*;
import java.net.URL;
import org.xml.sax.*;
import java.util.Date;
import javax.servlet.*;
import java.util.Hashtable;
import javax.servlet.http.*;
import com.jclark.xsl.sax.*;
import java.util.Enumeration;
import org.lamerfest.user.UserData;

/**
   <h4>XSLEnabledServlet</h4>
   A servlet that enables other servlets to easily
   transform the xml content they've produced.
   Basically the super class of other servlets in
   the org.lamerfest.servlets-package.
   XSLEnabledServlet has an internal stylesheet cache
   for fast transformations.
 */

public class XSLEnabledServlet extends HttpServlet {

    // The cache that will be used for all stylesheets.
    // Maps filenames to XSLProcessor instances.
    static Hashtable cache = new Hashtable();

    // statistics
    static int transformed, transformedOk;
    static long totalTime;

    // the root dir, under which all stylesheets are placed
    private static String stylesheetRoot;

    public void init(ServletConfig conf) throws ServletException {
	System.out.println("[XSLEnabledServlet] init");
	// find out our xsl root
	stylesheetRoot = conf.getServletContext().getInitParameter("stylesheetRoot");
	if(stylesheetRoot == null) throw new ServletException("XSL file root not specified");
	File xslRoot = new File(stylesheetRoot);
	if(!xslRoot.isDirectory() || !xslRoot.exists()) throw new ServletException("XSL file root not found / is not a directory (" + stylesheetRoot + ")");
    }

    /**
       Transforms the xml-content provided by
       the reader to the output stream of the
       HttpServletResponse.
       @param reader the reader to be read from
       @param xslFile the xsl stylesheet file
       @param resp the servlet response
       @param params a hashtable with params to be passed to the xsl-file
     */
    protected void transform(Reader reader, String xslFileName, HttpServletResponse resp, Hashtable params) throws IOException {

	transformed++;
	Date then = new Date();

	System.out.println("[XSLEnabledServlet] \\ Starting transformation, cache size: " + cache.size());

	File xslFile = new File(stylesheetRoot, xslFileName);
	if(xslFile.isDirectory() || !xslFile.exists()){
	    ServletOutputStream out = resp.getOutputStream();
	    out.println("Error: not a valid stylesheet");
	    return;
	}

	try {

	    // the processor to be used in the transform
	    CachedXSLProcessor processor;

	    // check if the xsl-file is already in the cache
	    if(cache.containsKey(xslFileName)){
		// already in the cache, check whether
		// the file has changed
		CachedXSLProcessor cached = (CachedXSLProcessor)cache.get(xslFileName);
		if(cached.hasChanged()){
		    System.out.println("[XSLEnabledServlet]  | " + xslFileName + " has changed, reloading");
		    cached.setParser(createParser());
		    cached.load();
		} else System.out.println("[XSLEnabledServlet]  | using " + xslFileName + " from cache");
		processor = (CachedXSLProcessor)cached.clone();
	    } else {
		// xsl file not in cache, load it
		System.out.println("[XSLEnabledServlet]  | " + xslFileName + " NOT in cache, load it");
		processor =  new CachedXSLProcessor(xslFile);
		processor.setParser(createParser());
		processor.load();
		// Since we're this far, the xsl
		// stylesheet is valid, add it to the cache.
		// Synchronize access just in case
		synchronized(cache){
		    cache.put(xslFileName, processor);
		}
	    }
	    
	    processor.setParser(createParser());
	    OutputMethodHandlerImpl outputMethodHandler = new OutputMethodHandlerImpl(processor);
	    processor.setOutputMethodHandler(outputMethodHandler);
	    outputMethodHandler.setDestination(new ServletDestination(resp));

	    // set params
	    if(params != null && params.size() > 0){
		Enumeration keys = params.keys();
		Enumeration values = params.elements();
		while(keys.hasMoreElements())
		    processor.setParameter((String)keys.nextElement(), (String)values.nextElement());
	    }
	    
	    // cross your fingers
	    processor.parse(new InputSource(reader));

	    transformedOk++;
	    long timeItTook = new Date().getTime() - then.getTime();
	    totalTime += timeItTook;

	    System.out.println("[XSLEnabledServlet] / done in " + timeItTook + " msecs");

	    if(transformedOk % 10 == 0){
		// on every 5th time show stats
		System.out.println(
				   "[XSLEnabledServlet]\n\ttransformation stats:\n\t" + 
				   transformed + " transformations started\n\t" +
				   transformedOk + " of them finished ok\n\t" +
				   (1.0 * totalTime / transformedOk) + " msecs avg. time"
				   );
	    }
	    
	} catch(SAXException saxe){
	    ServletOutputStream out = resp.getOutputStream();
	    out.println("Looks like someone doesn't know how to do proper xml:\nSAX Error: " + saxe);
	} catch(IOException ioe){
	    ServletOutputStream out = resp.getOutputStream();
	    out.println("I/O Error: " + ioe);
	} catch(Exception e){
	    ServletOutputStream out = resp.getOutputStream();
	    out.println("Error: " + e);
	}

    }

    /**
       Transforms the xml-content provided by
       the reader to the output stream of the
       HttpServletResponse.
       @param reader the reader to be read from
       @param xslFile the xsl stylesheet file
       @param resp the servlet response
     */
    protected void transform(Reader reader, String xslFileName, HttpServletResponse resp) throws IOException {
	transform(reader, xslFileName, resp, null);
    }

    /**
       Returns the given file as an InputSource.
       Code taken directly from J. Clark's XSLServlet.
     */
    static InputSource fileInputSource(File file){

	String path = file.getAbsolutePath();
	String fSep = System.getProperty("file.separator");
	if (fSep != null && fSep.length() == 1)
	    path = path.replace(fSep.charAt(0), '/');
	if (path.length() > 0 && path.charAt(0) != '/')
	    path = '/' + path;
	try {
	    return new InputSource(new URL("file", "", path).toString());
	} catch(java.net.MalformedURLException e) {
	    /* According to the spec this could only happen if the file
	       protocol were not recognized. */
	    throw new Error("unexpected MalformedURLException");
	}

    }

    /**
       Returns a SAX XML parser.
       Taken from J. Clark's code as well.
     */
    static Parser createParser() throws Exception {
	return (Parser)Class.forName("com.jclark.xml.sax.CommentDriver").newInstance();
    }

    /**
       A simple inner class that represents
       a cached stylesheet. The hasChanged-method
       can be used to determine whether the xsl
       stylesheet has changed since the last time
       it was parsed.
    */
    class CachedXSLProcessor extends XSLProcessorImpl {

	private Date loaded = null;
	private File xsl = null;

	public CachedXSLProcessor(File f){
	    super();
	    xsl = f;
	}

	public boolean hasChanged(){
	    if(loaded == null || xsl == null) return true; // hmmm ... shouldn't happen
	    return xsl.lastModified() - loaded.getTime() > 0;
	}

	public void load() throws SAXException, IOException {
	    super.loadStylesheet(fileInputSource(xsl));
	    loaded = new Date();
	}

    }


    /**
       Adds the start of the XML
     */
    protected void addStart(PrintWriter xml, HttpServletRequest req, String title){

	xml.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
	xml.println("<lamerfest>");

	HttpSession session = req.getSession(false);
	if(session != null){
	    Object obj = session.getAttribute("lamerfest.UserData");
	    if(obj != null){
		// user is logged in.
		UserData ud = (UserData)obj;
		xml.println("<user>");
		xml.println("<userID>" + ud.userID + "</userID>");
		xml.println("<password>" + ud.password + "</password>");
		xml.println("<realName>" + ud.realName + "</realName>");
		xml.println("<level>" + ud.level + "</level>");
		xml.println("</user>");
	    }
	}

	xml.println("<application title=\"" + title  + "\">");

    }

    /**
       Adds the end tags.
     */
    protected void addEnd(PrintWriter xml){

	xml.println("</application>");
	xml.println("</lamerfest>");

    }

}
