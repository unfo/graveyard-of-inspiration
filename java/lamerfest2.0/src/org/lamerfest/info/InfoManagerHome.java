/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.info;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
   <h4>InfoManagerHome</h4>
   InfoManager enterprise bean's home interface.
 */

public interface InfoManagerHome extends EJBHome {

    public InfoManager create() throws CreateException, RemoteException;

}
