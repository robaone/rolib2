package com.robaone;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldValidatorTest {
	FieldValidator validator;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsURL() {
		String url = "http://www.google.com?search=This%20is%20my%20search";
		assertTrue(FieldValidator.isUrl(url));
		assertFalse(FieldValidator.isUrl(null));
	}

	@Test
	public void testIsURLOtherPort() {
		String url = "http://www.google.com:8080?search=This%20is%20my%20search";
		assertTrue(FieldValidator.isUrl(url));
	}
	
	@Test
	public void testIsNotURL() {
		String url = "ssl://www.google.com?search=This%20is%20my%20search";
		assertFalse(FieldValidator.isUrl(url));
	}
	
	@Test
	public void testIsEmail() {
		String email = "arobateau@citizensrx.com";
		assertTrue(FieldValidator.isEmail(email));
	}
	
	@Test
	public void testIsNotEmail() {
		String not_email = "abc";
		assertFalse(FieldValidator.isEmail(not_email));
	}
	
	@Test
	public void testIsZipCode() {
		String zip = "00112";
		assertTrue(FieldValidator.isZipCode(zip));
	}
	
	@Test
	public void testIsZipCode2() {
		String zip = "00112-1234";
		assertTrue(FieldValidator.isZipCode(zip));
	}
	
	@Test
	public void testIsNotZipCode() {
		String zip = "0012";
		assertFalse(FieldValidator.isZipCode(zip));
	}
	
	@Test
	public void testExists() {
		assertFalse(FieldValidator.exists(null));
		assertFalse(FieldValidator.exists(""));
		assertFalse(FieldValidator.exists(" "));
		assertTrue(FieldValidator.exists("I exist!"));
	}
	
	@Test
	public void testIsNumber() {
		assertFalse(FieldValidator.isNumber(null));
		assertFalse(FieldValidator.isNumber("123abc"));
		assertTrue(FieldValidator.isNumber("123"));
		assertTrue(FieldValidator.isNumber("123.002"));
	}
	
	@Test
	public void testParseDate() throws Exception {
		String date = "1/1/2018";
		java.util.Date date_obj = FieldValidator.parseDate(date);
		assertNotNull(date_obj);
	}
	
	@Test
	public void testParseDate2() throws Exception {
		String date = "1/1/2018 09:12:00.023";
		java.util.Date date_obj = FieldValidator.parseDate(date);
		assertNotNull(date_obj);
	}
	
	@Test
	public void testParseDateNotADate() throws Exception {
		String date = "This is not a date";
		try {
			java.util.Date date_obj = FieldValidator.parseDate(date);
			assertNull(date_obj);
			fail("Excdption not thrown");
		}catch(Exception e) {
			
		}
	}
	
	@Test
	public void testIsDate() {
		assertFalse(FieldValidator.isDate(null));
		assertFalse(FieldValidator.isDate("123"));
		assertTrue(FieldValidator.isDate("2018-01-01"));
		assertTrue(FieldValidator.isDate("1/1/2018"));
		assertTrue(FieldValidator.isDate("1/1/2018 1:23:00.0"));
	}
	
	@Test
	public void testGetDate() throws Exception {
		java.util.Date data = FieldValidator.getDate("1/1/2018");
		assertNotNull(data);
	}
	
	@Test
	public void testMatches() {
		assertTrue(FieldValidator.matches("abc", "[a-z]+"));
		assertFalse(FieldValidator.matches(null, ".+"));
	}
}
