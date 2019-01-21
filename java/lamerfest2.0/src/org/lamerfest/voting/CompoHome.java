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
   <h4>CompoHome</h4>
   The home interface for the Compo entity bean.
 */

public interface CompoHome extends EJBHome {

    /**
       Creates a Compo entity bean.
       @param compoID the id for this Compo
       @param compoName the name for this Compo
       @param compoDesc the description for this Compo
       @return compo the remote reference to the created Compo bean.
     */
    public Compo create(int compoID,
			String compoName,
			String compoDesc) throws CreateException, RemoteException;

    /**
       Finds an Compo by its primary key
       @param pk the primary key
       @return compo a remote ref. to the Compo found
     */
    public Compo findByPrimaryKey(Integer pk) throws FinderException, RemoteException;

    /**
       Returns all the Compos in the database.
       @return enum an Enumeration with all the Compos in the database
     */
    public Enumeration findAll() throws FinderException, RemoteException;

}
