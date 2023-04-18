package agent;

import java.util.ArrayList;
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
public class NKPhenotype extends Phenotype {
	
	private int[] bitstring;
	
	/**
	 * Creates a random NKPhenotype
	 */
	public NKPhenotype() {
		this.bitstring = new int[Constants.N];
		
		//Set each bit to a random 0 or 1
		for(int bit=0; bit<Constants.N; bit++)
		{
			bitstring[bit] = SeededRandom.getInstance().nextInt(2);
		}
	}
	
	/**
	 * Creates a NKPhenotype with the specified bitstring
	 * @param bitstring
	 */
	public NKPhenotype(int[] bitstring) {
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
			neighbors.add(new NKPhenotype(neighbor));
		}
		return neighbors;
	}

	@Override
	public Phenotype getIdenticalCopy() {
		int[] bitstringCopy = new int[bitstring.length];
		for(int bit=0; bit<bitstring.length; bit++)
		{
			bitstringCopy[bit]=bitstring[bit];
		}
		return new NKPhenotype(bitstringCopy);
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
	//See the lessons on NK landscapes for a more thorough description
	public int getNKTableIndex() {
		int tableIndex = 0;
		for(int bit=0; bit<bitstring.length; bit++)
		{
			if(bitstring[bit]==1)
			{
				tableIndex += Math.pow(2, Constants.N-bit-1);
			}
		}
		return tableIndex;
	}
	
	public int[] getBitstring() {
		return bitstring;
	}
}
