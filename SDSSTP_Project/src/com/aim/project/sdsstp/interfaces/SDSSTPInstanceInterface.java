package com.aim.project.sdsstp.interfaces;

import java.util.ArrayList;

import com.aim.project.sdsstp.instance.InitialisationMode;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.solution.SDSSTPSolution;

public interface SDSSTPInstanceInterface {
	
	public String getInstanceName();

	public SDSSTPSolution createSolution(InitialisationMode mode);
	
	public ObjectiveFunctionInterface getSDSSTPObjectiveFunction();
	
	public int getNumberOfLandmarks();
	
	public SDSSTPLocation getLocationForLandmark(int deliveryId);
	
	public SDSSTPLocation getTourOffice();
	
	public ArrayList<SDSSTPLocation> getSolutionAsListOfLocations(SDSSTPSolutionInterface oSolution);
}
