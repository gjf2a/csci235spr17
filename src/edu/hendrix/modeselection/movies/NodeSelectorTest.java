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
		assertEquals(1, sample.getCurrentIndex());
		
		NodeSelector ns = new NodeSelector(sample.createFrom(4, 1));
		assertEquals(4, ns.size());
		
		
	}

}
