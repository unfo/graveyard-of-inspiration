/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

/**
   <h4>UserData</h4>
   A simple wrapper class (a bulk accessor)
   that wraps the info contained in a User entity 
   bean. This reduces network overhead; instead of 
   invocating getPassword(), getRealName() etc. on the
   remote interface, just invoke getUserData().
   Only to be used from clients, no setter for this in
   User bean.
 */

public class UserData implements java.io.Serializable {

    public String userID;
    public String password;
    public String realName;
    public int level;

    public UserData(String userID, String password, String realName, int level){
	this.userID = userID;
	this.password = password;
	this.realName = realName;
	this.level = level;
    }

}
