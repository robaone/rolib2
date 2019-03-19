package com.robaone.xml;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XMLDocumentReaderTest {
	XMLDocumentReader reader;
	@Before
	public void setUp() throws Exception {
		reader = new XMLDocumentReader();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRead() throws Exception {
		reader.read(this.getClass().getResourceAsStream("sample.xml"));
		assertEquals("Ansel Robateau",reader.findXPathString("//name"));
		assertEquals(1,reader.findXPathNode("//contact").getLength());
		assertEquals("Ansel Robateau",reader.findXPathString(reader.findXPathNode("//contact").item(0), "name"));
		assertNotNull(reader.toString());
		reader.marshall(new ByteArrayOutputStream());
	}

}
