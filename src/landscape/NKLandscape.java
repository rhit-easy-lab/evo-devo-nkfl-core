package landscape;

import java.util.Random;

import agent.Bitstring;
import agent.Phenotype;
import control.Constants;

public class NKLandscape implements FitnessFunction {
	// Instance Variables
	public double[] fitTable; // This is set in generateFitnessTable

	public final int landscapeSeed, n, k;
	Random landscapeRnd; // This exists so our entire landscape has its own seed

	/**
	 * Initializes a NK fitness landscape using the constants
	 * 
	 * @param seed for the landscape
	 */
	public NKLandscape(int seed) {
		this.landscapeSeed = seed;
		this.landscapeRnd = new Random(landscapeSeed);
		this.n = Constants.N;
		this.k = Constants.K;
		
		double[][] interactionTable = generateRandomInteractionTable();
		generateFitnessTable(interactionTable); // this has no return value because it stores its data in the landscape
												// globals
	}
	
	/**
	 * Initializes a NK fitness landscape
	 * 
	 * @param seed for the landscape
	 * @param n number of genes in the phenotype
	 * @param k number of epistatic interactions
	 */
	public NKLandscape(int seed, int n, int k) {
		this.landscapeSeed = seed;
		this.landscapeRnd = new Random(landscapeSeed);
		this.n = n;
		this.k = k;
		
		double[][] interactionTable = generateRandomInteractionTable();
		generateFitnessTable(interactionTable); // this has no return value because it stores its data in the landscape
												// globals
	}

	/**
	 * The NKLandscape stores all of its fitnesses in the fitTable. In order to get
	 * the fitness of a NKPhenotype we need to convert the bitstring into an integer
	 * 
	 * @param p
	 * @return
	 */
	@Override
	public double getFitness(Phenotype p) {
		// Since this is an NKLandscape, we cast to NKPhenotype
		Bitstring phenotype = (Bitstring) p;
		// Get the fitness of the phenotype from our table using the provided tableIndex
		// function
		return fitTable[phenotype.getNKTableIndex()];
	}

	@Override
	public void changeCycle() {
		// Static Landscape, do nothing
	}

	/**
	 * Generates a completely random table of interactions.
	 * 
	 * @return random table size [n][2^(k+1)]
	 */
	public double[][] generateRandomInteractionTable() {
		double[][] interactions = new double[n][(int) Math.pow(2, (k + 1))];
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < (int) Math.pow(2, (k + 1)); y++) {
				interactions[x][y] = landscapeRnd.nextDouble();
			}
		}
		return interactions;
	}

	/**
	 * Generates a fitness table (length n) from the passed interaction table
	 * 
	 * Saves the minFitness, maxFitness, and fitnessTable in globals as this
	 * information will be useful later
	 * 
	 * @param interactionTable the table of interactions that will be used to create
	 *                         the fitness table
	 */
	public void generateFitnessTable(double[][] interactionTable) {
		double[] fitnessTable = new double[1 << n];
		double minFitness = 100000;
		double maxFitness = 0;

		for (int genotypeInt = 0; genotypeInt < ((int) Math.pow(2, n)); genotypeInt++) {
			double fitness = 0;

			for (int gene = 0; gene < n; gene++) // loops through each gene (bit)
			{
				int interactions = 0;
				for (int neighbor = 0; neighbor < k + 1; neighbor++) // loop through all our neighbors
				{
					int neighborIndex = (gene + neighbor) % n;
					interactions = interactions
							| ((genotypeInt >>> (n - 1 - neighborIndex)) & 1) << (k - neighbor);
				}
				fitness += interactionTable[gene][interactions]; // add our neighbor's fitness to our own
			}
			fitnessTable[genotypeInt] = fitness; // store our calculated fitness

			// This updates the min and max fitnesses
			if (minFitness > fitness) {
				minFitness = fitness;
			}
			if (maxFitness < fitness) {
				maxFitness = fitness;
			}
		}

		this.fitTable = scaleTable(fitnessTable, maxFitness, minFitness);
	}

	/**
	 * Scales a table to range from 0-1 instead of (max-min) called by
	 * generateFitnessTable
	 * 
	 * @param table
	 * @param max
	 * @param min
	 * @return scaled table with max 1 and min 0
	 */
	public double[] scaleTable(double[] table, double max, double min) {
		double[] scaledTable = new double[table.length];
		for (int index = 0; index < table.length; index++) {
			scaledTable[index] = (table[index] - min) / (max - min); // makes all table values 0-1
//			scaledTable[index] = Math.pow(scaledTable[index], 8); // scale
		}
		return scaledTable;
	}

	@Override
	public String toString() {
		return "Local NK Landscape N: "+n+" K: "+k + " Seed: " + landscapeSeed;
	}
	
	
}
