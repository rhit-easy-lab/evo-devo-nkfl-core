package agent;

import java.util.ArrayList;
import java.util.List;

import control.Constants;
import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;

/**
 * The Agent class represents the agents that navigate the landscape. They use
 * their developmental program and blocks to construct their developmental strategy,
 * then execute their developmental strategy by incrementally making changes to their
 * phenotype.
 * 
 * This implementation uses the NKPhenotype for use on NK landscapes.
 * 
 * @author Jacob Ashworth
 *
 */
public class Agent implements Comparable<Agent> {
	//Fields related to evolutionary past
	private Agent parent = null;
	
	//Fields related to developmental strategy
	private List<Integer> program;
	private List<List<Step>> blocks;
	//Test
	int testr = 0;
	int testsc = 0;
	int testsf = 0;
	int testss = 0;
	//These fields are generated by compileStrategy()
	private List<Step> strategy;
	/* This integer tracks the next step to run, so currentStep=0 means
	 * step 0 has not yet been executed. */
	private int currentStep = 0;
	 
	//Fields related to phenotype.
	private Phenotype phenotype;
	/* phenotypeHistory is indexed by step number, so phenotypeHistory.get(4)
	 * would give you the phenotype immediately before step 4 was executed */
	private List<Phenotype> phenotypeHistory;
	
	//Fields related to fitness
	private FitnessFunction fitnessFunction;
	private double fitness;
	//fitnessHistory is indexed the same as phenotypeHistory
	private List<Double> fitnessHistory;
	private List<Step> stepList = new ArrayList<Step>();
	private List<String> stringList = new ArrayList<String>();
	
	/**
	 * Default constructor for Agent. Creates an agent with a random initial phenotype,
	 * program, and blocks.
	 * 
	 * @param fitnessFunction FitnessFunction for the agent to operate on
	 */
	public Agent(FitnessFunction fitnessFunction)
	{
		//Switch statement to control which phenotype type is initialized
		this.phenotype = Agent.getRandomPhenotype();
		this.fitnessFunction = fitnessFunction;
		this.fitness = fitnessFunction.getFitness(phenotype); 
		
		//Create a random program
		program = new ArrayList<Integer>();
		for(int programIndex=0; programIndex < Constants.PROGRAM_LENGTH; programIndex++)
		{
			program.add(SeededRandom.getInstance().nextInt(Constants.NUMBER_OF_BLOCKS));
		}
		//Create specific blocks
		//Experiment, remove if needed***
		blocks = new ArrayList<List<Step>>();
		
		for(int block = 0; block < Constants.NUMBER_OF_BLOCKS; block++) {
			List<Step> thisBlock = new ArrayList<Step>();
			//*** Hardcoded experiment stuff
//			if(block == 0) {
//				thisBlock.add(Step.SteepestClimb);
//				thisBlock.add(Step.SteepestClimb);
//			}
//			if(block == 1){
//				thisBlock.add(Step.SteepestFall);
//			}else {
//				thisBlock.add(Step.SteepestClimb);
//			}
			for(int stepIndex=0; stepIndex < Constants.BLOCK_LENGTH; stepIndex++)
				{
					thisBlock.add(Step.randomStep());
				}
				blocks.add(thisBlock);
		}
				
//			for(int size = 0; size < Constants.BLOCK_LENGTH; size++) {
//				
//				if(size==3) {
//					thisBlock.add(Step.SteepestFall);
//					
//				}else {
//					thisBlock.add(Step.SteepestClimb);
//				}
//			}
//			
//			
//			blocks.add(thisBlock);
//		}
			
			
//		for(int block=0; block < Constants.NUMBER_OF_BLOCKS; block++)
//		{
//			List<Step> thisBlock = new ArrayList<Step>();
//			if(block == 0) {
//				thisBlock.add(Step.SteepestClimb);
//				for(int stepIndex=1; stepIndex < Constants.BLOCK_LENGTH; stepIndex++)
//				{
//					thisBlock.add(Step.SameStep);
//					
//				}
//			}
//			if(block == 1) {
//				thisBlock.add(Step.SteepestFall);
//				for(int stepIndex=1; stepIndex < Constants.BLOCK_LENGTH; stepIndex++)
//				{
//					thisBlock.add(Step.SameStep);
//				}
//			}else {
//				for(int stepIndex = 0; stepIndex < Constants.BLOCK_LENGTH; stepIndex++) {
//					thisBlock.add(Step.RandomWalk);
//				}
//			}
			
				
			
			
			
//			if(block==0) {
//				thisBlock.add(Step.SteepestClimb);
//				for(int stepIndex=1; stepIndex < Constants.BLOCK_LENGTH; stepIndex++)
//				{
//					thisBlock.add(Step.SameStep);
//				}
//				blocks.add(thisBlock);
//			}else {
//				for(int stepIndex=0; stepIndex < Constants.BLOCK_LENGTH; stepIndex++)
//				{
//					thisBlock.add(Step.SteepestFall);
//				}
//				blocks.add(thisBlock);
						
			
			
//			for(int stepIndex=0; stepIndex < Constants.BLOCK_LENGTH; stepIndex++)
//			{
//				thisBlock.add(Step.randomStep());
//			}
//			blocks.add(thisBlock);
		//}
		//Compile the program and blocks into the strategy
		this.compileStrategyAndInitializeHistory();
	}
	
	
	
	
	
