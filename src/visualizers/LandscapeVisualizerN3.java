package visualizers;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;
import landscape.NumOnes;
import visualizerComponents.N3LandscapeFrame;


public class LandscapeVisualizerN3 {
	
	//Modify this method to return whichever FitnessFunction you want to be displayed as an N=3 landscape
	public static FitnessFunction buildFitnessFunction()
	{
//		FitnessFunction f = new NumOnes();
		
		FitnessFunction f = new NKLandscape(SeededRandom.getInstance().nextInt(), 3, 0);
		return f;
	}
	
	public static void main(String[] args) throws IOException 
	{
		JFrame frame = new JFrame();
		
		FitnessFunction f = buildFitnessFunction();
		N3LandscapeFrame landscape = new N3LandscapeFrame(f, 500, 20, 20);
		
		frame.add(landscape);
		
		frame.setBounds(100, 100, 700, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
