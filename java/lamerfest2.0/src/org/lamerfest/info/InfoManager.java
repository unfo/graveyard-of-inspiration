/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.info;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
   <h4>InfoManager</h4>
   Manages information items (articles and such)
   in Lamerfest 2.0's bloody splendin J2EE system.
 */

public interface InfoManager extends EJBObject {

    /**
       Finds all the info items in the database.
       @return infoItems an array with all the items found
     */
    public InfoItem[] findAll() throws RemoteException, FinderException;

    /**
       Finds an info item with the specified primary key.
       @return infoItem the info item requested
     */
    public InfoItem findById(int id) throws RemoteException, FinderException;

    /**
       Adds an item to the database.
       @param heading heading of the article
       @param content the contents of the article
       @throws iEE if the item already exists in the database
     */
    public void add(String heading, String content) throws RemoteException, CreateException;

    /**
       Updates the data of an item in the database.
       @param infoItem the bulk access representing the item
     */
    public void update(InfoItem infoItem) throws RemoteException, FinderException;

    /**
       Deletes the specified info item from the database
       @param id the id of the item to be deleted
     */
    public void delete(int id) throws RemoteException, RemoveException;

}
