package com.aim.project.sdsstp.heuristics;

import java.util.Arrays;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 */
public class HeuristicOperators {

	/*
	 *  PLEASE NOTE THAT USE OF THIS CLASS IS OPTIONAL BUT WE
	 *  STRONGLY RECOMMEND THAT YOU IMPLEMENT ANY COMMON FUNCTIONALITY
	 *  IN HERE TO HELP YOU WITH IMPLEMENTING THE HEURISTICS.
	 *
	 *  (HINT: It might be worthwhile to have a method that performs adjacent swaps in here :)) 
	 */

	private static final boolean ENABLE_CHECKING = false;

	private ObjectiveFunctionInterface obj;

	public HeuristicOperators() {

	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.obj = f;
	}
	// new creation
	public ObjectiveFunctionInterface getObjectiveFunction() {
		
		return obj;
		
	}
	
	

	private void performAdjacentSwap(SDSSTPSolutionInterface solution, int var_a) {

		// OPTIONAL: this might be useful to implement and reuse in your heuristics!
	}
}
