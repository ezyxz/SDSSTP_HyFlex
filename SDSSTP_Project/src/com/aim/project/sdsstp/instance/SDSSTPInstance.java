package com.aim.project.sdsstp.instance;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.solution.SDSSTPSolution;
import com.aim.project.sdsstp.solution.SolutionRepresentation;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 * Methods needing to be implemented:
 * - public SDSSTPSolution createSolution(InitialisationMode mode)
 */
public class SDSSTPInstance implements SDSSTPInstanceInterface {

	private final String strInstanceName;

	private final int iNumberOfLandmarks;

	private final SDSSTPLocation oTourOffice;

	private final SDSSTPLocation[] aoLandmarks;

	private final Random oRandom;

	private final ObjectiveFunctionInterface oObjectiveFunction;

	public SDSSTPInstance(String strInstanceName, int iNumberOfLandmarks,
			com.aim.project.sdsstp.instance.SDSSTPLocation oTourOffice,
			com.aim.project.sdsstp.instance.SDSSTPLocation[] aoLandmarks, Random oRandom,
			ObjectiveFunctionInterface f) {

		this.strInstanceName = strInstanceName;
		this.iNumberOfLandmarks = iNumberOfLandmarks;
		this.oTourOffice = oTourOffice;
		this.aoLandmarks = aoLandmarks;
		this.oRandom = oRandom;
		this.oObjectiveFunction = f;
	}

	@Override
	public SDSSTPSolution createSolution(InitialisationMode mode) {
		if(mode.equals(InitialisationMode.RANDOM)) {

			boolean[] num_seleced = new boolean[iNumberOfLandmarks];
			int[] iRepresentation = new int[iNumberOfLandmarks];
			for (int i = 0; i < iNumberOfLandmarks; i++) {
				boolean bIFRun = false;
				while(!bIFRun) {
					int num = oRandom.nextInt(iNumberOfLandmarks);
					if(!num_seleced[num]) {
						iRepresentation[i] = num;
						num_seleced[num] = true;
						bIFRun = true;
					}
				}
			}
			
			SolutionRepresentation oSolutionRepresentation = new SolutionRepresentation(iRepresentation);
			int iObjectiveFunctionValue =  oObjectiveFunction.getObjectiveFunctionValue(oSolutionRepresentation);
			return new SDSSTPSolution(oSolutionRepresentation, iObjectiveFunctionValue, iNumberOfLandmarks);
			
		}else if(mode.equals(InitialisationMode.CONSTRUCTIVE)) {
			
			boolean[] bLandmarkSeleced = new boolean[iNumberOfLandmarks];
			int[] iRepresentation = new int[iNumberOfLandmarks];
			
			// Find First Landmark
			int iMINTravelTimeFromTourOfficeToLandmark = Integer.MAX_VALUE;
			int iMINTravelTimeFromTourOfficeToLandmarkID = -1;
			for (int i = 0; i < iRepresentation.length; i++) {
				int iTempTime = oObjectiveFunction.getTravelTimeFromTourOfficeToLandmark(i);
				if(iTempTime < iMINTravelTimeFromTourOfficeToLandmark) {
					iMINTravelTimeFromTourOfficeToLandmark = iTempTime;
					iMINTravelTimeFromTourOfficeToLandmarkID = i;
				}
			}
			iRepresentation[0] = iMINTravelTimeFromTourOfficeToLandmarkID;
			bLandmarkSeleced[iMINTravelTimeFromTourOfficeToLandmarkID] = true;
			
			//Find successive landmark
			int iPreviousLandmarkID = iMINTravelTimeFromTourOfficeToLandmarkID;
			
			for (int i = 1; i < iRepresentation.length; i++) {
				int iMINTravelTimeFromLandmarkToNext = Integer.MAX_VALUE;
				int iMINTravelTimeFromLandmarkToNextID = -1;
				for (int j = 0; j < iRepresentation.length; j++) {
					if(! bLandmarkSeleced[j]) {
						int iTempTime = oObjectiveFunction.getTravelTime(iPreviousLandmarkID, j);
						if(iTempTime < iMINTravelTimeFromLandmarkToNext) {
							iMINTravelTimeFromLandmarkToNext = iTempTime;
							iMINTravelTimeFromLandmarkToNextID = j;
						}
					}		
				}
				iRepresentation[i] = iMINTravelTimeFromLandmarkToNextID;
				bLandmarkSeleced[iMINTravelTimeFromLandmarkToNextID] = true;
				iPreviousLandmarkID = iMINTravelTimeFromLandmarkToNextID;
			}
			SolutionRepresentation oSolutionRepresentation = new SolutionRepresentation(iRepresentation);
			int iObjectiveFunctionValue =  oObjectiveFunction.getObjectiveFunctionValue(oSolutionRepresentation);
			return new SDSSTPSolution(oSolutionRepresentation, iObjectiveFunctionValue, iNumberOfLandmarks);	
		}
		
		
		return null;
	}

	@Override
	public ObjectiveFunctionInterface getSDSSTPObjectiveFunction() {

		return oObjectiveFunction;
	}

	@Override
	public int getNumberOfLandmarks() {

		return iNumberOfLandmarks;
	}

	@Override
	public SDSSTPLocation getLocationForLandmark(int deliveryId) {

		return aoLandmarks[deliveryId];
	}

	@Override
	public SDSSTPLocation getTourOffice() {

		return this.oTourOffice;
	}

	@Override
	public ArrayList<SDSSTPLocation> getSolutionAsListOfLocations(SDSSTPSolutionInterface oSolution) {

		ArrayList<SDSSTPLocation> locs = new ArrayList<>();
		locs.add(oTourOffice);
		int[] aiDeliveries = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		for (int i = 0; i < aiDeliveries.length; i++) {
			locs.add(getLocationForLandmark(aiDeliveries[i]));
		}
		locs.add(oTourOffice);
		return locs;
	}

	@Override
	public String getInstanceName() {
		
		return strInstanceName;
	}

}