	/**
	 * Constructor for Agent. Creates an agent with a given initial phenotype,
	 * program, and blocks.
	 * 
	 * @param fitnessFunction FitnessFunction for the agent to operate on
	 */
	public Agent(FitnessFunction fitnessFunction, Phenotype p)
	{
		this(fitnessFunction);
		
		//Set our phenotype to the given one. Make a copy so we don't have a ton of agents linked to the same phenotype
		this.phenotype = p.getIdenticalCopy();
		this.compileStrategyAndInitializeHistory();
	}
	
	/**
	 * Constructor used to exactly specify an agent, with all relevant fields. 
	 * Mostly called by the identicalChild() function.
	 */
	public Agent(FitnessFunction fitnessFunction, Phenotype phenotype, List<Integer> program, List<List<Step>> blocks, Agent parent)
	{
		this.phenotype = phenotype;
		this.fitnessFunction = fitnessFunction;
		this.fitness = fitnessFunction.getFitness(phenotype);
		this.program = program;
		this.blocks = blocks;
		this.parent = parent;
		//Compile the program and blocks into the strategy
		this.compileStrategyAndInitializeHistory();
	}
	
	public static Phenotype getRandomPhenotype() {
		switch(Constants.PHENOTYPE_TYPE.toLowerCase()) {
			case "nkphenotype":
				return new NKPhenotype();
			case "nkphenotypefast":
				return new NKPhenotypeFast();
			case "exaptphenotype":
				return new ExaptPhenotype((ExaptPhenotype) ExaptPhenotype.getFirst(100, 4, 30, 4, 0, 100, 3));
			default:
				System.out.println("PHENOTYPE_TYPE not recognized");
				return null;
		}
	}
	
	/**
	 * This method compiles the developmental strategy from the program and
	 * the blocks by concatenating copies of the blocks specified in the order
	 * of the program
	 */
	private void compileStrategyAndInitializeHistory()
	{
		strategy = new ArrayList<Step>();
		for(Integer blockIndex : program)
		{
			List<Step> block = blocks.get(blockIndex);
			strategy.addAll(block);
		}
		
		//Experiment that worked:
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(1));
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(2));
//		strategy.addAll(blocks.get(2));
		String test = blocks.get(0).get(0).toString();
		if(test == "RandomWalk") {
			testr++;
		}
		if(test == "SteepestClimb") {
			testsc++;
		}
		if(test == "SteepestFall") {
			testsf++;
		}
		if(test == "SameStep") {
			testss++;
		}
