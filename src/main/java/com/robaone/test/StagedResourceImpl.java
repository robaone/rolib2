package com.robaone.test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Pattern;

import com.robaone.log.LogErrorWriter;

public class StagedResourceImpl implements StagedResource {
	private File resourceFile;
	public StagedResourceImpl(File file) {
		this.resourceFile = file;
	}
	@Override
	public void replaceContent(String regex, String replacement)
			throws Exception {
		String content = this.readAsString();
		try {
			Pattern.compile(regex);
			content = content.replaceAll(regex, replacement);
		} catch(Exception e) {
			content = content.replace(regex, replacement);
		}
		save(content);
	}
	@Override
	public String toString() {
		return this.getFile().toString();
	}
	@Override
	public String readAsString() {
		FileReader reader = null;
		StringBuffer bout = null;
		try{
			try{
				char[] buffer = new char[512];
				reader = new FileReader(this.getFile());
				for(int i = reader.read(buffer);i > -1;i = reader.read(buffer)){
					bout = bout == null ? new StringBuffer() : bout;
					bout.append(buffer, 0, i);
				}
			}finally{
				if(reader != null){
					reader.close();
				}
			}
		}catch(Exception e){
			LogErrorWriter.log(this.getClass(), e);
		}
		return bout == null ? null :  bout.toString();
	}
	@Override
	public File getFile() {
		return this.resourceFile;
	}
	@Override
	public void save(String content) throws Exception {
		try(FileWriter writer = new FileWriter(this.getFile())){
			writer.write(content);
			writer.flush();
		}
	}

}
