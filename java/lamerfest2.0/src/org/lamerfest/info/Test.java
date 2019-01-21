package org.lamerfest.info;

import java.io.*;
import javax.naming.*;
import java.util.Properties;
import java.rmi.RemoteException;

public class Test {

    public static void main(String args[]){

	try {

	    Context jndiContext = getInitialContext();
	    Object ref = jndiContext.lookup("info/InfoManagerBean");
	    InfoManagerHome home = (InfoManagerHome)javax.rmi.PortableRemoteObject.narrow(ref, InfoManagerHome.class);

	    InfoManager infoMgr = home.create();

	    if(args[0].equals("findAll")){
		InfoItem[] all = infoMgr.findAll();
		for(int xx = 0; xx < all.length; xx++){
		    InfoItem current = all[xx];
		    System.out.println(current.id + " - " +
				       current.heading.substring(0,5) + "... - " +
				       current.content.substring(0,5) + "..."
				       );
		}
	    } else if(args[0].equals("findById")){
		InfoItem current = infoMgr.findById(Integer.parseInt(args[1]));
		System.out.println(current.id + " - " +
				   current.heading.substring(0,5) + "... - " +
				   current.content.substring(0,5) + "..."
				   );
	    } else if(args[0].equals("add")){
		infoMgr.add(args[1], args[2]);
	    } else if(args[0].equals("update")){
		infoMgr.update(new InfoItem(Integer.parseInt(args[1]), args[2], args[3]));
	    } else if(args[0].equals("delete")){
		infoMgr.delete(Integer.parseInt(args[1]));
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
	} catch(javax.ejb.FinderException fe){
	    System.out.println("Didn't find any users :(");
	    fe.printStackTrace();
	} catch(javax.ejb.CreateException ce){
	    System.out.println("Couldn't create user manager! :(");
	    ce.printStackTrace();
	} catch(javax.ejb.RemoveException re){
	    System.out.println("Couldn't delete user!!");
	    re.printStackTrace();
	}
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
