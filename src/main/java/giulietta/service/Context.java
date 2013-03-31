package giulietta.service;

import giulietta.config.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Context {

	private static Properties m_properties = loadProperties();
	
	
	private static Properties loadProperties(){
		File propFile = new File(Config.GIULIETTA_PROPERTIES_PATH.getValue());
		FileReader reader;
		try {
			reader = new FileReader(propFile);
			Properties properties=new Properties();
			properties.load(reader);
			return properties;

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getProperty(Config config) {
		return (String)m_properties.getProperty(config.getValue());
	}
	
	public static String getProperty(Config config,String def) {
		if (m_properties.contains(config.getValue())) {
			return getProperty(config);
		}
		return def;
	}
	
	public static String getString(String property){
		return (String)m_properties.getProperty(property); 
	}
	
	public static boolean getBool(Config config) {
		return Boolean.valueOf((String)m_properties.getProperty(config.getValue()));
	}
	
	public static FileReader getFileReader(String property){
		try {
			return new FileReader(new File(m_properties.getProperty(property)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} 
	}
	
	
}
