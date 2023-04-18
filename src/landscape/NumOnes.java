package landscape;

import agent.NKPhenotype;
import agent.Phenotype;

/**
 * Very simple fitness function example. Works with NKPhenotype, but instead of
 * running an actual NKLandscape, it instead just checks how many ones are in the phenotype.
 * 
 * @author Jacob Ashworth
 *
 */
public class NumOnes implements FitnessFunction {

	@Override
	public double getFitness(Phenotype p) {
		int[] bitstr = ((NKPhenotype) p).getBitstring();
		int score = 0;
		for(int i=0; i<bitstr.length; i++)
		{
			if(bitstr[i]==1)
			{
				score++;
			}
		}
		return score;
	}

	@Override
	public void changeCycle() {
		// Do Nothing
	}

}
