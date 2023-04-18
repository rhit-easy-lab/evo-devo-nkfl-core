package control;

import java.util.Random;

@SuppressWarnings("serial")
public class SeededRandom extends Random {
	private static SeededRandom thisSingle = null;
	
	private SeededRandom(long seed)
	{
		//If the seed is -1, that means we want a random seed
		if(seed != -1)
		{			
			this.setSeed(seed);
		}
	}
	
	public static SeededRandom getInstance() {
		if (thisSingle == null) {
			thisSingle = new SeededRandom(Constants.SEED);
		}
		return thisSingle;
	}
}
