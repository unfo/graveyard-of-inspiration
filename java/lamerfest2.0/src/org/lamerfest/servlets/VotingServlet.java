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
import org.lamerfest.voting.*;
import javax.naming.InitialContext;
import org.lamerfest.user.UserData;
import javax.naming.NamingException;

/**
   <h4>VotingServlet</h4>
   A separate servlet for actual voting.
 */

public class VotingServlet extends XSLEnabledServlet {

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	// first we authenticate
	boolean isLoggedIn = false;
	boolean isAdmin = false;
	String userID = null;
	HttpSession session = req.getSession(false);
	if(session != null){
	    Object obj = session.getAttribute("lamerfest.UserData");
	    if(obj != null){
		isLoggedIn = true;
		UserData ud = (UserData)obj;
		if(ud.level == 10) isAdmin = true;
		userID = ud.userID;
	    }
	}

	if(!isLoggedIn){
	    resp.sendRedirect("/login?targetURL=/voting");
	    return;
	}

	// initialize xml stuff
	StringWriter sw = new StringWriter();
	PrintWriter xml = new PrintWriter(sw);
	addStart(xml, req, "Compo-‰‰nestys");

	// for testing ejb speed
	Date ejbStart = new Date();

	// try to get VotingManager ejb home
	boolean ejbOk = false;
	VotingManagerHome home = null;
	VotingManager voting = null;
	try {
	    home = (VotingManagerHome)new InitialContext().lookup("voting/VotingManagerBean");
	    voting = home.create();
	    ejbOk = true;
	} catch(NamingException ne){
	    xml.println("<error>Pavut on kadoksissa!</error>");
	} catch(CreateException ce){
	    xml.println("<error>InfoManagerHome ei l‰htenyt k‰yntiin.</error>");
	}

	if(!ejbOk){
	    // duh. we'll stop here.
	    addEnd(xml);
	    transform(new StringReader(sw.getBuffer().toString()), "voting.xsl", resp);
	    return;
	}

	// ready for business.
	String action = req.getParameter("action");
	String compoID = req.getParameter("compoID");
	String entryID = req.getParameter("entryID");

	xml.println("<data>");

	if(action != null && action.equals("getEntries") && compoID != null && getInt(compoID) != -1){
	    try {
		EntryData[] entryData = voting.getEntries(getInt(compoID));
		xml.println("<entries compoID=\"" + compoID + "\">");
		for(int pp = 0; pp < entryData.length; pp++){
		    EntryData ed = entryData[pp];
		    xml.println("<entry>");
		    xml.println("<entryID>" + ed.entryID + "</entryID>");
		    xml.println("<entryName>" + ed.entryName + "</entryName>");
		    xml.println("<entryDesc>" + ed.entryDesc + "</entryDesc>");
		    xml.println("<entryAuthor>" + ed.entryAuthor + "</entryAuthor>");
		    xml.println("</entry>");
		}
		xml.println("</entries>");
	    } catch(Exception e){
		xml.println("<error>Virhe etsitt‰ess‰ tuotoksia: " + e + "</error>");
	    }
	} else if(action != null && action.equals("vote") && compoID != null && getInt(compoID) != -1){
	    try {
		EntryData[] entryData = voting.getEntries(getInt(compoID));
		xml.println("<entries compoID=\"" + compoID + "\" vote=\"true\">");
		for(int pp = 0; pp < entryData.length; pp++){
		    EntryData ed = entryData[pp];
		    xml.println("<entry>");
		    xml.println("<entryID>" + ed.entryID + "</entryID>");
		    xml.println("<entryName>" + ed.entryName + "</entryName>");
		    xml.println("<entryDesc>" + ed.entryDesc + "</entryDesc>");
		    xml.println("<entryAuthor>" + ed.entryAuthor + "</entryAuthor>");
		    xml.println("</entry>");
		}
		xml.println("</entries>");
	    } catch(Exception e){
		xml.println("<error>Virhe etsitt‰ess‰ ‰‰nestett‰vi‰ tuotoksia: " + e + "</error>");
	    }
	} else if(action != null && action.equals("getResults") && compoID != null && getInt(compoID) != -1){
	    try {
		CompoResults cr = voting.getResults(getInt(compoID), isAdmin);
		xml.println("<results compoID=\"" + compoID + "\">");
		for(int pp = 0; pp < cr.entryData.length; pp++){
		    EntryData ed = cr.entryData[pp];
		    xml.println("<entry>");
		    xml.println("<entryID>" + ed.entryID + "</entryID>");
		    xml.println("<entryName>" + ed.entryName + "</entryName>");
		    xml.println("<entryDesc>" + ed.entryDesc + "</entryDesc>");
		    xml.println("<entryAuthor>" + ed.entryAuthor + "</entryAuthor>");
		    xml.println("<voteCount>" + ed.voteCount + "</voteCount>");
		    xml.println("</entry>");
		}
		xml.println("</results>");
	    } catch(BrowsingNotAllowedException bnae){
		xml.println("<error>Tulokset eiv‰t ole viel‰ selattavissa!</error>");
	    } catch(Exception e){
		xml.println("<error>Virhe etsitt‰ess‰ tuloksia: " + e + "</error>");
	    }
	} else if(action != null && action.equals("castVote") &&
		  compoID != null && getInt(compoID) != -1 &&
		  entryID != null && getInt(entryID) != -1){
	    // the voting itself!
	    try {
		voting.castVote(userID, getInt(compoID), getInt(entryID));
		xml.println("<message>ƒ‰nesi on kirjattu!!</message>");
	    } catch(AlreadyVotedException ave){
		xml.println("<error>Olet jo ‰‰nest‰nyt t‰ss‰ compossa! Soo-soo!</error>");
	    } catch(VotingNotAllowedException vnae){
		xml.println("<error>T‰ss‰ compossa ei voi ‰‰nest‰‰ viel‰!</error>");
	    } catch(Exception e){
		xml.println("<error>Virhe: " + e + "</error>");
	    }
	} else {
	    // default is to list compos
	    try {
		CompoData[] compos = voting.getCompos();
		xml.println("<compos>");
		for(int uu = 0; uu < compos.length; uu++){
		    CompoData cd = compos[uu];
		    xml.println("<compo>");
		    xml.println("<compoID>" + cd.compoID + "</compoID>");
		    xml.println("<compoName>" + cd.compoName + "</compoName>");
		    xml.println("<compoDesc>" + cd.compoDesc + "</compoDesc>");
		    xml.println("<votingActive>" + cd.votingActive + "</votingActive>");
		    xml.println("<browsable>" + cd.browsable + "</browsable>");
		    xml.println("</compo>");
		}
		xml.println("</compos>");
	    } catch(Exception e){
		xml.println("<error>Virhe etsitt‰ess‰ compoja (" + e + ")</error>");
	    }
	}

	xml.println("</data>");

	xml.println("<ejbProcessingTime>" + (new Date().getTime() - ejbStart.getTime()) + "</ejbProcessingTime>");

	// add the end of the xml and transform
	addEnd(xml);
	transform(new StringReader(sw.getBuffer().toString()), "voting.xsl", resp);

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
