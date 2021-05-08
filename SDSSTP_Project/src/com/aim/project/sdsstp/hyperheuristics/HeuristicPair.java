package com.aim.project.sdsstp.hyperheuristics;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 */
public class HeuristicPair {

	private final int h1;
	
	private final int h2;
	
	public HeuristicPair(int h1, int h2) {
		
		this.h1 = h1;
		this.h2 = h2;
	}
	
	/**
	 * Returns the first heuristic in the pair.
	 * @return The first heuristic in the pair
	 */
	public int getFirst() {
		
		return this.h1;
	}
	
	/**
	 * Returns the last heuristic in the pair.
	 * @return The last heuristic in the pair
	 */
	public int getLast() {
		
		return this.h2;
	}
	
	public String getString() {
		return this.h1 + ", " + this.h2;
	}
}
