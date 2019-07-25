package com.robaone.json;

import java.io.EOFException;

import org.json.JSONObject;

import com.robaone.dbase.types.StringType;
import com.robaone.util.LineReader;

public class JSONPath implements Runnable {
	private Exception exception;
	private String json;
	private String value;
	private String path;

	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				throw new Exception("Missing path argument");
			}
			JSONPath converter = new JSONPath();
			converter.setJson(converter.getInput());
			converter.setPath(args[0]);
			System.err.println("Path: " + converter.getPath());
			converter.run();
			if (converter.getException() != null) {
				throw converter.getException();
			}
			System.out.println(new StringType(converter.getValue()));
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		try {
			JSONObject jo = new JSONObject(this.getJson());
			Object value = null;
			value = JSONUtils.getJsonPath(jo, getPath());
			if (value != null) {
				this.setValue(value.toString());
			}
		} catch (Exception e) {
			this.setException(e);
		}
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

	public String getValue() {
		return value;
	}

	public void setValue(String xml) {
		this.value = xml;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
