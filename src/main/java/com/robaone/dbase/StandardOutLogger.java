package com.robaone.dbase;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StandardOutLogger extends Logger {

	public StandardOutLogger(){
		super("name",null);
	}
	protected StandardOutLogger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}
	
	@Override
	public void info(String message){
		System.out.println(getTimestamp()+": "+message);
	}
	
	@Override
	public void log(Level level, String message) {
		System.out.println(getTimestamp()+": "+level+": " + message);
	}

	private Timestamp getTimestamp() {
		return new java.sql.Timestamp(new java.util.Date().getTime());
	}
}
