package com.robaone.xml;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ROTransformerTest {
	ROTransformer transformer;
	@Before
	public void setUp() throws Exception {
		transformer = new ROTransformer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransform() throws Exception {
		String sampleXsl = IOUtils.toString(this.getClass().getResourceAsStream("sample.xsl"));
		String xmlDocument = IOUtils.toString(this.getClass().getResourceAsStream("sample.xml"));
		String resultXml = this.transformer.transform(sampleXsl, xmlDocument);
		assertNotNull(resultXml);
		assertEquals("Ansel Robateau",resultXml);
	}
	
	@Test
	public void testTransformInputStream() throws Exception {
		InputStream sampleXsl = this.getClass().getResourceAsStream("sample.xsl");
		InputStream xmlDocument = this.getClass().getResourceAsStream("sample.xml");
		this.transformer = new ROTransformer(sampleXsl);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		this.transformer.transformToStream(xmlDocument,bout);
		String resultXml = bout.toString();
		assertNotNull(resultXml);
		assertEquals("Ansel Robateau",resultXml);
	}

	@Test
	public void testTransformInitializedWithStylesheet() throws Exception {
		String sampleXsl = IOUtils.toString(this.getClass().getResourceAsStream("sample.xsl"));
		String xmlDocument = IOUtils.toString(this.getClass().getResourceAsStream("sample.xml"));
		this.transformer = new ROTransformer(sampleXsl);
		String resultXml = this.transformer.transform(xmlDocument);
		assertNotNull(resultXml);
		assertEquals("Ansel Robateau",resultXml);
	}
	
}
