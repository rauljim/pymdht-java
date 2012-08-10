package se.kth.pymdht;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

import org.junit.Test;

import se.kth.pymdht.Controller.LookupDone;
import se.kth.pymdht.IncomingMsg.MsgError;

public class TestController{

	@Test
	public void test(){
		int swift_port = 8888;
		
		Controller c = new Controller();
		//waiting for ppsp/swift handshake
		List<DatagramPacket> datagrams_to_send = null;
		try {
			datagrams_to_send = c.on_heartbeat();
		} catch (LookupDone e1) {
			e1.printStackTrace();
			fail();
		}
		assertEquals(0, datagrams_to_send.size());
		//handshake comes
		byte [] raw_handshake = {0,0,0,0, 4, 0,0,0,0, 1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0, 0, 1,2,3,4};
		DatagramPacket datagram = null;
		try {
			datagram = new DatagramPacket(raw_handshake, raw_handshake.length, new InetSocketAddress(swift_port));
		} catch (SocketException e) {
			e.printStackTrace();
			fail();
		}
		datagrams_to_send = c.on_datagram_received(datagram);
		// lookup queries + one handshake reply
		assertEquals(GetPeersLookup.NUM_UNSTABLE_PER_ROUND + 1, datagrams_to_send.size());

		// no responses, socket times out: more queries
		try {
			datagrams_to_send = c.on_heartbeat();
		} catch (LookupDone e1) {
			e1.printStackTrace();
			fail();
		}
		assertEquals(GetPeersLookup.NUM_UNSTABLE_PER_ROUND, datagrams_to_send.size());
		
		// get nodes
		byte[] bencoded = "d1:rd2:id20:abcdefghij01234567895:nodes26:12345678901234567890abcdef5:token8:aoeusnthe1:t2:aa1:y1:re".getBytes();
		IncomingMsg msg = null;
		try {
			datagram = new DatagramPacket(bencoded, bencoded.length, new InetSocketAddress(1111));
		} catch (SocketException e) {
			e.printStackTrace();
			fail();
		}
		datagrams_to_send = c.on_datagram_received(datagram);
		assertEquals(1, datagrams_to_send.size());
		assertEquals(25958, datagrams_to_send.get(0).getPort()); //'ef' == 25958
		//back to bootstrap nodes
		try {
			datagrams_to_send = c.on_heartbeat();
		} catch (LookupDone e1) {
			e1.printStackTrace();
			fail();
		}
		assertEquals(GetPeersLookup.NUM_UNSTABLE_PER_ROUND, datagrams_to_send.size());
		
		//get peers
		bencoded = "d1:rd2:id20:abcdefghij01234567895:token8:aoeusnth6:valuesl6:axje.u6:idhtnmee1:t2:aa1:y1:re".getBytes();
		try {
			datagram = new DatagramPacket(bencoded, bencoded.length, new InetSocketAddress(1112));
		} catch (SocketException e) {
			e.printStackTrace();
			fail();
		}
		datagrams_to_send = c.on_datagram_received(datagram);
		assertEquals(GetPeersLookup.NUM_UNSTABLE_PER_ROUND + 1, datagrams_to_send.size());
		assertEquals(swift_port, datagrams_to_send.get(GetPeersLookup.NUM_UNSTABLE_PER_ROUND).getPort()); //send peers to swift/ppsp
	}
}