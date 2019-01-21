/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

import java.sql.*;
import javax.ejb.*;
import javax.naming.*;
import java.util.Vector;
import javax.sql.DataSource;
import java.util.Enumeration;

/**
   <h4>CompoBean</h4>
   The actual Compo entity bean. This bean
   uses bean-managed persistence, so it updates itself
   to the database etc. when told to do so by the container.
 */

public class CompoBean implements EntityBean {

    // persistent data fields of this Compo bean
    public int compoID;
    public String compoName;
    public String compoDesc;
    public boolean votingActive;
    public boolean browsable;

    // This is not a persistent field, it is populated
    // with EntryPKs of the entries that belong to this
    // Compo when ejbLoad is invoked. Once called,
    // getEntries will return an array of remote references
    // to the Entries whose EntryPKs are in this Vector.
    private Vector myEntries;

    // reference to our EntityContext
    public EntityContext entityContext;

    // getter and setter methods for the persistent data follow

    public void setName(String compoName){
	this.compoName = compoName;
    }

    public String getName(){
	return compoName;
    }

    public void setDesc(String compoDesc){
	this.compoDesc = compoDesc;
    }

    public String getDesc(){
	return compoDesc;
    }

    public void setVotingActive(boolean votingActive){
	this.votingActive = votingActive;
    }

    public boolean getVotingActive(){
	return votingActive;
    }

    public void setBrowsable(boolean browsable){
	this.browsable = browsable;
    }

    public boolean getBrowsable(){
	return browsable;
    }

    public void setCompoData(CompoData compoData){
	this.compoName = compoData.compoName;
	this.compoDesc = compoData.compoDesc;
	this.votingActive = compoData.votingActive;
	this.browsable = compoData.browsable;
    }

    public CompoData getCompoData(){
	return new CompoData(compoID, compoName, compoDesc, votingActive, browsable);
    }

