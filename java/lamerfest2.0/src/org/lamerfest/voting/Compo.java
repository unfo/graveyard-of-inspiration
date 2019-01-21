/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
   <h4>Compo</h4>
   The remote interface for the Compo entity bean.
 */

public interface Compo extends EJBObject {

    /**
       Sets the name of this Compo.
       @param compoName the new name for this Compo.
     */
    public void setName(String compoName) throws RemoteException;

    /**
       Gets the name of this Compo.
       @return compoName the name of this compo
     */
    public String getName() throws RemoteException;

    /**
       Sets the description for this Compo.
       @param compoDesc the new description
     */
    public void setDesc(String compoDesc) throws RemoteException;

    /**
       Gets the description for this Compo.
       @return compoDesc some descriptive words about this Compo.
     */
    public String getDesc() throws RemoteException;

    /**
       Sets whether users can vote in this compo.
       @param votingActive whether voting should be possible or not
     */
    public void setVotingActive(boolean votingActive) throws RemoteException;

    /**
       Returns true if voting is active in this compo.
       @return votingActive whether users can vote in this compo at the moment
     */
    public boolean getVotingActive() throws RemoteException;

    /**
       This can be used to control whether
       the users can browse the results of
       this Compo. By default, the value is false.
       @param browsable whether the results are browsable
     */
    public void setBrowsable(boolean browsable) throws RemoteException;

    /**
       This returns true if the results of this compo are browsable.
       @return browsable can the results be browed
     */
    public boolean getBrowsable() throws RemoteException;

    /**
       Sets all the data with a bulk accessor.
       @param compoData the bulk accessor
     */
    public void setCompoData(CompoData compoData) throws RemoteException;

    /**
       Gets all the data with a bulk accessor.
       @return compoData the data
     */
    public CompoData getCompoData() throws RemoteException;

    /*
       Returns all the Entries for this Compo.
       @return entries all the Entries in this Compo.
     */
    public Entry[] getEntries() throws RemoteException;

}
