package evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;
import control.Constants;
import landscape.FitnessFunction;

/**
 * The Generation class is primarily just a set of utility functions
 * built around a list of agents. It represnets a single generation of
 * agents in the simulation.
 * 
 * @author Jacob Ashworth
 *
 */
public class Generation {
	
	private List<Agent> agents;
	
	/**
	 * Constructor that creates a random generation of agents
	 * @param f
	 */
	public Generation(FitnessFunction f)
	{
		agents = new ArrayList<Agent>();
		for(int agent=0; agent<Constants.GENERATION_SIZE; agent++)
		{
			agents.add(new Agent(f));
		}
	}
	
	/**
	 * Constructor that creates a generation using a list of agents
	 * @param agents
	 */
	public Generation(List<Agent> agents)
	{
		this.agents = agents;
	}
	
	/**
	 * Runs each agent's strategy
	 */
	public void executeAllStrategies()
	{
		for(Agent a : agents)
		{
			a.executeStrategy();
		}
		//sort at the end
		this.sortAgents();
	}
	
	public List<Agent> getAgents()
	{
		return agents;
	}
	
	//Since the best agent is accessed far more frequently than the worst, a sorted
	//generation has the best agent at index 0
	public void sortAgents()
	{
		Collections.sort(agents);
		Collections.reverse(agents);
	}

	public Agent getBest() {
		this.sortAgents();
		return agents.get(0);
	}
}
