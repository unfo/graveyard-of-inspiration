/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

import javax.ejb.*;
import java.util.Enumeration;
import java.rmi.RemoteException;

/**
   <h4>EntryHome</h4>
   The home interface for the Entry entity bean.
 */

public interface EntryHome extends EJBHome {

    /**
       Creates an Entry entity bean.
       @param compoID the id of the compo this entry belongs to
       @param entryID the id of this entry
       @param entryName the name of this entry
       @param entryDesc description
       @param entryAuthor author of this entry
       @return entry the remote reference to the created Entry bean.
     */
    public Entry create(int compoID,
			int entryID,
			String entryName,
			String entryDesc,
			String entryAuthor) throws CreateException, RemoteException;

    /**
       Finds an Entry by its primary key
       @param entryID the primary key
       @return entry the Entry found
     */
    public Entry findByPrimaryKey(EntryPK pk) throws FinderException, RemoteException;

    /**
       Returns all the Entries in the database.
       @return enum an Enumeration with all the Entries in the database
     */
    public Enumeration findAll() throws FinderException, RemoteException;

}
