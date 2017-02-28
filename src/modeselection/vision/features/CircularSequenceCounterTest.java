package modeselection.vision.features;

import static org.junit.Assert.*;

import org.junit.Test;

public class CircularSequenceCounterTest {
	
	public final static byte TAG_1 = 1, TAG_2 = 2;

	@Test
	public void test1() {
		CircularSequenceCounter c = new CircularSequenceCounter(5);
		c.set(0, TAG_1);
		c.set(1, TAG_1);
		c.set(2, TAG_2);
		c.set(3, TAG_1);
		c.set(4, TAG_2);
		c.countAll(TAG_1);
		assertEquals(2, c.getLongest());
		c.countAll(TAG_2);
		assertEquals(1, c.getLongest());
	}

	@Test
	public void test2() {
		CircularSequenceCounter c = new CircularSequenceCounter(5);
		c.set(0, TAG_1);
		c.set(1, TAG_2);
		c.set(2, TAG_2);
		c.set(3, TAG_1);
		c.set(4, TAG_1);
		c.countAll(TAG_2);
		assertEquals(2, c.getLongest());
		c.countAll(TAG_1);
		assertEquals(3, c.getLongest());
	}

	@Test
	public void test3() {
		CircularSequenceCounter c = new CircularSequenceCounter(5);
		c.set(0, TAG_2);
		c.set(1, TAG_2);
		c.set(2, TAG_2);
		c.set(3, TAG_1);
		c.set(4, TAG_1);
		c.countAll(TAG_2);
		assertEquals(3, c.getLongest());
		c.countAll(TAG_1);
		assertEquals(2, c.getLongest());
	}
}
