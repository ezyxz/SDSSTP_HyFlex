package com.aim.project.sdsstp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.XOHeuristicInterface;

public class OX implements XOHeuristicInterface {
	
	private final Random random;
	
	private ObjectiveFunctionInterface f;

	public OX(Random random) {
		
		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		return -1;
	}

	@Override
	public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2,
			SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		
		int iLandmarks = p1.getNumberOfLandmarks();
		int[] iRepresentationP1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] iRepresentationP2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		int[] iRepresentationC = new int[iLandmarks];
		
		for (int i = 0; i < iRepresentationC.length; i++) {
			iRepresentationC[i] = -1;
		}
		int iTimes = 0;
		
		if(intensityOfMutation < 0.2) iTimes= 1;
		else if(intensityOfMutation < 0.4) iTimes= 2;
		else if(intensityOfMutation < 0.6) iTimes= 3;
		else if(intensityOfMutation < 0.8) iTimes= 4;
		else if(intensityOfMutation < 1.0) iTimes= 5;
		else if(intensityOfMutation == 1.0) iTimes= 6;
		

		
		int iFunctionValue = 0;
		for (int i = 0; i < iTimes; i++) {
			int iStartingPoint = random.nextInt(iLandmarks);
			ArrayList <Integer> ListCycle = new ArrayList <Integer>();
			ListCycle.add(iStartingPoint);
			if(random.nextBoolean()) {
				//random to return C1
				int iNextPoint = getIndexInArray(iRepresentationP1, iRepresentationP2[iStartingPoint]);
				while(! ListCycle.contains(Integer.valueOf(iNextPoint))) {
					ListCycle.add(iNextPoint);
					iNextPoint = getIndexInArray(iRepresentationP1, iRepresentationP2[iNextPoint]);
				}
				while(!ListCycle.isEmpty()) {
					int index = ListCycle.remove(0);
					iRepresentationC[index] = iRepresentationP1[index];
				}
				for (int j = 0; j < iRepresentationC.length; j++) {
					if(iRepresentationC[j] == -1) {
						iRepresentationC[j] = iRepresentationP2[j];
					}
				}

			}else {
				//random to return C2
				int iNextPoint = getIndexInArray(iRepresentationP2, iRepresentationP1[iStartingPoint]);
				while(! ListCycle.contains(Integer.valueOf(iNextPoint))) {

					ListCycle.add(iNextPoint);
					iNextPoint = getIndexInArray(iRepresentationP2, iRepresentationP1[iNextPoint]);
										
				}
				while(!ListCycle.isEmpty()) {
					int index = ListCycle.remove(0);
					iRepresentationC[index] = iRepresentationP2[index];
				}
				for (int j = 0; j < iRepresentationC.length; j++) {
					if(iRepresentationC[j] == -1) {
						iRepresentationC[j] = iRepresentationP1[j];
					}
				}
				
			}
			
			
			c.getSolutionRepresentation().setSolutionRepresentation(iRepresentationC);
			iFunctionValue = f.getObjectiveFunctionValue(c.getSolutionRepresentation());
			c.setObjectiveFunctionValue(iFunctionValue);	
			
		}
		
		return iFunctionValue;
		
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.f = f;
	}
	
	private int getIndexInArray(int[] arr, int value) {
		int i = 0;
		for (i = 0; i < arr.length; i++) {
			if(arr[i] == value) return i;
		}
		return -1;
	}
	
}
