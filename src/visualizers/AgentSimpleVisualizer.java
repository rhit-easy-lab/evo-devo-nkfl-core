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


public class AgentSimpleVisualizer {
	
	//Modify this method to return whichever FitnessFunction you want to be displayed as an N=3 landscape
	public static FitnessFunction buildFitnessFunction()
	{
		FitnessFunction f = new NKLandscape(SeededRandom.getInstance().nextInt(), 7, 5);
		return f;
	}
	
	//Modify this method to return what you want the starting phenotype to be
	public static Phenotype buildStartingPhenotype()
	{
		int[] startLoc = {0, 0, 0, 0, 0, 0, 0};
		Bitstring b = new Bitstring(startLoc);
		
		return b;
	}
	
	//Modify this method to return whatever you want the starting strategy to be
	public static List<Step> buildStartingStrategy()
	{
		List<Step> simpleProgram = new ArrayList<Step>();

		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.SteepestFall);
		simpleProgram.add(Step.RandomWalk);
		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.RandomWalk);
		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.SteepestClimb);

		return simpleProgram;
	}
	
	public static void main(String[] args) throws IOException 
	{	
		JFrame frame = new JFrame();
		
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout(3, 3));
		
		FitnessFunction f = buildFitnessFunction();
		
		AgentSimple agent = new AgentSimple(f, buildStartingPhenotype(), buildStartingStrategy());
		AgentSimpleFrame asf = new AgentSimpleFrame(agent, f, 20, 20, 1300, 600);
		
		
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
		
		frame.add(asf, BorderLayout.CENTER);
		
		frame.setBounds(100, 100, 1350, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
