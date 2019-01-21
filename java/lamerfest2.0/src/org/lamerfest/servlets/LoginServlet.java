/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.servlets;

import java.io.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.lamerfest.user.*;
import javax.ejb.FinderException;

/**
   <h4>LoginServlet</h4>
   Simply logs users in the Lamerfest 2.0 system.
 */

public class LoginServlet extends XSLEnabledServlet {

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String action = req.getParameter("action");
	String username = req.getParameter("user");
	String password = req.getParameter("pwd");
	String targetURL = req.getParameter("targetURL");

	String referer = req.getHeader("HTTP_REFERER");

	boolean badLogin = false;
	boolean systemError = false;

	if(action != null && action.equals("login") && username != null && password != null){
	    try {
		UserHome home = (UserHome)new InitialContext().lookup("user/UserBean");
		User user = home.findByPrimaryKey(username);
		UserData ud = user.getUserData();
		if(!ud.password.equals(password)){
		    badLogin = true;
		} else {
		    // login ok, create session, and include UserData
		    HttpSession session = req.getSession(true);
		    session.setAttribute("lamerfest.UserData", ud);
		    if(targetURL != null) resp.sendRedirect(targetURL);
		    else resp.sendRedirect("/index");
		    return;
		}
	    } catch(FinderException fe){
		badLogin = true;
	    } catch(NamingException ne){
		systemError = true;
	    }
	}

	StringWriter sw = new StringWriter();
	PrintWriter xml = new PrintWriter(sw);

	addStart(xml, req, "Login");

	if(targetURL != null){
	    xml.println("<targetURL>" + targetURL  + "</targetURL>");
	} else if(referer != null){
	    xml.println("<targetURL>" + referer  + "</targetURL>");
	}

	if(username != null) xml.println("<user>" + username  + "</user>");
	if(password != null) xml.println("<pwd>" + password  + "</pwd>");

	if(badLogin) xml.println("<error>Loggaus ei onnistunut, tarkista tunnus ja salasana.</error>");
	if(systemError) xml.println("<error>Systeemi kusahti ..</error>");

	addEnd(xml);

	transform(new StringReader(sw.getBuffer().toString()), "login.xsl", resp);

    }

}
