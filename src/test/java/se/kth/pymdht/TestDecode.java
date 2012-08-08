package se.kth.pymdht;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;

import se.kth.pymdht.Bencode.BencodeError;

public class TestDecode{
	
	@Before
	public void setUp(){
		;
	}
	
//	@Test
//	public void t(){
//		//Encode
//		Bencode b_en = new Bencode();
//        SortedMap rootDictionary = (SortedMap) b_en.getRootElement();
//        SortedMap info;
//		
//        Bencode b_de = new Bencode();
//        b_de.setRootElement(info);
//
//
//
//	}

	@Test
	public void test_dict(){
		
		String str = "d2:kk1:ve";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			fail();
		}
		SortedMap<ByteBuffer, Object> dict = (SortedMap<ByteBuffer, Object>) b.getRootElement();
		SortedMap<ByteBuffer, Object> d = dict;
		assertEquals(1, d.size());
		ByteBuffer k = ByteBuffer.wrap("kk".getBytes());
		ByteBuffer v = ByteBuffer.wrap("v".getBytes());
		assertTrue(d.containsKey(k));
		assertEquals(v, d.get(k));
	}
	
	@Test
	public void test_int(){
		
		String str = "i22e";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			fail();
		}
		assertEquals(new Long(22), (Long) b.getRootElement());
	}
	
	@Test
	public void test_str(){
		String str = "3:a8z";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			fail();
		}
		assertEquals(ByteBuffer.wrap("a8z".getBytes()), (ByteBuffer) b.getRootElement());
	}
	
	@Test
	public void test_list(){
		String str = "l3:one3:twoe";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			fail();
		}
		List<ByteBuffer> l = (List<ByteBuffer>) b.getRootElement();
		assertEquals(ByteBuffer.wrap("one".getBytes()), l.get(0));
		assertEquals(ByteBuffer.wrap("two".getBytes()), l.get(1));
	}
	
	@Test
	public void bad_list(){
		String str = "l3:one3:two";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void bad_dict(){
		String str = "d2:kk1:v";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void bad_str(){
		String str = "dd";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void bad_str2(){
		String str = "di2e";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void evil_str_size(){
		String str = "0:";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void evil_str_size2(){
		String str = "999999999999999999999999999999999999999999:";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void too_short_str(){
		String str = "3:a";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void extra_chars(){
		String str = "3:jdfls";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void extra_chars2(){
		String str = "d2:kk1:veXX";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}

	@Test
	public void when_long_is_too_short(){
		String str = "i999999999999999999999999999999999999999999e";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}

	//TODO@Test
	public void deep_recursion(){
		String str = "lllllllleeeeeeee";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			return;
		}
		fail();
	}
	
	@Test
	public void real_msg(){
		String str = 
"d1:rd2:id20:abcdefghij01234567895:token8:aoeusnth6:valuesl6:axje.u6:idhtnmee1:t2:aa1:y1:re";
		InputStream in =  new ByteArrayInputStream(str.getBytes());
		Bencode b = null;
		try {
			b = new Bencode(in);
		} catch (BencodeError e) {
			fail();
		}
		Map<ByteBuffer, Object> d = (Map<ByteBuffer, Object>) b.getRootElement();
		Object elem = d.get(ByteBuffer.wrap("t".getBytes()));
		assertNotNull(elem);
		ByteBuffer tid = (ByteBuffer) d.get(ByteBuffer.wrap("t".getBytes()));
		assertEquals("aa", new String(tid.array()));
		
		elem = d.get(ByteBuffer.wrap("y".getBytes()));
		assertNotNull(elem);
		ByteBuffer type = (ByteBuffer) d.get(ByteBuffer.wrap("y".getBytes()));
		assertEquals("r", new String(type.array()));
		
		elem = d.get(ByteBuffer.wrap("r".getBytes()));
		assertNotNull(elem);
		d = (Map<ByteBuffer, Object>) d.get(ByteBuffer.wrap("r".getBytes()));
		
		elem = d.get(ByteBuffer.wrap("id".getBytes()));
		assertNotNull(elem);
		ByteBuffer id = (ByteBuffer) d.get(ByteBuffer.wrap("id".getBytes()));
		assertEquals("abcdefghij0123456789", new String(id.array()));
		
		elem = d.get(ByteBuffer.wrap("values".getBytes()));
		assertNotNull(elem);
		List<ByteBuffer> peers = (List<ByteBuffer>) d.get(ByteBuffer.wrap("values".getBytes()));
		assertEquals(2, peers.size());
		
		elem = peers.get(0);
		assertNotNull(elem);
		ByteBuffer c_peer = (ByteBuffer) peers.get(0);
		assertEquals("axje.u", new String(c_peer.array()));
	}
	
//	@Test
//	public void test_decode(){
//		for(i,expected,bencoded:enumerate(test_data)){data = null;
//		try{data = decode(bencoded);
//		}catch (Exception){debug_print(i,bencoded,expected,e);
//		};
//		;
//		};
//	}
//	public void test_decode_error(){
//		for(i,bencoded,expected:enumerate(test_data_decode_error)){try{decode(bencoded);
//		}catch (expected){;
//		};
//		catch (Exception){debug_print(i,bencoded,expected,e);
//		};
//		;
//		};
//	}
//	public void test_decode_unexpected_error(){
//		this.assertRaises(DecodeError,decode,"llee","z");
//	}
}
