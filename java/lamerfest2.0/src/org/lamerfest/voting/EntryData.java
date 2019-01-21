/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

/**
   <h4>EntryData</h4>
   A bulk accessor for the data of an Entry.
 */

public class EntryData implements java.io.Serializable {

    // public fields
    public int entryID;
    public int compoID;
    public String entryName;
    public String entryDesc;
    public String entryAuthor;
    public int voteCount;

    public EntryData(int e, int c, String n, String d, String a){
	this.entryID = e;
	this.compoID = c;
	this.entryName = n;
	this.entryDesc = d;
	this.entryAuthor = a;
	this.voteCount = -1;
    }

    public EntryData(int e, int c, String n, String d, String a, int v){
	this.entryID = e;
	this.compoID = c;
	this.entryName = n;
	this.entryDesc = d;
	this.entryAuthor = a;
	this.voteCount = v;
    }

}
