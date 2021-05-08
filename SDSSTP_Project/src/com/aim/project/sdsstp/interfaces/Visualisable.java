package com.aim.project.sdsstp.interfaces;

import com.aim.project.sdsstp.instance.SDSSTPLocation;

public interface Visualisable {

	public SDSSTPLocation[] getRouteOrderedByLocations();
	
	public SDSSTPInstanceInterface getLoadedInstance();
}
