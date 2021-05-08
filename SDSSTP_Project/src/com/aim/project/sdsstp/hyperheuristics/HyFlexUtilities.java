package com.aim.project.sdsstp.hyperheuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 */
public class HyFlexUtilities {

	public static int[] getHeuristicSetOfTypes(ProblemDomain problem, HeuristicType type, HeuristicType... types) {
		
		ArrayList<HeuristicType> h_types = new ArrayList<>();
		h_types.add(type);
		
		// add any optional heuristic types
		Arrays.stream(types).forEach( h_types::add );
		
		// create an array of all the heuristic IDs of the above-mentioned heuristic types.
		return h_types.stream()
					  .map( problem::getHeuristicsOfType )
					  .flatMapToInt( IntStream::of )
					  .toArray();
	}
}
