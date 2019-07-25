package com.robaone.dbase;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggedConnection implements Connection {
	private Connection con;
	private Logger logger;

	public LoggedConnection(Connection con,Logger l){
		this.con = con;
		this.logger = l;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.con.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.con.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		return this.con.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		logger.log(Level.INFO,sql);
		return con.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		logger.log(Level.INFO,sql);
		return con.prepareCall(sql);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		logger.log(Level.INFO,sql);
		return con.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		con.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return con.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		con.commit();
	}

	@Override
	public void rollback() throws SQLException {
		con.rollback();
	}

	@Override
	public void close() throws SQLException {
		con.close();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return con.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return con.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		con.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return con.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		con.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return con.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		con.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return con.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return con.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		con.clearWarnings();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return con.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return con.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		logger.log(Level.FINE,sql);
		return con.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return con.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		con.setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		con.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return con.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return con.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return con.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		con.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		con.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return con.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		logger.log(Level.INFO,sql);
		return con.prepareStatement(sql, resultSetType, resultSetConcurrency,resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		logger.log(Level.INFO,sql);
		return con.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		logger.log(Level.INFO,sql);
		return con.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		logger.log(Level.INFO,sql);
		return con.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		logger.log(Level.INFO,sql);
		return con.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		return con.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return con.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return con.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return con.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return con.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		con.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		con.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return con.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return con.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return con.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return con.createStruct(typeName, attributes);
	}
	public void abort(Executor arg0) throws SQLException {
		this.con.abort(arg0);
	}
	public int getNetworkTimeout() throws SQLException {
		return this.con.getNetworkTimeout();
	}
	public String getSchema() throws SQLException {
		return con.getSchema();
	}
	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
		con.setNetworkTimeout(arg0, arg1);
	}
	public void setSchema(String arg0) throws SQLException {
		con.setSchema(arg0);
	}
}
