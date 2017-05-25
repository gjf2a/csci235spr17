package edu.hendrix.modeselection.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.hendrix.modeselection.util.Util;

public class NavigatorTest {
	private ArrayNavigator<String> nav = new ArrayNavigator<>();

	@Before
	public void setup() {
		for (String s: new String[]{"a", "b", "c", "d"}) {
			nav.add(s);
		}
		assertEquals(0, nav.getCurrentIndex());
	}
	
	@Test
	public void testNext() {
		for (int i = 0; i < nav.size() * 2; i++) {
			assertEquals(i % nav.size(), nav.getCurrentIndex());
			nav.next();
		}
	}
	
	@Test
	public void testPrev() {
		for (int i = nav.size() - 1; i > -nav.size(); i--) {
			nav.prev();
			assertEquals(Util.trueMod(i, nav.size()), nav.getCurrentIndex());
		}
	}
	
}
