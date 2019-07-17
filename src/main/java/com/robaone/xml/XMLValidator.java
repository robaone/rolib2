package com.robaone.xml;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.robaone.log.LogErrorWriter;
import com.robaone.util.LineReader;

public class XMLValidator {
	private String xsd;

	public XMLValidator(String xsd) {
		this.xsd = xsd.substring(xsd.indexOf("<"));
	}

	public boolean validate(Document xml) throws Exception {
		ByteArrayInputStream bin = new ByteArrayInputStream(this.xsd.getBytes());
		Source schemaFile = new StreamSource(bin);
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		try {
			validator.validate(new DOMSource(xml));
			return true;
		} catch (SAXException e) {
			System.out.println("NOT valid");
			System.out.println("Reason: " + e.getLocalizedMessage());
			throw e;
		}
	}

	public void validate(String xml) throws Exception {
		XMLDocumentReader reader = new XMLDocumentReader();
		reader.read(xml.substring(xml.indexOf("<")));
		this.validate(reader.getDocument());
	}
	
	public static void main(String[] args) {
		try{
			XMLValidator validator = new XMLValidator(FileUtils.readFileToString(new File(args[0]),"UTF-8"));
			XMLDocumentReader reader = new XMLDocumentReader();
			reader.read(validator.getInput());
			boolean validated = validator.validate(reader.getDocument());
			if(validated){
				System.out.println("IS valid");
			}else{
				System.out.println("NOT valid");
			}
		}catch(Exception e){
			LogErrorWriter.log(XMLValidator.class, e);
			System.err.println("Usage: [xsd schema file]");
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
}
