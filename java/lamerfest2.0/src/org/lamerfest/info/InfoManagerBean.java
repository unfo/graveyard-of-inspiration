/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.info;

import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
   <h4>InfoManagerBean</h4>
   The actual bean class for InfoManager.
 */

public class InfoManagerBean implements SessionBean {

    /**
       A simple helper to get the Home interface for a bean
     */
    private Object getHome(String path, Class type){
	try {
	    InitialContext jndiContext = new InitialContext();
   	    Object ref = jndiContext.lookup(path);
	    return PortableRemoteObject.narrow(ref,type);
	} catch (Exception e) { throw new EJBException(e); }
    }

    /**
       Finds all the items in the database
     */
    public InfoItem[] findAll() throws FinderException {
	InfoHome home = (InfoHome)getHome("info/InfoBean", InfoHome.class);
	try {
	    Collection all = home.findAll();
	    Info[] infos = (Info[])all.toArray(new Info[0]);
	    InfoItem[] items = new InfoItem[infos.length];
	    for(int uu = 0; uu < items.length; uu++){
		items[uu] = infos[uu].getData();
	    }
	    return items;
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Finds an item by id.
     */
    public InfoItem findById(int id) throws FinderException {
	InfoHome home = (InfoHome)getHome("info/InfoBean", InfoHome.class);
	try {
	    Info item = home.findByPrimaryKey(new Integer(id));
	    return item.getData();
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Adds the specified item to the database
     */
    public void add(String heading, String content) throws CreateException {
	InfoHome home = (InfoHome)getHome("info/InfoBean", InfoHome.class);
	try {
	    // find out the highest id in use
	    Collection all = home.findAll();
	    Iterator i = all.iterator();
	    int maxId = 1;
	    while(i.hasNext()){
		Info info = (Info)i.next();
		int current = ((Integer)info.getPrimaryKey()).intValue();
		maxId = current > maxId ? current : maxId;
	    }
	    // create the new info item
	    Info created = home.create(++maxId, heading, content);
	} catch(RemoteException re){
	    throw new EJBException(re);
	} catch(FinderException fe){
	    // let's consider this a runtime error
	    throw new EJBException(fe);
	}
    }

    /**
       Updates the data of an item in the database
     */
    public void update(InfoItem infoItem) throws FinderException {
	InfoHome home = (InfoHome)getHome("info/InfoBean", InfoHome.class);
	try {
	    // first lookup the correct bean
	    Info toBeUpdated = home.findByPrimaryKey(new Integer(infoItem.id));
	    // update the data
	    toBeUpdated.setHeading(infoItem.heading);
	    toBeUpdated.setContent(infoItem.content);
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    /**
       Deletes the item denoted by the given id.
     */
    public void delete(int id) throws RemoveException {
	InfoHome home = (InfoHome)getHome("info/InfoBean", InfoHome.class);
	try {
	    home.remove(new Integer(id));
	} catch(RemoteException re){
	    throw new EJBException(re);
	}
    }

    // empty implementations follow

    public void ejbCreate(){ }
    public void ejbRemove(){ }
    public void ejbActivate(){ }
    public void ejbPassivate(){ }
    public void setSessionContext(SessionContext sc){ }

}
