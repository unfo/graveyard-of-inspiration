/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.servlets;

import java.io.*;
import javax.servlet.*;
import java.util.Hashtable;
import javax.servlet.http.*;

/**
   <h4>StaticServlet</h4>
   This servlet serves static xml-files.
   <p>
   Requests for different xml-files can be
   mapped to StaticServlet (e.g. *.xml =>
   StaticServlet) which then transforms them.
 */

public class StaticServlet extends XSLEnabledServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String path = req.getRequestURI();
	File inputFile = new File("/lamerfest/dev/webroot", path);

	if(!inputFile.isFile()){
	    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found: " + inputFile.getAbsolutePath());
	    return;
	}

	StringWriter sw = new StringWriter();
	PrintWriter xml = new PrintWriter(sw);

	addStart(xml, req, "");

	Hashtable params = new Hashtable();
	params.put("staticFileName", path);

	String xsl = "static.xsl";
	String pathInfo = req.getQueryString();
	if(pathInfo != null)
	    xsl = pathInfo;

	addEnd(xml);

	transform(new StringReader(sw.getBuffer().toString()), xsl, resp, params);

    }

}
