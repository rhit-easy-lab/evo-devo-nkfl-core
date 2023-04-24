package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import agent.Agent;
import agent.NKPhenotype;
import agent.Phenotype;
import control.Constants;
import control.PropParser;
import evolution.Generation;
import landscape.FitnessFunction;
import landscape.NumOnes;

class EvolutionTest {
	
	public void init() {
		PropParser.load("src/test/testConfig.properties");
	}

	/**
	 * Tests to ensure that generations are created with the correct spread of bits in the phenotype
	 * 
	 * In theory can fail <1% of the time due to randomness, will very rarely actually fail
	 */
	@Test
	void RandomGenerationCreation() {
		init();
		FitnessFunction f = new NumOnes();
		
		int[] sums = new int[100];
		for(int gennum=0; gennum < 20; gennum++)
		{
			Generation g = new Generation(f);
			for(Agent a : g.getAgents())
			{
				for(int bit=0; bit<Constants.N; bit++)
				{
					sums[bit+gennum*4] = sums[bit] + ((NKPhenotype)(a.getPhenotypeHistory().get(0))).getBitstring()[bit];
				}
			}
		}
		
		int range = 20;
		int successes = 0;
		for(int i=0; i<Constants.N*20; i++)
		{
			if((50-range <= sums[i]) && (sums[i] <= 50+range))
			{
				successes++;
			}
		}
		
		assertTrue(successes >= 80);
	}
	
	/**
	 * Tests to make sure that generations sort fitnesses from best to worst, and getBest returns the best agent
	 */
	@Test
	void AgentsRunAndSort() {
		init();
		FitnessFunction f = new NumOnes();
		Generation g = new Generation(f);
		g.executeAllStrategies();
		
		double fit = g.getBest().getFinalFitness();
		
		for(Agent a : g.getAgents())
		{
			assertTrue(a.getFinalFitness() <= fit);
			fit = a.getFinalFitness();
		}
	}

}
