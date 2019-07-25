package com.robaone.dbase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.robaone.json.JSONUtils;
import com.robaone.xml.ROTransform;
import com.robaone.xml.XMLDocumentReader;

abstract public class XSLConnectionBlock<DTO> extends XMLConnectionBlock<DTO> {
	private boolean normalize = true;
	private JSONObject normalizedParameters;

	public XSLConnectionBlock(String queryName, int page, int limit)
			throws ParserConfigurationException, JSONException {
		super(queryName, page, limit);
	}

	@Override
	protected void initialize() {
		this.setNormalizedParameters(null);
		super.initialize();
	}

	/***
	 * Normalize represents the ability to parse parameters for the existence of
	 * JSON Objects or JSON Arrays. If found, the parameter will be converted from a
	 * string to an object or array so that it can be converted to xml in its native
	 * format.
	 * 
	 * @return
	 */
	public boolean isNormalize() {
		return normalize;
	}

	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	@Override
	protected InputStream getQueryFile() throws Exception {
		InputStream in = getQueryFileResource();
		ROTransform trn = new ROTransform(in);
		String xmlconfig = trn.transformXML(createParameterDocument());
		ByteArrayInputStream bin = new ByteArrayInputStream(xmlconfig.getBytes());
		return bin;
	}

	protected InputStream getQueryFileResource() {
		String filename = this.getClass().getName().split("[.]")[this.getClass().getName().split("[.]").length - 1];
		filename += ".xsl";
		InputStream in = this.getClass().getResourceAsStream(filename);
		return in;
	}

	@Override
	protected Object getParameterValue(Node item, String name, String type) throws Exception {
		Object o = null;
		if ("node".equals(type)) {
			String node_value = this.getQueryDocument().findXPathString(item, ".");
			o = node_value;
		} else if ("xpath".equals(type)) {
			String xpath = this.getParameterQuery(item);
			NodeList nodes = this.getParameterReader().findXPathNode("/parameter/" + name);
			if (nodes.getLength() > 0) {
				Node node = nodes.item(0);
				String node_value = this.getParameterReader().findXPathString(node, xpath);
				o = node_value;
			} else {
				throw new JSONException("Could not find xpath, " + xpath + " for " + name);
			}
		} else {
			o = JSONUtils.getJsonPath(this.getNormalizedParameters(), name);
			if (o instanceof JSONObject || o instanceof JSONArray) {
				o = o.toString();
			}
		}
		o = this.formatValue(o, name, type);
		return o;
	}

	protected Document createParameterDocument()
			throws JSONException, ParserConfigurationException, SAXException, IOException {
		JSONObject parameter_jo = normalizeParameters();
		String xml = XML.toString(parameter_jo, "parameters");
		XMLDocumentReader reader = new XMLDocumentReader();
		reader.read(xml);
		return reader.getDocument();
	}

	protected JSONObject normalizeParameters() throws JSONException {
		JSONObject parameter_jo = new JSONObject();
		Iterator<String> keys = (Iterator<String>) this.getParameters().keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = this.getParameters().get(key);
			parameter_jo.put(key, value);
		}
		if (this.isNormalize()) {
			String normalized = JSONUtils.normalize(parameter_jo.toString());
			parameter_jo = new JSONObject(normalized);
		}
		return parameter_jo;
	}

	public JSONObject getNormalizedParameters() throws JSONException {
		if (this.normalizedParameters == null) {
			this.setNormalizedParameters(this.normalizeParameters());
		}
		return normalizedParameters;
	}

	public void setNormalizedParameters(JSONObject normalizedParameters) {
		this.normalizedParameters = normalizedParameters;
	}
}
