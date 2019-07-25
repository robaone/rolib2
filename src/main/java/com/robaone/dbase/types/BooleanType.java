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

public class BooleanType extends DBType {

  public BooleanType() {
  }
  public BooleanType(boolean val){
    this.m_val = new Boolean(val);
  }
  public BooleanType(Boolean val){
    this.m_val = val;
  }
  public BooleanType(String val) throws com.robaone.dbase.DBSQLValidationException{
    this.setValue(val);
  }
  public Object getNewObject(String val) throws com.robaone.dbase.DBSQLValidationException {
    if(val.toLowerCase().equals("n")) val = "false";
    if(val.toLowerCase().equals("no")) val = "false";
    if(val.toLowerCase().equals("y")) val = "true";
    if(val.toLowerCase().equals("yes")) val = "true";
    if(val.toLowerCase().equals("1")) val = "true";
    if(val.equals("0")) val = "false";
    return new Boolean(val);
  }
  public void setValue(boolean val){
    this.m_val = new Boolean(val);
    this.m_isnew = true;
  }
  public void setValue(Boolean val){
    this.m_val = val;
    this.m_isnew = true;
  }
  public String getJavaCode(){
    return ".booleanValue()";
  }
}