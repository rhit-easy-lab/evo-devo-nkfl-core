package evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;
import control.Constants;
import control.SeededRandom;

/**
 * @author Emile Marois
 *
 */
import java.util.*;

public class SelectionRanked implements SelectionStrategy {

    @Override
    public Generation getNextGeneration(Generation parentGeneration) {
        List<Agent> parents = parentGeneration.getAgents();
        List<Agent> nextGeneration = new ArrayList<>();

        // Add elite agents to the next generation without mutation
        for (int elite = 0; elite < Constants.ELITISM_QUANTITY; elite++) {
            nextGeneration.add(parents.get(elite).identicalChild());
        }

        // Calculate rank-based selection probabilities
        int populationSize = parents.size();
        double[] probabilities = new double[populationSize];
        double totalRank = (populationSize * (populationSize + 1)) / 2.0; // Sum of ranks 1 to N

        for (int i = 0; i < populationSize; i++) {
            probabilities[i] = (populationSize - i) / totalRank; // Higher ranks have higher probabilities
        }

        // Create a cumulative probability array for easier selection
        double[] cumulativeProbabilities = new double[populationSize];
        cumulativeProbabilities[0] = probabilities[0];
        for (int i = 1; i < populationSize; i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + probabilities[i];
        }

        // Select agents for the next generation until it's full
        while (nextGeneration.size() < Constants.GENERATION_SIZE) {
            double randomValue = SeededRandom.getInstance().nextDouble();
            double randomValue2 = SeededRandom.getInstance().nextDouble();

            // Find the selected agent based on the random value and cumulative probabilities
            for (int i = 0; i < populationSize; i++) {
                if (randomValue <= cumulativeProbabilities[i]) {
                	
                	for(int j = 0; j < populationSize; j++) {
                		
                		if(randomValue2 <= cumulativeProbabilities[j]) {
                			Agent selectedAgent = parents.get(i).crossover(parents.get(j));
                            selectedAgent.mutate();
                            nextGeneration.add(selectedAgent);
                            break;
                		}
                		
                	}
                   
                    break;
                }
            }
        }

        return new Generation(nextGeneration);
    }
}
