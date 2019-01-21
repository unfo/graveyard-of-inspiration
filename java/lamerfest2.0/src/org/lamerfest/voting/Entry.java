/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
   <h4>Entry</h4>
   The remote interface for the Entry entity bean,
   which in turn represents an entry in a compo.
 */

public interface Entry extends EJBObject {

    /**
       Sets the name of the entry.
       @param entryName the name for the entry
     */
    public void setName(String entryName) throws RemoteException;

    /**
       Gets the name of the entry.
       @return entryName the name of the entry
     */
    public String getName() throws RemoteException;

    /**
       Sets the description of this entry.
       @param entryDesc the description
     */
    public void setDesc(String entryDesc) throws RemoteException;

    /**
       Gets the description of this entry.
       @return entryDesc the description
     */
    public String getDesc() throws RemoteException;

    /**
       Sets the author (group/person) of this entry.
       @param entryAuthor the ppl behind this entry
     */
    public void setAuthor(String entryAuthor) throws RemoteException;

    /**
       Gets the author of this entry.
       @return entryAuthor the author's name
     */
    public String getAuthor() throws RemoteException;

    /**
       Sets the data of this entry with a bulk accessor.
       @param entryData the data to be set
     */
    public void setEntryData(EntryData entryData) throws RemoteException;

    /**
       Gets the data of this entry.
       @return entryData the data of this entry.
     */
    public EntryData getEntryData() throws RemoteException;

    /**
       Gets the vote count of the Entry.
       @return voteCount how many votes the Entry got.
     */
    public int getVoteCount() throws RemoteException;

}
