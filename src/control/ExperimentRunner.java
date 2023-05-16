package control;

import java.io.IOException;

import evolution.Simulation;

/**
 * This class exists to house the main method of the simulator,
 * as well as handling connection to parts external to the simulator,
 * such as loading in config files and writing results to files.
 * 
 * @author Jacob Ashworth
 *
 */
public class ExperimentRunner {
	
	public static void main(String[] args) throws IOException 
	{
		//The first argument passed into ExperimentRunner is the configuration file.
		//If no configuration file is specified, it runs using defaultConfig.properties
		if(args.length>=1)
		{
			System.out.println("Using the configuration file: " + args[0]);
			PropParser.load(args[0]);
		}
		else
		{
			System.out.println("No configuration file specified. Continuing with default paramaters.");
			PropParser.load(PropParser.defaultFilename);
		}
		
		//Set up the .csv writer
		ExperimentWriter writer = new ExperimentWriter();
		
		System.out.println("Writing to csv file " + ExperimentWriter.rename(Constants.FILENAME));
		//Run all of our experiments, and write them to the file as we go
		long startTime = System.currentTimeMillis()/1000;
		for(int simulationNum=0; simulationNum<Constants.SAMPLE_SIZE; simulationNum++)
		{
			Simulation sim = new Simulation();
			sim.runSimulation();
			writer.writeSim(sim, Constants.GENERATION_SPACING, Constants.REQUIRE_LAST_GENERATION);
			
			long endTime = System.currentTimeMillis()/1000;
			long estimatedRemainingTime = (endTime-startTime)/(simulationNum+1)*(Constants.SAMPLE_SIZE-simulationNum-1);
			System.out.println("Simulation " + (simulationNum+1) + " of " + Constants.SAMPLE_SIZE + " complete, estimated minutes remaining: " + Math.round(100.0*estimatedRemainingTime/60.0)/100.0);
		}
		
		//finish up
		writer.closePrintWriter();
		System.out.println("Completed, experiment written to " + ExperimentWriter.rename(Constants.FILENAME));
	}
}
