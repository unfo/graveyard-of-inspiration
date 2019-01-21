/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.info;

import javax.ejb.*;
import java.util.Collection;
import java.rmi.RemoteException;

/**
   <h4>InfoHome</h4>
   The home interface for the Info enterprise bean.
 */

public interface InfoHome extends EJBHome {

    public Info create(int id, String heading, String content) throws CreateException, RemoteException;

    // container-implemented finder methods
    public Info findByPrimaryKey(Integer id) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;

}
