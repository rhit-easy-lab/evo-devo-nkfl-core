package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.Test;

import agent.Agent;
import agent.NKPhenotype;
import agent.Phenotype;
import agent.Step;
import control.Constants;
import control.PropParser;
import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NumOnes;

class AgentTest {
	//Common test params
	
	private void init()
	{
		PropParser.load("src/test/testConfig.properties");
	}
	
	/**
	 * Common methods for a commonly used test agent, the agent using NKPhenotype
	 * 
	 * Do not change or move the instantiated items. They are set to values to
	 * correspond with the tests, and they are not global to assure that each testPhenotype
	 * created has no cross-dependencies.
	 * @return
	 */
	private Agent getTestAgent()
	{
		int[] bitstr = {0, 1, 0, 1};
		
		NKPhenotype testPhenotype = new NKPhenotype(bitstr);
		FitnessFunction fitFunction = new NumOnes();
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
		return new Agent(fitFunction, testPhenotype, testProg, testBlocks, null);
	}
	
	@Test
	void TestConfigSuccess()
	{
		init();
		assertEquals(Constants.FILENAME, "JUNITTESTFILE");
	}
	
	@Test
	void NKPhenotypeReturnCorrectNeighbors() {
		int[] bitstr = {0, 1, 0, 1};
		int[] neighbor1 = {1, 1, 0, 1};
		int[] neighbor2 = {0, 0, 0, 1};
		int[] neighbor3 = {0, 1, 1, 1};
		int[] neighbor4 = {0, 1, 0, 0};
		NKPhenotype test = new NKPhenotype(bitstr);
		List<Phenotype> neighbors = test.getNeighbors();
		assertEquals(4, neighbors.size());
		assertArrayEquals(neighbor1, ((NKPhenotype)neighbors.get(0)).getBitstring());
		assertArrayEquals(neighbor2, ((NKPhenotype)neighbors.get(1)).getBitstring());
		assertArrayEquals(neighbor3, ((NKPhenotype)neighbors.get(2)).getBitstring());
		assertArrayEquals(neighbor4, ((NKPhenotype)neighbors.get(3)).getBitstring());
	}
	
	@Test
	void NKPhenotypeTableIndexTest() {
		int[] bitstr = {0, 1, 0, 1};
		
		NKPhenotype testPhenotype = new NKPhenotype(bitstr);
		assertEquals(Math.pow(2, 2)+Math.pow(2, 0), testPhenotype.getNKTableIndex());
		
		int[] bitstr2 = {1, 1, 1, 0};
		
		NKPhenotype testPhenotype2 = new NKPhenotype(bitstr2);
		assertEquals(Math.pow(2, 3)+Math.pow(2,2)+Math.pow(2, 1), testPhenotype2.getNKTableIndex());
		
		int[] bitstr3 = {0, 0, 0, 0};
		
		NKPhenotype testPhenotype3 = new NKPhenotype(bitstr3);
		assertEquals(0, testPhenotype3.getNKTableIndex());
	}

	/**
	 * Initializes a simple agent that just needs to climb 3 times
	 */
	@Test
	void AgentSimpleProgram() {
		//test setup
		init();
		List<Step> b1 = new ArrayList<Step>();
		b1.add(Step.SteepestClimb);
		b1.add(Step.SteepestClimb);
		List<Step> b2 = new ArrayList<Step>();
		b2.add(Step.SteepestFall);
		b2.add(Step.SteepestFall);
		
		//test strategy compilation
		Agent testAgent = getTestAgent();
		assertEquals(6, testAgent.strategy.size());
		List<Step> expectedStrategy = new ArrayList<Step>();
		expectedStrategy.addAll(b1);
		expectedStrategy.addAll(b2);
		expectedStrategy.addAll(b1);
		assertEquals(expectedStrategy, testAgent.strategy);
		
		//test fitness throughout strategy
		testAgent.executeStrategy();
		List<Double> e = new ArrayList<Double>();
		e.add(2.0);
		e.add(3.0);
		e.add(4.0);
		e.add(3.0);
		e.add(2.0);
		e.add(3.0);
		e.add(4.0);
		//test finished variables
		assertEquals(e, testAgent.getFitnessHistory());
		assertEquals(7, testAgent.getFitnessHistory().size()); //should be size 7, including initial fitness and 6 steps
	}
	
