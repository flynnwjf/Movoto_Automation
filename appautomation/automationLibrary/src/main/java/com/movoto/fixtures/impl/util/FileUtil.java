package com.movoto.fixtures.impl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FilenameUtils;

import com.movoto.fixtures.FileType;

public class FileUtil {

	public static synchronized Map<String, String> loadPropertiesFile(String source) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			if (source != null) {
				File file = new File(source);
				if (file.exists()) {
					in = new FileInputStream(file);
					properties.load(in);
					Map<String, String> map = new HashMap<>();
					for (final String name : properties.stringPropertyNames())
						map.put(name, properties.getProperty(name));
					return map;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public static Map<String, String> readPropertiesFile(String filePath) throws IOException {
		File configFile = new File(filePath);
		Map<String, String> map = new HashMap<>();
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			reader.close();
			for (final String name : props.stringPropertyNames()) {
				if (props.getProperty(name) != null && props.getProperty(name).length() > 0) {
					map.put(name, props.getProperty(name));
				} else {
					map.put(name, null);
				}
			}
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		}
		return map;
	}
	
	public static synchronized FileInputStream getFileAsInStream(String filePath) throws FileNotFoundException{
		FileInputStream fis = null;
		File file = new File(filePath);
		if(file.exists()){
			fis = new FileInputStream(file);
		}
		return fis;
	}
	
	public static Map<String, String> readFileAsMap(String filePath) throws IOException {
		Map<String, String> map = new HashMap<>();
		if (filePath != null) {
			String extension = FilenameUtils.getExtension(filePath);
			switch (extension) {
			case FileType.FILE_TYPE_PROPERTIES:
				map = readPropertiesFile(filePath);
				break;
			case FileType.FILE_TYPE_JSON:
				
				break;
			case FileType.FILE_TYPE_EXCEL:

				break;
			case FileType.FILE_TYPE_EXCEL_OLD:

				break;
			default:
				break;
			}
		}
		return map;
	}	
	
}
