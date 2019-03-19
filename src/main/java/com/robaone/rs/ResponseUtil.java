package com.robaone.rs;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ResponseUtil {
	
	public static com.robaone.json.JSONResponse<?> captureException(com.robaone.json.JSONResponse<?> response, Exception e) {
		response.setStatus(1);
		response.setError(e.getMessage());
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		response.getProperties().setProperty("trace", sw.toString());
		return response;
	}
}
