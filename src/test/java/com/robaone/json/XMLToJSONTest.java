package com.robaone.json;

import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLToJSONTest {
	XMLToJSON converter;
	String xml;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		converter = new XMLToJSON();
		xml = IOUtils.toString(this.getClass().getResourceAsStream("sample.xml"),"UTF-8");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToJSONObject() throws Exception {
		JSONObject jo_reference = XML.toJSONObject(xml);
		JSONObject jo = converter.toJSONObject(xml);
		assertNotNull(jo_reference);
		assertNotNull(jo);
		System.out.println(jo_reference.toString());
		System.out.println(jo.toString());
		assertEquals("01",jo.getJSONObject("parent").getJSONArray("string_array").getString(0));
		assertEquals("my name",jo.getJSONObject("parent").getJSONObject("object").getJSONObject("field_with_attr").getString("@name"));
	}

}
