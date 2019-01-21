/*
  This software is distributed under the
  GPL license, see the terms at gnu.org.
  (C) Copyright Jari Aarniala, 2000
*/

package org.lamerfest.voting;

import javax.ejb.*;
import java.rmi.RemoteException;

/**
   <h4>VotingManager</h4>
   The remote interface for the VotingManager session
   bean. This bean handles all the voting actions etc.
 */

public interface VotingManager extends EJBObject {

    // General methods, used by all clients.

    /**
       Returns data for all the compos.
       @return compoData an array with with a CompoData for every compo.
     */
    public CompoData[] getCompos() throws RemoteException, FinderException;

    /**
       Returns data for Entries in the specified Compo.
       @param compoID the Compo whose entries to return.
       @return entryData data for all the entries in the specified Compo
     */
    public EntryData[] getEntries(int compoID) throws RemoteException, FinderException;

    /**
       Casts a vote for an Entry in a Compo.
       @param userID the id of the user casting his/her vote.
       @param compoID the id of the Compo where the vote is cast
       @param entryID the id of the Entry for whom the vote is cast.
       @throws alreadyVotedException if user has already voted in the Compo
       @throws votingNotAllowedException if voting is not currently allowed in the Compo
     */
    public void castVote(String userID, int compoID, int entryID)
	throws RemoteException, AlreadyVotedException, VotingNotAllowedException;

    /**
       Gets the results for a Compo.
       Administrators can browse the results
       at any given time, users only when
       browsing is allowed for the Compo.
       @param compoID the Compo whose results to return
       @param isAdmin whether the user is an admin
       @return compoResults the results
     */
    public CompoResults getResults(int compoID, boolean isAdmin)
	throws RemoteException, BrowsingNotAllowedException, FinderException;

    // Administrative methods, only used by admin clients.

    /**
       Adds a Compo the system.
       @param compoData the data for the new Compo
     */
    public void addCompo(CompoData compoData) throws RemoteException, CreateException;

    /**
       Adds an Entry to a Compo.
       @param entryData the data for the new Entry (entryID won't be used, it'll be assigned automatically)
     */
    public void addEntry(EntryData entryData) throws RemoteException, CreateException;

    /**
       Updates a Compo's data.
       @param compoData the new data for the Compo
     */
    public void updateCompo(CompoData compoData) throws RemoteException, FinderException;

    /**
       Updates an Entry's data
       @param entryData the new data for the Entry.
     */
    public void updateEntry(EntryData entryData) throws RemoteException, FinderException;

    /**
       Removes a Compo. All the Entries that
       belong to this Compo will also be deleted,
       as well as the Votes.
       @param compoID the id of the Compo to be deleted.
     */
    public void removeCompo(int compoID) throws RemoteException, RemoveException;

    /**
       Removes an Entry from a Compo as well
       as any votes the Entry has gotten.
       @param compoID the id of the Compo the Entry belongs to
       @param entryID the id of the Entry to be removed.
     */
    public void removeEntry(int compoID, int entryID) throws RemoteException, RemoveException;

    /**
       Resets the votes of an Compo.
       @param compoID the id of the Compo
     */
    public void resetVotes(int compoID) throws RemoteException;

}
