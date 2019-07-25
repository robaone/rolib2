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
 * @author Ansel Robateau
 * @version 1.3
 */

public class DoubleType extends DBType {

	public DoubleType() {
	}

	public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
		if (val == null || val.length() == 0)
			return null;
		else
			return new Double(val);
	}

	public void setValue(double val) {
		this.m_val = new Double(val);
		this.m_isnew = true;
	}

	public void setValue(Double val) {
		this.m_val = val;
		this.m_isnew = true;
	}

	public String getJavaCode() {
		return ".doubleValue()";
	}
}