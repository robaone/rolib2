package com.robaone.xml;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

public class ROTransformTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTransformXMLString() throws Exception {
		ROTransform trn = new ROTransform(getStylesheet());
		String xml = trn.transformXML(getDocument());
		System.out.println(xml);
	}

	private String getDocument() {
		String str = "<doc>\n"+
				"  <timestamp>1415386018085</timestamp>\n"+
				"</doc>";
		return str;
	}

	private String getStylesheet() {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
				"<xsl:stylesheet version=\"1.0\"\n"+
				"\txmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xala"+
				"n=\"http://xml.apache.org/xalan\" xmlns:java=\"http://xml.apach"+
				"e.org/xslt/java\" exclude-result-prefixes=\"java\">\n"+
				"<xsl:template match=\"/\">\n"+
				"   <xsl:value-of select=\"java:java.lang.Double.parseDouble(/doc/timestamp)\"/>\n"+
				"</xsl:template>\n"+
				"</xsl:stylesheet>";
		return str;
	}

}
