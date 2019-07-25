package com.robaone.dbase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HDBConnectionManagerTest {
	HDBConnectionManager manager;
	@Before
	public void setUp() throws Exception {
		manager = new HDBConnectionManager() {

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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetConnection() throws Exception {
		Connection con = manager.getConnection();
		assertNotNull(con);
		con.close();
	}
	
	@Test
	public void testCloseConnection() throws Exception {
		Connection con = new LoggedConnection(manager.getConnection(),Logger.getGlobal());
		assertNotNull(con);
		manager.closeConnection(con);
		assertTrue(con.isClosed());
	}

}
