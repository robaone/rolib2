package com.robaone.xml;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLPathParserTest {
	XMLPathParser parser;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		parser = new XMLPathParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() {
		String path = "/name/first/text()";
		String xml = "<name><first>First</first><last>Last</last></name>";
		parser.setPath(path);
		parser.setXml(xml);
		parser.run();
		assertNull(parser.getException());
		assertEquals("First",parser.getOutput());
	}

}
