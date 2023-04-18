package evolution;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;
import control.Constants;

/**
 * Truncation implementation of SelectionStrategy. The most basic selection type:
 * the top half of the generation 'survive' and are cloned identically to the next
 * generation, and then mutated copies fill out the rest of the generation.
 * 
 * Truncation selection ignores elitism.
 * 
 * @author Jacob Ashworth
 *
 */
public class SelectionTruncation implements SelectionStrategy {

	@Override
	public Generation getNextGeneration(Generation parentGeneration) {
		int survivors = Constants.GENERATION_SIZE/2;
		
		List<Agent> parents = parentGeneration.getAgents();
		List<Agent> children = new ArrayList<Agent>();
		
		//top half survive
		for(int survivor=0; survivor<survivors; survivor++)
		{
			Agent copy = parents.get(survivor).identicalChild();
			children.add(copy);
		}
		
		//if GENERATION_SIZE odd, one more survives (but doesn't get a child)
		if(Constants.GENERATION_SIZE % 2 == 1)
		{
			Agent copy = parents.get(survivors).identicalChild();
			children.add(copy);
		}
		
		//now we make mutated children of all survivors, then return it as a generation
		for(int survivor=0; survivor<survivors; survivor++)
		{
			Agent child = parents.get(survivor).identicalChild();
			child.mutate();
			children.add(child);
		}
		
		return new Generation(children);
	}
	
}
