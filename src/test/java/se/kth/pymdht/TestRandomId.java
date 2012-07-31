package se.kth.pymdht;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestRandomId {
	@Test
	public void test(){
		Id id0 = new RandomId();
		Id id1 = new RandomId();
		assertFalse(id0.equals(id1));
	}
}