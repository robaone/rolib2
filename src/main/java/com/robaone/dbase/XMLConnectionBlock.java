package com.robaone.dbase;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.robaone.FieldValidator;
import com.robaone.xml.XMLDocumentReader;

public abstract class XMLConnectionBlock<DTO> extends ConnectionBlock {
	private static final String STARTINDEX = "start_index";
	private static final String ENDINDEX = "end_index";
	private static final String RESULTPAGE = "result_page";
	private static final String RESULTLIMIT = "result_limit";
	private static final String OFFSET = "offset";
	private JSONObject parameters;
	private int page;
	private int limit;
	private int totalRows;
	private int startRow;
	private int endRow;
	private String queryName;
	private XMLDocumentReader reader;
	private XMLDocumentReader parameterReader;
	private boolean isQuery = true;
	private int updated;
	private ArrayList<DTO> records = new ArrayList<DTO>();
	private HashMap<String,String> errors = new HashMap<String,String>();
	private static Logger logger = Logger.getLogger(XMLConnectionBlock.class.getName());
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public JSONObject getParameters() {
		return parameters;
	}
	public void setParameters(JSONObject parameters) {
		this.parameters = parameters;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) throws JSONException {
		this.page = page;
		this.setParameter(RESULTPAGE, page);
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) throws JSONException {
		this.limit = limit;
		this.setParameter(RESULTLIMIT, limit);
	}
	public DTO getRecord(int index){
		return this.records.get(index);
	}
	public int getRecordCount(){
		return this.records.size();
	}
	public ArrayList<DTO> getRecords(){
		return this.records;
	}
	public XMLConnectionBlock(String queryName,int page,int limit) throws ParserConfigurationException, JSONException{
		this.setPage(page);
		this.setLimit(limit);
		this.setQueryName(queryName);
		this.parameterReader = new XMLDocumentReader();
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getQueryName(){
		return this.queryName;
	}
	@Override
	protected void run() throws Exception {
		initialize();
		if(this.isQuery()){
			this.executeQuery(this.getQueryName());
		}else{
			this.executeUpdate(this.getQueryName());
		}
	}
	protected void initialize() {
		this.records.clear();
		this.errors.clear();
	}
	private void executeUpdate(String queryName) throws Exception {
		this.buildQueryDoc();
		setPage();
		this.prepareStatement(getQueryStatement());
		this.applyParameters(this.getQueryName());
		this.setUpdated(this.executeUpdate());
	}
	private void setUpdated(int executeUpdate) {
		this.updated = executeUpdate;
	}
	public int getUpdated(){
		return this.updated;
	}
	protected void applyParameters(String queryName) throws Exception {
		NodeList parameters = getParameters(queryName);
		for(int i = 0; i < parameters.getLength();i++){
			Node item = parameters.item(i);
			this.setPreparedParameter(item,i+1);
		}
		if(this.errors.size() > 0){
			throw new QueryParameterException(this.errors);
		}
	}
	private void setPreparedParameter(Node item,int parameterIndex) throws SQLException {
		String name = getNodeAttributeValue(item,"name");
		String type = getNodeAttributeValue(item,"type");
		try{
			Object o = null;
			o = getParameterValue(item, name, type);
			if(o == JSONObject.NULL){
				this.getPS().setNull(parameterIndex, getAttributeType(type));
			}else{
				this.getPS().setObject(parameterIndex, o);
			}
		}catch(Exception e){
			addFieldError(name,e.getMessage());
		}
	}
	protected Object getParameterValue(Node item, String name, String type)
			throws Exception {
		Object o = null;
		if("node".equals(type)){
			o = this.getQueryDocument().findXPathString(item, ".");
		}else if("xpath".equals(type)){
			String xpath = this.getParameterQuery(item);
			NodeList nodes = this.getParameterReader().findXPathNode("/parameter/"+name);
			if(nodes.getLength() > 0){
				Node node = nodes.item(0);
				String node_value = this.getParameterReader().findXPathString(node,xpath);
				o = node_value;
			}else{
				throw new JSONException("Could not find xpath, "+xpath+" for "+name);
			}
		}else{
			if(!this.getParameters().has(name)){
				throw new JSONException("You must enter a ("+type+") value");
			}
			if(!this.getParameters().isNull(name)){
				o = this.getParameters().get(name);
			}
		}
		o = formatValue(o,name,type);
		return o;
	}
	protected String getParameterQuery(Node item) throws Exception {
		String element_value = this.getQueryDocument().findXPathString(item, ".");
		if(!FieldValidator.exists(element_value)){
			element_value = ".";
		}
		return element_value;
	}
	public Object formatValue(Object obj,String name,String type){
		return obj;
	}
	public int getAttributeType(String textContent) {
		if(textContent.equals("string")){
			return java.sql.Types.VARCHAR;
		}else if(textContent.equals("int")){
			return java.sql.Types.INTEGER;
		}else if(textContent.equals("date")){
			return java.sql.Types.DATE;
		}
		return 0;
	}
	private String getNodeAttributeValue(Node item,String name) {
		Node attribute = item.getAttributes().getNamedItem(name);
		String value = attribute.getTextContent();
		return value;
	}
	private void addFieldError(String name, String message) {
		this.errors.put(name,message);
	}
	protected String getQueryStatement() throws Exception {
		String query = this.getQueryDocument().findXPathString("//ResultSet[@name=\""+this.getQueryName()+"\"]//PreparedStatement");
		logger.fine(query);
		return query;
	}
	protected String getCountQueryStatement() throws Exception {
		String query = this.getQueryDocument().findXPathString("//ResultSet[@name=\""+this.getQueryName()+"_count\"]//PreparedStatement");
		logger.fine(query);
		return query;
	}
	protected Integer getParameterCount(String queryName) throws Exception {
		String value = this.getQueryDocument().findXPathString("count(//ResultSet[@name=\""+queryName+"\"]//Parameter)");
		return new Integer(value);
	}
	protected NodeList getParameters(String queryName) throws Exception {
		NodeList nodes = getQueryDocument().findXPathNode("//ResultSet[@name=\""+queryName+"\"]//Parameter");
		return nodes;
	}
	public XMLDocumentReader getQueryDocument() throws ParserConfigurationException {
		if(this.reader == null){
			this.setQueryDocument(new XMLDocumentReader());
		}
		return this.reader;
	}
	protected void setQueryDocument(XMLDocumentReader reader) {
		this.reader = reader;
	}
	protected void setPage() throws JSONException {
		int startindex = ( this.getLimit() * this.getPage() ) + 1;
		int endindex = startindex + this.getLimit() - 1;
		int offset = startindex - 1;
		this.setParameter(STARTINDEX,startindex);
		this.setParameter(ENDINDEX,endindex);
		this.setParameter(OFFSET,offset);
	}
	public void setParameter(String name, Object value) throws JSONException {
		if(this.parameters == null){
			this.parameters = new JSONObject();
		}
		this.parameters.put(name, value);
	}
	protected void executeQuery(String queryName) throws Exception {
		this.buildQueryDoc();
		setPage();
		this.prepareStatement(getQueryStatement());
		this.applyParameters(this.getQueryName());
		this.executeQuery();
		loadRecords();
		this.getPreparedStatement().close();
		this.getResultSet().close();
		this.prepareStatement(getCountQueryStatement());
		this.applyParameters(this.getQueryName()+"_count");
		this.executeQuery(0);
		loadRecordCount();
		int endindex = 0;
		endindex = this.getRecords().size()+(this.getPage()*this.getLimit()) - 1;
		if(endindex < 0){
			endindex = 0;
		}else if(endindex+1 > this.getTotalRows()){
			endindex = this.getTotalRows() > 0 ? this.getTotalRows() - 1 : 0;
		}
		this.setEndRow(endindex);
		try{
			this.setStartRow(Integer.parseInt(this.parameterReader.findXPathString(STARTINDEX)));
		}catch(Exception e){
			int startindex = this.getParameters().getInt(RESULTPAGE) * this.getParameters().getInt(RESULTLIMIT);
			if(startindex > endindex){
				startindex = endindex;
			}
			this.setStartRow(startindex);
		}
		logger.fine("RecordCount = "+this.getRecordCount() + ": TotalRecords = "+this.getTotalRows());
	}
	protected void loadRecordCount() throws SQLException {
		if(next()){
			int count = this.getResultSet().getInt(1);
			this.setTotalRows(count);
		}
	}
	protected void loadRecords() throws SQLException, Exception {
		while(next()){
			DTO record = this.bindRecord();
			this.addRecord(record);
		}
	}
	protected void addRecord(DTO record) {
		this.records.add(record);
	}
	abstract protected DTO bindRecord() throws Exception;
	
	protected void buildQueryDoc() throws Exception {
		this.getQueryDocument().read(getQueryFile());
		this.parameterReader.read(XML.toString(this.parameters, "parameter"));
	}
	protected InputStream getQueryFile() throws Exception {
		String filename = this.getClass().getName().split("[.]")[this.getClass().getName().split("[.]").length-1];
		filename += ".xml";
		InputStream in = this.getClass().getResourceAsStream(filename);
		return in;
	}
	protected XMLDocumentReader getParameterReader() {
		return this.parameterReader;
	}
	public boolean isQuery() {
		return isQuery;
	}
	public void setAsQuery() {
		this.isQuery = true;
	}
	public void setAsUpdate() {
		this.isQuery = false;
	}

}
