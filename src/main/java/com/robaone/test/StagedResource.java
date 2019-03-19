package com.robaone.test;

import java.io.File;

public interface StagedResource {

	void replaceContent(String string, String replacement_string) throws Exception;
	
	File getFile();
	
	void save(String content) throws Exception;

	String readAsString();

}
