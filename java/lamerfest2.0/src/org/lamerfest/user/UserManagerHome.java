/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
   <h4>UserManagerHome</h4>
   The home interface for the UserManager enterprise bean.
 */

public interface UserManagerHome extends EJBHome {

    public UserManager create() throws RemoteException, CreateException;

}
