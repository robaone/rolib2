package com.robaone.json;

import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.robaone.FieldValidator;
import com.robaone.xml.XMLDocumentReader;

public class JSONUtils {
	public static JSONObject renameField(JSONObject jo, String currentName, String newName) throws Exception {
		if (!FieldValidator.exists(currentName)) {
			throw new Exception("Current field name not specified");
		}
		if (!FieldValidator.exists(newName)) {
			throw new Exception("New field name not specified");
		}
		if (jo.has(newName)) {
			throw new Exception("Field, " + newName + ", already exists");
		}
		if (jo.has(currentName)) {
			jo.put(newName, jo.get(currentName));
			jo.remove(currentName);
		}
		return jo;
	}

	/**
	 * Create a new json object where the field names are converted to lower case.
	 * 
	 * @param jo
	 * @param recursive
	 * @return
	 * @throws Exception
	 */
	public static org.json.JSONObject fieldsToLowerCase(org.json.JSONObject jo, boolean recursive) throws Exception {
		org.json.JSONObject retval = new org.json.JSONObject();
		Iterator<String> keys = jo.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = jo.get(key);
			if (recursive) {
				if (value instanceof org.json.JSONObject) {
					value = fieldsToLowerCase((org.json.JSONObject) value, recursive);
				} else if (value instanceof org.json.JSONArray) {
					org.json.JSONArray ja = (org.json.JSONArray) value;
					for (int i = 0; i < ja.length(); i++) {
						Object array_value = ja.get(i);
						if (array_value instanceof org.json.JSONObject) {
							array_value = fieldsToLowerCase((org.json.JSONObject) array_value, recursive);
						}
						ja.put(i, array_value);
					}
				}
			}
			retval.put(key.toLowerCase(), value);
		}
		return retval;
	}

	public static Object getJsonPath(org.json.JSONObject jo, String path) throws Exception {
		String[] path_split = path.split("\\|");
		path = path_split[0];
		String[] items = path.split("\\.");
		Object o = null;
		Pattern p = Pattern.compile("([a-zA-Z_\\.][a-zA-Z_0-9]*)?\\[([0-9]+)\\]");
		o = jo;
		for (String item : items) {
			item = item.trim();
			if (item.length() > 0 && o != null) {
				item = "." + item;
				Matcher m = p.matcher(item);
				if (m.matches()) {
					item = m.group(1);
					Integer index = Integer.parseInt(m.group(2));
					if (o instanceof org.json.JSONObject) {
						jo = (org.json.JSONObject) o;
						if (item.startsWith(".") && !item.equals(".")) {
							item = item.substring(1);
						}
						if (jo.has(item) || item.equals(".")) {
							org.json.JSONArray ja = jo.getJSONArray(item);
							o = ja.get(index);
						} else {
							if (item.startsWith(".")) {
								item = item.substring(1);
							}
							o = jo.get(item);
						}
					} else if (o instanceof org.json.JSONArray) {
						org.json.JSONArray ja = (org.json.JSONArray) o;
						o = ja.get(index);
					} else {
						o = null;
					}
				} else if (o instanceof org.json.JSONObject) {
					jo = (org.json.JSONObject) o;
					if (item.startsWith(".") && !item.equals(".")) {
						item = item.substring(1);
						o = jo.get(item);
					} else {
						throw new Exception("Path not found");
					}
				} else {
					throw new Exception("Invalid Path");
				}
			} else {
				throw new Exception("Path not found");
			}
		}
		if (path_split.length > 0 && o != null) {
			for (int i = 1; i < path_split.length; i++) {
				String filter = path_split[i].trim();
				if (filter.equalsIgnoreCase("length")) {
					if (o instanceof org.json.JSONArray) {
						o = ((org.json.JSONArray) o).length();
					} else if (o instanceof org.json.JSONObject) {
						o = ((org.json.JSONObject) o).length();
					} else {
						o = o.toString().length();
					}
				}
			}
		}
		return o;
	}

	public static String normalize(String json) throws JSONException {
		String retval = json;
		try {
			org.json.JSONObject jo = new org.json.JSONObject(json);
			retval = normalize(jo).toString();
		} catch (Exception e) {
			try {
				org.json.JSONArray ja = new org.json.JSONArray(json);
				retval = normalize(ja).toString();
			} catch (Exception e3) {
				retval = json;
			}
		}
		return retval;
	}

	public static org.json.JSONArray normalize(org.json.JSONArray ja) throws JSONException {
		org.json.JSONArray retval_ja = new org.json.JSONArray();
		for (int i = 0; i < ja.length(); i++) {
			Object value = ja.get(i);
			if (value instanceof String) {
				String str = value.toString();
				try {
					if (str.startsWith("{") && str.endsWith("}")) {
						org.json.JSONObject str_jo = new org.json.JSONObject(str);
						retval_ja.put(new org.json.JSONObject(JSONUtils.normalize(str_jo.toString())));
					} else if (str.startsWith("[") && str.endsWith("]")) {
						org.json.JSONArray str_ja = new org.json.JSONArray(str);
						retval_ja.put(new org.json.JSONArray(JSONUtils.normalize(str_ja.toString())));
					} else {
						retval_ja.put(str);
					}
				} catch (Exception e2) {
					retval_ja.put(str);
				}
			} else if (value instanceof org.json.JSONObject) {
				value = normalize((org.json.JSONObject) value);
				retval_ja.put(i, value);
			} else if (value instanceof org.json.JSONArray) {
				value = normalize((org.json.JSONArray) value);
				retval_ja.put(i, value);
			} else {
				retval_ja.put(value);
			}
		}
		return retval_ja;
	}

	public static org.json.JSONObject normalize(org.json.JSONObject jo) throws JSONException {
		Iterator<String> keys = jo.keys();
		org.json.JSONObject retval_jo = new org.json.JSONObject();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = jo.get(key);
			if (value instanceof String) {
				String str = value.toString();
				try {
					if (str.startsWith("{") && str.endsWith("}")) {
						org.json.JSONObject str_jo = new org.json.JSONObject(str);
						retval_jo.put(key, new org.json.JSONObject(JSONUtils.normalize(str_jo.toString())));
					} else if (str.startsWith("[") && str.endsWith("]")) {
						org.json.JSONArray str_ja = new org.json.JSONArray(str);
						retval_jo.put(key, new org.json.JSONArray(JSONUtils.normalize(str_ja.toString())));
					} else {
						retval_jo.put(key, str);
					}
				} catch (Exception e) {
					retval_jo.put(key, str);
				}
			} else if (value instanceof org.json.JSONObject) {
				value = normalize((org.json.JSONObject) value);
				retval_jo.put(key, value);
			} else if (value instanceof org.json.JSONArray) {
				value = normalize((org.json.JSONArray) value);
				retval_jo.put(key, value);
			} else {
				retval_jo.put(key, value);
			}
		}
		return retval_jo;
	}

	public static Properties toProperties(org.json.JSONObject jsonObject) {
		Properties props = new Properties();
		Iterator<String> it = jsonObject.keys();
		while (it.hasNext()) {
			try {
				Object key = it.next();
				Object value = jsonObject.get(key.toString());
				props.put(key, value);
			} catch (Exception e) {
			}
		}
		return props;
	}

	public static org.json.JSONObject merge(org.json.JSONObject old_jo, org.json.JSONObject new_jo)
			throws JSONException {
		org.json.JSONObject merged = new org.json.JSONObject(old_jo.toString());
		Iterator<String> iterator = new_jo.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = new_jo.get(key);
			merged.put(key, value);
		}
		return merged;
	}

	public static String toXMLbyAttribute(org.json.JSONObject jsonObject, String string) throws Exception {
		XMLDocumentReader reader = new XMLDocumentReader();
		reader.read("<node></node>");
		Document doc = reader.getDocument();
		Attr root_name = doc.createAttribute("name");
		root_name.setNodeValue(string);
		Node root = doc.getFirstChild();
		root.getAttributes().setNamedItem(root_name);
		JSONUtils.buildXMLDocument(doc, root, jsonObject);
		return reader.toString();
	}

	protected static Node buildXMLDocument(Document doc, Node parent, org.json.JSONObject jsonObject) throws Exception {
		Iterator<String> keys = (Iterator<String>) jsonObject.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = jsonObject.get(key);
			createXMLDocumentElement(doc, parent, key, value);
		}
		return parent;
	}

	public static void createXMLDocumentElement(Document doc, Node parent, String key, Object value) throws Exception {
		if (value instanceof org.json.JSONObject) {
			Node new_node = doc.createElement("node");
			Attr new_name = doc.createAttribute("name");
			new_name.setNodeValue(key);
			new_node.getAttributes().setNamedItem(new_name);
			parent.appendChild(new_node);
			buildXMLDocument(doc, new_node, (org.json.JSONObject) value);
		} else if (value instanceof org.json.JSONArray) {
			org.json.JSONArray array = (org.json.JSONArray) value;
			for (int i = 0; i < array.length(); i++) {
				Object array_value = array.get(i);
				createXMLDocumentElement(doc, parent, key, array_value);
			}
		} else {
			Node new_node = doc.createElement("node");
			Attr new_name = doc.createAttribute("name");
			new_name.setNodeValue(key);
			new_node.getAttributes().setNamedItem(new_name);
			new_node.setTextContent(value.toString());
			parent.appendChild(new_node);
		}
	}

	public static void append(org.json.JSONArray destination, org.json.JSONArray addition) throws JSONException {
		for (int i = 0; i < addition.length(); i++) {
			destination.put(addition.get(i));
		}
	}

	public static boolean fieldValueExists(org.json.JSONObject record_jo, String string) throws JSONException {
		if (record_jo.has(string) && !record_jo.isNull(string) && FieldValidator.exists(record_jo.getString(string))) {
			return true;
		} else {
			return false;
		}
	}
}
