package se.kth.pymdht;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.kth.pymdht.Id.IdError;

public class TestLookup{
	Id info_hash=null, id1=null, id2=null, id3=null, id4 = null;
	Node node1=null, node2=null, node3=null, node4 = null;
	LookupQueue lq;
	
	@Before
	public void setUp(){
		try {
			info_hash = new Id("0000000000000000000000000000000000000000");
			id1 = new Id("1000000000000000000000000000000000000000");
			id2 = new Id("2000000000000000000000000000000000000000");
			id3 = new Id("3000000000000000000000000000000000000000");
			id4 = new Id("4000000000000000000000000000000000000000");
		} catch (IdError e) {
			e.printStackTrace();
			fail();
		}
		try {
			node1 = new Node(new InetSocketAddress(Inet4Address.getByName(
					"1.1.1.1"), 1111), id1);
			node2 = new Node(new InetSocketAddress(Inet4Address.getByName(
					"1.1.1.2"), 2222), id2);
			node3 = new Node(new InetSocketAddress(Inet4Address.getByName(
					"1.1.1.3"), 3333), id3);
			node4 = new Node(new InetSocketAddress(Inet4Address.getByName(
					"1.1.1.4"), 4444), id4);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			fail();
		}
		List<InetSocketAddress> unstable = new ArrayList<InetSocketAddress>();
		unstable.add(node3.addr);
		unstable.add(node4.addr);
		List<InetSocketAddress> stable = new ArrayList<InetSocketAddress>();

		lq = new LookupQueue(info_hash, unstable, stable);
	}

	@Test
	public void qnode(){
		InetSocketAddress addr1 = new InetSocketAddress(2222);
		Node node1 = new Node(addr1);
		QueuedNode qnode1 = new QueuedNode(node1, new Id(BigInteger.valueOf(2)));
		InetSocketAddress addr2 = new InetSocketAddress(2222);
		Node node2 = new Node(addr2);
		QueuedNode qnode2 = new QueuedNode(node2, new Id(BigInteger.valueOf(3)));
		assert(qnode1.compareTo(qnode1) == 0);
		assert(qnode2.compareTo(qnode2) == 0);
		assert(qnode1.compareTo(qnode2) > 0);
		assert(qnode2.compareTo(qnode1) < 0);
	}
	
	@Test
	public void lookup_queue(){
		
		List<Node> nodes_to_query = lq.get_nodes_to_query();
		assertEquals(2, nodes_to_query.size());
		assertEquals(node3.addr, nodes_to_query.get(0).addr);
		assertEquals(node4.addr, nodes_to_query.get(1).addr);
		
		ArrayList<Node> found_nodes = new ArrayList<Node>(0);
		lq.on_response(node3, found_nodes);
		nodes_to_query = lq.get_nodes_to_query();
		assertEquals(0, nodes_to_query.size());

		found_nodes.add(node2);
		found_nodes.add(node1);
		lq.on_response(node4, found_nodes);
		nodes_to_query = lq.get_nodes_to_query();
		assertEquals(1, nodes_to_query.size());
		assertEquals(node1, nodes_to_query.get(0));
		nodes_to_query = lq.get_nodes_to_query();
		assertEquals(1, nodes_to_query.size());
		assertEquals(node2.addr, nodes_to_query.get(0).addr);
		nodes_to_query = lq.get_nodes_to_query();
		assertEquals(0, nodes_to_query.size());
}
}