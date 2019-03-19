package com.robaone.test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseTest {
	private String workingFolder;
	Logger log = Logger.getLogger(this.getClass().getName());
	public static final String FILESEPARATOR = System.getProperty("file.separator");
	protected void stageResource(String path,String filename) throws Exception {
		log.fine("stageResource(String path="+path+",String filename="+filename);
		InputStream in = this.getClass().getResourceAsStream(path);
		File outFile = new File(this.getWorkingFolder() + System.getProperty("file.separator")+filename);
		String parent = outFile.getParent();
		new File(parent).mkdirs();
		FileOutputStream fout = null;
		try{
			fout = new FileOutputStream(outFile);
			copy(in, fout);
		}finally{
			try{
				in.close();
			}catch(Exception e){}
			try{
				fout.flush();
				fout.close();
			}catch(Exception e){}
		}
	}
	protected void copy(InputStream in, FileOutputStream fout) throws IOException {
		if(in == null){
			throw new NullPointerException("input stream is null");
		}
		if(fout == null){
			throw new NullPointerException("ouput stream is null");
		}
		byte[] buffer = new byte[500];
		for(int i = in.read(buffer);i > -1;i = in.read(buffer)){
			fout.write(buffer, 0, i);
		}
	}
	protected void createStagingFolder(String folder) throws Exception {
		File file = new File(this.getWorkingFolder()+"/"+folder);
		file.mkdirs();
	}
	protected boolean deleteStagedFile(String filename){
		File file = new File(this.getWorkingFolder()+System.getProperty("file.separator")+filename);
		return file.delete();
	}
	protected boolean deleteStagedFile(final Pattern filename){
		File[] files = new File(this.getWorkingFolder()).listFiles(new FileFilter(){

			@Override
			public boolean accept(File pathname) {
				Matcher m = filename.matcher(pathname.getName());
				return m.matches() && pathname.isFile();
			}
			
		});
		boolean retval = true;
		for(File file : files){
			if(!file.delete()){
				retval = false;
			}
		}
		return retval;
	}
	public StagedResource getResource(String filename) throws Exception {
		File file = new File(this.getWorkingFolder()+"/"+filename);
		StagedResource resource = new StagedResourceImpl(file);
		return resource;
	}
	public StagedResource getResource(File file) throws Exception {
		StagedResource resource = new StagedResourceImpl(file);
		return resource;
	}
	public boolean deleteStagedFolder(String folder){
		File file = new File(this.getWorkingFolder()+System.getProperty("file.separator")+folder);
		this.deleteFolder(file);
		return file.delete();
	}
	public boolean deleteStagedFolder(final Pattern filename){
		File[] files = new File(this.getWorkingFolder()).listFiles(new FileFilter(){

			@Override
			public boolean accept(File pathname) {
				Matcher m = filename.matcher(pathname.getName());
				return m.matches() && pathname.isDirectory();
			}
			
		});
		boolean retval = true;
		for(File file : files){
			if(!this.deleteFolder(file)){
				retval = false;
			}
		}
		return retval;
	}
	private boolean deleteFolder(File f) {
		boolean retval = true;
		if(f.isDirectory()){
			File[] files = f.listFiles();
			for(File fd : files){
				if(fd.isDirectory()){
					if(!deleteFolder(fd)){
						retval = false;
					}
				}else{
					if(!fd.delete()){
						retval = false;
					}
				}
			}
			retval = f.delete();
		}
		return retval;
	}
	
	protected Enumeration<Object> createEnumeration(final Object[] array) {
		Enumeration<Object> e = new Enumeration<Object>(){
			int index = 0;
			@Override
			public boolean hasMoreElements() {
				return array.length > index;
			}

			@Override
			public Object nextElement() {
				return array[index++];
			}

		};
		return e;
	}
	
	public String getWorkingFolder() {
		return workingFolder;
	}
	public void setWorkingFolder(String workingFolder) {
		this.workingFolder = workingFolder;
		File file = new File(workingFolder);
		file.mkdirs();
	}
	
}