package com.robaone.net;

import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ROHttpRequestTest {
	private ROHttpRequest request;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		request = new ROHttpRequest(){

			@Override
			protected void handleResponse(int statuscode, InputStream stream,Map<String,List<String>> headers)
					throws Exception {
				System.out.println("Status Code: "+statuscode);
				if(stream != null){
					System.out.println(IOUtils.toString(stream));
				}
				if(headers != null){
					System.out.println("Headers\n=========");
					String[] keys = headers.keySet().toArray(new String[0]);
					for(String key : keys){
						List<String> list = headers.get(key);
						for(int i = 0; i < list.size();i++){
							System.out.println("\t"+key+": "+list.get(i));
						}
					}
				}
			}
			
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHandleResponse() throws Exception {
		request.handleResponse(200, null,null);
	}

	@Test
	public void testWriteLog() {
		request.writeLog("This should not appear");
		request.setWriter(System.out);
		request.writeLog("This should appear");
	}

	@Test
	public void testGetUrlParameters() {
		assertNull(request.getUrlParameters());
		request.setUrlParameters("abc=123");
		assertEquals("abc=123",request.getUrlParameters());
	}

	@Test
	public void testGetUrl() {
		assertNull(request.getUrl());
		request.setUrl("http://google.com");
		assertEquals("http://google.com",request.getUrl());
	}

	@Test
	public void testGetWriter() {
		assertNull(request.getWriter());
		request.setWriter(System.out);
		assertNotNull(request.getWriter());
	}
	
	@Test
	public void testGet() throws Exception {
		request.setWriter(System.out);
		request.setUrl("http://ip.jsontest.com/");
		request.setUrlParameters("");
		//request.sendGet();
	}

	@Test
	public void testPost() throws Exception {
		request.setWriter(System.out);
		request.setUrl("http://ip.jsontest.com/");
		request.setUrlParameters("");
		//request.sendPost();
	}
	
	@Test
	public void testGetHttps() throws Exception {
		request.setWriter(System.out);
		request.setUrl("https://google.com");
		request.setUrlParameters("");
		request.sendGet();
	}
}
