package visualizers;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
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
import agent.Step;
import control.PropParser;
import control.SeededRandom;
import landscape.FitnessFunction;
import landscape.NKLandscape;
import visualizerComponents.AgentSimpleFrame;
import visualizerComponents.N3LandscapeFrame;

/**
 * This class exists to house the main method of the simulator,
 * as well as handling connection to parts external to the simulator,
 * such as loading in config files and writing results to files.
 * 
 * @author Jacob Ashworth
 *
 */
public class LandscapeAgentVisualizerN3 {
	
	
	public static void main(String[] args) throws IOException 
	{
		//The first argument passed into ExperimentRunner is the configuration file.
		//If no configuration file is specified, it runs using defaultConfig.properties
		if(args.length>=1)
		{
			PropParser.load(args[0]);
		}
		else
		{
			System.out.println("No configuration file specified. Continuing with default paramaters.");
			PropParser.load(PropParser.defaultFilename);
		}
		
		JFrame frame = new JFrame();
		
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout(3, 3));
		
		FitnessFunction f = new NKLandscape(SeededRandom.getInstance().nextInt(), 3, 2);
		N3LandscapeFrame landscape = new N3LandscapeFrame(f, 500, 20, 20);
		
		List<Step> simpleProgram = new ArrayList<Step>();
		simpleProgram.add(Step.RandomWalk);
		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.RandomWalk);
		simpleProgram.add(Step.SteepestClimb);
		simpleProgram.add(Step.SteepestFall);
		simpleProgram.add(Step.SteepestFall);
		int[] startLoc = {0, 0, 0};
		Bitstring b = new Bitstring(startLoc);
		AgentSimple agent = new AgentSimple(f, b, simpleProgram);
		landscape.addAgentSimple(agent);
		AgentSimpleFrame asf = new AgentSimpleFrame(agent, 0, 20);
		
		
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
	}
}
