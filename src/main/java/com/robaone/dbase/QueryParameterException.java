package com.robaone.dbase;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.robaone.FieldValidator;

public class QueryParameterException extends Exception {
	private static final long serialVersionUID = -1285407929870128674L;
	HashMap<String, String> errors = new HashMap<String, String>();

	public QueryParameterException(HashMap<String, String> errors) {
		this.errors = errors;
	}

	@Override
	public String getMessage() {
		if (FieldValidator.exists(super.getMessage())) {
			return super.getMessage();
		} else {
			StringBuffer message = new StringBuffer();
			for (String field : this.getParameters()) {
				String error = this.getError(field);
				JSONObject jo = new JSONObject();
				try {
					jo.put(field, error);
				} catch (JSONException e) {
				}
				message.append(jo.toString());
			}
			return message.toString();
		}
	}

	@Override
	public void printStackTrace() {
		printErrors(System.err);
		super.printStackTrace();
	}

	@Override
	public void printStackTrace(PrintStream ps) {
		this.printErrors(ps);
		super.printStackTrace(ps);
	}

	@Override
	public void printStackTrace(PrintWriter pw) {
		this.printErrors(pw);
		super.printStackTrace(pw);
	}

	private void printErrors(PrintWriter pw) {
		String[] keys = this.getParameters();
		for (String key : keys) {
			pw.println(key + ": " + this.getError(key));
		}
	}

	private void printErrors(PrintStream err) {
		String[] keys = this.getParameters();
		for (String key : keys) {
			err.println(key + ": " + this.getError(key));
		}
	}

	public String[] getParameters() {
		return this.errors.keySet().toArray(new String[0]);
	}

	public String getError(String parameter) {
		return this.errors.get(parameter);
	}
}
