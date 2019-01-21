/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.servlets;

import java.io.*;
import javax.ejb.*;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import org.lamerfest.info.*;
import javax.naming.InitialContext;
import org.lamerfest.user.UserData;
import javax.naming.NamingException;

/**
   <h4>InfoManagerServlet</h4>
   Uses InfoManager session bean to manage
   news in the Lamerfest 2.0's J2EE system.
 */

public class InfoManagerServlet extends XSLEnabledServlet {

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	// first we authenticate
	boolean isAdmin = false;
	HttpSession session = req.getSession(false);
	if(session != null){
	    Object obj = session.getAttribute("lamerfest.UserData");
	    if(obj != null){
		UserData ud = (UserData)obj;
		if(ud.level == 10) isAdmin = true;
	    }
	} else {
	    resp.sendRedirect("/login?targetURL=/infomanager");
	    return;
	}

	if(!isAdmin){
	    resp.sendRedirect("/");
	    return;
	}

	// initialize xml stuff
	StringWriter sw = new StringWriter();
	PrintWriter xml = new PrintWriter(sw);
	addStart(xml, req, "Uutishallinta");

	// for testing ejb speed
	Date ejbStart = new Date();

	// try to get InfoManager ejb home
	boolean ejbOk = false;
	InfoManagerHome home = null;
	InfoManager infoMgr = null;
	try {
	    home = (InfoManagerHome)new InitialContext().lookup("info/InfoManagerBean");
	    infoMgr = home.create();
	    ejbOk = true;
	} catch(NamingException ne){
	    xml.println("<error>Pavut on kadoksissa!</error>");
	} catch(CreateException ce){
	    xml.println("<error>InfoManagerHome ei lähtenyt käyntiin.</error>");
	}

	if(!ejbOk){
	    // duh. we'll stop here.
	    addEnd(xml);
	    transform(new StringReader(sw.getBuffer().toString()), "infomanager.xsl", resp);
	    return;
	}

	// all ok, let's do some business
	String action = req.getParameter("action");
	String id = req.getParameter("id");
	String heading = req.getParameter("heading");
	String content = req.getParameter("content");

	if(action != null){
	    if(action.equals("delete")){
		if(id != null && getInt(id) != -1){
		    try {
			infoMgr.delete(getInt(id));
			xml.println("<message>Tuhottiin uutinen " + id + ".</message>");
		    } catch(RemoveException re){
			xml.println("<error>Uutista " + id + " ei saatu tuhottua ... hmmm .. (" + re + ").</error>");
		    }
		} else xml.println("<error>hmmm, action=delete, mutta id:tä ei vaan ole</error>");
	    } else if(action.equals("add")){
		if(
		   heading != null && heading.length() > 0 &&
		   content != null && content.length() > 0
		   ){
		    try {
			infoMgr.add(heading, content);
			xml.println("<message>Uutinen lisätty</message>");
		    } catch(CreateException ce){
			xml.println("<error>Uutista ei voitu lisätä .. hmm .. (" + ce + ")</error>");
		    }
		} else xml.println("<error>Et antanut tarpeeksi tietoja uutisen lisäämistä varten.</error>");
	    } else if(action.equals("update")){
		if(
		   heading != null && heading.length() > 0 &&
		   content != null && content.length() > 0 &&
		   id != null && getInt(id) != -1
		   ){
		    try {
			infoMgr.update(new InfoItem(getInt(id), heading, content));
			xml.println("<message>Uutinen muokattu</message>");
		    } catch(FinderException fe){
			xml.println("<error>Uutista ei löydy kannasta, sitä ei voitu myöskään muokata. (ylläri)</error>");
		    }
		} else xml.println("<error>Uutista ei voitu päivittää, tarkista kentät.</error>");
	    }
	}

	// finally list all the items
	try {
	    InfoItem[] all = infoMgr.findAll();
	    xml.println("<items>");
	    for(int ee = 0; ee < all.length; ee++){
		xml.println("<item>");
		xml.println("<id>" + all[ee].id + "</id>");
		xml.println("<heading>" + all[ee].heading + "</heading>");
		xml.println("</item>");
	    }
	    xml.println("</items>");
	} catch(FinderException fe){
	    xml.println("<error>Uutisia ei saatu listattua, prkl.. (" + fe + ")</error>");
	}

	xml.println("<ejbProcessingTime>" + (new Date().getTime() - ejbStart.getTime()) + "</ejbProcessingTime>");

	// add the end of the xml and transform
	addEnd(xml);
	transform(new StringReader(sw.getBuffer().toString()), "infomanager.xsl", resp);

    }

    /**
       Parses the given String to an int.
       @param str the String to be parsed
       @return retVal -1 if not a number, else the int value of the String
     */
    private static int getInt(String str){
	int retVal = -1;
	try {
	    retVal = Integer.parseInt(str);
	} catch(NumberFormatException nfe){}
	return retVal;
    }

}
