package control;

import java.util.Random;

import javax.naming.OperationNotSupportedException;

@SuppressWarnings("serial")
public class SeededRandom extends Random {
	protected static SeededRandom thisSingle = null;
	private long seed;
	
	protected SeededRandom(long seed)
	{
		//If the seed is -1, that means we want a random seed
		if(seed != -1)
		{			
			this.seed = seed;
		}
		else
		{
			this.seed = this.nextLong();
		}
		this.setSeed(seed);
	}
	
	public static SeededRandom getInstance() {
		if (thisSingle == null) {
			thisSingle = new SeededRandom(Constants.SEED);
		}
		return thisSingle;
	}
	
	/**
	 * Method used for seeding by JUNIT tests. Only works when filename is set to JUNITTESTFILE.
	 * 
	 * @param S, new seededrandom to use
	 * @throws OperationNotSupportedException 
	 */
	public static void swapSeededRandomJUNITTest(SeededRandom s) throws OperationNotSupportedException
	{
		if(Constants.FILENAME.equals("JUNITTESTFILE"))
		{
			if(s == null)
			{
				SeededRandom.thisSingle = new SeededRandom(Constants.SEED);
			}
			else
			{
				SeededRandom.thisSingle = s;
			}
		}
		else
		{
			throw new OperationNotSupportedException();
		}
	}
	
	public long getSeed()
	{
		return seed;
	}
}
