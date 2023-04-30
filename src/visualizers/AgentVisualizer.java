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

import agent.Agent;
import agent.AgentSimple;
import agent.Bitstring;
import agent.Phenotype;
import agent.Step;
import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;
import visualizerComponents.AgentFrame;
import visualizerComponents.AgentSimpleFrame;
import visualizerComponents.N3LandscapeFrame;


public class AgentVisualizer {
	
	public static FitnessFunction buildFitnessFunction()
	{
		FitnessFunction f = new NKLandscape(SeededRandom.getInstance().nextInt(), 7, 5);
		return f;
	}
	
	public static Phenotype buildStartingPhenotype()
	{
		int[] startLoc = {0, 0, 0, 0, 0, 0, 1};
		Bitstring b = new Bitstring(startLoc);
		
		return b;
	}
	
	public static List<Integer> buildStartingProgram()
	{
		List<Integer> simpleProgram = new ArrayList<Integer>();

		simpleProgram.add(0);
		simpleProgram.add(1);
		simpleProgram.add(0);
		simpleProgram.add(0);
		simpleProgram.add(1);
		simpleProgram.add(1);
		
		return simpleProgram;
	}
	
	public static List<List<Step>> buildStartingBlocks()
	{
		List<List<Step>> blocks = new ArrayList<List<Step>>();
		
		//build block 1
		List<Step> block1 = new ArrayList<Step>();
		block1.add(Step.SteepestClimb);
		block1.add(Step.SteepestClimb);
		blocks.add(block1);
		
		//build block 2
		List<Step> block2 = new ArrayList<Step>();
		block2.add(Step.SteepestFall);
		block2.add(Step.RandomWalk);
		blocks.add(block2);
		
		//build block 3
		List<Step> block3 = new ArrayList<Step>();
		block3.add(Step.RandomWalk);
		block3.add(Step.RandomWalk);
		blocks.add(block3);
		
		return blocks;
	}
	
	public static void main(String[] args) throws IOException 
	{	
		JFrame frame = new JFrame();
		
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout(3, 3));
		
		FitnessFunction f = buildFitnessFunction();
		
		Agent agent = new Agent(f, buildStartingPhenotype(), buildStartingProgram(), buildStartingBlocks());
		AgentFrame af = new AgentFrame(agent, f, 20, 20, 1300, 600);
		
		
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
		
		frame.add(af, BorderLayout.CENTER);
		
		frame.setBounds(100, 100, 1350, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
