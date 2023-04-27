package visualizer;

import java.io.IOException;

import control.PropParser;

/**
 * This class exists to house the main method of the simulator,
 * as well as handling connection to parts external to the simulator,
 * such as loading in config files and writing results to files.
 * 
 * @author Jacob Ashworth
 *
 */
public class LandscapeVisualizer {
	
	public static void main(String[] args) throws IOException 
	{
		//The first argument passed into ExperimentRunner is the configuration file.
		//If no configuration file is specified, it runs using defaultConfig.properties
		if(args.length>=1)
		{
			PropParser.load(args[0]);
		}
		else
		{
			System.out.println("No configuration file specified. Continuing with default paramaters.");
			PropParser.load(PropParser.defaultFilename);
		}
		
		//Set up the .csv writer
		
	}
}
