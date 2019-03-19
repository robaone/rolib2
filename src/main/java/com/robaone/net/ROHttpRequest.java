package com.robaone.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.robaone.FieldValidator;


abstract public class ROHttpRequest {

	private final String USER_AGENT = "Mozilla/5.0";
	private String url;
	private PrintStream writer;
	private String urlParameters;
	private Properties headerProperties;
	private Vector<Integer> acceptedCodes = new Vector<Integer>();

	public ROHttpRequest() {
		acceptedCodes.add(200);
		acceptedCodes.add(201);
	}

	// HTTP GET request
	public void sendGet() throws Exception {

		String url = this.getUrl();
		if (FieldValidator.exists(this.getUrlParameters())) {
			url += "?" + this.getUrlParameters();
		}
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		Properties props = this.getHeaderProperties();
		String[] keys = props.keySet().toArray(new String[0]);
		for (String key : keys) {
			con.setRequestProperty(key, props.getProperty(key));
		}

		writeLog("\nSending 'GET' request to URL : " + url);
		int responseCode = con.getResponseCode();
		writeLog("Response Code : " + responseCode);
		if (this.isAcceptedResponseCode(responseCode)) {
			InputStream in = con.getInputStream();
			handleResponse(responseCode, in, con.getHeaderFields());
			in.close();
		} else {
			InputStream in = con.getErrorStream();
			handleResponse(responseCode, in, con.getHeaderFields());
			in.close();
		}
	}

	abstract protected void handleResponse(int statuscode, InputStream stream,
			Map<String, List<String>> headers) throws Exception;

	protected void writeLog(String string) {
		if (this.getWriter() != null) {
			this.getWriter().println(string);
		}
	}

	// HTTP POST request
	public void sendPost() throws Exception {

		String url = this.getUrl();
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		Properties props = this.getHeaderProperties();
		String[] keys = props.keySet().toArray(new String[0]);
		for (String key : keys) {
			con.setRequestProperty(key, props.getProperty(key));
		}
		String urlParameters = this.getUrlParameters();

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		writeLog("\nSending 'POST' request to URL : " + url);
		writeLog("Post parameters : " + urlParameters);
		int responseCode = con.getResponseCode();
		writeLog("Response Code : " + responseCode);
		if (this.isAcceptedResponseCode(responseCode)) {
			InputStream in = con.getInputStream();
			handleResponse(responseCode, in, con.getHeaderFields());
			in.close();
		} else {
			InputStream in = con.getErrorStream();
			handleResponse(responseCode, in, con.getHeaderFields());
			in.close();
		}
	}

	// HTTP PUT request
	public void sendPut() throws Exception {
		String line;
		InputStream stderr = null;
		InputStream stdout = null;

		// launch EXE and grab stdin/stdout and stderr
		List<String> list = new ArrayList<String>();
		list.add("curl");
		list.add("-X");
		list.add("PUT");
		String[] keys = this.getHeaderProperties().keySet()
				.toArray(new String[0]);
		for (int i = 0; i < keys.length; i++) {
			list.add("-H");
			list.add(keys[i] + ": "
					+ this.getHeaderProperties().getProperty(keys[i]));
		}
		list.add("-d");
		list.add(this.getUrlParameters());
		list.add(this.getUrl());
		String[] cmd = list.toArray(new String[0]);
		Process process = Runtime.getRuntime().exec(cmd);
		stderr = process.getErrorStream();
		stdout = process.getInputStream();

		// clean up if any output in stdout
		BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(
				stdout));
		StringBuffer buff = new StringBuffer();

		while ((line = brCleanUp.readLine()) != null) {
			if (buff.length() > 0) {
				buff.append("\n");
			}
			buff.append(line);
		}
		brCleanUp.close();

		// clean up if any output in stderr
		brCleanUp = new BufferedReader(new InputStreamReader(stderr));
		while ((line = brCleanUp.readLine()) != null) {
			System.err.println("[Process]: " + line);
		}
		brCleanUp.close();
		int exitCode = process.waitFor();
		InputStream in = new ByteArrayInputStream(buff.toString().getBytes());
		this.handleResponse(exitCode == 0 ? 200 : 400, in, null);
		in.close();

	}

	// HTTP DELETE request
	public void sendDelete() throws Exception {
		String url = this.getUrl();
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("DELETE");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		Properties props = this.getHeaderProperties();
		String[] keys = props.keySet().toArray(new String[0]);
		for (String key : keys) {
			con.setRequestProperty(key, props.getProperty(key));
		}
		String urlParameters = this.getUrlParameters();

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		writeLog("\nSending 'DELETE' request to URL : " + url);
		writeLog("Delete parameters : " + urlParameters);
		int responseCode = con.getResponseCode();
		writeLog("Response Code : " + responseCode);
		if (!this.isAcceptedResponseCode(responseCode)) {
			writeLog(con.getContent().toString());
		}
		if (this.isAcceptedResponseCode(responseCode)) {
			InputStream in = con.getInputStream();
			handleResponse(responseCode, in, con.getHeaderFields());
			in.close();
		} else {
			InputStream in = con.getErrorStream();
			handleResponse(responseCode, in, con.getHeaderFields());
			in.close();
		}
	}

	protected String getUrlParameters() {
		return this.urlParameters;
	}

	public void setUrlParameters(String parameters) {
		this.urlParameters = parameters;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PrintStream getWriter() {
		return writer;
	}

	public void setWriter(PrintStream out) {
		this.writer = out;
	}

	public Properties getHeaderProperties() {
		if (this.headerProperties == null) {
			this.setHeaderProperties(new Properties());
		}
		return headerProperties;
	}

	public void setHeaderProperties(Properties headerProperties) {
		this.headerProperties = headerProperties;
	}
	
	public void setAcceptedResponseCodes(Vector<Integer> codes) {
		this.acceptedCodes = codes;
	}
	
	public void addAcceptedResponseCode(int code) {
		this.acceptedCodes.add(code);
	}
	
	public void removeAcceptedResponseCode(int code) {
		this.acceptedCodes.remove(code);
	}
	
	public Vector<Integer> getAcceptedResponseCodes() {
		return this.acceptedCodes;
	}
	
	public boolean isAcceptedResponseCode(int code) {
		for(int response : this.getAcceptedResponseCodes()){
			if(response == code){
				return true;
			}
		}
		return false;
	}

}