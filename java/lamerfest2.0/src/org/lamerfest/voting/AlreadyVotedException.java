/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

/**
   <h4>AlreadyVotedException</h4>
 */

public class AlreadyVotedException extends Exception {

    public AlreadyVotedException(){ }

    public String toString(){
	return "The user has already voted in this Compo";
    }

}

