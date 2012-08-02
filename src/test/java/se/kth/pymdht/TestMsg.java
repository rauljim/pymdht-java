package se.kth.pymdht;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;


public class TestMsg {

	@Test
	public void parse_response_with_peers() {
		byte[] bencoded = "d1:rd2:id20:abcdefghij01234567895:token8:aoeusnth6:valuesl6:axje.u6:idhtnmee1:t2:aa1:y1:re".getBytes();
		DatagramPacket datagram = null;
		try {
			datagram = new DatagramPacket(bencoded, bencoded.length, InetAddress.getByAddress("1234".getBytes()), 8000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		IncomingMsg msg = null;
		try {
			msg = new IncomingMsg(datagram);
		} catch (MsgError e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(2, msg.peers.size());
		
	}

}
