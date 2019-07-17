package com.robaone.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.robaone.FieldValidator;
import com.robaone.log.LogErrorWriter;

/**
 * <pre>
 *    Copyright Mar 21, 2012 Ansel Robateau
         http://www.robaone.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 * </pre>
 * 
 * @author Ansel
 *
 */
public class ROTransform {
	protected Transformer m_transformer;
	private HashMap<String, Object> parameters;

	public ROTransform(InputStream in) throws Exception {
		TransformerFactory tFactory = newFactory();
		StreamSource xsltSrc = new StreamSource(in);
		Transformer transformer = tFactory.newTransformer(xsltSrc);
		this.m_transformer = transformer;
	}

	protected TransformerFactory newFactory()
			throws TransformerFactoryConfigurationError, TransformerConfigurationException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		return tFactory;
	}

	public ROTransform(File file) throws Exception {
		try (FileInputStream fin = new FileInputStream(file)) {
			TransformerFactory tFactory = newFactory();
			StreamSource xsltSrc = new StreamSource(fin);
			Transformer transformer = tFactory.newTransformer(xsltSrc);
			this.m_transformer = transformer;
		} finally {

		}

	}

	public ROTransform(String xslt) throws Exception {
		TransformerFactory tFactory = newFactory();
		StreamSource xsltSrc = new StreamSource(new ByteArrayInputStream(xslt.getBytes("UTF-8")));
		Transformer transformer = tFactory.newTransformer(xsltSrc);
		this.m_transformer = transformer;
	}

	public String transformXML(Document doc) throws Exception {
		setParameters();
		XMLDocumentReader reader = new XMLDocumentReader();
		reader.read(doc);
		return transformXML(reader.toString());
	}

	public String transformXML(String xml) throws Exception {
		setParameters();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(out);
		ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		this.m_transformer.transform(new StreamSource(in), result);
		String retval = new String(out.toByteArray());
		return retval;
	}

	protected void setParameters() throws Exception {
		this.m_transformer.clearParameters();
		if (this.getParameters() != null) {
			String[] keys = this.getParameters().keySet().toArray(new String[0]);
			for (String key : keys) {
				this.m_transformer.setParameter(key, this.getParameters().get(key));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			if (args.length >= 3) {
				FileInputStream fin = new FileInputStream(args[1]);
				ROTransform trn = new ROTransform(fin);
				try {
					trn.setParameters(parseParameters(args[3]));
				} catch (Exception e) {
				}
				String xml = trn.transformXML(FileUtils.readFileToString(new File(args[0]), "UTF-8"));
				FileWriter fileWriter = new FileWriter(new File(args[2]));
				IOUtils.write(xml, fileWriter);
				fileWriter.flush();
				fileWriter.close();
				if (FieldValidator.exists(xml)) {
					System.out.println("Success");
				} else {
					throw new Exception("Error transforming");
				}
			} else {
				throw new Exception("Not enough arguments");
			}
		} catch (Exception e) {
			System.out.println("Usage: [xml] [xsl] [output] [(optional jsonobject) parameters]");
			LogErrorWriter.log(ROTransform.class, e);
			System.exit(1);
		}
	}

	public static HashMap<String, Object> parseParameters(String json_string) throws Exception {
		JSONObject parameters = new org.json.JSONObject(json_string);
		Iterator<String> keys = parameters.keys();
		HashMap<String, Object> map = new HashMap<String, Object>();
		while (keys.hasNext()) {
			String key = keys.next();
			map.put(key, parameters.get(key));
		}
		return map;
	}

	public HashMap<String, Object> getParameters() {
		if (this.parameters == null) {
			this.setParameters(new HashMap<String, Object>());
		}
		return parameters;
	}

	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}
}
