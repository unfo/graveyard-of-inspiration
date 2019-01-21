package org.lamerfest.voting;

import java.io.*;
import javax.naming.*;
import java.util.Properties;
import java.util.Enumeration;
import java.rmi.RemoteException;

public class TestManager {

    /*
 else if(args[0].equals("")){
		
		System.out.println("");
	    }
     */

    public static void main(String args[]){

	try {

	    Context jndiContext = getInitialContext();
	    Object ref = jndiContext.lookup("voting/VotingManagerBean");
	    VotingManagerHome home = (VotingManagerHome)
		javax.rmi.PortableRemoteObject.narrow(ref, VotingManagerHome.class);

	    VotingManager mgr = home.create();

	    String a = args[0];

	    if(a.equals("getCompos")){
		CompoData[] compos = mgr.getCompos();
		for(int ee = 0; ee < compos.length; ee++) Test.pcd(compos[ee]);
	    } else if(a.equals("getEntries")){
		EntryData[] entries = mgr.getEntries(Integer.parseInt(args[1]));
		for(int pp = 0; pp < entries.length; pp++) TestEntry.ped(entries[pp]);
	    } else if(a.equals("castVote")){
		mgr.castVote(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		System.out.println("Vote was cast.");
	    } else if(a.equals("addCompo")){
		CompoData compoData = new CompoData(
			    -1,
			    args[1],
			    args[2],
			    false,
			    false
		    );
		mgr.addCompo(compoData);
		System.out.println("Added Compo");
	    } else if(a.equals("addEntry")){
		EntryData entryData = new EntryData(-1, Integer.parseInt(args[1]),
						    args[2], args[3], args[4]);
		mgr.addEntry(entryData);
		System.out.println("Added Entry");
	    } else if(a.equals("updateCompo")){
		CompoData compoData = new CompoData(
			    Integer.parseInt(args[1]),
			    args[2],
			    args[3],
			    args[4].equals("1"),
			    args[5].equals("1")
		    );
		mgr.updateCompo(compoData);
		System.out.println("Updated Compo");
	    } else if(a.equals("updateEntry")){
		EntryData entryData = new EntryData(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
						    args[3], args[4], args[5]);
		mgr.updateEntry(entryData);
		System.out.println("Updated Entry");
	    } else if(a.equals("removeCompo")){
		mgr.removeCompo(Integer.parseInt(args[1]));
		System.out.println("Remove Compo :( sniff");
	    } else if(a.equals("removeEntry")){
		mgr.removeEntry(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		System.out.println("Remove Entry :( sob");
	    } else if(a.equals("resetVotes")){
		mgr.resetVotes(Integer.parseInt(args[1]));
		System.out.println("Reset votes");
	    } else if(a.equals("getResults")){
		CompoResults c = mgr.getResults(Integer.parseInt(args[1]), args[2].equals("1"));
		System.out.println("Results for compo #" + c.compoData.compoID +
				   " - " + c.compoData.compoName + ":");
		EntryData[] d = c.entryData;
		for(int oo = 0; oo < d.length; oo++)
		    System.out.println("#" + d[oo].entryID + ", " +
				       d[oo].entryName + " by " +
				       d[oo].entryAuthor + " : " + d[oo].voteCount + " votes");

	    }

	} catch(RemoteException re){
	    System.out.println("Caught remote exception .. display stack trace?");
	    try {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String x = br.readLine();
		if(x != null && x.equals("y"))
		    re.printStackTrace();
	    } catch(Exception e){}
	} catch(NamingException ne){
	    ne.printStackTrace();
	} catch(javax.ejb.DuplicateKeyException dke){
	    System.out.println("Duplicate keys! :(");
	    dke.printStackTrace();
	} catch(javax.ejb.CreateException ce){
	    System.out.println("Couldn't create a remote interface! :(");
	    ce.printStackTrace();
	} catch(javax.ejb.FinderException fe){
	    System.out.println("Couldn't find stuff! :(");
	    fe.printStackTrace();
	} catch(AlreadyVotedException ave){
	    System.out.println("User has already voted in that Compo!");
	    ave.printStackTrace();
	} catch(VotingNotAllowedException vnae){
	    System.out.println("Users can't currently vote in that Compo!");
	    vnae.printStackTrace();
	} catch(BrowsingNotAllowedException bnae){
	    System.out.println("Users can't currently browse in that Compo! .. but admins can!");
	    bnae.printStackTrace();
	} catch(javax.ejb.RemoveException re){
	    System.out.println("Couldn't remove stuff! :(");
	    re.printStackTrace();
	}
    }

    static void pcd(Compo c) throws RemoteException {
	CompoData d = c.getCompoData();
	System.out.println(d.compoID + ":" + d.compoName + ":" + d.compoDesc + ":" + d.votingActive + ":" + d.browsable);
    }

    public static Context getInitialContext() throws NamingException {
	Properties p = new Properties();
	p.setProperty(
		      "java.naming.factory.initial",
		      "org.jnp.interfaces.NamingContextFactory"
		      );
	p.setProperty(
		      "java.naming.provider.url",
		      "localhost:1099"
		      );
	return new InitialContext(p);
    }

}
