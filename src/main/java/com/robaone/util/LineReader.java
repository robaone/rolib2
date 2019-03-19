package com.robaone.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class LineReader {
	protected FileReader m_file;
	protected BufferedReader m_buff;
	public LineReader(String filename) throws Exception {
		this.m_file = new FileReader(filename);
		this.m_buff = new BufferedReader(this.m_file);
	}
	public LineReader(File file) throws Exception {
		this.m_file = new FileReader(file);
		this.m_buff = new BufferedReader(this.m_file);
	}
	public LineReader(InputStream in) throws Exception {
		this.m_buff = new BufferedReader(new InputStreamReader(in));
	}
	public LineReader(FileReader file) throws Exception {
		this.m_file = file;
		this.m_buff = new BufferedReader(this.m_file);
	}
	public String ReadLine() throws Exception {
		String buffer = "";
		buffer = this.m_buff.readLine();
		if(buffer != null){
			return buffer;
		}else{
			close();
			throw new java.io.EOFException("End of File Reached");
		}
	}
	public void close() throws IOException{
		if(this.m_file != null){
			try{this.m_buff.close();}catch(Exception e){}
		}
	}
}