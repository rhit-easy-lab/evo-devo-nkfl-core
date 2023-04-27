package control;

import java.util.Random;

import javax.naming.OperationNotSupportedException;

@SuppressWarnings("serial")
public class SeededRandom extends Random {
	protected static SeededRandom thisSingle = null;
	
	protected SeededRandom()
	{

	}
	
	public static SeededRandom getInstance() {
		if (thisSingle == null) {
			thisSingle = new SeededRandom();
		}
		return thisSingle;
	}
}
