/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

/**
   <h4>CompoResults</h4>
   A class that wraps the results of a Compo -
   the information on both the Compo and its Entries.
 */

public class CompoResults implements java.io.Serializable {

    // public fields
    public CompoData compoData;
    public EntryData[] entryData;

    public CompoResults(CompoData compoData, EntryData[] entryData){
	this.compoData = compoData;
	this.entryData = entryData;
    }

}
