/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.servlets;

import java.io.*;
import javax.ejb.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.lamerfest.info.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
   <h4>InfoTVServlet</h4>
   Prints out news items in xml so that
   they can be displayed on the bigscreen(TM).
 */

public class InfoTVServlet extends XSLEnabledServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	// initialize xml stuff
	StringWriter sw = new StringWriter();
	PrintWriter xml = new PrintWriter(sw);
	addStart(xml, req, "InfoTV");

	try {
	    InfoManagerHome home = (InfoManagerHome)new InitialContext().lookup("info/InfoManagerBean");
	    InfoManager infoMgr = home.create();
	    InfoItem[] all = infoMgr.findAll();
	    xml.println("<items>");
	    for(int ee = 0; ee < all.length; ee++){
		xml.println("<item>");
		xml.println("<id>" + all[ee].id + "</id>");
		xml.println("<heading>" + all[ee].heading + "</heading>");
		xml.println("<content><![CDATA[" + all[ee].content + "]]></content>");
		xml.println("</item>");
	    }
	    xml.println("</items>");
	} catch(Exception e){
	    xml.println("<error>VIHRE! (" + e + ")</error>");
	}

	// add the end of the xml and transform
	addEnd(xml);
	transform(new StringReader(sw.getBuffer().toString()), "infotv.xsl", resp);

    }

}
