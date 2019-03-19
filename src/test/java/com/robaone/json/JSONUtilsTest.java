package com.robaone.json;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONUtilsTest {

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
	public void testRenameField() throws Exception {
		JSONObject jo = new org.json.JSONObject();
		jo.put("test", "testfield");
		JSONUtils.renameField(jo, "test", "newField");
		assertFalse(jo.has("test"));
		assertTrue(jo.has("newField"));
		assertEquals("testfield",jo.getString("newField"));
	}

	@Test
	public void testRenameFieldExistingField() throws Exception {
		JSONObject jo = new org.json.JSONObject();
		jo.put("test", "testfield");
		jo.put("newField", "value");
		try{
			JSONUtils.renameField(jo, "test", "newField");
			fail("Exception not thrown");
		}catch(Exception e){
			assertEquals("Field, newField, already exists",e.getMessage());
		}
	}

	@Test
	public void testRenameFieldMissingName() throws Exception {
		JSONObject jo = new org.json.JSONObject();
		jo.put("test", "testfield");
		jo.put("newField", "value");
		try{
			JSONUtils.renameField(jo, null, "newField");
			fail("Exception not thrown");
		}catch(Exception e){
			assertEquals("Current field name not specified",e.getMessage());
		}
	}

	@Test
	public void testRenameFieldMissingNewName() throws Exception {
		JSONObject jo = new org.json.JSONObject();
		jo.put("test", "testfield");
		jo.put("newField", "value");
		try{
			JSONUtils.renameField(jo, "test", null);
			fail("Exception not thrown");
		}catch(Exception e){
			assertEquals("New field name not specified",e.getMessage());
		}
	}

	@Test
	public void testJSONPath() throws Exception {
		JSONObject jo = new org.json.JSONObject();
		jo.put("test", "testfield");
		jo.put("newField", "value");
		Object o = JSONUtils.getJsonPath(jo, "test");
		assertEquals("testfield",o.toString());
	}

	@Test
	public void testJSONPath2() throws Exception {
		JSONObject jo = new org.json.JSONObject("{\"body\":\"This is the body\",\"properties\":{\"field\":\"value\"},\"data\":[{\"item\":1},{\"item\":2}]}");
		Object o = JSONUtils.getJsonPath(jo, "body");
		assertEquals("This is the body",o.toString());
		o = JSONUtils.getJsonPath(jo, "properties.field");
		assertEquals("value",o.toString());
		o = JSONUtils.getJsonPath(jo, "data[0].item");
		assertEquals("1",o.toString());
	}

	@Test
	public void testJSONPath3() throws Exception {
		JSONObject jo = new org.json.JSONObject("{\"csv\":[[\"test\"],[\"test2\"]]}");
		Object o = JSONUtils.getJsonPath(jo, "csv[1].[0]");
		assertEquals("test2",o.toString());
		o = JSONUtils.getJsonPath(jo, "csv[0].[0]");
		assertEquals("test",o.toString());

	}

	@Test
	public void testJSONPathFilter() throws Exception {
		JSONObject jo = new org.json.JSONObject("{\"csv\":[[\"test\"],[\"test2\"]]}");
		Object o = JSONUtils.getJsonPath(jo, "csv | length");
		assertEquals("2",o.toString());
		o = JSONUtils.getJsonPath(jo, "csv[0] | length");
		assertEquals("1",o.toString());

	}

	@Test
	public void testJSONPathNotFound() throws Exception {
		JSONObject jo = new org.json.JSONObject("{\"csv\":[[\"test\"],[\"test2\"]]}");
		Object o = null;
		try{
			o = JSONUtils.getJsonPath(jo, " | length");
			fail("Exception not thrown");
		}catch(Exception e){
			assertEquals("Path not found",e.getMessage());
		}
		assertNull(o);
		try{
			o = JSONUtils.getJsonPath(jo, "cav");
			fail("Exception not thrown");
		}catch(Exception e){
			assertEquals("JSONObject[\"cav\"] not found.",e.getMessage());
		}
		assertNull(o);
		try{
			o = JSONUtils.getJsonPath(jo, "csv[23].[1]");
			fail("Exception not thrown");
		}catch(Exception e){
			assertEquals("JSONArray[23] not found.", e.getMessage());
		}
		assertNull(o);

	}
	
	@Test
	public void testNormalize() throws Exception {
		String str = "{\"object\":\"{\\\"field\\\":\\\"[\\\\\\\"value\\\\\\\"]\\\"}\"}";
		String normalized = JSONUtils.normalize(str);
		assertEquals("{\"object\":{\"field\":[\"value\"]}}",normalized);
	}
	
	@Test
	public void testFieldsToLowerCaseNonRecursive() throws Exception {
		JSONObject data = new org.json.JSONObject();
		data.put("FIELD1", "value");
		data.put("properties", new org.json.JSONObject());
		data.getJSONObject("properties").put("FIELD2", "value2");
		JSONObject lowercase = JSONUtils.fieldsToLowerCase(data, false);
		assertEquals("value",lowercase.get("field1"));
		assertEquals("value2",lowercase.getJSONObject("properties").getString("FIELD2"));
	}
	
	@Test
	public void testFieldsToLowerCaseRecursive() throws Exception {
		JSONObject data = new org.json.JSONObject();
		data.put("FIELD1", "value");
		data.put("properties", new org.json.JSONObject());
		JSONArray array = new org.json.JSONArray();
		data.put("array", array);
		array.put("test");
		JSONObject obj = new org.json.JSONObject();
		obj.put("ARRAYFIELD", "array_value");
		array.put(obj);
		data.getJSONObject("properties").put("FIELD2", "value2");
		JSONObject lowercase = JSONUtils.fieldsToLowerCase(data, true);
		assertEquals("value",lowercase.get("field1"));
		assertEquals("value2",lowercase.getJSONObject("properties").getString("field2"));
		assertEquals("array_value",lowercase.getJSONArray("array").getJSONObject(1).getString("arrayfield"));
	}
}
