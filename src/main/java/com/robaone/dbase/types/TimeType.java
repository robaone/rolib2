package com.robaone.dbase.types;

import java.sql.Time;
import java.text.SimpleDateFormat;

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
public class TimeType extends DBType {

	public TimeType() {
	}

	public TimeType(java.util.Date val) {
		this.m_val = new java.sql.Time(val.getTime());
	}

	public TimeType(java.sql.Date val) {
		this.m_val = new java.sql.Time(val.getTime());
	}

	public TimeType(long val) {
		this.m_val = new java.sql.Time(val);
	}

	public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
		return null;
	}

	public void setValue(Time val) {
		this.m_val = val;
		this.m_isnew = true;
	}

	public void setValue(long val) {
		this.m_val = new Time(val);
		this.m_isnew = true;
	}

	public String getStringValue() {
		SimpleDateFormat df = new SimpleDateFormat("h:mm a");
		if (this.m_val == null)
			return "";
		return df.format(new java.sql.Date(((Time) this.getValue()).getTime()));
	}

	public String getSQLValue() {
		if (this.m_val == null)
			return "null";
		else
			return "'" + this.m_val.toString() + "'";
	}

	public void setValue(String val) throws com.robaone.dbase.DBSQLValidationException {
		SimpleDateFormat df1 = new SimpleDateFormat("h:mm a");
		SimpleDateFormat df2 = new SimpleDateFormat("h:mma");
		SimpleDateFormat df3 = new SimpleDateFormat("H:mm");
		SimpleDateFormat df4 = new SimpleDateFormat("H:mm:ss");
		if (val.trim().length() == 0)
			return;
		long time;
		try {
			time = df1.parse(val).getTime();
			this.m_val = new Time(time);
		} catch (Exception e) {
			try {
				time = df2.parse(val).getTime();
				this.m_val = new Time(time);
			} catch (Exception e1) {
				try {
					time = df3.parse(val).getTime();
					this.m_val = new Time(time);
				} catch (Exception e2) {
					try {
						time = df4.parse(val).getTime();
						this.m_val = new Time(time);
					} catch (Exception e3) {
						throw new com.robaone.dbase.DBSQLValidationException("Cannot parse " + val);
					}
				}
			}
		}
		this.m_isnew = true;
	}
}