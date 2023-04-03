package control;

public class Constants {
//	Overall Constants
	public static final int SEED = Integer.parseInt(PropParser.getProperty("seed"));
	
//	NK Landscape Constants
	public static final int N = Integer.parseInt(PropParser.getProperty("N"));
	public static final int K = Integer.parseInt(PropParser.getProperty("K"));
	
//	Evolution Constants
	public static final double PHENOTYPE_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("PhenotypeMutationRate"));
	
//	Developmental Program Constants
	public static final String STEPS = PropParser.getProperty("steps");
}
