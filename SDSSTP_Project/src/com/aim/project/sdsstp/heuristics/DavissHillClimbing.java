package com.aim.project.sdsstp.heuristics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aim.project.sdsstp.interfaces.HeuristicInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;


/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 * Methods needing to be implemented:
 * - public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation)
 * - public boolean isCrossover()
 * - public boolean usesIntensityOfMutation()
 * - public boolean usesDepthOfSearch()
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	private final Random random;
	
	public DavissHillClimbing(Random random) {
	
		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		int iLandmarks = solution.getNumberOfLandmarks();
		int[] iRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();
		int iTimes = 0;
		int[] iTempRepresentation =  iRepresentation.clone();
		int inprovement = 0;
		if(depthOfSearch < 0.2) iTimes= 1;
		else if(depthOfSearch < 0.4) iTimes= 2;
		else if(depthOfSearch < 0.6) iTimes= 3;
		else if(depthOfSearch < 0.8) iTimes= 4;
		else if(depthOfSearch < 1.0) iTimes= 5;
		else if(depthOfSearch == 1.0) iTimes= 6;
		
		int iRunTime = 0;
		while(iRunTime < iTimes) {
			
			int iRandomLocation = random.nextInt(iLandmarks);
			
			if(iRandomLocation == iLandmarks - 1) {
				int[] swap_iTempRepresentation = iTempRepresentation.clone();
				int temp = swap_iTempRepresentation[iRandomLocation];
				swap_iTempRepresentation[iRandomLocation] = swap_iTempRepresentation[0];
				swap_iTempRepresentation[0] = temp;
				int delta = valueByDeltaEvaluation(iTempRepresentation, swap_iTempRepresentation,0,iLandmarks);
				if( delta < 0 ) {
					iTempRepresentation = swap_iTempRepresentation;
					inprovement += delta;
				}				
			}else {
				int[] swap_iTempRepresentation = iTempRepresentation.clone();
				int temp = swap_iTempRepresentation[iRandomLocation];
				swap_iTempRepresentation[iRandomLocation] = swap_iTempRepresentation[iRandomLocation+1];
				swap_iTempRepresentation[iRandomLocation+1] = temp;
				int delta = valueByDeltaEvaluation(iTempRepresentation, swap_iTempRepresentation,0,iLandmarks);
				if( delta < 0 ) {
					iTempRepresentation = swap_iTempRepresentation;
					inprovement += delta;
				}		
			}
			
			iRunTime++;
		}
		
		double init_value=  solution.getObjectiveFunctionValue();
		int iFunctionValue;
		if(inprovement == 0) {	
			return init_value;
		}else {
			solution.getSolutionRepresentation().setSolutionRepresentation(iTempRepresentation);
			iFunctionValue = (int) (init_value + inprovement);
			solution.setObjectiveFunctionValue(iFunctionValue);		
		}
		
		return iFunctionValue;
	}

	@Override
	public boolean isCrossover() {

		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return true;
	}
	
	private int valueByDeltaEvaluation(int[] init_iRepresentation, int[] iRepresentation, int iFunctionValue, int iLandmarks) {
		// Delta Object Function Value Calculation
		List<Integer> sub_index = new ArrayList<>();
		for (int i = 0; i < init_iRepresentation.length; i++) {
			if(init_iRepresentation[i] != iRepresentation[i]) {
				sub_index.add(i);
			}
		}
		//Sub
		for (int i = 0; i < sub_index.size(); i++) {
			if(sub_index.get(i) != 0 && sub_index.get(i) != iLandmarks - 1) {
				int prev_loc = init_iRepresentation[sub_index.get(i)-1];
				int loc = init_iRepresentation[sub_index.get(i)];
				int next_loc = init_iRepresentation[sub_index.get(i)+1];
				int prev_value = getObjectiveFunction().getTravelTime(prev_loc, loc);
				if(i > 0 && sub_index.get(i-1) + 1 == sub_index.get(i)) {
					prev_value = 0;
				}
				int next_value = getObjectiveFunction().getTravelTime(loc, next_loc);
				iFunctionValue -= (prev_value + next_value);
			}else if(sub_index.get(i) == 0) {
				int loc = init_iRepresentation[0];
				int next_loc = init_iRepresentation[1];
				int prev_value = getObjectiveFunction().getTravelTimeFromTourOfficeToLandmark(loc);
				int next_value = getObjectiveFunction().getTravelTime(loc, next_loc);
				iFunctionValue -= (prev_value + next_value);
			}else {
				int loc = init_iRepresentation[iLandmarks - 1];
				int prev_loc = init_iRepresentation[iLandmarks - 2];
				int prev_value = getObjectiveFunction().getTravelTime(prev_loc, loc);
				if(i > 0 && sub_index.get(i-1) + 1 == sub_index.get(i)) {
					prev_value = 0;
				}
				int next_value = getObjectiveFunction().getTravelTimeFromLandmarkToTourOffice(loc);
				iFunctionValue -= (prev_value + next_value);

			}

			
		}
		//Add
        for (int i = 0; i < sub_index.size(); i++) {
			
			if(sub_index.get(i) != 0 && sub_index.get(i) != iLandmarks - 1) {
				int prev_loc = iRepresentation[sub_index.get(i)-1];
				int loc = iRepresentation[sub_index.get(i)];
				int next_loc = iRepresentation[sub_index.get(i)+1];
				int prev_value = getObjectiveFunction().getTravelTime(prev_loc, loc);
				if(i > 0 && sub_index.get(i-1) + 1 == sub_index.get(i)) {
					prev_value = 0;
				}
				int next_value = getObjectiveFunction().getTravelTime(loc, next_loc);
				iFunctionValue += (prev_value + next_value);

			}else if(sub_index.get(i) == 0) {
				int loc = iRepresentation[0];
				int next_loc = iRepresentation[1];
				int prev_value = getObjectiveFunction().getTravelTimeFromTourOfficeToLandmark(loc);
				int next_value = getObjectiveFunction().getTravelTime(loc, next_loc);
				iFunctionValue += (prev_value + next_value);
			}else {
				int loc = iRepresentation[iLandmarks - 1];
				int prev_loc = iRepresentation[iLandmarks - 2];
				int prev_value = getObjectiveFunction().getTravelTime(prev_loc, loc);
				if(i > 0 && sub_index.get(i-1) + 1 == sub_index.get(i)) {
					prev_value = 0;
				}
				int next_value = getObjectiveFunction().getTravelTimeFromLandmarkToTourOffice(loc);
				iFunctionValue += (prev_value + next_value);

			}

			
		}	
        return iFunctionValue;
	}
}
