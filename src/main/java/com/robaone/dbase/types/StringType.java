package com.robaone.dbase.types;

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

public class StringType extends DBType {

	public StringType() {
	}

	public StringType(String val) {
		this.m_val = val;
	}

	public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
		return new String(val);
	}

	public String getSQLValue() {
		if (this.m_val != null) {
			return "'" + this.filter(this.getValue().toString()) + "'";
		} else {
			return "null";
		}
	}

	public void setValue(String val) throws com.robaone.dbase.DBSQLValidationException {
		this.m_val = val;
		this.m_isnew = true;
	}

	protected String filter(String str) {
		String retval = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\'') {
				retval += "\\" + str.charAt(i);
			} else if (str.charAt(i) == '\\') {
				retval += "\\" + str.charAt(i);
			} else if (str.charAt(i) == '\"') {
				retval += "\\" + str.charAt(i);
			} else {
				retval += str.charAt(i);
			}
		}
		return retval;
	}
}