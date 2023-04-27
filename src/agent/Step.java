package agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	RandomWalk, SteepestClimb, SteepestFall;
	
	//List of all steps currently in use, as specified by the config file
	public static final List<Step> validSteps = Collections.unmodifiableList(getValidSteps());
	
	/**
	 * Parses Constants.STEPS to create a list of which steps are allowed.
	 * Should only be called during the construction of the validSteps list.
	 * 
	 * @return List of valid steps, as specified by Constants.STEPS
	 */
	private static List<Step> getValidSteps() {
		return Arrays.asList(Step.values());
	}
	
	/**
	 * Utility function to get a random element of the validSteps list.
	 */
	public static Step randomStep() {
		return validSteps.get(SeededRandom.getInstance().nextInt(validSteps.size()));
	}
}