//		System.out.print("SteepestClimb" + testsc + "  ");
//		System.out.print("SteepestFall" + testsf + "  ");
//		System.out.print("SameStep" + testss + "  ");
//		System.out.print("RandomWalk" + testr + "  ");
		
		this.phenotypeHistory = new ArrayList<Phenotype>();
		this.phenotypeHistory.add(phenotype);
		this.fitnessHistory = new ArrayList<Double>();
		this.fitness = fitnessFunction.getFitness(phenotype); 
		this.fitnessHistory.add(fitness);
	}
	
	/**
	 * Method to check if the agent has executed all steps in its developmental strategy
	 * @return
	 */
	public boolean agentDeveloped()
	{
		return currentStep == strategy.size();
	}
	
	/**
	 * Executes the current step of the developmental strategy
	 */
	public void executeSingleStep()
	{
		//Get the step to execute and increment step counter
		Step stepToExecute = strategy.get(currentStep);
		currentStep++;
		
		//This is the main switch statement that controls which step is executed
		//When adding new steps, create a new step function and enum in Step, and add
		//it to the switch statement.
		switch(stepToExecute) {
			case RandomWalk:
				randomWalk();
				break;
			case SteepestClimb:
				steepestClimb();
				break;
			case SteepestFall:
				steepestFall();
				break;
			case SameStep:
				sameStep();
				break;
		}
		
		//update the agent's fitness value
		fitness = fitnessFunction.getFitness(phenotype);
		
		//Store information
		phenotypeHistory.add(phenotype);
		fitnessHistory.add(fitness);
	}
	
	private void steepestClimb()
	{
		stringList.add("SteepestClimb");
		List<Phenotype> locations = phenotype.getNeighbors();
		locations.add(phenotype);
		Phenotype bestLocation = locations.get(0);
		for(Phenotype location : locations)
		{
			if(fitnessFunction.getFitness(location) > fitnessFunction.getFitness(bestLocation))
			{
				bestLocation = location;
			}
		}
		phenotype = bestLocation;
	}






	/**
	 * Executes the entire developmental strategy
	 * 
	 * If strategyExecutionSampleSize>1, this method will
	 * execute the entire strategy many times, but will delete
	 * the contents of pastPhenotypes since tracking past phenotype
	 * with a sample size greater than 1 isn't helpful
	 */
	public void executeStrategy()
	{
		
		if(Constants.STRATEGY_SAMPLE_SIZE == 1)
		{
			while(!agentDeveloped())
			{
				executeSingleStep();
			}
		}
		else
		{
			//Set up somewhere to average our fitness information
			double[] summedFitnesses = new double[strategy.size()];
			//Run our strategy STRATEGY_SAMPLE_SIZE times, and sum the results
			for(int sample=0; sample<Constants.STRATEGY_SAMPLE_SIZE; sample++)
			{
				//Wipe all information and reset agent to initial configuration
				currentStep = 0;
				phenotype = phenotypeHistory.get(0);
				phenotypeHistory.clear();
				phenotypeHistory.add(phenotype);
				fitness = fitnessHistory.get(0);
				fitnessHistory.clear();
				fitnessHistory.add(fitness);
				//Run the strategy
				while(!agentDeveloped())
				{
					executeSingleStep();
				}
				//Store fitness information
				for(int stepIndex = 0; stepIndex < strategy.size(); stepIndex++)
				{
					summedFitnesses[stepIndex]=summedFitnesses[stepIndex] + fitnessHistory.get(stepIndex);
				}
			}
			//Now store the average fitness at each step in the pastFitnesses list
			double initialFitness = fitnessHistory.get(0);
			fitnessHistory.clear();
			fitnessHistory.add(initialFitness);
			for(int stepIndex = 0; stepIndex < strategy.size(); stepIndex++)
			{
				fitnessHistory.add(summedFitnesses[stepIndex] / (double) Constants.STRATEGY_SAMPLE_SIZE);
			}
			fitness = fitnessHistory.get(fitnessHistory.size()-1);
		}
	}
	
	/**
	 * Makes an identical copy of the agent, except that this is a child
	 * of the current agent, so its parent is set accordingly
	 * 
	 * The child is setup under its initial configuration, so even if the
	 * parent's strategy has been run, the child's strategy will not yet
	 * be run
	 * 
	 * @return child that is identical to the parent
	 */
	public Agent identicalChild()
	{
		//We need to make a copy of each list to ensure the object references aren't passed
		List<Integer> childProgram = new ArrayList<Integer>();
		for(Integer i : program)
		{
			childProgram.add(i);
		}
		
		List<List<Step>> childBlocks = new ArrayList<List<Step>>();
		for(int block=0; block < blocks.size(); block++)
		{
			List<Step> childBlock = new ArrayList<Step>();
			for(Step s : blocks.get(block))
			{
				childBlock.add(s);
			}
			childBlocks.add(childBlock);
		}
		
		Phenotype childPhenotype = phenotype.getIdenticalCopy();
		
		//Use the constructor to make the new agent, and return it
		return new Agent(fitnessFunction, childPhenotype, childProgram, childBlocks, this);
	}
	
	/**
	 * Mutates the agent, using config specified mutation rates to make changes
	 * to the program, blocks, and phenotype
	 */
	public void mutate()
	{
		//phenotype mutation
		phenotype.mutate();
		
		//program mutation
		for(int programIndex=0; programIndex < program.size(); programIndex++)
		{
			if(SeededRandom.getInstance().nextDouble() < Constants.PROGRAM_MUTATION_RATE && program.size()>0)
			{
				//Ensure we don't roll the same block again
				int newBlock = SeededRandom.getInstance().nextInt(blocks.size());
				if(newBlock >= program.get(programIndex))
				{
					newBlock = (newBlock + 1) % blocks.size();
				}
				program.set(programIndex, newBlock);
			}
		}
		//block mutation
		for(int block=0; block < blocks.size(); block++)
		{
			for(int blockIndex=0; blockIndex < blocks.get(0).size(); blockIndex++)
			{
				if(SeededRandom.getInstance().nextDouble() < Constants.BLOCK_MUTATION_RATE && blocks.size()>0)
				{
					//Ensure we don't roll the same step again
					Step currentStep = blocks.get(block).get(blockIndex);
					List<Step> newSteps = new ArrayList<Step>();
					for(Step s : Step.validSteps)
					{
						if(!s.equals(currentStep))
						{
							newSteps.add(s);
						}
					}
					blocks.get(block).set(blockIndex, newSteps.get(SeededRandom.getInstance().nextInt(newSteps.size())));
				}
			}
		}
		
		//Refresh these values since they may have changed
		this.compileStrategyAndInitializeHistory();
	}
	
	/**
	 * Compares fitness for sorting
	 */
	@Override
	public int compareTo(Agent other) {
		if(this.fitness > other.fitness)
		{
			return 1;
		}
		else if(this.fitness == other.fitness)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * This area is for the different types of steps in the developmental program. These
	 * steps are called during the execution of executeSingleStep or executeStrategy.
	 * 
	 * These steps are only responsible for updating the value in phenotype. Saving previous
	 * phenotypes and updating the fitness is the responsibility of executeSingleStep
	 */
	
	/**
	 * Chooses a random neighbor of the current phenotype to be the new phenotype
	 */
	private void randomWalk()
	{
		stringList.add("RandomWalk");
		List<Phenotype> neighbors = phenotype.getNeighbors();
		phenotype = neighbors.get(SeededRandom.getInstance().nextInt(neighbors.size()));
	}
	
	private void sameStep()
	{
		
		Step same = strategy.get(currentStep - 1);
		stepList.add(same);
		if(same.toString() == "SameStep") {
			if(stringList.size() < 1) {
				randomWalk();
				stringList.add("RandomWalk");
				return;
			}
			String last = stringList.get(stringList.size() - 1);
			if(last == "SteepestClimb") {
				steepestClimb();
				stringList.add("SteepestClimb");
			}
			if(last == "SteepestFall") {
				steepestFall();
				stringList.add("SteepestFall");
			}
			if(last == "RandomWalk") {
				randomWalk();
				stringList.add("RandomWalk");
			} 
		}else {
	
		if(same.toString() == "SteepestClimb") {
			steepestClimb();
			stringList.add("SteepestClimb");
		}
		if(same.toString() == "SteepestFall") {
			steepestFall();
			stringList.add("SteepestFall");
		}
		if(same.toString() == "RandomWalk") {
			randomWalk();
			stringList.add("RandomWalk");
		}
		}
	}
	/**
	 * Chooses the lowest fitness neighbor to be the new phenotype
	 */
	private void steepestFall()
	{
		stringList.add("SteepestFall");
		List<Phenotype> locations = phenotype.getNeighbors();
		locations.add(phenotype);
		Phenotype worstLocation = locations.get(0);
		for(Phenotype location : locations)
		{
			if(fitnessFunction.getFitness(location) < fitnessFunction.getFitness(worstLocation))
			{
				worstLocation = location;
			}
		}
		phenotype = worstLocation;
	}
	//-------------------------------------------------------------------------------------------------------------------

	
	public List<List<Step>> getBlocks() {
		return blocks;
	}

	public List<Integer> getProgram() {
		return program;
	}

	public List<Double> getFitnessHistory() {
		return fitnessHistory;
	}
	
	public List<Step> getStrategy() {
		return strategy;
	}
	
	public List<Phenotype> getPhenotypeHistory(){
		return phenotypeHistory;
	}
	
	/**
	 * Returns the fitness of the agent if it is developed, otherwise it prints an error and returns -1.
	 * @return
	 */
	public double getFinalFitness() {
		if(agentDeveloped())
		{
			return fitness;
		}
		else
		{
			System.out.println("getFinalFitness called on undeveloped agent");
			return -1;
		}
	}
	public double getFitness() {
		return fitness;
	}
	public Phenotype getPheno() {
		return phenotype;
	}
	/**
	 * Returns the phenotype of the agent if it is developed, otherwise it prints an error and returns null.
	 * @return
	 */
	public Phenotype getFinalPhenotype() {
		if(agentDeveloped())
		{
			return phenotype;
		}
		else
		{
			System.out.println("getFinalFitness called on undeveloped agent");
			return null;
		}
	}
}
