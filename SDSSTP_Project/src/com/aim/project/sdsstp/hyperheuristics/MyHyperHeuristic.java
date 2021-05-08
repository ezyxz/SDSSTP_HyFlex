package com.aim.project.sdsstp.hyperheuristics;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.aim.project.sdsstp.AIM_SDSSTP;
import com.aim.project.sdsstp.SolutionPrinter;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 */
public class MyHyperHeuristic extends HyperHeuristic {
	
	public MyHyperHeuristic(long seed) {
		
		super(seed);
	}

	@Override
	protected void solve(ProblemDomain problem) {
		int MemorySize = 6;
		problem.setMemorySize(MemorySize);
		
		for (int i = 0; i < MemorySize - 1; i++) {
			problem.initialiseSolution(i);
		}
		
		
		int[] Heuristic_Score = new int[] {1,2,3,3,5,6,6};

		int h = 1;
		boolean accept = false;
		
		int[] xos = problem.getHeuristicsOfType(HeuristicType.CROSSOVER);
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i : xos) {
			set.add(i);
		}
		int index_c = 0;
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");
		long iteration = 0;
		int[] c = new int[] {1,4,3,4};
		int c_length = c.length;
		while(!hasTimeExpired() ) {
			accept = false;
			int candidateIndex = 0;
			double candidateValue = Integer.MIN_VALUE;
			int bestValue = (int) problem.getBestSolutionValue();
			candidateIndex = rng.nextInt(MemorySize - 1);
			if(bestValue == problem.getFunctionValue(candidateIndex)) {
				int tempIndex = candidateIndex;
				while(tempIndex == candidateIndex) {
					candidateIndex = rng.nextInt(MemorySize - 1);
				}
				
			}
			
			candidateValue = problem.getFunctionValue(candidateIndex);
			double current = candidateValue;
			
//IOM DOS Selection
			double gap = Math.abs(current - bestValue);
			if( gap < bestValue * 0.01) {
				problem.setIntensityOfMutation(0.1);
				problem.setDepthOfSearch(0.1);
			}else if(gap < bestValue * 0.03){
				problem.setIntensityOfMutation(0.3);
				problem.setDepthOfSearch(0.3);
			}else if(gap < bestValue * 0.06) {
				problem.setIntensityOfMutation(0.6);
				problem.setDepthOfSearch(0.6);
			}else {
				problem.setIntensityOfMutation(0.8);
				problem.setDepthOfSearch(0.8);
			}
			
			h = getMaxIndex(Heuristic_Score);
//Heuristic
			double new_candidateValue;
			
			if(set.contains(h)) {
				//get best 2 solution
				int c1Index = 0;
				int c2Index = 1;
				if(problem.getFunctionValue(c1Index) > problem.getFunctionValue(c2Index)) {
					c2Index = 0;
					c1Index = 1;
				}
				
				for (int i = 2; i <  MemorySize - 1; i++) {
					if(problem.getFunctionValue(i) < problem.getFunctionValue(c1Index)) {
						c1Index = i;
					}else if(problem.getFunctionValue(i) < problem.getFunctionValue(c2Index)){
						c2Index = i;
					}
				}

				new_candidateValue = problem.applyHeuristic(h, c1Index, c2Index, MemorySize - 1);
				
			} else {
				new_candidateValue = problem.applyHeuristic(h, candidateIndex, MemorySize - 1);
			}
// Reenforcement learning
			boolean ifBetter= new_candidateValue < current;	
			if(ifBetter) Heuristic_Score[h]++;
			else Heuristic_Score[h]--;
// Adaptive Threshold					
			if(index_c == c_length) index_c = 0;
			double eta = (Math.log(bestValue)+c[index_c])/bestValue + 1;
			index_c++;
			boolean cond2 = eta*bestValue > new_candidateValue;
			if(ifBetter || cond2) accept = true;
			System.out.println(iteration +"\t"+current+"\t"+new_candidateValue+"\t"+accept+"\t"+bestValue);
			
			if(accept) {
				problem.copySolution(MemorySize - 1, candidateIndex);			
			}
			iteration++;
		}
		
		int[] cities = ((AIM_SDSSTP) problem).getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		List<SDSSTPLocation> routeLocations = new ArrayList<>();
		
		for(int i = 0; i < ((AIM_SDSSTP) problem).getBestSolution().getNumberOfLandmarks(); i++) {
			routeLocations.add(((AIM_SDSSTP) problem).instance.getLocationForLandmark(cities[i]));
		}
		
		SDSSTPSolutionInterface oSolution = ((AIM_SDSSTP) problem).getBestSolution();
		SolutionPrinter.printSolution(((AIM_SDSSTP) problem).instance.getSolutionAsListOfLocations(oSolution));
	}

	@Override
	public String toString() {

		return "MyHyperHeuristic by Xinyuan";
	}
	
	private int getMaxIndex(int[] arr) {
		int index = 0;
		int max_value = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if(arr[i] > max_value) {
				index = i;
				max_value = arr[i];
			}
		}
		return index;
	}
	

}
