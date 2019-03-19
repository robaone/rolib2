package com.robaone.net;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ROQueryString {

	private String query = "";

	public ROQueryString(HashMap<String, String> map) throws UnsupportedEncodingException {
		Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pairs = it.next();
			query += URLEncoder.encode(pairs.getKey(),"UTF8") + "=" +         
					URLEncoder.encode(pairs.getValue(),"UTF8");
			if (it.hasNext()) { query += "&"; }
		}
	}

	public ROQueryString(Object name, Object value) throws UnsupportedEncodingException {
		query = URLEncoder.encode(name.toString(),"UTF8") + "=" +         
				URLEncoder.encode(value.toString(),"UTF8");
	}

	public ROQueryString() { query = ""; }

	public synchronized void add(Object name, Object value) throws UnsupportedEncodingException {
		if(value != null){
			if (!query.trim().equals("")) query += "&";
			query += URLEncoder.encode(name.toString(),"UTF8") + "=" +         
					URLEncoder.encode(value.toString(),"UTF8");
		}
	}

	public String toString() { return query; }
}