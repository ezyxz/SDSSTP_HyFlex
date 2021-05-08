package com.aim.project.sdsstp.interfaces;

public interface XOHeuristicInterface extends HeuristicInterface {

	/**
	 * 
	 * @param p1 Parent solution 1
	 * @param p2 Parent solution 2
	 * @param depthOfSearch current DOS setting
	 * @param intensityOfMutation currentIOM setting
	 */
	public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2, SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation);
	
}
