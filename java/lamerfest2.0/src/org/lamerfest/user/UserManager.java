/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
   <h4>UserManager</h4>
   The remote interface for the UserManager enterprise bean.
 */

public interface UserManager extends EJBObject {

    public void addUser(String userID, String password,
			String realName, int level) throws RemoteException, UserExistsException;

    public void updateUser(String userID, String password,
			   String realName, int level) throws RemoteException, FinderException;

    public UserData[] findAll() throws RemoteException, FinderException;

    public void deleteUser(String userID) throws RemoteException, RemoveException;

}
