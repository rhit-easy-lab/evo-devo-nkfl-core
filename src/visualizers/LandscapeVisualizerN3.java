package visualizers;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

import control.PropParser;
import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;
import visualizerComponents.N3LandscapeFrame;

/**
 * This class exists to house the main method of the simulator,
 * as well as handling connection to parts external to the simulator,
 * such as loading in config files and writing results to files.
 * 
 * @author Jacob Ashworth
 *
 */
public class LandscapeVisualizerN3 {
	
	
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
		
		JFrame frame = new JFrame();
		
		FitnessFunction f = new NKLandscape(SeededRandom.getInstance().nextInt(), 3, 0);
		N3LandscapeFrame landscape = new N3LandscapeFrame(f, 500, 20, 20);
		
		frame.add(landscape);
		
		frame.setBounds(100, 100, 700, 700);
        frame.setVisible(true);
	}
}
