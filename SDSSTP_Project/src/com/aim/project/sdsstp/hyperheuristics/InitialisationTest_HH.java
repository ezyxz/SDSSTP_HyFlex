package com.aim.project.sdsstp.hyperheuristics;


import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;


public class InitialisationTest_HH extends HyperHeuristic {
	
	public InitialisationTest_HH(long seed) {
		
		super(seed);
	}

	@Override
	protected void solve(ProblemDomain problem) {

		problem.initialiseSolution(0);
		hasTimeExpired();
	}

	@Override
	public String toString() {

		return "InitialisationTest_HH";
	}
}
