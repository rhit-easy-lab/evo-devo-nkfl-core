package evolution;

import java.util.ArrayList;
import java.util.Arrays;

import control.Constants;
import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;
import landscape.NumOnes;

/**
 * Represents a single evolutionary simulation on a single landscape.
 * 
 * @author Jacob Ashworth
 *
 */
public class Simulation {
	
	//Fitness Function used in the simulation
	private FitnessFunction fitFunction;
	//Stores all generations of the simulation
	private ArrayList<Generation> generations = new ArrayList<>();
	//Used to generate new generations
	private SelectionStrategy selectionStrategy;
	
	public Simulation()
	{
		//Switch statement to control which fitness function is initialized
		switch(Constants.FITNESS_FUNCTION_TYPE.toLowerCase()) {
			case "nklandscape":
				this.fitFunction = new NKLandscape(SeededRandom.getInstance().nextInt());
				break;
			case "numones":
				this.fitFunction = new NumOnes();
				break;
			default:
				System.out.println("FITNESS_FUNCTION_TYPE not recognized");
				this.fitFunction = null;
		}
		
		//Switch statement to control selection type used in evolutionary loop
		switch(Constants.SELECTION_TYPE.toLowerCase()) {
			case "truncation":
				this.selectionStrategy = new SelectionTruncation();
				break;
			case "tournament":
				this.selectionStrategy = new SelectionTournament();
				break;
			default:
				System.out.println("SELECTION_TYPE not recognized");
				this.fitFunction = null;
		}
		
		Generation initialGeneration = new Generation(fitFunction);
		initialGeneration.executeAllStrategies();
		generations.add(initialGeneration);
	}
	
	public Simulation(Generation initialGeneration, FitnessFunction fitFunction, SelectionStrategy selectStrategy)
	{
		this.fitFunction = fitFunction;
		this.selectionStrategy = selectStrategy;
		initialGeneration.executeAllStrategies();
		generations.add(initialGeneration);
	}
	
	public void runSimulation()
	{
		runSimulation(Constants.NUM_GENERATIONS);
	}
	
	public void runSimulation(int numGenerations)
	{
		for(int generationNumber = 1; generationNumber < numGenerations+1; generationNumber++)
		{
			//if enough generations have passed, invoke the fitness function's dynamic behavior
			if(Constants.GENERATIONS_PER_CYCLE % Constants.GENERATIONS_PER_CYCLE == 0)
			{
				fitFunction.changeCycle();
			}
			//make and run the next generation
			Generation nextGeneration = selectionStrategy.getNextGeneration(generations.get(generations.size()-1));
			nextGeneration.executeAllStrategies();
			generations.add(nextGeneration);
		}
	}
	
	public ArrayList<Generation> getGenerations() {
		return generations;
	}

}
