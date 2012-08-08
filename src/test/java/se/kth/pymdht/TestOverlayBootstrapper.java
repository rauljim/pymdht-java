package se.kth.pymdht;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestOverlayBootstrapper{
	
	private OverlayBootstrapper ob;
	@Before
	public void setUp(){
		this.ob = new OverlayBootstrapper();
		ob.toString();
	}
	
	@Test
	public void unstable(){
		List<InetSocketAddress> addrs = ob.get_sample_unstable_addrs(2);
		assertEquals(2, addrs.size());
		assertFalse(addrs.get(0).equals(ob.get_sample_unstable_addrs(1).get(0)));
	}
	@Test
	public void stable(){
		List<InetSocketAddress> addrs = ob.get_shuffled_stable_addrs();
		assertTrue(addrs.size() > 0);
	}
}