/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

/**
   <h4>BrowsingNotAllowedException</h4>
 */

public class BrowsingNotAllowedException extends Exception {

    public BrowsingNotAllowedException(){ }

    public String toString(){
	return "You are not allowed to browse the results at the moment";
    }

}

