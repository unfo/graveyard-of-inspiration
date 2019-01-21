/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
   <h4>VotingManagerHome</h4>
   The home interface for the VotingManager session bean.
 */

public interface VotingManagerHome extends EJBHome {

    /**
       Creates a remote reference to the VotingManager session bean.
       @return votingManager the remote reference.
     */
    public VotingManager create() throws RemoteException, CreateException;

}
