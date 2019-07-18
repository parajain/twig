package com.twig.template.core.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;



public class FileProcess {
	public static List<String> getLines(String filename) {
		List<String> lines  = new ArrayList<String>();
		try{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filename)));
			
			String strline = null;
			while((strline = reader.readLine()) != null) {
				lines.add(strline);			
			}
		reader.close();		
		}catch(Exception e){
			e.printStackTrace();
		}
		return lines;
	}
	
	public static String readFile(String filename) {
		List<String> strings = getLines(filename);
		String str = String.join("", strings);
		return str;
	}

	public static void doSave(String fileName, String str) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(str.getBytes());
			fos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		fos = null;
	}

	public static void doAppend(String fileName, String str) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName, true);
			fos.write(str.getBytes());
			fos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		fos = null;
	}


}

