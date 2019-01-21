/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

import javax.ejb.*;
import java.util.Collection;
import java.rmi.RemoteException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
   <h4>UserManagerBean</h4>
   The actual bean class for UserManager.
 */

public class UserManagerBean implements SessionBean {

    /**
       A simple helper to get the Home interface for a bean
     */
    private Object getHome(String path, Class type){
	try {
	    InitialContext jndiContext = new InitialContext();
   	    Object ref = jndiContext.lookup(path);
	    return PortableRemoteObject.narrow(ref,type);
	} catch (Exception e) { throw new EJBException(e); }
    }

    /**
       Adds a user to the system.
     */
    public void addUser(String userID, String password,
			String realName, int level) throws UserExistsException {

	UserHome home = (UserHome)getHome("user/UserBean", UserHome.class);
	System.out.println("Adding user: " + userID + "," + password + "," + realName + "," + level);
	try {
	    User user = home.create(userID, password, realName, level);
	} catch(DuplicateKeyException dke){
	    throw new UserExistsException();
	} catch(Exception ce){
	    throw new EJBException(ce);
	}

    }

    /**
       Updates a user's info.
     */
    public void updateUser(String userID, String password,
			String realName, int level) throws FinderException {

	UserHome home = (UserHome)getHome("user/UserBean", UserHome.class);
	try {
	    User user = home.findByPrimaryKey(userID);
	    user.setPassword(password);
	    user.setRealName(realName);
	    user.setUserLevel(level);
	} catch(RemoteException re){
	    throw new EJBException(re);
	}

    }

    /**
       Finds all users, returns a UserData array
     */
    public UserData[] findAll() throws FinderException {
	UserHome home = (UserHome)getHome("user/UserBean", UserHome.class);
	try {
	    Collection all = home.findAll();
	    User[] allUsers = (User[])all.toArray(new User[0]);
	    UserData[] ud = new UserData[allUsers.length];
	    for(int ii = 0; ii < allUsers.length; ii++){
		ud[ii] = allUsers[ii].getUserData();
	    }
	    return ud;
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Deletes the specified user.
     */
    public void deleteUser(String userID) throws RemoveException {
	UserHome home = (UserHome)getHome("user/UserBean", UserHome.class);
	try {
	    home.remove(userID);
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    // empty implementations follow

    public void ejbCreate(){ }
    public void ejbRemove(){ }
    public void ejbActivate(){ }
    public void ejbPassivate(){ }
    public void setSessionContext(SessionContext sc){ }

}
