package com.robaone.xml;

import java.io.EOFException;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.robaone.FieldValidator;
import com.robaone.util.LineReader;

public class XMLPathParser implements Runnable {
	private Exception exception;
	private String xml;
	private String path;
	private String output;
	private String namespace;
	private static String prefix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static void main(String[] args) {
		try{
			if(args.length == 0){
				throw new Exception("Usage: [xpath] [(optional)namespace:]\n\n\tWhen using namespaces, the prefix is 'ns'.");
			}
			XMLPathParser parser = new XMLPathParser();
			System.err.println("Path = "+args[0]);
			parser.setPath(args[0]);
			try{
				parser.setNamespace(args[1]);
			}catch(Exception e){}
			parser.setXml(parser.getInput());
			parser.run();
			if(parser.getException() != null){
				throw parser.getException();
			}
			System.out.println(parser.getOutput());
		}catch(Exception e){
			e.printStackTrace();
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
		XMLDocumentReader reader = null;
		try{
			reader = new XMLDocumentReader();
			if(FieldValidator.exists(this.getNamespace())){
				reader.setNamespace("ns", this.getNamespace());
			}
			reader.read(this.getXml());
			NodeList nl = reader.findXPathNode(getPath());
			StringBuffer buffer = new StringBuffer();
			for(int i = 0 ; i < nl.getLength();i++){
				Node node = nl.item(i);
				if(buffer.length() > 0){
					buffer.append("\n");
				}
				String str = reader.toString(node);
				if(str.startsWith(prefix)){
					str = str.substring(prefix.length());
				}
				buffer.append(str);
			}
			this.setOutput(buffer.toString());
		}catch(XPathExpressionException e){
			try{
			    String txt = reader.findXPathString(getPath());
			    this.setOutput(txt);
			}catch(Exception e1){
				this.setException(e1);
			}
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

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml.substring(xml.indexOf('<'));
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
