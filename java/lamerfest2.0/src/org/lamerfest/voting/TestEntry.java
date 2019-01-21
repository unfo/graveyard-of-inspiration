package org.lamerfest.voting;

import java.io.*;
import javax.naming.*;
import java.util.Properties;
import java.util.Enumeration;
import java.rmi.RemoteException;

public class TestEntry {

    /*
 else if(args[0].equals("")){
		
		System.out.println("");
	    }
     */

    public static void main(String args[]){

	try {

	    Context jndiContext = getInitialContext();
	    Object ref = jndiContext.lookup("voting/EntryBean");
	    EntryHome home = (EntryHome)javax.rmi.PortableRemoteObject.narrow(ref, EntryHome.class);

	    if(args[0].equals("create")){
		home.create(Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3], args[4], args[5]);
		System.out.println("Created");
		return;
	    } else if(args[0].equals("find")){
		Entry entry = home.findByPrimaryKey(new EntryPK(Integer.parseInt(args[1]), Integer.parseInt(args[2])));
		ped(entry);
		return;
	    } else if(args[0].equals("remove")){
		home.remove(new EntryPK(Integer.parseInt(args[1]), Integer.parseInt(args[2])));
		System.out.println("Removed.");
		return;
	    } else if(args[0].equals("update")){
		Entry entry = home.findByPrimaryKey(new EntryPK(Integer.parseInt(args[1]), Integer.parseInt(args[2])));
		entry.setEntryData(new EntryData(-1, -1, args[3], args[4], args[5]));
		System.out.println("Updated.");
		return;
	    }

	    Enumeration all = home.findAll();

	    while(all.hasMoreElements()){
		Entry entry = (Entry)all.nextElement();
		ped(entry);
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
	} catch(javax.ejb.RemoveException re){
	    System.out.println("Couldn't remove stuff! :(");
	    re.printStackTrace();
	}
    }

    static void ped(Entry e) throws RemoteException {
	EntryData d = e.getEntryData();
	System.out.println(d.compoID + ":" + d.entryID + ":" + d.entryName + ":" + d.entryDesc + ":" + d.entryAuthor);
    }

    static void ped(EntryData d) throws RemoteException {
	System.out.println(d.compoID + ":" + d.entryID + ":" + d.entryName + ":" + d.entryDesc + ":" + d.entryAuthor);
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
