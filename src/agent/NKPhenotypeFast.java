package agent;

import java.util.ArrayList;
import java.util.List;

import control.Constants;
import control.SeededRandom;

public class NKPhenotypeFast extends NKPhenotype {
	private int phenotype;
	
	/**
	 * Creates a random NKPhenotype
	 */
	public NKPhenotypeFast() {
		this(Constants.N);
	}
	
	/**
	 * Creates a random NKPhenotype from a specified n
	 * @param n
	 */
	public NKPhenotypeFast(int n) {
		phenotype = SeededRandom.getInstance().nextInt(1 << n);
	}
	
	/**
	 * Creates a random NKPhenotype from a specified n
	 * @param n
	 */
	public NKPhenotypeFast(int n, int phenotype) {
		this.phenotype = phenotype;
	}

//	List<Phenotype> neighbors = null;
	@Override
	public List<Phenotype> getNeighbors() {
//		if(neighbors == null)
//		{
			List<Phenotype> neighbors = new ArrayList<Phenotype>();
			
			//Add each phenotype that is one bit flip away from the current
			for(int flipLocation=0; flipLocation<Constants.N; flipLocation++)
			{
				int neighbor = 1 << flipLocation;
				neighbors.add(new NKPhenotypeFast(Constants.N, neighbor ^ phenotype));
			}
//			this.neighbors = neighbors;
//		}
		return neighbors;
	}

	@Override
	public NKPhenotype getIdenticalCopy() {
		return new NKPhenotypeFast(Constants.N, phenotype);
	}

	@Override
	public void mutate() {
		for(int bit=0; bit<Constants.N; bit++)
		{
			if(SeededRandom.getInstance().nextDouble() < Constants.PHENOTYPE_MUTATION_RATE)
			{
				flipBit(bit);
			}
		}
	}
	
	//Method to turn the bitstring into a index, usable by the NKLandscape.
	/**
	 * Returns the bitstring as a single integer, representing its location in the array used by NKLandscape.
	 * 
	 * The first index is the lowest power of 2, and the final index is the highest power of 2, so the
	 * bitstring [0,1,0,1] on an N=4 landscape returns 0*2^0+1*2^1+0*2^2+1*2^3 = 2^1+2^3 = 9
	 * @return
	 */
	public int getNKTableIndex() {
		return phenotype;
	}
	
	public int[] getBitstring() {
		return null;
	}
	
	/**
	 * Returns the distance (as an integer) to another NKPhenotype. Used in testing and hamming distance calculations
	 * @param other
	 * @return
	 */
	public int getDistance(NKPhenotype other)
	{
		return -1;
	}
	
	/**
	 * Flips the specified bit of a 
	 * @param index
	 */
	public void flipBit(int index) {
		this.phenotype = phenotype ^ (1<<index);
//		neighbors = null;
	}
}
