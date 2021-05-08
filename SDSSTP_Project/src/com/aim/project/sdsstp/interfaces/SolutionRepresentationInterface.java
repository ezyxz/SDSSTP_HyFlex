package com.aim.project.sdsstp.interfaces;

public interface SolutionRepresentationInterface extends Cloneable {
	
	public int[] getSolutionRepresentation();
	
	public void setSolutionRepresentation(int[] solution);
	
	public int getNumberOfLandmarks();
	
	public SolutionRepresentationInterface clone();
}
