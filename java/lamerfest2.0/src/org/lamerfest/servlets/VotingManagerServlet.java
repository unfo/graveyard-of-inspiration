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
   <h4>VotingManagerServlet</h4>
   Uses VotingManager session bean to manage
   voting (compos, entries etc.) in the Lamerfest 2.0's J2EE system.
 */

public class VotingManagerServlet extends XSLEnabledServlet {

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
	    resp.sendRedirect("/login?targetURL=/votingmanager");
	    return;
	}

	if(!isAdmin){
	    resp.sendRedirect("/");
	    return;
	}

	// initialize xml stuff
	StringWriter sw = new StringWriter();
	PrintWriter xml = new PrintWriter(sw);
	addStart(xml, req, "ƒ‰nestyshallinta");

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
	    transform(new StringReader(sw.getBuffer().toString()), "votingmanager.xsl", resp);
	    return;
	}

	// ready for business.
	String action = req.getParameter("action");

	String compoID = req.getParameter("compoID");
	String compoName = req.getParameter("compoName");
	String compoDesc = req.getParameter("compoDesc");
	String votingActive = req.getParameter("votingActive");
	String browsable = req.getParameter("browsable");

	String entryID = req.getParameter("entryID");
	String entryName = req.getParameter("entryName");
	String entryDesc = req.getParameter("entryDesc");
	String entryAuthor = req.getParameter("entryAuthor");

	boolean showEntries = false;
	int activeCompo = -1;

	xml.println("<data>");

	if(action != null){
	    if(action.equals("addCompo")){
		if(compoName != null && compoName.length() > 0 &&
		   compoDesc != null && compoDesc.length() > 0
		   ){
		    try {
			voting.addCompo(new CompoData(-1, compoName, compoDesc, false, false));
			xml.println("<message>Compo lis‰tty kantaan.</message>");
		    } catch(Exception e){
			xml.println("<error>Virhe lis‰tt‰ess‰ kantaan: " + e + "</error>");
		    }
		} else xml.println("<error>Virhe lis‰tt‰ess‰ compoa, t‰yt‰ molemmat kent‰t.</error>");
	    } else if(action.equals("removeCompo")){
		if(compoID != null && getInt(compoID) != -1){
		    try {
			voting.removeCompo(getInt(compoID));
			xml.println("<message>Compo #" + compoID + " poistettu</message>");
		    } catch(Exception e){
			xml.println("<error>Virhe poistettaessa compoa! (" +e+ ")</error>");
		    }
		} else xml.println("<error>Virhe poistettaessa compoa, invaliidit parametrit.</error>");
	    } else if(action.equals("resetVotes")){
		if(compoID != null && getInt(compoID) != -1){
		    try {
			voting.resetVotes(getInt(compoID));
			xml.println("<message>Compon #" + compoID + " ‰‰nestystilanne nollattu</message>");
		    } catch(Exception e){
			xml.println("<error>Virhe nollattaessa compon #" + compoID + " ‰‰ni‰! (" +e+ ")</error>");
		    }
		} else xml.println("<error>Virhe nollattaessa compon #" + compoID + " ‰‰ni‰, invaliidit parametrit!</error>");
	    } else if(action.equals("update compo")){
		if(compoID != null && getInt(compoID) != -1 &&
		   compoName != null && compoName.length() > 0 &&
		   compoDesc != null && compoDesc.length() > 0
		   ){
		    try {
			boolean v = votingActive != null && votingActive.equals("set");
			boolean b = browsable != null && browsable.equals("set");
			CompoData cd = new CompoData(getInt(compoID), compoName, compoDesc, v, b);
			voting.updateCompo(cd);
			xml.println("<message>Compon #" + compoID + " tiedot p‰ivitetty</message>");
		    } catch(Exception e){
			xml.println("<error>Virhe p‰ivitett‰ess‰ compon #" + compoID + " tietoja! (" +e+ ")</error>");
		    }
		} else xml.println("<error>Virhe p‰ivitett‰ess‰ compon #" + compoID + " tietoja, invaliidit parametrit!</error>");

		// Entry stuff begins
	    } else if(action.equals("getEntries")){
		showEntries = true;
	    } else if(action.equals("addEntry")){
		showEntries = true;
		if(compoID != null && getInt(compoID) != -1 &&
		   entryName != null && entryName.length() > 0 &&
		   entryDesc != null && entryDesc.length() > 0 &&
		   entryAuthor != null && entryAuthor.length() > 0
		   ){
		    try {
			voting.addEntry(new EntryData(-1, getInt(compoID), entryName, entryDesc, entryAuthor));
			xml.println("<message>Lis‰ttiin entry</message>");
		    } catch(Exception e){
			xml.println("<error>Virhe: " + e + "</error>");
		    }
		} else xml.println("<error>Invaliidit parametrit! T‰yt‰ kaikki kent‰t.</error>");
	    } else if(action.equals("removeEntry")){
		showEntries = true;
		if(compoID != null && getInt(compoID) != -1 &&
		   entryID != null && getInt(entryID) != -1
		   ){
		    try {
			voting.removeEntry(getInt(compoID), getInt(entryID));
			xml.println("<message>Poistettiin entry #" + entryID + "</message>");
		    } catch(Exception e){
			xml.println("<error>Virhe poistettaessa entry‰ #" + entryID + ": " + e + "</error>");
		    }
		} else xml.println("<error>Invaliidit parametrit.</error>");
	    } else if(action.equals("update entry")){
		showEntries = true;
		if(compoID != null && getInt(compoID) != -1 &&
		   entryID != null && getInt(entryID) != -1 &&
		   entryName != null && entryName.length() > 0 &&
		   entryDesc != null && entryDesc.length() > 0 &&
		   entryAuthor != null && entryAuthor.length() > 0
		   ){
		    try {
			voting.updateEntry(new EntryData(getInt(entryID), getInt(compoID), entryName, entryDesc, entryAuthor));
			xml.println("<message>Entry #" + entryID + " p‰ivitettiin onnistuneesti.</message>");
		    } catch(Exception e){
			xml.println("<error>Virhe p‰ivitett‰ess‰ entry‰: " + e + "</error>");
		    }
		} else xml.println("<error>Tarkista, ett‰ t‰ytit kaikki kent‰t.</error>");
	    }

	}

	if(!showEntries){
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
	} else {
	    if(compoID != null && getInt(compoID) != -1){
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
		    xml.println("<error>Virhe etsitt‰ess‰ entryj‰: " + e + "</error>");
		}
	    } else xml.println("<error>Invaliidit parametrit!</error>");
	}

	xml.println("</data>");

	xml.println("<ejbProcessingTime>" + (new Date().getTime() - ejbStart.getTime()) + "</ejbProcessingTime>");

	// add the end of the xml and transform
	addEnd(xml);
	transform(new StringReader(sw.getBuffer().toString()), "votingmanager.xsl", resp);

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
