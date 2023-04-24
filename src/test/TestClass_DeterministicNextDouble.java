package test;

import java.util.Random;

import control.SeededRandom;

@SuppressWarnings("serial")
public class TestClass_DeterministicNextDouble extends SeededRandom {
	private double returnValue=0;
	
	public TestClass_DeterministicNextDouble(double returnValue)
	{
		super(0);
		this.returnValue = returnValue;
	}
	
	/**
	 * Returns the stored double
	 * @return
	 */
	@Override
	public double nextDouble()
	{
		return returnValue;
	}
	
	public void replaceNextDouble(double newValue)
	{
		this.returnValue = newValue;
	}
}
