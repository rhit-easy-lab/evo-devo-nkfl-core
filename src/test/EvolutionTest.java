package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import agent.Agent;
import agent.NKPhenotype;
import agent.Phenotype;
import agent.Step;
import control.Constants;
import control.PropParser;
import evolution.Generation;
import evolution.SelectionStrategy;
import evolution.SelectionTournament;
import evolution.SelectionTruncation;
import landscape.FitnessFunction;
import landscape.NumOnes;
import evolution.Simulation;

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
	
	/**
	 * Tests the truncation selection method. Uses an empty strategy and the NumOnes fitness function for
	 * fitness to make it as simple as possible
	 */
	@Test
	void TruncationTest() {
		init();
		
		int[] goodBitstring = {1, 1, 1, 1};
		int[] middleBitstring = {1, 0, 0, 0};
		int[] badBitstring = {0, 0, 0, 0};
		
		Phenotype good = new NKPhenotype(goodBitstring);
		Phenotype mid = new NKPhenotype(middleBitstring);
		Phenotype bad = new NKPhenotype(badBitstring);
		
		FitnessFunction f = new NumOnes();
		
		List<Integer> emptyProgram = new ArrayList<Integer>();
		List<List<Step>> emptyBlocks = new ArrayList<List<Step>>();
		
		Agent goodAgent = new Agent(f, good, emptyProgram, emptyBlocks, null);
		Agent midAgent = new Agent(f, mid, emptyProgram, emptyBlocks, null);
		Agent badAgent = new Agent(f, bad, emptyProgram, emptyBlocks, null);
		
		List<Agent> testGen = new ArrayList<Agent>();
		
		for(int i=0; i<25; i++)
		{
			testGen.add(goodAgent.identicalChild());
			testGen.add(midAgent.identicalChild());
			testGen.add(midAgent.identicalChild());
			testGen.add(badAgent.identicalChild());
		}
		
		assertEquals(100, testGen.size());
		assertEquals(0, testGen.get(0).getStrategy().size());
		
		Generation testGeneration = new Generation(testGen);
		testGeneration.executeAllStrategies();
		
		//Generation 1 has 25 with 4 fitness, 50 with 1 fitness, and 25 with 0 fitness, so average=150/100=1.5
		assertEquals(1.5, testGeneration.getAverageFinalFitness());
		
		SelectionStrategy select = new SelectionTruncation();
		//Truncation should kill 25 with 1 fitness and 25 with 0, then clone the 25 with 4 and 25 with 1, so
		//the new average fitness is 4*50+1*50=250/100=2.5. With a .25 mutation rate, we can expect that the next
		//generation will be above 2.25 fitness
		Generation nextGeneration = select.getNextGeneration(testGeneration);
		nextGeneration.executeAllStrategies(); 
		assertTrue(2.25 < nextGeneration.getAverageFinalFitness());
		
		//Now we check to make sure that the expected survivors (25 4 and 25 1) are actually in the next generation.
		int[] numFit = new int[5];
		for(Agent a : nextGeneration.getAgents())
		{
			numFit[(int) a.getFinalFitness()] += 1;
		}
		
		assertTrue(25 <= numFit[4]);
		assertTrue(25 <= numFit[1]);
		assertEquals(100, numFit[0]+numFit[1]+numFit[2]+numFit[3]+numFit[4]);
	}
	
	/**
	 * Tests the truncation selection method. Uses an empty strategy and the NumOnes fitness function for
	 * fitness to make it as simple as possible
	 */
	@Test
	void TournamentTest() {
		init();
		
		int[] goodBitstring = {1, 1, 1, 1};
		int[] middleBitstring = {1, 0, 0, 0};
		int[] badBitstring = {0, 0, 0, 0};
		
		Phenotype good = new NKPhenotype(goodBitstring);
		Phenotype mid = new NKPhenotype(middleBitstring);
		Phenotype bad = new NKPhenotype(badBitstring);
		
		FitnessFunction f = new NumOnes();
		
		List<Integer> emptyProgram = new ArrayList<Integer>();
		List<List<Step>> emptyBlocks = new ArrayList<List<Step>>();
		
		Agent goodAgent = new Agent(f, good, emptyProgram, emptyBlocks, null);
		Agent midAgent = new Agent(f, mid, emptyProgram, emptyBlocks, null);
		Agent badAgent = new Agent(f, bad, emptyProgram, emptyBlocks, null);
		
		List<Agent> testGen = new ArrayList<Agent>();
		
		for(int i=0; i<25; i++)
		{
			testGen.add(goodAgent.identicalChild());
			testGen.add(midAgent.identicalChild());
			testGen.add(midAgent.identicalChild());
			testGen.add(badAgent.identicalChild());
		}
		
		assertEquals(100, testGen.size());
		assertEquals(0, testGen.get(0).getStrategy().size());
		
		Generation testGeneration = new Generation(testGen);
		testGeneration.executeAllStrategies();
		
		//Generation 1 has 25 with 4 fitness, 50 with 1 fitness, and 25 with 0 fitness, so average=150/100=1.5
		assertEquals(1.5, testGeneration.getAverageFinalFitness());
		
		SelectionStrategy select = new SelectionTournament();

		Generation nextGeneration = select.getNextGeneration(testGeneration);
		nextGeneration.executeAllStrategies(); 
		assertTrue(2.0 < nextGeneration.getAverageFinalFitness());
		
		//Now we check to make sure that the expected survivors (25 4 and 25 1) are actually in the next generation.
		int[] numFit = new int[5];
		for(Agent a : nextGeneration.getAgents())
		{
			numFit[(int) a.getFinalFitness()] += 1;
		}
		
		assertTrue(10 <= numFit[4]);//testing elitism
		assertEquals(100, numFit[0]+numFit[1]+numFit[2]+numFit[3]+numFit[4]);
	}
}
