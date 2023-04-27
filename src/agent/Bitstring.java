package agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.Constants;
import control.SeededRandom;

/**
 * This is the phenotype to be used with the basic NK Fitness Landscape.
 * 
 * This phenotype is a list of N bits, where neighbors are defined by
 * each list of N bits that is a singular bit flip away from our current
 * phenotype.
 * 
 * @author Jacob Ashworth
 *
 */
public class Bitstring extends Phenotype {
	
	private int[] bitstring;
	
	/**
	 * Creates a random NKPhenotype
	 */
	public Bitstring() {
		this(Constants.N);
	}
	
	/**
	 * Creates a random NKPhenotype from a specified n
	 * @param n
	 */
	public Bitstring(int n) {
		this.bitstring = new int[n];
		
		//Set each bit to a random 0 or 1
		for(int bit=0; bit<n; bit++)
		{
			bitstring[bit] = SeededRandom.getInstance().nextInt(2);
		}
	}
	
	/**
	 * Creates a NKPhenotype with the specified bitstring
	 * @param bitstring
	 */
	public Bitstring(int[] bitstring) {
		this.bitstring = bitstring;
	}

	@Override
	public List<Phenotype> getNeighbors() {
		List<Phenotype> neighbors = new ArrayList<Phenotype>();
		
		//Add each phenotype that is one bit flip away from the current
		for(int flipLocation=0; flipLocation<bitstring.length; flipLocation++)
		{
			int[] neighbor = new int[bitstring.length];
			for(int bit=0; bit<bitstring.length; bit++)
			{
				if(bit!=flipLocation)
				{
					neighbor[bit]=bitstring[bit];
				}
				else
				{
					neighbor[bit]=(bitstring[bit]+1)%2; //Simple way to flip a bit
				}
			}
			neighbors.add(new Bitstring(neighbor));
		}
		return neighbors;
	}

	@Override
	public Bitstring getIdenticalCopy() {
		int[] bitstringCopy = new int[bitstring.length];
		for(int bit=0; bit<bitstring.length; bit++)
		{
			bitstringCopy[bit]=bitstring[bit];
		}
		return new Bitstring(bitstringCopy);
	}

	@Override
	public void mutate() {
		for(int bit=0; bit<bitstring.length; bit++)
		{
			if(SeededRandom.getInstance().nextDouble() < Constants.PHENOTYPE_MUTATION_RATE)
			{
				bitstring[bit] = (bitstring[bit]+1)%2;
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
		int tableIndex = 0;
		for(int bit=0; bit<bitstring.length; bit++)
		{
			if(bitstring[bit]==1)
			{
				tableIndex += Math.pow(2, bit);
			}
		}
		return tableIndex;
	}
	
	public int[] getBitstring() {
		return bitstring;
	}
	
	/**
	 * Returns the distance (as an integer) to another NKPhenotype. Used in testing and hamming distance calculations
	 * @param other
	 * @return
	 */
	public int getDistance(Bitstring other)
	{
		int dist = 0;
		for(int index=0; index<bitstring.length; index++)
		{
			if(other.getBitstring()[index]!=bitstring[index])
			{
				dist += 1;
			}
		}
		return dist; 
	}
	
	/**
	 * Flips the specified bit of a 
	 * @param index
	 */
	public void flipBit(int index) {
		if(bitstring[index]==0) {
			bitstring[index]=1;
		}else {
			bitstring[index]=0;
		}
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(bitstring);
	}
}