	/**
	 * Test makes sure that identical children start with the same params, but do not have
	 * 
	 */
	@Test
	void testIdenticalChildDecoupledAndMutates()
	{
		init();
		TestClass_DeterministicNextDouble dnd = new TestClass_DeterministicNextDouble(0);
		try {
			SeededRandom.swapSeededRandomJUNITTest(dnd);
		} catch (OperationNotSupportedException e) {
			System.out.println("Failed to swap SeededRandom");
			assertTrue(false);
		}
		dnd.replaceNextDouble(0);//Guarantees mutations
		
		Agent testAgent = getTestAgent();
		Agent testChild = testAgent.identicalChild();

		
		assertEquals(testAgent.getBlocks(), testChild.getBlocks());
		assertEquals(testAgent.getProgram(), testChild.getProgram());
		assertEquals(testAgent.getStrategy(), testChild.getStrategy());
		
		testChild.mutate();
		
		assertNotEquals(testAgent.getBlocks(), testChild.getBlocks());
		assertNotEquals(testAgent.getProgram(), testChild.getProgram());
		assertNotEquals(testAgent.getStrategy(), testChild.getStrategy());
	}
	
	/**
	 * Test the mutation method of NKPhenotype. Per testConfig.properties, all mutation rates are expected
	 * to be 0.25 (phenotypeMutationRate, blockMutationRate, programMutationRate)
	 */
	@Test
	void NKPhenotypeObeysMutationRates() {
		init();
		TestClass_DeterministicNextDouble dnd = new TestClass_DeterministicNextDouble(0);
		try {
			SeededRandom.swapSeededRandomJUNITTest(dnd);
		} catch (OperationNotSupportedException e) {
			System.out.println("Failed to swap SeededRandom");
			assertTrue(false);
		}
		
		int[] control = {0, 1, 0, 1};
		for(double mrate=0.0; mrate<1.0; mrate+=0.01)
		{
			int[] bitstr = {0, 1, 0, 1};
			NKPhenotype testPhenotype = new NKPhenotype(bitstr);
			dnd.replaceNextDouble(mrate);
			testPhenotype.mutate();
			if(mrate < 0.25)
			{
				assertNotEquals(control, testPhenotype.getBitstring());
				assertEquals(4, testPhenotype.getDistance(new NKPhenotype(control)));
			}
			else
			{
				assertEquals(bitstr, testPhenotype.getBitstring());
			}
		}
	}
	
	/**
	 * Test the mutation method of NKPhenotype. Per testConfig.properties, all mutation rates are expected
	 * to be 0.25 (phenotypeMutationRate, blockMutationRate, programMutationRate)
	 */
	@Test
	void AgentObeysMutationRates() {
		init();
		TestClass_DeterministicNextDouble dnd = new TestClass_DeterministicNextDouble(0);
		try {
			SeededRandom.swapSeededRandomJUNITTest(dnd);
		} catch (OperationNotSupportedException e) {
			System.out.println("Failed to swap SeededRandom");
			assertTrue(false);
		}
		
		Agent control = getTestAgent();
		for(double mrate=0.0; mrate<1.0; mrate+=0.01)
		{
			Agent testAgent = control.identicalChild();
			dnd.replaceNextDouble(mrate);
			testAgent.mutate();
			if(mrate < 0.25)
			{
				assertNotEquals(control.getBlocks(), testAgent.getBlocks());
				assertNotEquals(control.getProgram(), testAgent.getProgram());
			}
			else
			{
				assertEquals(control.getBlocks(), testAgent.getBlocks());
				assertEquals(control.getProgram(), testAgent.getProgram());
			}
		}
	}
}
