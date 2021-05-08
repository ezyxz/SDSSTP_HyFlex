package com.aim.project.sdsstp.heuristics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aim.project.sdsstp.interfaces.HeuristicInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;


public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private final Random random;

	public AdjacentSwap(Random random) {

		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		int iLandmarks = solution.getNumberOfLandmarks();
		int[] iRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();
		int[] init_iRepresentation = iRepresentation.clone();
		int iFunctionValue = (int) solution.getObjectiveFunctionValue();
		int iTimes = 0;
		if(intensityOfMutation < 0.2) iTimes= 1;
		else if(intensityOfMutation < 0.4) iTimes= 2;
		else if(intensityOfMutation < 0.6) iTimes= 4;
		else if(intensityOfMutation < 0.8) iTimes= 8;
		else if(intensityOfMutation < 1.0) iTimes= 16;
		else if(intensityOfMutation == 1.0) iTimes= 32;
		for (int i = 0; i < iTimes; i++) {
			int iFirstLocation = random.nextInt(iLandmarks);
			if(iFirstLocation != iLandmarks-1) {
				int iTempLocation = iRepresentation[iFirstLocation];
				iRepresentation[iFirstLocation] = iRepresentation[iFirstLocation + 1];
				iRepresentation[iFirstLocation + 1] = iTempLocation;
			}else {
				int iTempLocation = iRepresentation[iFirstLocation];
				iRepresentation[iFirstLocation] = iRepresentation[0];
				iRepresentation[0] = iTempLocation;
			}
		}
		
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
		
        solution.getSolutionRepresentation().setSolutionRepresentation(iRepresentation);
        solution.setObjectiveFunctionValue(iFunctionValue);

		
		return iFunctionValue;
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

}

