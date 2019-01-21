/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.info;

import java.io.Serializable;

/**
   <h4>InfoItem</h4>
   A bulk accessor for the data of a Info
   enterprise bean. Cuts down on network
   traffic.
 */

public class InfoItem implements Serializable {

    // public fields
    public int id;
    public String heading;
    public String content;
    
    public InfoItem(int id, String heading, String content){
	this.id = id;
	this.heading = heading;
	this.content = content;
    }

}
