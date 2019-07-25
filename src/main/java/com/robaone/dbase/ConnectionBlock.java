package com.robaone.dbase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * <pre>   Copyright Mar 21, 2012 Ansel Robateau
         http://www.robaone.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.</pre>
 * @author Ansel
 *
 */
public abstract class ConnectionBlock {
	private java.sql.Connection con = null;
	private java.sql.PreparedStatement ps = null;
	private java.sql.ResultSet rs = null;
	private java.sql.CallableStatement call = null;
	private HDBConnectionManager m_cman = null;
	private Integer resultSetIndex = null;
	private Logger m_logger;
	public ConnectionBlock(){
	}
	public ConnectionBlock(Logger l){
		this.m_logger = l;
	}
	public void run(HDBConnectionManager cman) throws Exception{
		try{
			this.setConnectionManager(cman);
			this.run();
		}finally{
			this.close();
		}
	}
	protected abstract void run() throws Exception;
	protected java.sql.Connection getConnection(){
		return this.con;
	}
	protected void prepareStatement(String sql) throws SQLException{
		Connection connection = this.getConnection();
		if(connection != null){
			this.setPreparedStatement(connection.prepareStatement(sql));
		}else{
			throw new SQLException("No database connection");
		}
	}
	protected PreparedStatement getPS(){
		return this.getPreparedStatement();
	}
	protected CallableStatement getCall(){
		return this.getCallableStatement();
	}
	protected void executeCall() throws SQLException{
		this.setResultSet(this.getCallableStatement().executeQuery());
	}
	protected void executeQuery() throws SQLException{
		if(this.getLogger() != null){
			this.getLogger().info("calling PreparedStatement.execute()");
		}
		boolean isresultset = this.getPreparedStatement().execute();
		if(this.getLogger() != null){
			this.getLogger().info("isresultset = "+isresultset);
		}
		//If the resultset index is set, increment to the correct result set
		//else just get the first result set
		if(this.getResultSetIndex() == null){
			while(!isresultset && hasMoreResultSets()){
				isresultset = this.getPreparedStatement().getMoreResults();
			}
			if(isresultset){
				this.setResultSet(this.getPreparedStatement().getResultSet());
			}
		}else{
			for(int index = isresultset ? 1 : 0;index < this.getResultSetIndex();){
				if(!isresultset){
					while(!isresultset && hasMoreResultSets()){
						isresultset = this.getPreparedStatement().getMoreResults();
					}
				}else{
					isresultset = this.getPreparedStatement().getMoreResults();
				}
				if(isresultset){
					index++;
				}
			}
			if(isresultset){
				this.setResultSet(this.getPreparedStatement().getResultSet());
			}
		}
	}
	public boolean hasMoreResultSets() throws SQLException {
		boolean retval = (this.getPreparedStatement().getUpdateCount() == -1);
		return !retval;
	}
	protected void executeQuery(int rindex) throws SQLException{

		boolean isresultset = this.getPreparedStatement().execute();
		//If the resultset index is set, increment to the correct result set
		//else just get the first result set

		for(int index = isresultset ? 1 : 0;index < rindex;){
			if(!isresultset){
				while(!isresultset){
					isresultset = this.getPreparedStatement().getMoreResults();
				}
			}else{
				isresultset = this.getPreparedStatement().getMoreResults();
			}
			if(isresultset){
				index++;
			}
		}
		if(isresultset){
			this.setResultSet(this.getPreparedStatement().getResultSet());
		}

	}
	protected int executeUpdate() throws SQLException {
		return this.getPreparedStatement().executeUpdate();
	}
	protected boolean next() throws SQLException{
		return this.getResultSet().next();
	}
	protected void setConnection(java.sql.Connection connection){
		this.con = connection;
	}
	protected HDBConnectionManager getConnectionManager(){
		return new HDBConnectionManager(){

			public Connection getConnection() throws Exception {
				return con;
			}

			public void closeConnection(Connection m_con) throws Exception {

			}

		};
	}
	protected void setConnectionManager(HDBConnectionManager cman){
		this.m_cman  = cman;
		try{
			if(this.getLogger() != null){
				this.con = new LoggedConnection(this.m_cman.getConnection(),this.getLogger());
			}else{
				this.con = this.m_cman.getConnection();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void setCallableStatement(java.sql.CallableStatement c){
		this.call = c;
	}
	protected java.sql.CallableStatement getCallableStatement(){
		return this.call;
	}
	protected java.sql.PreparedStatement getPreparedStatement(){
		return this.ps;
	}
	protected void setPreparedStatement(java.sql.PreparedStatement p){
		this.ps = p;
	}
	protected java.sql.ResultSet getResultSet(){
		return this.rs;
	}
	protected void setResultSet(java.sql.ResultSet r){
		try {
			if(this.getLogger() != null){
				this.getLogger().info("Setting the resultset: FetchSize = " + r.getFetchSize());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.rs = r;
	}
	protected void close(){
		try{rs.close();}catch(Exception e){}
		try{call.close();}catch(Exception e){}
		try{ps.close();}catch(Exception e){}
		if(m_cman != null){
			try{m_cman.closeConnection(con);}catch(Exception e){}
		}else{
			try{con.close();}catch(Exception e){}
		}
	}
	
	protected JSONArray toJSONArray() throws Exception {
		JSONArray array = new JSONArray();
		ResultSetMetaData rsmdata = this.getResultSet().getMetaData();
		int column_count = rsmdata.getColumnCount();
		while(this.next()){
			JSONObject record_jo = new JSONObject();
			for(int i = 0; i < column_count;i++){
				String name = rsmdata.getColumnLabel(i+1);
				String value = this.getResultSet().getString(i+1);
				record_jo.put(name, value);
			}
			array.put(record_jo);
		}
		return array;
	}
	
	protected JSONObject resultSetToJSONObject() throws Exception {
		ResultSetMetaData rsmdata = this.getResultSet().getMetaData();
		int column_count = rsmdata.getColumnCount();
		JSONObject record_jo = new JSONObject();
		for(int i = 0; i < column_count;i++){
			String name = rsmdata.getColumnLabel(i+1);
			String value = this.getResultSet().getString(i+1);
			record_jo.put(name, value);
		}
		return record_jo;
	}
	public void setLogger(Logger logger){
		this.m_logger = logger;
	}
	public Logger getLogger() {
		if(this.m_logger == null && "true".equalsIgnoreCase(System.getProperty("debug", "false"))){
			Logger print = new StandardOutLogger();
			this.setLogger(print);
		}
		return this.m_logger;
	}
	public Integer getResultSetIndex() {
		return resultSetIndex;
	}
	public void setResultSetIndex(Integer resultSetIndex) {
		this.resultSetIndex = resultSetIndex;
	}
}
