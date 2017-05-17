package edu.hendrix.modeselection.movies;

import static org.junit.Assert.*;

import org.junit.Test;

public class MovieTest {

	@Test
	public void testIntPart() {
		assertEquals(1, Movie.getIntPart("1.yuyv"));
		assertEquals(22, Movie.getIntPart("22.yuyv"));
	}

}
