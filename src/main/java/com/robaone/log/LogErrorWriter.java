package com.robaone.log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class LogErrorWriter extends PrintStream {

	public LogErrorWriter(final String fileName) throws FileNotFoundException {
		super(new OutputStream() {
			Logger logger = Logger.getLogger(fileName);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			@Override
			public void write(int b) throws IOException {
				bout.write(b);
				
			}
			
			@Override
			public void flush() {
				logger.log(Level.SEVERE,bout.toString());
			}
			
		});
		
	}
	public static void log(Class<?> _class, Exception e) {
		LogErrorWriter.log(_class.getName(),e);
	}
	public static void log(String name, Exception e) {
		try {
			LogErrorWriter writer = new LogErrorWriter(name);
			e.printStackTrace(writer);
		}catch(Exception e2) {}
	}

}
