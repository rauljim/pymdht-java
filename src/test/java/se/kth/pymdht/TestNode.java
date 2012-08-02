package se.kth.pymdht;

import static org.junit.Assert.assertEquals;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class TestNode {
	
	public static byte[] a1 = new byte[]{1, 2, 3, 4};
	public InetSocketAddress addr1;
	public static byte[] a2 = new byte[]{2, 2, 3, 4};
	public Inet4Address addr2;
	
	@Before
	public void setUp(){
		try {
			addr1 = new InetSocketAddress(
					(InetAddress) Inet4Address.getByAddress(a1),
					2222);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test(){
		Node node1 = new Node(addr1, new RandomId());
		assertEquals(addr1, node1.addr);
//		assertEquals()
	}
	
//		node2 = Node(addr2,id2);
//		node1b = Node(addr1,null);
//		node1ip = Node("127.0.0.2",1111,id1);
//		node1port = Node(addr2,id1);
//		node1id = Node(addr1,id2);
//		this.assertEqual(str(node1),String.format("<node: %26r %r (version)>",addr1,id1));
//		// <node: ('127.0.0.1', 1111) 0x1313131313131313131313131313131313131313> 
//		this.assertEqual(node1.id,id1);
//		!node1.id.equals(id2);
//		node1.addr.equals(addr1);
//		this.assertEqual(node1.ip,addr1.get(0));
//		!node1.addr.equals(addr2);
//		node1.equals(node1);
//		!node1.equals(node1b);
//		node1b.id = id1;
//		node1.equals(node1b);
//		!node1.equals(node2);
//		!node1.equals(node1ip);
//		!node1.equals(node1port);
//		!node1.equals(node1id);
//	}
//	public void test_compact_addr(){
//		this.assertEqual(tc.CLIENT_NODE.compact_addr,utils.compact_addr(tc.CLIENT_ADDR));
//	}
//	public void test_distance(){
//		this.assertEqual(tc.CLIENT_NODE.distance(tc.SERVER_NODE),tc.CLIENT_ID.distance(tc.SERVER_ID));
//	}
//	public void test_compact(){
//		this.assertEqual(tc.CLIENT_NODE.compact(),(tc.CLIENT_ID._bin+utils.compact_addr(tc.CLIENT_ADDR)));
//	}
//	public void test_get_rnode(){
//		this.assertEqual(tc.CLIENT_NODE.get_rnode(1),RoutingNode(tc.CLIENT_NODE,1));
//	}
//	public void test_node_exceptions(){
//		this.assertRaises(AttributeError);
//		Node(addr1,id1).id = id2;
//	}
//	public void test_node_without_id(){
//		n1 = Node(tc.CLIENT_ADDR);
//		n2 = Node(tc.CLIENT_ADDR);
//		this.assertEqual(n1,n2);
//		n1.id = tc.CLIENT_ID;
//		this.assertTrue(!n1.equals(n2));
//		n2.id = tc.CLIENT_ID;
//		this.assertEqual(n1,n2);
//	}
}
