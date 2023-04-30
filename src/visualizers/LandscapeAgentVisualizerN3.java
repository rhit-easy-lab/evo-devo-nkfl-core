package visualizers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import agent.AgentSimple;
import agent.Bitstring;
import agent.Phenotype;
import agent.Step;
import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;
import visualizerComponents.AgentSimpleFrame;
import visualizerComponents.N3LandscapeFrame;


public class LandscapeAgentVisualizerN3 {
	
	//Modify this method to return whichever FitnessFunction you want to be displayed as an N=3 landscape
	public static FitnessFunction buildFitnessFunction()
	{
		FitnessFunction f = new NKLandscape(SeededRandom.getInstance().nextInt(), 3, 2);
		return f;
	}
	
	//Modify this method to change where the agent starts on the N=3 landscape. Note: you will have to keep the bitstring length 3 bits long
	public static Phenotype buildStartingPhenotype()
	{
		int[] startLoc = {0, 0, 0};
		Bitstring b = new Bitstring(startLoc);
		
		return b;
	}
	
	//Modify this method to change what steps the agent takes over its lifetime, by adding them to the list.
	//Try to keep these strategies less than 7 steps for the visualizer's sake.
	public static List<Step> buildStartingStrategy()
	{
		List<Step> simpleStrategy = new ArrayList<Step>();
		simpleStrategy.add(Step.RandomWalk);
		simpleStrategy.add(Step.SteepestClimb);
		simpleStrategy.add(Step.RandomWalk);
		simpleStrategy.add(Step.SteepestClimb);
		simpleStrategy.add(Step.SteepestFall);
		simpleStrategy.add(Step.SteepestFall);
		return simpleStrategy;
	}
	
	public static void main(String[] args) throws IOException 
	{	
		JFrame frame = new JFrame();
		
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout(3, 3));
		
		FitnessFunction f = buildFitnessFunction();
		N3LandscapeFrame landscape = new N3LandscapeFrame(f, 500, 20, 20);
		
		AgentSimple agent = new AgentSimple(f, buildStartingPhenotype(), buildStartingStrategy());
		landscape.addAgentSimple(agent);
		AgentSimpleFrame asf = new AgentSimpleFrame(agent, f, 0, 20);
		
		
		JButton agentActionButton = new JButton("Take Step");
		
		ActionListener agentActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!agent.agentDeveloped())
				{
					agent.executeSingleStep();
					frame.repaint();
				}
			}
		};
		agentActionButton.addActionListener(agentActionListener);
		frame.add(agentActionButton, BorderLayout.SOUTH);
		
		frame.add(landscape, BorderLayout.WEST);
		frame.add(asf, BorderLayout.EAST);
		
		frame.setBounds(100, 100, 1350, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
