package control;

import java.util.Random;

@SuppressWarnings("serial")
public class SeededRandom extends Random {
	private static SeededRandom thisSingle = null;
	
	private SeededRandom(long seed)
	{
		this.setSeed(seed);
	}
	
	public static SeededRandom getInstance() {
		if (thisSingle == null) {
			thisSingle = new SeededRandom(Constants.SEED);
		}
		return thisSingle;
	}
}
