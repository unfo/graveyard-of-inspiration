package org.lamerfest.voting;

import java.io.*;
import javax.naming.*;
import java.util.Properties;
import java.util.Enumeration;
import java.rmi.RemoteException;

public class Test {

    /*
 else if(args[0].equals("")){
		
		System.out.println("");
	    }
     */

    public static void main(String args[]){

	try {

	    Context jndiContext = getInitialContext();
	    Object ref = jndiContext.lookup("voting/CompoBean");
	    CompoHome home = (CompoHome)javax.rmi.PortableRemoteObject.narrow(ref, CompoHome.class);

	    if(args[0].equals("create")){
		home.create(Integer.parseInt(args[1]), args[2], args[3]);
		System.out.println("Created");
		return;
	    } else if(args[0].equals("find")){
		Compo compo = home.findByPrimaryKey(new Integer(Integer.parseInt(args[1])));
		pcd(compo);
		return;
	    } else if(args[0].equals("remove")){
		home.remove(new Integer(Integer.parseInt(args[1])));
		System.out.println("Removed.");
		return;
	    } else if(args[0].equals("update")){
		Compo compo = home.findByPrimaryKey(new Integer(Integer.parseInt(args[1])));
		compo.setCompoData(new CompoData(Integer.parseInt(args[1]), args[2], args[3], args[4].equals("1"), args[5].equals("1")));
		System.out.println("Updated.");
		return;
	    } else if(args[0].equals("getEntries")){
		Compo compo = home.findByPrimaryKey(new Integer(Integer.parseInt(args[1])));
		Entry[] e = compo.getEntries();
		for(int hh = 0; hh < e.length; hh++) TestEntry.ped(e[hh]);
		return;
	    }

	    Enumeration all = home.findAll();

	    while(all.hasMoreElements()){
		Compo compo = (Compo)all.nextElement();
		pcd(compo);
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

    static void pcd(Compo c) throws RemoteException {
	CompoData d = c.getCompoData();
	System.out.println(d.compoID + ":" + d.compoName + ":" + d.compoDesc + ":" + d.votingActive + ":" + d.browsable);
    }

    static void pcd(CompoData d) throws RemoteException {
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
