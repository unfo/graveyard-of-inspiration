/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

import java.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.sql.DataSource;
import javax.naming.Context;
import java.rmi.RemoteException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
   <h4>VotingManagerBean</h4>
   The actual bean class for VotingManager.
   VotingManager is a stateless session bean
   which handles all the activities related
   to voting at Lamerfest 2.0, such as casting
   the actual votes, creating new compos and
   entries and such.
 */

public class VotingManagerBean implements SessionBean {

    /**
       Gets all the Compos in the database and returns them.
       @return compoData an array with data for all the Compos found.
     */
    public CompoData[] getCompos() throws FinderException {
	CompoHome home = (CompoHome)getHome("voting/CompoBean", CompoHome.class);
	try {
	    Enumeration enum = home.findAll();
	    Vector designBug = new Vector();
	    while(enum.hasMoreElements())
		designBug.addElement(enum.nextElement());
	    CompoData[] compoData = new CompoData[designBug.size()];
	    for(int xx = 0; xx < compoData.length; xx++)
		compoData[xx] = ((Compo)designBug.elementAt(xx)).getCompoData();
	    return compoData;
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Gets the Entries for the specified Compo.
       @return entryData an array with the Entries
     */
    public EntryData[] getEntries(int compoID) throws FinderException {
	CompoHome home = (CompoHome)getHome("voting/CompoBean", CompoHome.class);
	try {
	    Compo compo = home.findByPrimaryKey(new Integer(compoID));
	    Entry[] entries = compo.getEntries();
	    EntryData[] entryData = new EntryData[entries.length];
	    for(int vv = 0; vv < entryData.length; vv++)
		entryData[vv] = entries[vv].getEntryData();
	    return entryData;
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Casts a vote.
       @param userID the id of the user casting his/her vote.
       @param compoID the id of the Compo where the vote is cast
       @param entryID the id of the Entry for whom the vote is cast.
       @throws alreadyVotedException if user has already voted in the Compo
       @throws votingNotAllowedException if voting is not currently allowed in the Compo
     */
    public void castVote(String userID, int compoID, int entryID)
	throws AlreadyVotedException, VotingNotAllowedException {

	// silence please.
	// attempting a dare-devil stunt.
	// we shall cast a vote! cross yer fingers mateys!
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    // get the connection (it's so nice to get a NullPointer when you forget ;)
	    conn = getConnection();

	    // find out whether one can vote in this Compo at the moment
	    ps = conn.prepareStatement("SELECT votingActive FROM Compo WHERE compoID = ?");
	    ps.setInt(1, compoID);
	    rs = ps.executeQuery();
	    if(!rs.next()) throw new EJBException("no such compo '" + compoID + "' !!");
	    boolean votingActive = "1".equals(rs.getString("votingActive"));
	    if(!votingActive) throw new VotingNotAllowedException();
	    rs.close();

	    // find out whether the user has already voted in this Compo
	    ps = conn.prepareStatement("SELECT * FROM Vote WHERE compoID = ? AND userID = ?");
	    ps.setInt(1, compoID);
	    ps.setString(2, userID);
	    rs = ps.executeQuery();
	    if(rs.next()) throw new AlreadyVotedException();

	    // find if the entryID exists within the Compo with compoID
	    ps = conn.prepareStatement("SELECT * FROM Entry WHERE compoID = ? AND entryID = ?");
	    ps.setInt(1, compoID);
	    ps.setInt(2, entryID);
	    rs = ps.executeQuery();
	    if(!rs.next()) throw new EJBException("No such entry in Compo #" + compoID);

	    // finally, vote!
	    ps = conn.prepareStatement("INSERT INTO Vote (compoID, entryID, userID) VALUES (?, ? , ?)");
	    ps.setInt(1, compoID);
	    ps.setInt(2, entryID);
	    ps.setString(3, userID);
	    if(ps.executeUpdate() != 1)
		throw new EJBException("Casting the vote failed!");

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(ps != null) ps.close();
		if(conn != null) conn.close();
		if(rs != null) rs.close();
	    } catch(SQLException sqle){
		System.out.println("SQL probs at VotingManagerBean:castVote");
		sqle.printStackTrace();
	    }
	}

    }

    /**
       Gets the results for a Compo.
       Administrators can browse the results
       at any given time, users only when
       browsing is allowed for the Compo.
       @param compoID the Compo whose results to return
       @param isAdmin whether the user is an admin
       @return compoResults the results
     */
    public CompoResults getResults(int compoID, boolean isAdmin) throws BrowsingNotAllowedException, FinderException {
	// tasks: check whether the results can be browsed
	// by the user -> throw exception or return results
	CompoHome home = (CompoHome)getHome("voting/CompoBean", CompoHome.class);
	try {
	    // find out whether these results
	    // can be browsed by the user
	    Compo ourCompo = home.findByPrimaryKey(new Integer(compoID));
	    if(!ourCompo.getBrowsable() && !isAdmin)
		throw new BrowsingNotAllowedException();
	    Entry[] entries = ourCompo.getEntries();
	    EntryData[] entryData = new EntryData[entries.length];
	    for(int yy = 0; yy < entryData.length; yy++){
		entryData[yy] = entries[yy].getEntryData();
		entryData[yy].voteCount = entries[yy].getVoteCount();
	    }
	    return new CompoResults(ourCompo.getCompoData(), entryData);
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Adds a Compo the system.
       @param compoData the data for the new Compo
     */
    public void addCompo(CompoData compoData) throws CreateException {
	CompoHome home = (CompoHome)getHome("voting/CompoBean", CompoHome.class);
	try {
	    // find out the highest id in use
	    Enumeration all = home.findAll();
	    int maxId = 0;
	    while(all.hasMoreElements()){
		Compo compo = (Compo)all.nextElement();
		int current = ((Integer)compo.getPrimaryKey()).intValue();
		maxId = current > maxId ? current : maxId;
	    }
	    // create the new Compo
	    Compo created = home.create(++maxId, compoData.compoName, compoData.compoDesc);
	} catch(RemoteException re){
	    throw new EJBException(re);
	} catch(FinderException fe){
	    // let's consider this a runtime error
	    throw new EJBException(fe);
	}
    }

    /**
       Adds an Entry to a Compo.
       @param entryData the data for the new Entry (entryID won't be used, it'll be assigned automatically)
     */
    public void addEntry(EntryData entryData) throws CreateException {

	EntryHome home = (EntryHome)getHome("voting/EntryBean", EntryHome.class);

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    // and again, forgot about this the first time. then came the NPE
	    conn = getConnection();

	    // find out the highest id of the Entries in this Compo
	    int maxID = 0;
	    String sql = "SELECT entryID FROM Entry WHERE " +
		"entryID >= ALL (SELECT entryID FROM Entry)";
	    ps = conn.prepareStatement(sql);
	    rs = ps.executeQuery();
	    if(rs.next()) maxID = rs.getInt("entryID");

	    // create the Entry
	    home.create(
			entryData.compoID,
			++maxID,
			entryData.entryName,
			entryData.entryDesc,
			entryData.entryAuthor
			);

	} catch(RemoteException re){
	    throw new EJBException(re);
	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		System.out.println("SQLException at VotingManagerBean:addEntry()");
		sqle.printStackTrace();
	    }
	}

    }

    /**
       Updates a Compo's data.
       @param compoData the new data for the Compo
     */
    public void updateCompo(CompoData compoData) throws FinderException {
	CompoHome home = (CompoHome)getHome("voting/CompoBean", CompoHome.class);
	try {
	    Compo compo = home.findByPrimaryKey(new Integer(compoData.compoID));
	    compo.setCompoData(compoData);
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Updates an Entry's data
       @param entryData the new data for the Entry.
     */
    public void updateEntry(EntryData entryData) throws FinderException {
	EntryHome home = (EntryHome)getHome("voting/EntryBean", EntryHome.class);
	try {
	    EntryPK pk = new EntryPK(entryData.compoID, entryData.entryID);
	    Entry entry = home.findByPrimaryKey(pk);
	    entry.setEntryData(entryData);
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Removes a Compo. All the Entries that
       belong to this Compo will also be deleted,
       as well as the Votes.
       @param compoID the id of the Compo to be deleted.
     */
    public void removeCompo(int compoID) throws RemoveException {
	// tasks: remove votes with compoID, remove
	// Entries with compoID and remove Compo with compoID
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	CompoHome compoHome = (CompoHome)getHome("voting/CompoBean", CompoHome.class);
	EntryHome entryHome = (EntryHome)getHome("voting/EntryBean", EntryHome.class);
	try {
	    // I seem to try to test whether a null
	    // connection is usable ;) it took me 
	    // yet another NullPointerEx to add the
	    // following line .. gosh
	    conn = getConnection();
	    // first remove votes
	    ps = conn.prepareStatement("DELETE FROM Vote WHERE compoID = ?");
	    ps.setInt(1, compoID);
	    if(ps.executeUpdate() != 1) throw new EJBException("Couldn't remove votes for Compo #" + compoID);
	    // then entries
	    ps = conn.prepareStatement("SELECT entryID FROM Entry WHERE compoID = ?");
	    ps.setInt(1, compoID);
	    rs = ps.executeQuery();
	    while(rs.next()) entryHome.remove(new EntryPK(compoID, rs.getInt("entryID")));
	    // and finally the Compo, let's do this the 'official' way
	    compoHome.remove(new Integer(compoID));
	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} catch(RemoteException re){
	    throw new EJBException(re);
	} finally {
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		System.out.println("SQL probs at VotingManagerBean:removeCompo");
		sqle.printStackTrace();
	    }
	}
    }

    /**
       Removes an Entry from a Compo as well
       as any votes the Entry has gotten.
       @param compoID the id of the Compo the Entry belongs to
       @param entryID the id of the Entry to be removed.
     */
    public void removeEntry(int compoID, int entryID) throws RemoveException {
	// tasks: remove votes with entryID and compoID,
	// remove entry with entryID and compoID
	Connection conn = null;
	PreparedStatement ps = null;
	EntryHome entryHome = (EntryHome)getHome("voting/EntryBean", EntryHome.class);
	try {
	    // no this is just a bad joke
	    conn = getConnection();
	    // first remove votes
	    ps = conn.prepareStatement("DELETE FROM Vote WHERE compoID = ? AND entryID = ?");
	    ps.setInt(1, compoID);
	    ps.setInt(2, entryID);
	    if(ps.executeUpdate() != 1) throw new EJBException("Couldn't remove votes for Entry #"
							       + entryID + " in Compo #" + compoID);
	    // then entry
	    entryHome.remove(new EntryPK(compoID, entryID));
	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} catch(RemoteException re){
	    throw new EJBException(re);
	} finally {
	    try {
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		System.out.println("SQL probs at VotingManagerBean:removeEntry");
		sqle.printStackTrace();
	    }
	}
    }

    /**
       Resets the votes of a Compo.
       @param compoID the id of the Compo
     */
    public void resetVotes(int compoID){
	// tasks: remove votes with compoID
	Connection conn = null;
	PreparedStatement ps = null;
	try {
	    // remove votes from this Compo
	    conn = getConnection();
	    ps = conn.prepareStatement("DELETE FROM Vote WHERE compoID = ?");
	    ps.setInt(1, compoID);
	    if(ps.executeUpdate() != 1) throw new EJBException("Couldn't remove votes for Compo #" + compoID);
	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		System.out.println("SQL probs at VotingManagerBean:resetVotes");
		sqle.printStackTrace();
	    }
	}
    }

    /**
       Obtains a database connection thru the JNDI context
       @return conn connection to the database
     */
    private Connection getConnection() throws SQLException {
	try {
	    Context jndiContext = new InitialContext();
	    DataSource ds = (DataSource)jndiContext.lookup("LamerfestDB");
	    return ds.getConnection();
	} catch(NamingException ne){
	    throw new EJBException(ne);
	}
    }

    /**
       A simple helper to get the Home interface for a bean
     */
    private Object getHome(String path, Class type){
	try {
	    InitialContext jndiContext = new InitialContext();
   	    Object ref = jndiContext.lookup(path);
	    return PortableRemoteObject.narrow(ref,type);
	} catch (Exception e) { throw new EJBException(e); }
    }

    // empty implementations follow

    public void ejbCreate(){ }
    public void ejbRemove(){ }
    public void ejbActivate(){ }
    public void ejbPassivate(){ }
    public void setSessionContext(SessionContext sc){ }

}
