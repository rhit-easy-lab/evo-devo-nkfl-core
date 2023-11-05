package agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import control.Constants;
import control.SeededRandom;

/**
 * The Step enum is used to represent the different steps that an agent can take over the course
 * of its developmental strategy. The actual code to execute the step is stored in Agent.java
 * 
 * @author Jacob Ashworth
 *
 */
public enum Step {
	//List all steps supported by the simulator
	//RandomWalk
	SteepestClimb, SteepestFall, SameStep;
	
	//List of all steps currently in use, as specified by the config file
	public static final List<Step> validSteps = Collections.unmodifiableList(getValidSteps());
	
	/**
	 * Parses Constants.STEPS to create a list of which steps are allowed.
	 * Should only be called during the construction of the validSteps list.
	 * 
	 * @return List of valid steps, as specified by Constants.STEPS
	 */
	private static List<Step> getValidSteps() {
		List<Step> validSteps = new ArrayList<Step>();
		String[] stepsAllowed = Constants.STEPS.split(",");
		for(String step : stepsAllowed)
		{
			validSteps.add(Step.valueOf(step));
		}
		return validSteps;
	}
	
	/**
	 * Utility function to get a random element of the validSteps list.
	 */
	public static Step randomStep() {
		//BeginOriginal
//		return validSteps.get(SeededRandom.getInstance().nextInt(validSteps.size()));
		//EndOriginal
		
		//Should make choosing SameStep more likely. If SameStep is not in the list of valid steps, will do the same thing as original.
		int placeHolder = 0;
		int sameStepLocation = 0;
		// Checks for SameStep
		for(int k = 0; k < validSteps.size(); k++) {
			if(validSteps.get(k) == Step.SameStep) {
				sameStepLocation = k;
				placeHolder++;
			}
		}
		//If SameStep is in the list, picks a random number between 0 and 1 on the uniform distribution. If that number is less than the input likelyhood for samestep, it returns SameStep. 
		// If not, it randomly chooses a step that is not SameStep and returns that. 
		if(placeHolder != 0) {
			double chooser = Math.random();
			if(chooser < Constants.WEIGHT_OF_SAMESTEP) {
				System.out.print("!SameStep Returned!");
				return Step.SameStep;
				}else {
					int nextChoice = 0;
					while(true) {
						nextChoice = SeededRandom.getInstance().nextInt(validSteps.size());
						if(nextChoice != sameStepLocation) {
							System.out.print("?Otherstep?");
							break;
						}
					}
					return validSteps.get(nextChoice);
					
				}
		}else {
			//If samestep is not in the list, it returns a random number from the list.
		return validSteps.get(SeededRandom.getInstance().nextInt(validSteps.size()));
		}
	}
}
