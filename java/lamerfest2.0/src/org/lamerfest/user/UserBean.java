/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

import javax.ejb.*;

/**
   <h4>UserBean</h4>
   The actual User enterprise bean.
 */

public class UserBean implements EntityBean {

    // the public variables that describe the User
    public String userID;
    public String password;
    public String realName;
    public int level;

    public String ejbCreate(String userID, String password, String realName, int level){
	this.userID = userID;
	this.password = password;
	this.realName = realName;
	this.level = level;
	// since we're using container-managed
	// persistence with the User bean, return null,
	// the container will return the actual primary key.
	return null;
    }

    public void ejbPostCreate(String userID, String password, String realName, int level){
	// no need to do anything
    }

    // setters & getters

    public String getUserID(){
	return userID;
    }

    public void setPassword(String password){
	this.password = password;
    }

    public String getPassword(){
	return password;
    }

    public void setUserLevel(int level){
	this.level = level;
    }

    public int getUserLevel(){
	return level;
    }

    public void setRealName(String realName){
	this.realName = realName;
    }

    public String getRealName(){
	return realName;
    }

    public UserData getUserData(){
	return new UserData(userID, password, realName, level);
    }

    // empty implementations follow

    public void setEntityContext(EntityContext ctx){
	// no implementation
    }

    public void unsetEntityContext(){
	// no implementation
    }

    public void ejbActivate(){
	// no implementation
    }

    public void ejbPassivate(){
	// no implementation
    }

    public void ejbLoad(){
	// no implementation
    }

    public void ejbStore(){
	// no implementation
    }

    public void ejbRemove(){
	// no implementation
    }

}
