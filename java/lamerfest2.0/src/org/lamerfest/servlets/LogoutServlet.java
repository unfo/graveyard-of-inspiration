/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.servlets;

import javax.servlet.*;
import java.io.IOException;
import javax.servlet.http.*;

/**
   <h4>LogoutServlet</h4>
   For once! Something really simple :)
 */

public class LogoutServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	HttpSession session = req.getSession(false);
	if(session != null){
	    session.invalidate();
	}

	resp.sendRedirect("/index");

    }

}
