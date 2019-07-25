package com.robaone.dbase.types;

import java.text.SimpleDateFormat;

import com.robaone.dbase.*;

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

abstract public class DBType {
	protected boolean m_isnew = false;
	protected Object m_val;

	public boolean haschanged() {
		return this.m_isnew;
	}

	public Object getValue() {
		return this.m_val;
	}

	public String getStringValue() {
		if (this.m_val == null)
			return "";
		else
			return m_val.toString();
	}

	public String toString() {
		return this.getStringValue();
	}

	public String getSQLValue() {
		if (this.m_val == null)
			return "null";
		else
			return m_val.toString();
	}

	public void setValue(String val) throws com.robaone.dbase.DBSQLValidationException {
		if (val.length() > 0) {
			this.m_val = this.getNewObject(val);
			this.m_isnew = true;
		}
	}

	abstract public Object getNewObject(String val) throws DBSQLValidationException;

	public String getJavaCode() {
		return "";
	}

	public static long parseDate(String str) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("M/d/y");
		return df.parse(str).getTime();
	}

	public static String formatDate(long date) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");
		return df.format(new java.util.Date(date));
	}
}