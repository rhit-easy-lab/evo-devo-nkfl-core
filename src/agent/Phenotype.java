package agent;

import java.util.List;

/**
 * This is the agent's phenotype. Since the phenotype and the
 * fitness function must agree in order for the program to make sense, ensure
 * that whenever you change the fitness function you change the phenotype
 * accordingly.
 * 
 * @author Jacob Ashworth
 *
 */
public abstract class Phenotype {
	/**
	 * Provides a list of other phenotypes that are considered 'neighbors'. Used by the
	 * agent during execution of the developmental program.
	 * @return list of neighboring phenotypes
	 */
	public abstract List<Phenotype> getNeighbors();
	
	/**
	 * Creates an identical copy of the phenotype. Used in evolutionary loop
	 * 
	 * @return Identical copy of phenotype, with no shared dependencies
	 */
	public abstract Phenotype getIdenticalCopy();
	
	/**
	 * Mutates the currently selected phenotype. Used in evolutionary loop
	 */
	public abstract void mutate();
}
