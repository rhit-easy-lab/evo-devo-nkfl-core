package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

//  https://stackoverflow.com/questions/29896927/constants-and-properties-in-java
/**
 * This class serves to translate the data from a configuration file into the initialization code you
 * run to start your program. This design principle reduces the chance of errors from re-running code
 * and also works well with the logger so that when you run something, you can store the parameters
 * you used when you decided to run it as well.
 * 
 * This version of PropParser also loads in a default file and uses those values for the parameters
 * not specified by the given configuration file.
 * 
 * @author Jason Yoder, Jacob Ashworth
 */

public class PropParser {
	public static final String defaultFilename = "config/defaultConfig.properties";
	
	public static void load(String filename) {
		//Load our config
		try {
			getInstance().load( new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			System.err.println("Config file filename: "+filename+" not found.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Load the default config
		try {
			getDefaultInstance().load( new FileReader(new File(defaultFilename)));
		} catch (FileNotFoundException e) {
			System.err.println("Default config file not found.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Singleton for our properties
	private static Properties properties;
	private static Properties getInstance() {
		if (properties == null) {
			properties = new Properties();
		}
		return properties;
	}
	
	//Singleton for default properties
	private static Properties defaultProperties;
	private static Properties getDefaultInstance() {
		if (defaultProperties == null) {
			defaultProperties = new Properties();
		}
		return defaultProperties;
	}

	//Gets a property, defaults to defaultProperties if not found
	public static String getProperty(String propID) {
		return getInstance().getProperty(propID, getDefaultInstance().getProperty(propID));
	}
	

}
