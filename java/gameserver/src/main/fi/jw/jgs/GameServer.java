/**
 * @author Jan Wikholm
 * This is the central server, on which everything runs.
 */
package fi.jw.jgs;
import java.io.IOException;
import java.net.ServerSocket;

public class GameServer {

	private ServerSocket mySock = null;
	private int port = 0;
	
	/**
	 * The Singleton pattern
	 */

	
	private static GameServer singleton = null;
	
	static GameServer getServer() {
		if (singleton == null) {
			singleton = new GameServer();
		}
		return singleton;
	}
	static GameServer getServer(int port) throws Exception {
		if (singleton == null) {
			singleton = new GameServer(port);
			return singleton;
		} else if (singleton.getPort() == port) {
			return singleton;
		} else {
			throw new Exception("Server already loaded");
		}
	}
	private GameServer() { }
	
	/**
	 * Create new GameServer with portnumber
	 * @param port
	 */
	private GameServer(int port) throws IOException {
		this.listenTo(port);
	}

	
	/* End of Singleton */
	
	
	
	public void listenTo(int port) throws IOException {
		if (this.port == 0) {
			mySock = new ServerSocket(port);
			this.port = port;
		} else {
			throw new IllegalStateException("Socket already created");
		}
	}
	
	public int getPort() {
		return port;
	}
	
	
	public void plugSocket() throws Exception {
		if (!mySock.isClosed())
				mySock.close();
	}
	
	public static void stopServer() throws Exception {
		singleton.plugSocket();
		singleton = null;
	}
}
/*        while (listening)
new KKMultiServerThread(serverSocket.accept()).start();

    serverSocket.close();
} */		
