package com.robaone.json;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONToXMLTest {
	JSONToXML converter;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		converter = new JSONToXML();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() {
		String str = "{\"field\":\"value\"}";
		converter.setJson(str);
		converter.run();
		String xml = converter.getXml();
		assertNull(converter.getException());
		assertEquals("<json><field>value</field></json>",xml);
	}
	
	@Test
	public void testNumericKey() {
		String str = "{\"012345\":\"value\"}";
		converter.setJson(str);
		converter.run();
		String xml = converter.getXml();
		assertNull(converter.getException());
		assertEquals("<json><_012345>value</_012345></json>",xml);
	}
	
	@Test
	public void testNumericStartKey() {
		String str = "{\"0abcdefg\":\"value\"}";
		converter.setJson(str);
		converter.run();
		String xml = converter.getXml();
		assertNull(converter.getException());
		assertEquals("<json><_0abcdefg>value</_0abcdefg></json>",xml);
	}
	
	@Test
	public void testAllInvalidStartKey() {
		String str = "{\"0abcdefg\":[{\"xml\":\"This should not start with xml\"},{\"abc_123 and that is that?\":\"This should not have special characters or spaces\"}]}";
		converter.setJson(str);
		converter.run();
		String xml = converter.getXml();
		assertNull(converter.getException());
		assertEquals("<json><_0abcdefg><_xml>This should not start with xml</_xml></_0abcdefg><_0abcdefg><abc_123_and_that_is_that_>This should not have special characters or spaces</abc_123_and_that_is_that_></_0abcdefg></json>",xml);
	}

}
