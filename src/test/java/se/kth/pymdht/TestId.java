package se.kth.pymdht;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestId{

	private static final String HEX_ID0 =     "0000000000000000000000000000000000000000";
	private static final String HEX_ID1 =     "0101010101010101010101010101010101010101";
	private static final String HEX_ID2 =     "ffffffffffffffffffffffffffffffffffffffff";
	private static final String HEX_DIST0_1 = "0101010101010101010101010101010101010101";
	private static final String HEX_DIST1_2 = "fefefefefefefefefefefefefefefefefefefefe";

	private Id id0, id1, id2;
	private Id dist0_1, dist1_2;

	@Before
	public void setUp(){
		try {
			id0 = new Id(HEX_ID0);
		} catch (IdError e) {
			fail();
		}
		try {
			id1 = new Id(HEX_ID1);
		} catch (IdError e) {
			fail();
		}
		try {
			id2 = new Id(HEX_ID2);
		} catch (IdError e) {
			fail();
		}
		try {
			dist0_1 = new Id(HEX_DIST0_1);
		} catch (IdError e) {
			fail();
		}
		try {
			dist1_2 = new Id(HEX_DIST1_2);
		} catch (IdError e) {
			fail();
		}
	}

	@Test
	public void test_short_hex(){
		try {
			id0 = new Id("1");
		} catch (Exception e) {
			return;
		}
		fail();
	}

	@Test
	public void test_long_hex(){
		try {
			id0 = new Id("1234567890123456789012345678901234567890aa");
		} catch (IdError e) {
			return;
		}
		fail();
	}

	@Test
	public void max_id(){
		try {
			id0 = new Id("ffffffffffffffffffffffffffffffffffffffff");
		} catch (IdError e) {
			fail();
		}
		for (int i=0; i<Id.ID_SIZE_BYTES; i++){
			assertEquals(0xffff, (int)(char)id0._bin[i]);
		}

	}

	//	@Test(expected = IdError.class)
	//	public void test_no_hex() throws IdError{
	//		id0 = new Id("123456789012345678901234567890123456789z");
	//	}

	//	@Test(expected = IdError)
	//	public void test_short_hex(){
	//		id = new Id("1");
	//	}
	//	
	//	@Test(expected = IdError)
	//	public void test_short_hex(){
	//		id = new Id("1");
	//	}
	//	
	@Test
	public void test_bin_id(){
		try {
			id1 = new Id(id0._bin);
		} catch (IdError e) {
			fail();
		}
		assertTrue(id0.equals(id1));
	}

	@Test
	public void test_equal(){
		try {
			id1 = new Id(HEX_ID0);
		} catch (IdError e) {
			fail();
		}
		assertTrue(id0.equals(id1));
		assertFalse(id0.equals(id2));
	}

	public void test_str(){

	}

	@Test
	public void test_distance(){
		assertTrue(id0.distance(id1).equals(dist0_1));
		assertTrue(id1.distance(id2).equals(dist1_2));
		assertFalse(id0.distance(id2).equals(dist1_2));
	}

	@Test
	public void test_compare(){
		assertTrue(id0.compareTo(id0) == 0);
		assertTrue(id1.compareTo(id1) == 0);
		assertTrue(id2.compareTo(id2) == 0);
		assertTrue(id0.compareTo(id1) < 0);
		assertTrue(id1.compareTo(id0) > 0);
		assertTrue(id1.compareTo(id2) < 0);
		assertTrue(id2.compareTo(id1) > 0);
	}

	@Test
	public void test_log_distance(){
		assertEquals(-1, id0.log_distance(id0));
		assertEquals(Id.ID_SIZE_BITS-8, id0.log_distance(id1));
		assertEquals(Id.ID_SIZE_BITS-1, id0.log_distance(id2));
	}
}
