package com.aim.project.sdsstp.interfaces;

public interface SDSSTPSolutionInterface extends Cloneable {
	
	public double getObjectiveFunctionValue();
	
	public void setObjectiveFunctionValue(double objectiveFunctionValue);
	
	public SolutionRepresentationInterface getSolutionRepresentation();
	
	public int getNumberOfLandmarks();

	public SDSSTPSolutionInterface clone();

}
