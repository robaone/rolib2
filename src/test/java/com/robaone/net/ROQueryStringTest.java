package com.robaone.net;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ROQueryStringTest {
	ROQueryString query;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		query = new ROQueryString();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() throws UnsupportedEncodingException {
		query.add("key", "value=");
		System.out.println(query.toString());
		assertEquals("key=value%3D",query.toString());
	}
	
	@Test
	public void testAddMap() throws UnsupportedEncodingException {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("key", "value=");
		query = new ROQueryString(map);
		System.out.println(query.toString());
		assertEquals("key=value%3D",query.toString());
	}

}
