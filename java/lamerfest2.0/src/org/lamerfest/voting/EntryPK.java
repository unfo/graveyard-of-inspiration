/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

/**
   <h4>EntryPK</h4>
   Primary key class for the Entry entity bean.
   It had to be implemented this way due to the
   fact that an Entry's primary key consists of
   two parts, compoID and entryID (in the database
   compoID is a foreign key and also a primary key)
 */

public class EntryPK implements java.io.Serializable {

    // public fields
    public int compoID;
    public int entryID;

    /**
       Empty constructor.
     */
    public EntryPK(){ }

    /**
       The constructor for EntryPK.
       @param compoID the id of the compo the entry referenced by this primary key belongs to
       @param entryID the id of the entry this primary key references
     */
    public EntryPK(int compoID, int entryID){
	this.compoID = compoID;
	this.entryID = entryID;
    }

    /**
       Returns a well-distributed hash
       code for this primary key.
       @return hashCode the hash code
     */
    public int hashCode(){
	StringBuffer sb = new StringBuffer();
	sb.append("compo");
	sb.append(compoID);
	sb.append("entry");
	sb.append(entryID);
	return sb.toString().hashCode();
    }

    /**
       Returns true if the parameter object is
       an EntryPK and has the same values as our instance.
       @param obj the object whose equality with our instance to check
       @return equals whether the object equals our instance
     */
    public boolean equals(Object obj){
	if(obj == null || !(obj instanceof EntryPK)) return false;
	else if(((EntryPK)obj).compoID == compoID && ((EntryPK)obj).entryID == entryID) return true;
	else return false;
    }

    /**
       A string presentation of this EntryPK
       @return stringPresentation
     */
    public String toString(){
	return new String("EntryPK[compoID=" + compoID + ",entryID" + entryID + "]");
    }

}
