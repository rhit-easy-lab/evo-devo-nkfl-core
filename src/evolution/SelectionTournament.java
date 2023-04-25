package evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;
import control.Constants;
import control.SeededRandom;

/**
 * Tournament selection works by taking random groups of size Constants.TOURNAMENT_SIZE from the parents
 * and populating the new generation with the highest fitness individual of each group. Obeys elitism,
 * and in theory should be more effective then truncation selection.
 * 
 * @author Jacob Ashworth
 *
 */
public class SelectionTournament implements SelectionStrategy {

	@Override
	public Generation getNextGeneration(Generation parentGeneration) {
		List<Agent> parents = parentGeneration.getAgents();
		List<Agent> nextGeneration = new ArrayList<Agent>();
		
		for(int elite=0; elite < Constants.ELITISM_QUANTITY; elite++)
		{
			nextGeneration.add(parents.get(elite).identicalChild());
		}
		
		List<Agent> tournament = new ArrayList<Agent>();
		while(nextGeneration.size() < Constants.GENERATION_SIZE)
		{
			tournament.clear();
			
			for(int contestant=0; contestant < Constants.TOURNAMENT_SIZE; contestant++)
			{
				tournament.add(parents.get(SeededRandom.getInstance().nextInt(parents.size())));
			}
			
			Collections.sort(tournament);
			Collections.reverse(tournament);//put the best first
			Agent winner = tournament.get(0).identicalChild();
			winner.mutate();
			nextGeneration.add(winner);
		}
		
		return new Generation(nextGeneration);
	}
	
}
