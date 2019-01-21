/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

import javax.ejb.*;
import java.util.Collection;
import java.rmi.RemoteException;

/**
   <h4>UserHome</h4>
   The home interface for the User enterprise bean.
 */

public interface UserHome extends EJBHome {

    public User create(String userID, String password, String realName, int level) throws CreateException, RemoteException;

    // find methods, all implemented by the container
    public User findByPrimaryKey(String pk) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Collection findByLevel(int level) throws FinderException, RemoteException;

}
