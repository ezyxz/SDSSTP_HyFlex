package com.aim.project.sdsstp.interfaces;

/**
 * 
 * @author Warren G. Jackson
 * @since 1.0.0
 * 
 * Interface for an objective function for SDSSTP.
 */
public interface ObjectiveFunctionInterface {

	/**
	 * Gets the objective function value of the specified solution.
	 * 
	 * @param solutionRepresentation The representation of the current solution
	 * @return The objective function of the current solution represented by <code>solutionRepresentation</code>
	 */
	public int getObjectiveFunctionValue(SolutionRepresentationInterface solutionRepresentation);
	
	/**
	 * Gets the time taken to get from the landmark <code>fromLandmarkId</code> to the landmark <code>toLandmarkId</code>.
	 * 
	 * @param fromLandmarkId ID of the starting city.
	 * @param toLandmarkId ID of the destination city.
	 * @return The time to travel between landmarks <code>fromLandmarkId</code> and <code>toLandmarkId</code>.
	 */
	public int getTravelTime(int fromLandmarkId, int toLandmarkId);
	
	/**
	 * Gets the time taken to get from the tour office to the specified landmark.
	 * 
	 * @param toLandmarkId The ID of the landmark.
	 * @return The time taken to get from the tour office to the specified landmark.
	 */
	public int getTravelTimeFromTourOfficeToLandmark(int toLandmarkId);

	/**
	 * Gets the time taken to get from the specified landmark to the tour office.
	 * 
	 * @param fromLandmarkId The ID of the landmark.
	 * @return The time taken to get from the specified landmark to the tour office.
	 */
	public int getTravelTimeFromLandmarkToTourOffice(int fromLandmarkId);
	
	/**
	 * Gets the visiting time for the specified landmark.
	 * 
	 * @param landmarkId The ID of the landmark.
	 * @return The visiting time at the landmark.
	 */
	public int getVisitingTimeAt(int landmarkId);
}
