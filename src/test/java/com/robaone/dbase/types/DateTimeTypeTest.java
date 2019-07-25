package com.robaone.dbase.types;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateTimeTypeTest {
	DateTimeType type;
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetNewObject() throws Exception {
		String date = "2017-05-03";
		type = new DateTimeType(date);
		System.out.println(type);
		assertNotNull(type);
	}
	
	@Test
	public void testGetNewObject2() throws Exception {
		String date = "2017-05-03 10:16";
		type = new DateTimeType(date);
		System.out.println(type);
		assertNotNull(type);
	}
	
	@Test
	public void testGetNewObject3() throws Exception {
		String date = "2017-05-03 10:16:34";
		type = new DateTimeType(date);
		System.out.println(type);
		assertNotNull(type);
	}
	
	@Test
	public void testGetNewObject4() throws Exception {
		String date = "2017-05-03 10:16:34.234";
		type = new DateTimeType(date);
		System.out.println(type);
		assertNotNull(type);
	}
	
	@Test
	public void testGetNewObject5() throws Exception {
		java.util.Date dt = new java.util.Date();
		type = new DateTimeType(dt);
		System.out.println(type);
		assertNotNull(type);
	}
	
	@Test
	public void testGetTimestamp() throws Exception {
		type = new DateTimeType(new java.util.Date());
		assertNotNull(type.getTimestamp());
	}
	
	@Test
	public void testToFormattedString1() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.SQL_FORMAT_TO_MINUTE);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testToFormattedString2() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.SQL_FORMAT_DATE_ONLY);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testToFormattedString3() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.SQL_FORMAT);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testToFormattedString4() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.SQL_FORMAT_FULL);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testToFormattedString5() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.US_FORMAT);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testToFormattedString6() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.US_FORMAT_COMPRESSED);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testToFormattedString7() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.US_FORMAT_DATE_ONLY);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testToFormattedString8() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.US_FORMAT_FULL);
		System.out.println(str);
		assertNotNull(str);
	}
	
	@Test
	public void testUSFormatFullLowerCase() throws Exception {
		type = new DateTimeType(new java.util.Date());
		String str = type.toFormattedString(DateTimeType.US_FORMAT_FULL);
		System.out.println(str.toLowerCase());
		assertNotNull(str);
	}
	
	@Test
	public void testInvalidFormat() {
		try{
			String date = "abc2017-05-03 10:16";
			type = new DateTimeType(date);
			fail("Exception not thrown");
		}catch(Exception e){
			assertNotNull(e);
			System.err.println(e.getMessage());
		}
	}

}
