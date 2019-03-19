package com.robaone.json;

import java.io.EOFException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.robaone.FieldValidator;
import com.robaone.util.LineReader;
import com.robaone.xml.XMLDocumentReader;

public class XMLToJSON implements Runnable {
	public class NULL {

	}
	public class NON_EMPTY {
		
	}
	private Exception exception;
	private String json;
	private String xml;
	private XMLDocumentReader reader;
	public static void main(String[] args) {
		try{
			XMLToJSON converter = new XMLToJSON();
			converter.setXml(converter.getInput());
			converter.run();
			if(converter.getException() != null){
				throw converter.getException();
			}
			System.out.println(converter.getJson());
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	@Override
	public void run() {
		try{
			JSONObject json = this.toJSONObject(getXml());
			String parentName = json.names().getString(0);
			String str = json.getJSONObject(parentName).toString();
			this.setJson(str);
		}catch(Exception e){
			this.setException(e);
		}
	}
	
	protected JSONObject toJSONObject(String xml) throws Exception {
		JSONObject retval = new org.json.JSONObject();
		XMLDocumentReader reader = this.getReader();
		reader.read(xml);
		Node root = reader.getDocument().getFirstChild();
		String name = root.getNodeName();
		retval.put(name, new org.json.JSONObject());
		parse(root,retval.getJSONObject(name));
		flatten(retval);
		return retval;
	}
	protected void flatten(JSONObject retval) throws Exception {
		@SuppressWarnings("rawtypes")
		Iterator keys = retval.keys();
		ArrayList<String> keys_to_remove = new ArrayList<String>();
		while(keys.hasNext()){
			Object key = keys.next();
			Object value = retval.get(key.toString());
			if(value instanceof JSONObject){
				JSONObject jo = (JSONObject)value;
				flatten(jo);
				if(jo.length() == 1 && jo.has("#text")){
					retval.put(key.toString(), getNodeValue(jo));
				}else{
					JSONUtils.renameField(jo, "#text", "content");
				}
			}else if(value instanceof JSONArray){
				JSONArray ja = (JSONArray)value;
				if(key.equals("#text")){
					keys_to_remove.add(key.toString());
				}else{
					flatten(ja);
				}
			}else{
				if(!(retval.get(key.toString()) == JSONObject.NULL)){
					retval.put(key.toString(), value.toString().trim());
				}
			}
		}
		for(String key_to_remove :keys_to_remove){
			retval.remove(key_to_remove);
		}
	}
	protected void flatten(JSONArray retval) throws Exception {
		for(int i = 0; i < retval.length();i++){
			Object value = null;
			try{
				value = retval.get(i);
			}catch(JSONException je){
				if(!je.getMessage().contains("not found")){
					throw je;
				}
			}
			if(value instanceof JSONObject){
				JSONObject jo = (JSONObject)value;
				flatten(jo);
				if(jo.length() == 1 && jo.has("#text")){
					retval.put(i,getNodeValue(jo));
				}else{
					JSONUtils.renameField(jo, "#text", "content");
				}
			}else if(value instanceof JSONArray){
				flatten((JSONArray)value);
			}
		}
	}
	public Object getNodeValue(JSONObject jo) throws JSONException {
		Object retval = jo.getString("#text");
		if(!retval.toString().startsWith("0") && FieldValidator.isNumber(retval.toString())){
			retval = new BigDecimal(retval.toString());
		}
		return retval;
	}
	protected void parse(Node root, JSONObject jsonObject) throws Exception {
		NodeList children = root.getChildNodes();
		if(root.hasAttributes()){
			parseObject(root, jsonObject);
		}
		for(int i = 0; i < children.getLength();i++){
			Node child = children.item(i);
			String name = child.getNodeName();
			if(jsonObject.has(name)){
				jsonObject.put(name, new org.json.JSONArray());
			}else if(child.hasChildNodes()){
				jsonObject.put(name, new org.json.JSONObject());
			}else{
				String value = child.getNodeValue();
				if(value == null){
					NULL n = new NULL();
					jsonObject.put(name, n);
				}else{
					jsonObject.put(name, value);
				}
			}
		}
		for(int i = 0; i < children.getLength();i++){
			Node child = children.item(i);
			String name = child.getNodeName();
			Object jsonvalue = jsonObject.get(name);
			if(child.hasAttributes()){
				parseObject(child, jsonvalue);
			}
			if(jsonvalue instanceof JSONObject){
				parse(child,(JSONObject)jsonvalue);
			}else if(jsonvalue instanceof JSONArray){
				JSONArray jsonArray = (JSONArray)jsonvalue;
				if(child.hasChildNodes()){
					JSONObject child_jo = new org.json.JSONObject();
					jsonArray.put(child_jo);
					parse(child,child_jo);
				}else{
					String nodeValue = child.getNodeValue();
					if(child.hasAttributes()){
						JSONObject child_attributes = getAttributes(child);
						jsonArray.put(child_attributes);
					}else{
						jsonArray.put(nodeValue);
					}
				}
			}else if(jsonvalue instanceof NULL){
				if(child.hasAttributes()){
					JSONObject child_attributes = getAttributes(child);
					jsonObject.put(name, child_attributes);
				}else{
					jsonObject.put(name, JSONObject.NULL);
				}
			}
		}
	}
	protected JSONObject getAttributes(Node child) throws JSONException {
		JSONObject child_attributes = new org.json.JSONObject();
		for(int att = 0; att < child.getAttributes().getLength();att++){
			String att_name = child.getAttributes().item(att).getNodeName();
			String att_value = child.getAttributes().item(att).getNodeValue();
			child_attributes.put("@"+att_name, att_value);
		}
		return child_attributes;
	}
	protected void parseObject(Node child, Object jsonvalue) throws Exception {
		if(jsonvalue instanceof JSONObject){
			parse(child.getAttributes(),(JSONObject)jsonvalue);
		}else if(jsonvalue instanceof JSONArray){
			JSONArray jsonArray = (JSONArray)jsonvalue;
			for(int j = 0; j < jsonArray.length();j++){
				try{
					Object currentJsonvalue = jsonArray.get(j);
					if(currentJsonvalue != null){
						parseObject(child,currentJsonvalue);
					}
				}catch(org.json.JSONException e){
					String message = e.getMessage();
					if(!"JSONArray[0] not found.".equals(message)){
						throw e;
					}else{
						parseObject(child,new org.json.JSONObject());
					}
				}
			}
		}
	}
	protected void parse(NamedNodeMap attributes, JSONObject jsonvalue) throws Exception {
		for(int i = 0; i < attributes.getLength();i++){
			Node attribute = attributes.item(i);
			String name = attribute.getNodeName();
			String value = attribute.getNodeValue();
			jsonvalue.put("@"+name, value);
		}
	}
	protected String getInput() throws Exception {
		LineReader lr = null;
		StringBuffer buffer = new StringBuffer();
		try{
			lr = new LineReader(System.in);
			do{
				if(buffer.length() > 0){
					buffer.append("\n");
				}
				String line = lr.ReadLine();
				if(line.equals(".")){
					break;
				}
				buffer.append(line);
			}while(true);
		}catch(EOFException eof){}
		return buffer.toString();
	}

	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml.substring(xml.indexOf('<'));
	}
	public XMLDocumentReader getReader() throws ParserConfigurationException {
		if(this.reader == null){
			this.setReader(new XMLDocumentReader());
		}
		return reader;
	}
	public void setReader(XMLDocumentReader reader) {
		this.reader = reader;
	}

}
