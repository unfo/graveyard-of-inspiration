/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.user;

/**
   <h4>UserExistsException</h4>
   Thrown by UserManager if client is trying
   to add a user that already exists.
 */

public class UserExistsException extends Exception {

    public UserExistsException(){ }

    public String toString(){
	return "User already in database";
    }

}

