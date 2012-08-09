package se.kth.pymdht;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestSwiftTracker{
	
	@Test
	public void test(){
		SwiftTracker st = new SwiftTracker();
		// channel zero, bin flag, bin, hash, handshake flag, sender's channel
		byte [] data = {0,0,0,0, 4, 0,0,0,0, 1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0, 0, 1,2,3,4}; 
		InetSocketAddress addr = new InetSocketAddress(9999);
		DatagramPacket datagram = null;
		try {
			datagram = new DatagramPacket(data, data.length, addr);
		} catch (SocketException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(st.on_handshake(datagram));
		
		byte[] cpeer_bytes = {1,2,3,4, 9,9};
		ByteBuffer cpeer = ByteBuffer.wrap(cpeer_bytes);
		ArrayList<ByteBuffer> cpeers = new ArrayList<ByteBuffer>(1);
		cpeers.add(cpeer);
		DatagramPacket pex_datagram = st.get_swift_pex_datagram(cpeers);
	}
}