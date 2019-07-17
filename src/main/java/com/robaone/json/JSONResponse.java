package com.robaone.json;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONArray;
import org.json.JSONObject;

import com.robaone.rs.ResponseUtil;


@XmlRootElement(name = "response")
public class JSONResponse<D> {
	public static final int OK = 0;
	public static final int FIELD_VALIDATION_ERROR = 2;
	public static final int LOGIN_REQUIRED = 3;
	public static final int GENERAL_ERROR = 1;
	public static final int CLOSED_FOR_BUSINESS = 4;

	private int status;
	private HashMap<String,String> mErrors = new HashMap<String,String>();
	private Vector<D> mData;
	private int startRow;
	private int endRow;
	private int totalRows;
	private String generalerror;
	private int page;
	private Properties mProps;
	public JSONResponse(){}

	public void setStatus(int i) {
		this.status = i;
	}

	public int getStatus(){
		return this.status;
	}
	public void setError(String message){
		this.generalerror = message;
	}
	public String getError(){
		return this.generalerror;
	}
	public void addError(String field, String message) {
		this.mErrors.put(field, message);
	}
	public HashMap<String,String> getErrors(){
		return this.mErrors;
	}

	protected void setErrors(
			HashMap<String, String> mErrors) {
		this.mErrors = mErrors;
	}
	public Vector<D> getData(){
		if(this.mData == null){
			this.mData = new Vector<D>();
		}
		return this.mData;
	}
	public void addData(D data){
		if(this.mData == null){
			this.mData = new Vector<D>();
		}
		this.mData.add(data);
		this.recalc();
	}

	public void recalc() {
		this.setTotalRows(this.getData().size());
		this.setEndRow(this.getData().size() > 0 ? this.getData().size() -1 : 0);
		this.setStartRow(0);
	}

	public void setStartRow(int startRows) {
		this.startRow = startRows;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow < 0 ? 0 : endRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalRows() {
		return totalRows < getEndRow() ? (getEndRow() + 1) : totalRows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int p){
		this.page = p;
	}
	public Properties getProperties(){
		if(mProps == null){
			mProps = new Properties();
		}
		return mProps;
	}

	@Override
	public String toString() {
		try{
			JSONObject jsonObject = toJSONObject();
			String retval = jsonObject.toString();
			return retval;
		}catch(Exception e){
			return super.toString();
		}
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new org.json.JSONObject(this);
		jsonObject.remove("class");
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void addArray(JSONArray array) throws Exception {
		for(int i = 0; i < array.length();i++){
			this.addData((D)array.get(i));
		}
		this.setEndRow(array.length() > 0 ? array.length() - 1 : 0);
		this.setTotalRows(array.length());
	}

	public void captureException(Exception e) {
		ResponseUtil.captureException(this, e);
	}
}