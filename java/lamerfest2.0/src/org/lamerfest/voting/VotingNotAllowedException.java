/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

/**
   <h4>VotingNotAllowedException</h4>
 */

public class VotingNotAllowedException extends Exception {

    public VotingNotAllowedException(){ }

    public String toString(){
	return "Voting is not allowed in this Compo at the moment.";
    }

}

