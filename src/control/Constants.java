package control;

public class Constants {
//	Overall Constants
	public static final int SEED = Integer.parseInt(PropParser.getProperty("seed"));
	public static final int SAMPLE_SIZE = Integer.parseInt(PropParser.getProperty("sampleSize"));
	public static final int STRATEGY_SAMPLE_SIZE = Integer.parseInt(PropParser.getProperty("strategyExecutionSampleSize"));
	public static final String FITNESS_FUNCTION_TYPE = PropParser.getProperty("fitnessFunctionType");
	public static final String PHENOTYPE_TYPE = PropParser.getProperty("phenotypeType");
	
//	CSV Output Constants	
	public static final String FILENAME = PropParser.getProperty("filename");
	public static final int GENERATION_SPACING = Integer.parseInt(PropParser.getProperty("generationSpacing"));
	public static final Boolean REQUIRE_LAST_GENERATION = Boolean.parseBoolean(PropParser.getProperty("requireLastGeneration"));
	
//	NK Landscape Constants
	public static final int N = Integer.parseInt(PropParser.getProperty("N"));
	public static final int K = Integer.parseInt(PropParser.getProperty("K"));
	public static final int GENERATIONS_PER_CYCLE = Integer.parseInt(PropParser.getProperty("generationsPerCycle"));
	
//	Evolution Constants
	public static final int NUM_GENERATIONS = Integer.parseInt(PropParser.getProperty("numGenerations"));
	public static final int GENERATION_SIZE = Integer.parseInt(PropParser.getProperty("generationSize"));
	public static final double PHENOTYPE_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("phenotypeMutationRate"));
	public static final double BLOCK_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("blockMutationRate"));
	public static final double PROGRAM_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("programMutationRate"));
	public static final String SELECTION_TYPE = PropParser.getProperty("selectionType");
	
//	Developmental Program Constants
	public static final String STEPS = PropParser.getProperty("steps");
	public static final int PROGRAM_LENGTH = Integer.parseInt(PropParser.getProperty("programLength"));
	public static final int BLOCK_LENGTH = Integer.parseInt(PropParser.getProperty("blockLength"));
	public static final int NUMBER_OF_BLOCKS = Integer.parseInt(PropParser.getProperty("numberOfBlocks"));
}
