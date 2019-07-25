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

public class LongType extends DBType {

	public LongType() {
	}

	public LongType(java.lang.Long val) {
		this.m_val = val;
	}

	public LongType(long val) {
		this.m_val = new Long(val);
	}

	public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
		return new Long(val);
	}

	public void setValue(long val) {
		this.m_val = new Long(val);
		this.m_isnew = true;
	}

	public void setValue(Long val) {
		this.m_val = val;
		this.m_isnew = true;
	}

	public String getJavaCode() {
		return ".longValue()";
	}
}