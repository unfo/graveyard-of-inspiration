/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
   <h4>User</h4>
   The remote interface for the User enterprise bean.
   The bean represents a user in Lamerfest 2.0's J2EE system.
 */

public interface User extends EJBObject {

    // these are self-explanatory

    public String getUserID() throws RemoteException;
    public void setPassword(String password) throws RemoteException;
    public String getPassword() throws RemoteException;
    public void setUserLevel(int level) throws RemoteException;
    public int getUserLevel() throws RemoteException;
    public void setRealName(String realName) throws RemoteException;
    public String getRealName() throws RemoteException;
    public UserData getUserData() throws RemoteException;

}
