package com.aim.project.sdsstp.runners;


import java.awt.Color;

import com.aim.project.sdsstp.AIM_SDSSTP;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.solution.SDSSTPSolution;
import com.aim.project.sdsstp.visualiser.SDSSTPView2;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 * @since 26/03/2021
 *
 * Runs a hyper-heuristic using a default configuration then displays the best solution found.
 */
public abstract class HH_Runner_Visual {

	public HH_Runner_Visual() {
		
	}
	
	public void run() {
		
		long seed = 13032020l;
		
		// should be able to solve this instance pretty quickly
		long timeLimit = 10_000l;
		
		// creates the problem domain as our SDSSTP problem
		AIM_SDSSTP problem = new AIM_SDSSTP(seed);
		
		// loads the square instance
		problem.loadInstance(0);
		
		// set up and run the hyper-heuristic
		HyperHeuristic hh = getHyperHeuristic(seed);
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(problem);
		hh.run();
		
		// display the solution on screen
		System.out.println("f(s_best) = " + hh.getBestSolutionValue());
		new SDSSTPView2(problem.instance, problem, Color.RED, Color.GREEN);
		
	}
	
	/** 
	 * Transforms the best solution found, represented as a TSPSolution, into an ordering of Location's
	 * which the visualiser tool uses to draw the tour.
	 */
	protected SDSSTPLocation[] transformSolution(SDSSTPSolution solution, AIM_SDSSTP problem) {
		
		return problem.getRouteOrderedByLocations();
	}
	
	/**
	 * Allows a general visualiser runner by making the HyperHeuristic abstract.
	 * You can sub-class this class to run any hyper-heuristic that you want.
	 */
	protected abstract HyperHeuristic getHyperHeuristic(long seed);
}
