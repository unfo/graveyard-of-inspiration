/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.info;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
   <h4>Info</h4>
   A news item / article in Lamerfest 2.0's system.
 */

public interface Info extends EJBObject {

    public void setHeading(String heading) throws RemoteException;
    public String getHeading() throws RemoteException;
    public void setContent(String content) throws RemoteException;
    public String getContent() throws RemoteException;
    public InfoItem getData() throws RemoteException;

}
