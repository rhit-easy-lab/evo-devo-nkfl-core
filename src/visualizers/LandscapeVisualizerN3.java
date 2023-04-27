package visualizers;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;
import visualizerComponents.N3LandscapeFrame;


public class LandscapeVisualizerN3 {
	
	
	public static void main(String[] args) throws IOException 
	{
		JFrame frame = new JFrame();
		
		FitnessFunction f = new NKLandscape(SeededRandom.getInstance().nextInt(), 3, 0);
		N3LandscapeFrame landscape = new N3LandscapeFrame(f, 500, 20, 20);
		
		frame.add(landscape);
		
		frame.setBounds(100, 100, 700, 700);
        frame.setVisible(true);
	}
}
