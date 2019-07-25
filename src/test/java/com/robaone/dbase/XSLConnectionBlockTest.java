package com.robaone.dbase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XSLConnectionBlockTest {
	ConnectionBlock create = new ConnectionBlock() {

		@Override
		protected void run() throws Exception {
			String sql = IOUtils.toString(this.getClass().getResourceAsStream("create_schema.sql"));
			this.prepareStatement(sql);
			this.executeUpdate();
		}

	};
	XSLConnectionBlock<String> select; 
	class TestBlock extends XSLConnectionBlock<String> {

		public TestBlock() throws ParserConfigurationException, JSONException {
			super("select", 0, 100);
		}

		@Override
		protected String bindRecord() throws Exception {
			String name = this.getResultSet().getString(1) + " " + this.getResultSet().getString(2);
			return name;
		}

	};
	ConnectionBlock insert = new ConnectionBlock() {

		@Override
		protected void run() throws Exception {
			String sql = IOUtils.toString(this.getClass().getResourceAsStream("insert_records.sql"));
			this.prepareStatement(sql);
			this.executeUpdate();
		}

	};
	ConnectionBlock delete = new ConnectionBlock() {

		@Override
		protected void run() throws Exception {
			String sql = IOUtils.toString(this.getClass().getResourceAsStream("delete_schema.sql"));
			this.prepareStatement(sql);
			this.executeUpdate();
		}

	};
	HDBConnectionManager connectionManager = new HDBConnectionManager() {
		
		@Override
		public Connection getConnection() throws Exception {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("db.properties"));
			String driver = properties.getProperty("jdbc.driverClassName");
			String url = properties.getProperty("jdbc.url");
			String username = "";
			String password = "";
			return DBManager.getConnection(driver, url, username, password);
		}

		@Override
		public void closeConnection(Connection m_con) throws Exception {
			m_con.close();
		}
	};
	@Before
	public void setUp() throws Exception {
		create.run(this.connectionManager);
	}

	@After
	public void tearDown() throws Exception {
		delete.run(this.connectionManager);
	}

	@Test
	public void testInsertSelect() throws Exception {
		insert.run(this.connectionManager);
		select = new TestBlock();
		select.setParameter("firstname","Ansel");
		select.run(this.connectionManager);
		assertEquals(1,select.getRecordCount());
		assertEquals("Ansel Robateau",select.getRecord(0));
	}
	
	@Test
	public void testInsertSelectAll() throws Exception {
		insert.run(this.connectionManager);
		select = new TestBlock();
		select.run(this.connectionManager);
		assertEquals(2,select.getRecordCount());
		assertEquals("Ansel Robateau",select.getRecord(0));
		assertEquals("Luke Smormon",select.getRecord(1));
	}

}
