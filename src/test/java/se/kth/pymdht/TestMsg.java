package se.kth.pymdht;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import se.kth.pymdht.Id.IdError;
import se.kth.pymdht.IncomingMsg.MsgError;


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

	@Test
	public void parse_response_with_nodes() {
		byte[] bencoded = "d1:rd2:id20:abcdefghij01234567895:nodes26:12345678901234567890abcdef5:token8:aoeusnthe1:t2:aa1:y1:re".getBytes();
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
		assertEquals(1, msg.all_nodes.size());	
	}
	
	@Test
	public void outgoing_query(){
		byte[] expected_bencoded = "d1:ad2:id20:abcdefghij01234567899:info_hash20:mnopqrstuvwxyz123456e1:q9:get_peers1:t2:aa1:y1:qe".getBytes();
		Id info_hash = null;
		Id my_id = null;
		try {
			my_id = new Id("abcdefghij0123456789".getBytes());
			info_hash = new Id("mnopqrstuvwxyz123456".getBytes());
		} catch (IdError e) {
			e.printStackTrace();
			fail();
		}
		byte[] actual_bencoded = new OutgoingGetPeersQuery(my_id, info_hash).get_bencoded();
		for (int i=0; i<actual_bencoded.length; i++){
//			System.out.print((char) actual_bencoded[i]);
		}
		assertEquals(expected_bencoded.length, actual_bencoded.length);
//		assertEquals(expected_bencoded, actual_bencoded);
	}
}
