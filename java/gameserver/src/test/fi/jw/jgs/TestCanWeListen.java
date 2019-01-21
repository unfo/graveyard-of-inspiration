/**
 ** @author Jan Wikholm
 ** @version $ REVISION 1.0 $
 **/
package fi.jw.jgs;

import junit.framework.TestCase;



public class TestCanWeListen extends TestCase {
	
	public void testOpenAndCloseSocket() throws Exception {
		GameServer gs = null;
		gs = GameServer.getServer(6666);
		gs.plugSocket();
		GameServer.stopServer();
	}
	
	public void testOpenIllegalPort() throws Exception  {
		GameServer gs = GameServer.getServer();
		gs.listenTo(0);
		GameServer.stopServer();
	}
	
	public void testGetPort() {
		try {
			int port = 1024;
			GameServer gs = GameServer.getServer(port);
			assertEquals(port, gs.getPort());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		try {
			GameServer.stopServer();
		} catch (Exception e) {
			fail("Could not cleanup server");
		}
	}
}
