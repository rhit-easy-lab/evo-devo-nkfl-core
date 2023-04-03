package landscape;

import agent.Phenotype;

/**
 * This is the fitness function that the simulator operates on.
 * 
 * Since specific fitness functions are closely related to the phenotypes which they
 * use, ensure that any change to the fitness function is reflected by the corresponding
 * change to the phenotype it uses.
 * 
 * The default implementation here uses the NKLandscape as a fitness function, and the
 * NKPhenotype as its corresponding phenotype. See those classes for further information.
 * 
 * @author Jacob Ashworth
 *
 */
public interface FitnessFunction {
	//Evaluates the fitness of a phenotype
	public double getFitness(Phenotype p);
	
	//Runs a change cycle of the landscape. On static fitness functions this does nothing.
	public void changeCycle();
}
