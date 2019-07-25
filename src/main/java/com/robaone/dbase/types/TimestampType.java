package com.robaone.dbase.types;

import java.sql.Timestamp;

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
public class TimestampType extends DBType {

	public TimestampType() {
	}

	public TimestampType(java.sql.Date val) {
		this.m_val = new Timestamp(val.getTime());
	}

	public TimestampType(java.sql.Timestamp val) {
		this.m_val = val;
	}

	public TimestampType(java.util.Date val) {
		this.m_val = new Timestamp(val.getTime());
	}

	public TimestampType(long val) {
		this.m_val = new Timestamp(val);
	}

	public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
		return Timestamp.valueOf(val);
	}

	public void setValue(Timestamp val) {
		this.m_isnew = true;
		this.m_val = val;
	}

	public void setValue(java.sql.Date val) {
		this.m_isnew = true;
		this.m_val = new Timestamp(val.getTime());
	}

	public void setValue(java.util.Date val) {
		this.m_isnew = true;
		this.m_val = new Timestamp(val.getTime());
	}

	public void setValue(long val) {
		this.m_isnew = true;
		this.m_val = new Timestamp(val);
	}

	public String getSQLValue() {
		if (this.m_val == null)
			return "null";
		else
			return "'" + this.m_val.toString() + "'";
	}

	public String getStringValue() {
		if (this.m_val == null)
			return "";
		return this.m_val.toString();
	}
}