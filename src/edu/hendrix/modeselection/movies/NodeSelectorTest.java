package edu.hendrix.modeselection.movies;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class NodeSelectorTest {

	@Test
	public void test() throws FileNotFoundException {
		Movie sample = new Movie(new File("demoMovie"));
		assertEquals(33, sample.size());
		assertEquals(1, sample.getFrameNumber());
		
		NodeSelector ns = new NodeSelector(sample.createFrom(4, 1));
		assertEquals(4, ns.size());
		
		ns.next();
		assertEquals(1, ns.getCurrentIndex());
		assertEquals(1, ns.getCurrentNode());
		ns.remove();
		assertEquals(2, ns.getCurrentNode());
		assertEquals(1, ns.getCurrentIndex());
	}

}
