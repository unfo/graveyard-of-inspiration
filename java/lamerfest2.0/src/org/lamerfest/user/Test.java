package org.lamerfest.user;

import java.io.*;
import javax.naming.*;
import java.util.Properties;
import java.rmi.RemoteException;

public class Test {

    public static void main(String args[]){

	try {

	    Context jndiContext = getInitialContext();
	    Object ref = jndiContext.lookup("user/UserManagerBean");
	    UserManagerHome home = (UserManagerHome)javax.rmi.PortableRemoteObject.narrow(ref, UserManagerHome.class);

	    UserManager userMgr = home.create();

	    if(args[0].equals("add") && args.length == 5){
		userMgr.addUser(args[1], args[2], args[3], Integer.parseInt(args[4]));
		System.out.println("User added");
	    } else if(args[0].equals("delete") && args.length == 2){
		userMgr.deleteUser(args[1]);
		System.out.println("user deleted");
	    } else if(args[0].equals("update") && args.length == 5){
		userMgr.updateUser(args[1], args[2], args[3], Integer.parseInt(args[4]));
		System.out.println("User info updated");
	    } else
		showUsage();

	    System.out.println("all users:");
	    UserData[] all = userMgr.findAll();
	    for(int xx = 0; xx < all.length; xx++){
		UserData current = all[xx];
		System.out.println(current.userID + ":" + current.password + ":" + current.realName + ":" + current.level);
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
	} catch(UserExistsException uee){
	    System.out.println("User " + args[1] + " already exists!");
	    uee.printStackTrace();
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

    static void showUsage(){
	System.out.println(
			   "Usage:\n" +
			   "add id password name level\nor\n" +
			   "update id password name level\nor\n" +
			   "delete id"
			   );
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
