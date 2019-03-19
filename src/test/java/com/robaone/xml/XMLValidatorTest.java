package com.robaone.xml;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.robaone.test.BaseTest;

public class XMLValidatorTest extends BaseTest {
	XMLValidator validator;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateString() throws Exception {
		String xsd = IOUtils.toString(this.getClass().getResourceAsStream("folder_settings.xsd"));
		validator = new XMLValidator(xsd);
		String xml = IOUtils.toString(this.getClass().getResourceAsStream("folder_settings.xml"));
		validator.validate(xml);
	}

}
