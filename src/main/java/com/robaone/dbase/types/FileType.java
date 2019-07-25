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

public class FileType extends StringType {
  protected String m_contenttype = "";
  protected String m_filename = "";
  protected byte[] m_bytes;
  public FileType() {
    super();
  }
  public FileType(String str) {
    super(str);
  }
  public String getContentType(){
    return this.m_contenttype;
  }
  public String getFileName(){
    return this.m_filename;
  }
  public void setContentType(String str){
    this.m_contenttype = str;
  }
  public void setFileName(String str){
    this.m_filename = str;
  }
  public void setValue(byte[] b){
    this.m_bytes = b;
  }
  public byte[] getBytes(){
    return this.m_bytes;
  }
}