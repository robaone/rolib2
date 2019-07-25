package com.robaone.dbase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBManagerTest {
	DBManager manager;
	Properties properties;
	@Before
	public void setUp() throws Exception {
		manager = new DBManager();
		properties = new Properties();
		properties.load(this.getClass().getResourceAsStream("db.properties"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetConnection() throws Exception {
		String driver = properties.getProperty("jdbc.driverClassName");
		String url = properties.getProperty("jdbc.url");
		String username = "";
		String password = "";
		Connection connection = DBManager.getConnection(driver, url, username, password);
		assertNotNull(connection);
		connection.close();
	}

}
