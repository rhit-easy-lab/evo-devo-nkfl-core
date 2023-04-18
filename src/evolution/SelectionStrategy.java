package evolution;

/**
 * The SelectionStrategy controls how new generations are created from
 * old generations. Implementations of SelectionStrategy are responsible
 * for creating the new generation, handling elitism, and calling the
 * mutation methods of the correct individuals.
 * 
 * As convention, implementations of SelectionStrategy are named Selection...
 * (e.g. SelectionTrunction for Truncation Selection) for consistency, and so
 * that they appear together in the file explorer.
 * 
 * Wikipedia's page on selection methods (https://en.wikipedia.org/wiki/Selection_(genetic_algorithm))
 * is a good resource for understanding these methods and how they operate.
 * 
 * @author Jacob Ashworth
 *
 */
public interface SelectionStrategy {
	//It is safe to assume parentGeneration is sorted when implementing this operation
	public Generation getNextGeneration(Generation parentGeneration);
}
