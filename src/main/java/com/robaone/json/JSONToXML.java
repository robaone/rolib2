package com.robaone.json;

import java.io.EOFException;
import java.util.Iterator;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import com.robaone.FieldValidator;
import com.robaone.util.LineReader;

public class JSONToXML implements Runnable {
	private Exception exception;
	private Options options;
	private String json;
	private String xml;
	private boolean attributeMode;

	public JSONToXML() {
		this.options = new Options();
		this.options.addOption("a", "attribute", false, "Attribute mode");
	}

	public static void main(String[] args) {
		try {
			JSONToXML converter = new JSONToXML();
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(converter.options, args);
			if (cmd.hasOption("a")) {
				converter.setAttributeMode(true);
			}
			converter.setJson(converter.getInput());
			converter.run();
			if (converter.getException() != null) {
				throw converter.getException();
			}
			System.out.println(converter.getXml());
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		try {
			org.json.JSONObject jsonObject = this.clean(new org.json.JSONObject(this.getJson()));
			if (this.isAttributeMode()) {
				String xml = JSONUtils.toXMLbyAttribute(jsonObject, "json");
				this.setXml(xml);
			} else {
				String xml = org.json.XML.toString(jsonObject, "json");
				this.setXml(xml);
			}
		} catch (Exception e) {
			this.setException(e);
		}
	}

	public org.json.JSONObject clean(org.json.JSONObject jsonObject) throws Exception {
		org.json.JSONObject retval = new org.json.JSONObject();
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			String safe_key = this.makeXmlSafe(key);
			Object value = jsonObject.get(key);
			if (value instanceof org.json.JSONObject) {
				value = this.clean((org.json.JSONObject) value);
			} else if (value instanceof org.json.JSONArray) {
				value = this.clean((org.json.JSONArray) value);
			}
			retval.put(safe_key, value);
		}
		return retval;
	}
	
	public org.json.JSONArray clean(org.json.JSONArray jsonArray) throws Exception {
		org.json.JSONArray retval = new org.json.JSONArray();
		for (int i = 0; i < jsonArray.length(); i++) {
			Object ja_value = jsonArray.get(i);
			if(ja_value instanceof org.json.JSONObject){
				ja_value = this.clean((org.json.JSONObject) ja_value);
			}else if(ja_value instanceof org.json.JSONArray){
				ja_value = this.clean((org.json.JSONArray) ja_value);
			}
			retval.put(ja_value);
		}
		return retval;
	}

	public String makeXmlSafe(String key) {
		String safe = key;
		if (FieldValidator.matches(safe, "^[Xx][Mm][Ll].*")) {
			safe = "_" + safe;
		}
		if (FieldValidator.matches(safe, "^[^A-Za-z_].*")) {
			safe = "_" + safe;
		}
		safe = safe.replaceAll("[^A-Za-z0-9\\-_]", "_");
		return safe;
	}

	protected String getInput() throws Exception {
		LineReader lr = null;
		StringBuffer buffer = new StringBuffer();
		try {
			lr = new LineReader(System.in);
			do {
				if (buffer.length() > 0) {
					buffer.append("\n");
				}
				String line = lr.ReadLine();
				if (line.equals(".")) {
					break;
				}
				buffer.append(line);
			} while (true);
		} catch (EOFException eof) {
		}
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
		this.xml = xml;
	}

	public boolean isAttributeMode() {
		return attributeMode;
	}

	public void setAttributeMode(boolean attributeMode) {
		this.attributeMode = attributeMode;
	}

}
