package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import agent.Agent;
import agent.NKPhenotype;
import agent.Phenotype;
import agent.Step;
import control.PropParser;
import landscape.FitnessFunction;
import landscape.NumOnes;

class AgentTest {
	int[] bitstr = {0, 1, 0, 1};
	int[] neighbor1 = {1, 1, 0, 1};
	int[] neighbor2 = {0, 0, 0, 1};
	int[] neighbor3 = {0, 1, 1, 1};
	int[] neighbor4 = {0, 1, 0, 0};
	
	FitnessFunction fitFunction = new NumOnes();
	
	private void init()
	{
		PropParser.load(PropParser.defaultFilename);
	}
	
	@Test
	void NKPhenotypeReturnCorrectNeighbors() {
		NKPhenotype test = new NKPhenotype(bitstr);
		List<Phenotype> neighbors = test.getNeighbors();
		assertEquals(4, neighbors.size());
		assertArrayEquals(neighbor1, ((NKPhenotype)neighbors.get(0)).getBitstring());
		assertArrayEquals(neighbor2, ((NKPhenotype)neighbors.get(1)).getBitstring());
		assertArrayEquals(neighbor3, ((NKPhenotype)neighbors.get(2)).getBitstring());
		assertArrayEquals(neighbor4, ((NKPhenotype)neighbors.get(3)).getBitstring());
	}

	/**
	 * Initializes a simple agent that just needs to climb 3 times
	 */
	@Test
	void AgentSimpleProgram() {
		//test setup
		init();
		NKPhenotype testPhenotype = new NKPhenotype(bitstr);
		List<Integer> testProg = new ArrayList<Integer>();
		testProg.add(0);
		testProg.add(1);
		testProg.add(0);
		List<List<Step>> testBlocks = new ArrayList<List<Step>>(); 
		List<Step> b1 = new ArrayList<Step>();
		b1.add(Step.SteepestClimb);
		b1.add(Step.SteepestClimb);
		List<Step> b2 = new ArrayList<Step>();
		b2.add(Step.SteepestFall);
		b2.add(Step.SteepestFall);
		testBlocks.add(b1);
		testBlocks.add(b2);
		
		//test strategy compilation
		Agent testAgent = new Agent(fitFunction, testPhenotype, testProg, testBlocks, null);
		assertEquals(6, testAgent.strategy.size());
		List<Step> expectedStrategy = new ArrayList<Step>();
		expectedStrategy.addAll(b1);
		expectedStrategy.addAll(b2);
		expectedStrategy.addAll(b1);
		assertEquals(expectedStrategy, testAgent.strategy);
		
		//test fitness throughout strategy
		assertEquals(2, testAgent.fitness);
		testAgent.executeSingleStep();
		assertEquals(3, testAgent.fitness);
		testAgent.executeSingleStep();
		assertEquals(4, testAgent.fitness);
		testAgent.executeSingleStep();
		assertEquals(3, testAgent.fitness);
		testAgent.executeSingleStep();
		assertEquals(2, testAgent.fitness);
		testAgent.executeSingleStep();
		assertEquals(3, testAgent.fitness);
		testAgent.executeSingleStep();
		assertEquals(4, testAgent.fitness);
	}
}
