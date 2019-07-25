package com.robaone.dbase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionBlockTest {
	ConnectionBlock create = new ConnectionBlock() {

		@Override
		protected void run() throws Exception {
			String sql = IOUtils.toString(this.getClass().getResourceAsStream("create_schema.sql"));
			this.prepareStatement(sql);
			this.executeUpdate();
		}

	};
	ConnectionBlock select = new ConnectionBlock() {

		@Override
		protected void run() throws Exception {
			String sql = IOUtils.toString(this.getClass().getResourceAsStream("select_records.sql"));
			this.prepareStatement(sql);
			this.executeQuery();
			int count = 0;
			while (this.next()) {
				String string = this.getResultSet().getString(1);
				assertNotNull(string);
				System.out.println(string);
				count++;
			}
			assertTrue(count > 0);
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
	public void testSelect() throws Exception {
		select.run(this.connectionManager);
	}
	
	@Test
	public void testInsertSelect() throws Exception {
		insert.run(this.connectionManager);
		select.run(this.connectionManager);
	}

}
