package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.Test;

import control.PropParser;
import control.SeededRandom;
import evolution.Generation;
import evolution.SelectionStrategy;
import evolution.SelectionTruncation;
import evolution.Simulation;
import landscape.FitnessFunction;
import landscape.NKLandscape;

class ComprehesiveTest {
	
	int sampleSize = 10;
	
	private void init()
	{
		PropParser.load("src/test/testConfig.properties");
		try {
			SeededRandom.swapSeededRandomJUNITTest(null);
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	int generationsAllowedForK0 = 20;
	@Test
	void testK0Solves() {
		init();
		
		int solves = 0;
		double averageFinalFit = 0.0;
		for(int i=0; i<sampleSize; i++)
		{
			FitnessFunction nkfl = new NKLandscape(SeededRandom.getInstance().nextInt(), 15, 0);
			SelectionStrategy select = new SelectionTruncation();
			Generation initialGeneration = new Generation(nkfl);
			
			Simulation testSim = new Simulation(initialGeneration, nkfl, select);
			testSim.runSimulation(generationsAllowedForK0);
			
			List<Generation> gens = testSim.getGenerations();
			averageFinalFit += gens.get(gens.size()-1).getBest().getFinalFitness();
			if(1.0==gens.get(gens.size()-1).getBest().getFinalFitness())
			{
				solves++;
			}
		}
		averageFinalFit /= sampleSize;
		System.out.println("Solved " + solves + " out of " + sampleSize + " K=0 landscapes, average final fitness:" + averageFinalFit);
		//Should solve all
		assertEquals(sampleSize, solves);
	}
	
	int generationsAllowedForK1 = 20;
	@Test
	void testK1Solves() {
		init();
		
		int solves = 0;
		double averageFinalFit = 0.0;
		for(int i=0; i<sampleSize; i++)
		{
			FitnessFunction nkfl = new NKLandscape(SeededRandom.getInstance().nextInt(), 15, 1);
			SelectionStrategy select = new SelectionTruncation();
			Generation initialGeneration = new Generation(nkfl);
			
			Simulation testSim = new Simulation(initialGeneration, nkfl, select);
			testSim.runSimulation(generationsAllowedForK1);
			
			List<Generation> gens = testSim.getGenerations();
			averageFinalFit += gens.get(gens.size()-1).getBest().getFinalFitness();
			if(1.0==gens.get(gens.size()-1).getBest().getFinalFitness())
			{
				solves++;
			}
		}
		averageFinalFit /= sampleSize;
		System.out.println("Solved " + solves + " out of " + sampleSize + " K=1 landscapes, average final fitness:" + averageFinalFit);
		//Should solve all
		assertTrue(sampleSize*.8 <= solves);
	}
	
	int generationsAllowedForK10 = 30;
	double maxSuccessesPercentK10 = .9;
	@Test
	void testNotK10Solves() {
		init();
		
		int solves = 0;
		double averageFinalFit = 0.0;
		for(int i=0; i<sampleSize; i++)
		{
			FitnessFunction nkfl = new NKLandscape(SeededRandom.getInstance().nextInt(), 15, 10);
			SelectionStrategy select = new SelectionTruncation();
			Generation initialGeneration = new Generation(nkfl);
			
			Simulation testSim = new Simulation(initialGeneration, nkfl, select);
			testSim.runSimulation(generationsAllowedForK10);
			
			List<Generation> gens = testSim.getGenerations();
			averageFinalFit += gens.get(gens.size()-1).getBest().getFinalFitness();
			if(1.0==gens.get(gens.size()-1).getBest().getFinalFitness())
			{
				solves++;
			}
		}
		averageFinalFit /= sampleSize;
		System.out.println("Solved " + solves + " out of " + sampleSize + " K=10 landscapes, average final fitness:" + averageFinalFit);
		assertTrue(solves <= sampleSize*maxSuccessesPercentK10);
	}
	
	int generationsAllowedForK5 = 20;
	double maxSuccessesPercentK5 = .9;
	double minSuccessesPercentK5 = .9;
	int K5SampleSize = 30;
	@Test
	void testSolvesK5WithinReason() {
		int solves = 0;
		double averageFinalFit = 0.0;
		for(int i=0; i<K5SampleSize; i++)
		{
			FitnessFunction nkfl = new NKLandscape(SeededRandom.getInstance().nextInt(), 15, 5);
			SelectionStrategy select = new SelectionTruncation();
			Generation initialGeneration = new Generation(nkfl);
			
			Simulation testSim = new Simulation(initialGeneration, nkfl, select);
			testSim.runSimulation(generationsAllowedForK5);
			
			List<Generation> gens = testSim.getGenerations();
			averageFinalFit += gens.get(gens.size()-1).getBest().getFinalFitness();
			if(1.0==gens.get(gens.size()-1).getBest().getFinalFitness())
			{
				solves++;
			}
		}
		averageFinalFit /= K5SampleSize;

		System.out.println("Solved " + solves + " out of " + K5SampleSize + " K=5 landscapes, average final fitness:" + averageFinalFit);

	}
}
