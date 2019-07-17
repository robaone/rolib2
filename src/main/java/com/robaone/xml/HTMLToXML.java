package com.robaone.xml;

import java.io.EOFException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;

import com.robaone.log.LogErrorWriter;
import com.robaone.util.LineReader;

public class HTMLToXML implements Runnable {
	private Exception exception;
	private String string;
	private String output;
	public static void main(String[] args) {
		try{
			HTMLToXML parser = new HTMLToXML();
			parser.setString(parser.getInput());
			parser.run();
			if(parser.getException() != null){
				throw parser.getException();
			}
			System.out.println(parser.getOutput());
		}catch(Exception e){
			LogErrorWriter.log(HTMLToXML.class,e);
			System.exit(1);
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

	@Override
	public void run() {
		try{
			final CleanerProperties props = new CleanerProperties();
			final HtmlCleaner htmlCleaner = new HtmlCleaner(props);
			final SimpleHtmlSerializer htmlSerializer = 
			    new SimpleHtmlSerializer(props);
			TagNode str = htmlCleaner.clean(this.getString());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			htmlSerializer.writeToStream(str, out);
			this.setOutput(out.toString());
		}catch(Exception e){
			this.setException(e);
		}
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getString() {
		return string;
	}

	public void setString(String xml) {
		this.string = xml;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}