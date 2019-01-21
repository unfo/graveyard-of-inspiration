/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

/**
   <h4>CompoData</h4>
   A bulk accessor for a Compo bean's data.
 */

public class CompoData implements java.io.Serializable {

    public int compoID;
    public String compoName;
    public String compoDesc;
    public boolean votingActive;
    public boolean browsable;

    public CompoData(int c, String n, String d, boolean v, boolean b){
	this.compoID = c;
	this.compoName = n;
	this.compoDesc = d;
	this.votingActive = v;
	this.browsable = b;
    }

}
