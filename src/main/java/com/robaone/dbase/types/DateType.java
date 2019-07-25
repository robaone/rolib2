package com.robaone.dbase.types;

import java.sql.Date;
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
 */
public class DateType extends DBType {

	public DateType() {
	}

	public DateType(java.sql.Date date) {
		this.m_val = date;
	}

	public DateType(java.util.Date date) {
		if (date != null)
			this.m_val = new java.sql.Date(date.getTime());
		else
			this.m_val = null;
	}

	public DateType(long date) {
		this.m_val = new java.sql.Date(date);
	}

	public DateType(String date) throws com.robaone.dbase.DBSQLValidationException {
		this.m_val = this.getNewObject(date);
	}

	public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
		try {
			return new Date(parseDate(val));
		} catch (Exception e1) {
			SimpleDateFormat df = new SimpleDateFormat("y-M-d");
			try {
				return new Date(df.parse(val).getTime());
			} catch (Exception e) {
				throw new DBSQLValidationException(e.getMessage());
			}
		}
	}

	public void setValue(Date val) {
		if (val == null)
			this.m_val = null;
		else
			this.m_val = new Date(val.getTime());
		this.m_isnew = true;
	}

	public void setValue(java.util.Date val) {
		if (val == null)
			this.m_val = null;
		else
			this.m_val = new Date(val.getTime());
		this.m_isnew = true;
	}

	public void setValue(long val) {
		this.m_val = new Date(val);
		this.m_isnew = true;
	}

	public String getStringValue() {
		if (this.m_val == null)
			return "";
		try {
			return formatDate(((Date) this.m_val).getTime());
		} catch (Exception e) {
			return "";
		}
	}

	public String getSQLValue() {
		if (this.m_val == null)
			return "null";
		return "{d '" + this.m_val.toString() + "'}";
	}
}