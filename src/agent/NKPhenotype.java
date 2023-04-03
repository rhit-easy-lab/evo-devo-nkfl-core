package agent;

import java.util.List;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Phenotype getIdenticalCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}
	
}
