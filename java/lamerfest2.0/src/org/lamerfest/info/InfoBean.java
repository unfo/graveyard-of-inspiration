/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.info;

import javax.ejb.*;

/**
   <h4>InfoBean</h4>
   The actual Info enterprise bean.
 */

public class InfoBean implements EntityBean {

    // public fields
    public Integer id;
    public String heading;
    public String content;

    public Integer ejbCreate(int id, String heading, String content){
	this.id = new Integer(id);
	this.heading = heading;
	this.content = content;
	// since we're using container-managed
	// persistence with this bean, return null,
	return null;
    }

    public void ejbPostCreate(int id, String heading, String content){
	// no need to do anything
    }

    // setters & getters

    public void setHeading(String heading){
	this.heading = heading;
    }

    public String getHeading(){
	return heading;
    }

    public void setContent(String content){
	this.content = content;
    }

    public String getContent(){
	return content;
    }

    public InfoItem getData(){
	return new InfoItem(id.intValue(), heading, content);
    }

    // empty implementations follow

    public void setEntityContext(EntityContext ctx){
	// no implementation
    }

    public void unsetEntityContext(){
	// no implementation
    }

    public void ejbActivate(){
	// no implementation
    }

    public void ejbPassivate(){
	// no implementation
    }

    public void ejbLoad(){
	// no implementation
    }

    public void ejbStore(){
	// no implementation
    }

    public void ejbRemove(){
	// no implementation
    }

}
