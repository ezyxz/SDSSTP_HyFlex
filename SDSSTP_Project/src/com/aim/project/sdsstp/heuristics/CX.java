package com.aim.project.sdsstp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.XOHeuristicInterface;


public class CX implements XOHeuristicInterface {

	private final Random random;

	private ObjectiveFunctionInterface f;

	public CX(Random random) {

		this.random = random;
	}

	@Override
	public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO
		return -1;
	}

	@Override
	public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2,
			SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		
		int iLandmarks = p1.getNumberOfLandmarks();
		int[] iRepresentationP1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] iRepresentationP2 = p2.getSolutionRepresentation().getSolutionRepresentation();
//		int[] iRepresentationC = c.getSolutionRepresentation().getSolutionRepresentation();
		int[] iRepresentationC = new int[iLandmarks];
		int iTimes = 0;
		
		if(intensityOfMutation < 0.2) iTimes= 1;
		else if(intensityOfMutation < 0.4) iTimes= 2;
		else if(intensityOfMutation < 0.6) iTimes= 3;
		else if(intensityOfMutation < 0.8) iTimes= 4;
		else if(intensityOfMutation < 1.0) iTimes= 5;
		else if(intensityOfMutation == 1.0) iTimes= 6;
		
		int i1CutPoint = iLandmarks/3; 
		int i2CutPoint = iLandmarks/3 * 2;
		
		int iFunctionValue = 0;
		for (int i = 0; i < iTimes; i++) {
			if(random.nextBoolean()) {
				//random to return C1	
				//fill preserved part
				for (int j = i1CutPoint; j <= i2CutPoint; j++) {
					iRepresentationC[j] = iRepresentationP1[j];
				}
				// extract sequence of cities in 2nd parent
				ArrayList <Integer> ListSequenceP2 = new ArrayList <Integer>();
				for (int j = i2CutPoint + 1; j < iLandmarks; j++) {
					ListSequenceP2.add(iRepresentationP2[j]);
				}
				for (int j = 0; j < i2CutPoint + 1; j++) {
					ListSequenceP2.add(iRepresentationP2[j]);
				}
				// remove cities in 1st offspring
				for (int j = i1CutPoint; j <= i2CutPoint; j++) {
					int iRemoveLocarion = iRepresentationC[j];
					ListSequenceP2.remove(Integer.valueOf(iRemoveLocarion));				
				}
				// place into first offspring
				int index = 0;
				for (int j = i2CutPoint + 1; j < iLandmarks; j++) {
					iRepresentationC[j] = ListSequenceP2.remove(0);
					index++;
				}
				

				for (int j = 0; j < i1CutPoint; j++) {
					iRepresentationC[j] = ListSequenceP2.remove(0);
				}
			}else {
				//random to return C2
				//fill preserved part
				for (int j = i1CutPoint; j <= i2CutPoint; j++) {
					iRepresentationC[j] = iRepresentationP2[j];
				}
				// extract sequence of cities in 2nd parent
				ArrayList <Integer> ListSequenceP1 = new ArrayList <Integer>();
				for (int j = i2CutPoint + 1; j < iLandmarks; j++) {
					ListSequenceP1.add(Integer.valueOf(iRepresentationP1[j]));
				}

				for (int j = 0; j < i2CutPoint + 1; j++) {
					ListSequenceP1.add(iRepresentationP1[j]);
				}
				// remove cities in 1st offspring
				for (int j = i1CutPoint; j <= i2CutPoint; j++) {
					int iRemoveLocarion = iRepresentationC[j];
					ListSequenceP1.remove(Integer.valueOf(iRemoveLocarion));				
				}
				// place into first offspring
				for (int j = i2CutPoint + 1; j < iLandmarks; j++) {
					iRepresentationC[j] = ListSequenceP1.remove(0);
				}
				for (int j = 0; j < i1CutPoint; j++) {
					iRepresentationC[j] = ListSequenceP1.remove(0);
				}
				
			}			
		}
		c.getSolutionRepresentation().setSolutionRepresentation(iRepresentationC);
		iFunctionValue = f.getObjectiveFunctionValue(c.getSolutionRepresentation());
		c.setObjectiveFunctionValue(iFunctionValue);
		
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
}