    /**
       getEntries returns all Entries in this Compo.
       The data is loaded on method invocation here,
       if loaded in ejbLoad, the data can get old.
     */
    public Entry[] getEntries(){

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    // get a connection to the database
	    conn = getConnection();

	    // get the Entries (or their EntryPKs) for this Compo
	    ps = conn.prepareStatement("SELECT entryID FROM Entry WHERE compoID = ?");
	    ps.setInt(1, compoID);
	    rs = ps.executeQuery();
	    myEntries = new Vector();
	    while(rs.next()) myEntries.addElement(new EntryPK(compoID, rs.getInt("entryID")));

	    Entry[] entries = new Entry[myEntries.size()];
	    EntryHome entryHome = (EntryHome)new InitialContext().lookup("voting/EntryBean");
	    for(int ee = 0; ee < entries.length; ee++){
		EntryPK pk = (EntryPK)myEntries.elementAt(ee);
		entries[ee] = entryHome.findByPrimaryKey(pk);
	    }
	    return entries;

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} catch(Exception e){
	    throw new EJBException(e);
	} finally {
	    // try to free all db-resources
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		System.out.println(sqle + " @ CompoBean:getEntries()");
		sqle.printStackTrace();
	    }
	}

    }

    /**
       ejbCreate method for the Compo bean.
       @param compoID id for the new compo
       @param compoName the name of the compo
       @param compoDesc descriptive words
       @return integer the primary key
     */
    public Integer ejbCreate(int compoID,
			     String compoName,
			     String compoDesc) throws CreateException {

	// do some checking here
	if(compoID < 1 || compoName == null || compoDesc == null)
	    throw new CreateException("Invalid parameter(s).");

	// set the persistent fields
	this.compoID = compoID;
	this.compoName = compoName;
	this.compoDesc = compoDesc;
	// these default to false in the beginning
	this.votingActive = false;
	this.browsable = false;

	// and now for the database stuff
	Connection conn = null;
	PreparedStatement duplicateCheck = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    // get a connection
	    conn = getConnection();

	    // check for duplicates
	    duplicateCheck = conn.prepareStatement("SELECT compoID FROM Compo WHERE compoID = ?");
	    duplicateCheck.setInt(1, compoID);
	    rs = duplicateCheck.executeQuery();
	    if(rs.next())
		throw new DuplicateKeyException("Compo with id " + compoID + " already in database");

	    // prepare the statement and set the values
	    ps = conn.prepareStatement(
	    "INSERT INTO Compo (compoID, compoName, compoDesc, votingActive, browsable) " +
	    "VALUES (?,?,?,?,?)");
	    ps.setInt(1, compoID);
	    ps.setString(2, compoName);
	    ps.setString(3, compoDesc);
	    ps.setString(4, "0");
	    ps.setString(5, "0");

	    // as always, cross your fingers
	    if(ps.executeUpdate() != 1) throw new CreateException("Failed to add Compo to database.");

	    Integer primaryKey = new Integer(compoID);
	    return primaryKey;

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(duplicateCheck != null) duplicateCheck.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		sqle.printStackTrace();
	    }
	}

    }

    public void ejbPostCreate(int compoID, String compoName, String compoDesc){
	// no implementation
    }

    // finder methods follow

    /**
       Finds a Compo by its primary key.
       @param integer the primary key
       @return integer the primary key
     */
    public Integer ejbFindByPrimaryKey(Integer pk) throws FinderException {

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    conn = getConnection();
	    ps = conn.prepareStatement("SELECT compoID FROM Compo WHERE compoID = ?");
	    ps.setInt(1, pk.intValue());
	    rs = ps.executeQuery();
	    if(!rs.next())
		// we don't have any matches, so the Compo doesn't exist
		throw new ObjectNotFoundException("No Compo with id " + pk.intValue() + " in the database.");

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    // finally try to release db resources
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		sqle.printStackTrace();
	    }
	}

	return pk;

    }

    /**
       Finds and returns all Compos in the database.
       If nothing found, returns an empty Enumeration
       (i.e. not null), following the EJB 1.1 spec.
       @return enum Enumeration with all the Compos found
     */
    public Enumeration ejbFindAll() throws FinderException {
	
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    
	    conn = getConnection();
	    ps = conn.prepareStatement("SELECT * FROM Compo");
	    rs = ps.executeQuery();

	    Vector keys = new Vector();
	    while(rs.next()) keys.addElement(new Integer(rs.getInt("compoID")));

	    // return a Enumeration, no matter whether
	    // there are any results or not
	    return keys.elements();

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    // release resources
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		sqle.printStackTrace();
	    }
	}

    }

    // setter and unsetter for the bean's entity context

    public void setEntityContext(EntityContext ctx){
	entityContext = ctx;
    }

    public void unsetEntityContext(){
	entityContext = null;
    }

    /**
       Populates the persistent data.
     */
    public void ejbLoad(){
	
	// get our primary key from the entity context
	Integer pk = (Integer)entityContext.getPrimaryKey();

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    
	    // get the connections, populate the data
	    conn = getConnection();
	    ps = conn.prepareStatement("SELECT * FROM Compo WHERE compoID = ?");
	    ps.setInt(1, pk.intValue());

	    rs = ps.executeQuery();
	    if(rs.next()){
		compoID = pk.intValue();
		compoName = rs.getString("compoName");
		compoDesc = rs.getString("compoDesc");
		votingActive = rs.getString("votingActive").equals("1");
		browsable = rs.getString("browsable").equals("1");
	    } else {
		throw new EJBException();
	    }

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		sqle.printStackTrace();
	    }
	}

    }

    /**
       Stores the information contained
       by this bean to the database.
     */
    public void ejbStore(){
	
	Connection conn = null;
	PreparedStatement ps = null;
	
	try {

	    // update the data
	    conn = getConnection();
	    ps = conn.prepareStatement(
                "UPDATE Compo SET compoName = ?, compoDesc = ?, " +
		"votingActive = ?, browsable = ? WHERE compoID = ?"
		);
	    ps.setString(1, compoName);
	    ps.setString(2, compoDesc);
	    ps.setString(3, votingActive ? "1" : "0");
	    ps.setString(4, browsable ? "1" : "0");
	    ps.setInt(5, compoID);
	    if(ps.executeUpdate() != 1) throw new EJBException("CompoBean @ ejbStore");

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		sqle.printStackTrace();
	    }
	}

    }

    /**
       Removes the data of the current bean
       instance from the database for good.
     */
    public void ejbRemove(){
	
	Connection conn = null;
	PreparedStatement ps = null;

	try {

	    // delete stuff
	    conn = getConnection();
	    ps = conn.prepareStatement("DELETE FROM Compo WHERE compoID = ?");
	    ps.setInt(1, compoID);
	    if(ps.executeUpdate() != 1) throw new EJBException("CompoBean: ejbRemove");

	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		sqle.printStackTrace();
	    }
	}

    }

    // two empty implementations follow

    public void ejbActivate(){
	// no implementation
    }

    public void ejbPassivate(){
	// no implementation
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

}
