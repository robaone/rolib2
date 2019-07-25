package com.robaone.dbase.types;

import java.text.SimpleDateFormat;

import com.robaone.dbase.DBSQLValidationException;

/**
 * <pre>   Copyright 2013 Ansel Robateau
         http://www.robaone.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.</pre>
 * @author Ansel
 *
 */
public class DateTimeType extends DBType{
	public static final int US_FORMAT_COMPRESSED = 0;
	public static final int US_FORMAT = 1;
	public static final int US_FORMAT_DATE_ONLY = 2;
	public static final int SQL_FORMAT_FULL = 3;
	public static final int SQL_FORMAT = 4;
	public static final int SQL_FORMAT_TO_MINUTE = 5;
	public static final int SQL_FORMAT_DATE_ONLY = 6;
	public static final int US_FORMAT_FULL = 7;

	public DateTimeType(java.util.Date dt) {
		this.m_val = new java.sql.Timestamp(dt.getTime());
	}
	
	public DateTimeType(String date) throws Exception {
		this.m_val = this.getNewObject(date);
	}
	
	public java.sql.Timestamp getTimestamp(){
		return (java.sql.Timestamp)this.m_val;
	}

	@Override
	public Object getNewObject(String val) throws DBSQLValidationException {
		SimpleDateFormat[] formats = this.getSupportedFormats();
		java.util.Date dt = null;
		for(SimpleDateFormat format : formats){
			try{
				dt = format.parse(val);
				break;
			}catch(Exception e){
				
			}
		}
		if(dt == null){
			throw new DBSQLValidationException("Unsupported date time format");
		}
		return new java.sql.Timestamp(dt.getTime());
	}
	
	public SimpleDateFormat[] getSupportedFormats() {
		SimpleDateFormat[] formats = {
				new SimpleDateFormat("MM/dd/yy hh:mma"),
				new SimpleDateFormat("MM/dd/yy hh:mm a"),
				new SimpleDateFormat("MM/dd/yy"),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
				new SimpleDateFormat("yyyy-MM-dd HH:mm"),
				new SimpleDateFormat("yyyy-MM-dd"),
				new SimpleDateFormat("MM/dd/yyyy hh:mm a")
		};
		return formats;
	}
	
	public String toFormattedString(int format_index) {
		SimpleDateFormat format = this.getSupportedFormats()[format_index];
		String retval = format.format(this.getTimestamp());
		return retval;
	}

}
