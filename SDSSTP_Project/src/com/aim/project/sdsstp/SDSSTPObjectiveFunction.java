package com.aim.project.sdsstp;


import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

public class SDSSTPObjectiveFunction implements ObjectiveFunctionInterface {

	private final int[][] aiTimeDistanceMatrix;

	private final int[] aiTimeDistancesFromTourOffice;

	private final int[] aiTimeDistancesToTourOffice;

	private final int[] aiVisitingDurations;

	public SDSSTPObjectiveFunction(int[][] aiTimeDistanceMatrix, int[] aiTimeDistancesFromTourOffice,
			int[] aiTimeDistancesToTourOffice, int[] aiVisitingDurations) {

		this.aiTimeDistanceMatrix = aiTimeDistanceMatrix;
		this.aiTimeDistancesFromTourOffice = aiTimeDistancesFromTourOffice;
		this.aiTimeDistancesToTourOffice = aiTimeDistancesToTourOffice;
		this.aiVisitingDurations = aiVisitingDurations;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface solution) {
		
		int[] iRepresentation = solution.getSolutionRepresentation();
		
		
		int iLandmarks = solution.getNumberOfLandmarks();
		int iTimeBetweenOffice = getTravelTimeFromTourOfficeToLandmark(iRepresentation[0]) + 
				                 getTravelTimeFromLandmarkToTourOffice(iRepresentation[iLandmarks-1]);
		int iSUMTimeTravel = 0;
		int iSUMTimeVisit = 0;
		for (int i = 0; i < iRepresentation.length-1; i++) {
			iSUMTimeTravel += getTravelTime(iRepresentation[i], iRepresentation[i+1]);
		}
		for (int i = 0; i < iRepresentation.length; i++) {
			iSUMTimeVisit += getVisitingTimeAt(i);
		}
		

	
		return iTimeBetweenOffice + iSUMTimeTravel + iSUMTimeVisit;
	}

	@Override
	public int getTravelTime(int location_a, int location_b) {
		
		return aiTimeDistanceMatrix[location_a][location_b];
		
	}

	@Override
	public int getVisitingTimeAt(int landmarkId) {
		
		return aiVisitingDurations[landmarkId];
		
	}

	@Override
	public int getTravelTimeFromTourOfficeToLandmark(int toLandmarkId) {
		
		return aiTimeDistancesFromTourOffice[toLandmarkId];
		
	}

	@Override
	public int getTravelTimeFromLandmarkToTourOffice(int fromLandmarkId) {
		
		return aiTimeDistancesToTourOffice[fromLandmarkId];
		
	}
}
