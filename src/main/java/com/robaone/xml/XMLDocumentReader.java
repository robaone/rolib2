package com.robaone.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.robaone.log.LogErrorWriter;

public class XMLDocumentReader {

	private Document m_doc;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private XPathFactory xfactory;
	private XPath xpath;
	public XMLDocumentReader() throws ParserConfigurationException {
		factory = DocumentBuilderFactory.newInstance();
		factory.setFeature(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
		factory.setFeature(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		xfactory = XPathFactory.newInstance();
		xpath = xfactory.newXPath();
	}

	public XMLDocumentReader(InputStream m_reader2) throws SAXException, IOException, ParserConfigurationException {
		this();
		m_doc = builder.parse(m_reader2);
	}

	public XMLDocumentReader(File xml) throws SAXException, IOException, ParserConfigurationException {
		this();
		try(FileInputStream fin = new FileInputStream(xml)){
			this.read(fin);
		}
	}

	public void read(InputStream fin) throws SAXException, IOException {
		m_doc = builder.parse(fin);
	}

	public void read(Document doc) {
		m_doc = doc;
	}

	public void read(String xml) throws SAXException, IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(xml.getBytes());
		this.read(bin);
	}

	public void setNamespace(final String prefix, final String namespace_uri) throws Exception {
		xpath.setNamespaceContext(new NamespaceContext() {

			public String getNamespaceURI(String arg0) {
				return namespace_uri;
			}

			public String getPrefix(String arg0) {
				return prefix;
			}

			public Iterator<String> getPrefixes(String arg0) {
				HashMap<String, String> m = new HashMap<String, String>();
				m.put(prefix, namespace_uri);
				return m.keySet().iterator();
			}

		});
	}

	public NodeList findXPathNode(String path) throws Exception {
		return this.findXPathNode(m_doc, path);
	}

	public NodeList findXPathNode(Node n, String path) throws Exception {
		XPathExpression expr = xpath.compile(path);
		return (NodeList) expr.evaluate(n, XPathConstants.NODESET);
	}

	public String findXPathString(String path) throws Exception {
		return this.findXPathString(m_doc, path);
	}

	public String findXPathString(Node n, String path) throws Exception {
		XPathExpression expr = xpath.compile(path);
		return (String) expr.evaluate(n, XPathConstants.STRING);
	}

	public Document getDocument() {
		return this.m_doc;
	}

	public String toString()
	{
		try
		{
			Document doc = this.getDocument();
			StringWriter writer = serialize(doc);
			return writer.toString();
		}
		catch(TransformerException ex)
		{
			LogErrorWriter.log(this.getClass(), ex);
			return "";
		}
	}

	protected StringWriter serialize(Node doc)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = newTransformerFactory();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		return writer;
	}
	protected TransformerFactory newTransformerFactory()
			throws TransformerFactoryConfigurationError, TransformerConfigurationException {
		TransformerFactory tf = this.newTransformerFactory();
		tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		return tf;
	}

	public void marshall(OutputStream out) throws Exception {
		DOMSource domSource = new DOMSource(this.getDocument());
		StreamResult result = new StreamResult(out);
		TransformerFactory tf = this.newTransformerFactory();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
	}

	public void read(File file) throws SAXException, IOException {
		try(FileInputStream fin = new FileInputStream(file)){
			this.read(fin);
		}
	}

	public String toString(NodeList nl)
			throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < nl.getLength(); i++) {
			buffer.append(this.serialize(nl.item(i)));
		}
		return buffer.toString();
	}

	public String toString(Node node)
			throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		return this.serialize(node).toString();
	}
}
