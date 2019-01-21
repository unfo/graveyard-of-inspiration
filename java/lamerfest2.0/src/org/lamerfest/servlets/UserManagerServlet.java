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
import org.lamerfest.user.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
   <h4>UserManagerServlet</h4>
   Uses UserManager session bean to manage
   users in the Lamerfest 2.0's J2EE system.
 */

public class UserManagerServlet extends XSLEnabledServlet {

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
	    resp.sendRedirect("/login?targetURL=/usermanager");
	    return;
	}

	if(!isAdmin){
	    resp.sendRedirect("/");
	    return;
	}

	// initialize xml stuff
	StringWriter sw = new StringWriter();
	PrintWriter xml = new PrintWriter(sw);
	addStart(xml, req, "Käyttäjänhallinta");

	// for testing ejb speed
	Date ejbStart = new Date();

	// try to get UserManager ejb home
	boolean ejbOk = false;
	UserManagerHome home = null;
	UserManager userMgr = null;
	try {
	    home = (UserManagerHome)new InitialContext().lookup("user/UserManagerBean");
	    userMgr = home.create();
	    ejbOk = true;
	} catch(NamingException ne){
	    xml.println("<error>Pavut on kadoksissa!</error>");
	} catch(CreateException ce){
	    xml.println("<error>UserManagerHome ei lähtenyt käyntiin.</error>");
	}

	if(!ejbOk){
	    // duh. we'll stop here.
	    addEnd(xml);
	    transform(new StringReader(sw.getBuffer().toString()), "usermanager.xsl", resp);
	    return;
	}

	// get parameters we're interested in
	String action = req.getParameter("action");
	String userID = req.getParameter("userID");
	String password = req.getParameter("password");
	String realName = req.getParameter("realName");
	String levelStr = req.getParameter("level");

	if(action != null){
	    if(action.equals("update")){
		if(
		   userID != null && userID.length() > 0 &&
		   password != null && password.length() > 0 &&
		   realName != null && realName.length() > 0 &&
		   levelStr != null && getLevel(levelStr) != -1
		   ){
		    try {
			userMgr.updateUser(userID, password, realName, getLevel(levelStr));
			xml.println("<message>Päivitettiin käyttäjän " + userID + " tiedot.</message>");
		    } catch(FinderException fe){
			xml.println("<error>Käyttäjää " + userID + " ei löytynyt kannasta, vaikea päivittää tietoja</error>");
		    }
		} else
		    xml.println("<error>Tietoja ei voitu päivittää, tarkista kentät!</error>");
	    } else if(action.equals("add")){
		if(
		   userID != null && userID.length() > 0 &&
		   password != null && password.length() > 0 &&
		   realName != null && realName.length() > 0 &&
		   levelStr != null && getLevel(levelStr) != -1
		   ){
		    try {
			userMgr.addUser(userID, password, realName, getLevel(levelStr));
			xml.println("<message>Luotiin käyttäjä " + userID + ".</message>");
		    } catch(UserExistsException uee){
			xml.println("<error>Käyttäjä " + userID + " löytyy jo kannasta.</error>");
		    }
		} else
		    xml.println("<error>Käyttäjää ei voitu luoda, tarkista kentät!</error>");
	    } else if(action.equals("delete")){
		if(userID != null && userID.length() > 0){
		    try {
			userMgr.deleteUser(userID);
			xml.println("<message>Tuhottiin käyttäjä " + userID + ".</message>");
		    } catch(RemoveException re){
			xml.println("<error>Käyttäjää " + userID + " ei voitu poistaa! (" + re + ")</error>");
		    }
		} else
		    xml.println("<error>userID = null ... hmmm</error>");
	    }
	}

	try {
	    // list all users
	    UserData[] allUsers = userMgr.findAll();
	    xml.println("<users>");
	    for(int ii = 0; ii < allUsers.length; ii++){
		UserData ud = allUsers[ii];
		xml.println("<user>");
		xml.println("<userID>" + ud.userID + "</userID>");
		xml.println("<password>" + ud.password + "</password>");
		xml.println("<realName>" + ud.realName + "</realName>");
		xml.println("<level>" + ud.level + "</level>");
		xml.println("</user>");
	    }
	    xml.println("</users>");
	} catch(FinderException fe){
	    xml.println("<error>Ei löytynyt käyttäjiä (?!?!)</error>");
	}

	xml.println("<ejbProcessingTime>" + (new Date().getTime() - ejbStart.getTime()) + "</ejbProcessingTime>");

	// add the end of the xml and transform
	addEnd(xml);
	transform(new StringReader(sw.getBuffer().toString()), "usermanager.xsl", resp);

    }

    /**
       Parses the given String to an int.
       @param level the String to be parsed
       @return retVal -1 if not a number, else the int value of the String
     */
    private static int getLevel(String level){
	int retVal = -1;
	try {
	    retVal = Integer.parseInt(level);
	} catch(NumberFormatException nfe){}
	return retVal;
    }

}
