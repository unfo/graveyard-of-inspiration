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
   <h4>EntryBean</h4>
   Bean class of EntryBean, uses bean-managed persistence.
 */

public class EntryBean implements EntityBean {

    // persistent data fields of this Entry bean
    public int entryID;
    public int compoID;
    public String entryName;
    public String entryDesc;
    public String entryAuthor;

    // reference to our EntityContext
    public EntityContext entityContext;

    // getter and setter methods for the persistent data follow

    public void setName(String entryName){
	this.entryName = entryName;
    }

    public String getName(){
	return entryName;
    }

    public void setDesc(String entryDesc){
	this.entryDesc = entryDesc;
    }

    public String getDesc(){
	return entryDesc;
    }

    public void setAuthor(String entryAuthor){
	this.entryAuthor = entryAuthor;
    }

    public String getAuthor(){
	return entryAuthor;
    }

    public void setEntryData(EntryData entryData){
	this.entryName = entryData.entryName;
	this.entryDesc = entryData.entryDesc;
	this.entryAuthor = entryData.entryAuthor;
    }

    public EntryData getEntryData(){
	return new EntryData(entryID, compoID, entryName, entryDesc, entryAuthor);
    }

    public int getVoteCount(){
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    conn = getConnection();
	    ps = conn.prepareStatement("SELECT count(*) AS voteCount FROM" +
				       " Vote WHERE compoID = ? AND entryID = ?");
	    ps.setInt(1, compoID);
	    ps.setInt(2, entryID);
	    rs = ps.executeQuery();
	    if(!rs.next())
		// this really shouldn't happen, since we do a count()!
		throw new EJBException("hmmmm, no records with count()");
	    return rs.getInt("voteCount");
	} catch(SQLException sqle){
	    throw new EJBException(sqle);
	} finally {
	    try {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	    } catch(SQLException sqle){
		System.out.println("SQLException at EntryBean:getVoteCount()" +
				   " when freeing resources");
		sqle.printStackTrace();		
	    }
	}
    }

    /**
       ejbCreate method for the Entry bean.
       @param compoID the compo to which this entry belongs to
       @param entryID the id of this entry
       @param entryName the name of this entry
       @param entryDesc description
       @param entryAuthor author
       @return entryPK the primary key
     */
    public EntryPK ejbCreate(int compoID,
			     int entryID,
			     String entryName,
			     String entryDesc,
			     String entryAuthor) throws CreateException {

	// do some checking here
	if(compoID < 1 || entryID < 1 || entryName == null || entryDesc == null || entryAuthor == null)
	    throw new CreateException("Invalid parameter(s).");

	// set the persistent fields
	this.compoID = compoID;
	this.entryID = entryID;
	this.entryName = entryName;
	this.entryDesc = entryDesc;
	this.entryAuthor = entryAuthor;

	// and now for the database stuff
	Connection conn = null;
	PreparedStatement duplicateCheck = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    // get a connection
	    conn = getConnection();

	    // check for duplicates
	    duplicateCheck = conn.prepareStatement("SELECT entryID FROM Entry WHERE compoID = ? AND entryID = ?");
	    duplicateCheck.setInt(1, compoID);
	    duplicateCheck.setInt(2, entryID);
	    rs = duplicateCheck.executeQuery();
	    if(rs.next())
		throw new DuplicateKeyException("Entry #" + entryID + " already in compo #" + compoID);

	    // prepare the statement and set the values
	    ps = conn.prepareStatement(
	    "INSERT INTO Entry (entryID, compoID, entryName, entryDesc, entryAuthor) " +
	    "VALUES (?,?,?,?,?)");
	    ps.setInt(1, entryID);
	    ps.setInt(2, compoID);
	    ps.setString(3, entryName);
	    ps.setString(4, entryDesc);
	    ps.setString(5, entryAuthor);

	    // as always, cross your fingers
	    if(ps.executeUpdate() != 1) throw new CreateException("Failed to add the Entry to database.");

	    EntryPK pk = new EntryPK(compoID, entryID);
	    return pk;

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

    public void ejbPostCreate(int compoID, int entryID, String entryName, String entryDesc, String entryAuthor){
	// no implementation
    }

    // finder methods follow

    /**
       Finds an Entry by its primary key.
       @param entryPK the primary key
       @return entryPK the primary key
     */
    public EntryPK ejbFindByPrimaryKey(EntryPK pk) throws FinderException {

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    conn = getConnection();
	    ps = conn.prepareStatement("SELECT entryID FROM Entry WHERE compoID = ? AND entryID = ?");
	    ps.setInt(1, pk.compoID);
	    ps.setInt(2, pk.entryID);
	    rs = ps.executeQuery();
	    if(!rs.next())
		// we don't have any matches, so the Entry doesn't exist
		throw new ObjectNotFoundException("No Entry with id " + pk.entryID + " in compo #" + pk.compoID);

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
       Finds and returns all Entries in the database.
       If nothing found, returns an empty Enumeration
       (i.e. not null), following the EJB 1.1 spec.
       @return enum Enumeration with all the Entries found
     */
    public Enumeration ejbFindAll() throws FinderException {
	
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    
	    conn = getConnection();
	    ps = conn.prepareStatement("SELECT * FROM Entry");
	    rs = ps.executeQuery();

	    Vector keys = new Vector();
	    while(rs.next())
		keys.addElement(new EntryPK(rs.getInt("compoID"), rs.getInt("entryID")));

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
	EntryPK pk = (EntryPK)entityContext.getPrimaryKey();

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    
	    // get the connections, populate the data
	    conn = getConnection();
	    ps = conn.prepareStatement("SELECT * FROM Entry WHERE compoID = ? AND entryID = ?");
	    ps.setInt(1, pk.compoID);
	    ps.setInt(2, pk.entryID);

	    rs = ps.executeQuery();
	    if(rs.next()){
		compoID = pk.compoID;
		entryID = pk.entryID;
		entryName = rs.getString("entryName");
		entryDesc = rs.getString("entryDesc");
		entryAuthor = rs.getString("entryAuthor");
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
                "UPDATE Entry SET entryName = ?, entryDesc = ?, " +
		"entryAuthor = ? WHERE compoID = ? AND entryID = ?"
		);
	    ps.setString(1, entryName);
	    ps.setString(2, entryDesc);
	    ps.setString(3, entryAuthor);
	    ps.setInt(4, compoID);
	    ps.setInt(5, entryID);
	    if(ps.executeUpdate() != 1) throw new EJBException("EntryBean @ ejbStore");

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
	    ps = conn.prepareStatement("DELETE FROM Entry WHERE compoID = ? AND entryID = ?");
	    ps.setInt(1, compoID);
	    ps.setInt(2, entryID);
	    if(ps.executeUpdate() != 1) throw new EJBException("EntryBean @ ejbRemove");

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
