package com.robaone.dbase.types;
/**
 * <pre>   Copyright Mar 21, 2012 Ansel Robateau
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
public class ByteType extends DBType {
  protected byte[] m_bytes;
  public ByteType(byte[] bytes) {
    this.m_bytes = bytes;
  }
  public ByteType(){
    this.m_bytes = new byte[0];
  }
  public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
    return val;
  }
  public void setValue(String val) throws com.robaone.dbase.DBSQLValidationException {
    if(val.length() > 0){
      this.m_val = this.getNewObject(val);
      this.m_isnew = true;
      this.m_bytes = new byte[0];
    }
  }
  public void setValue(byte[] bytes) {
    this.m_bytes = bytes;
    this.m_isnew = true;
  }
  public byte[] getBytes(){
    return this.m_bytes;
  }
}