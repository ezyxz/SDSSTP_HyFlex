package com.aim.project.sdsstp.solution;

import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] representation;
	
	public SolutionRepresentation(int[] representation) {
		
		this.representation = representation;
	}
	
	@Override
	public int[] getSolutionRepresentation() {

		return representation;
		
	}

	@Override
	public void setSolutionRepresentation(int[] solution) {
		this.representation = solution;
	}

	@Override
	public int getNumberOfLandmarks() {

		return representation.length;
		
	}

	@Override
	public SolutionRepresentationInterface clone() {
		SolutionRepresentation oSolutionRepresentationClone = null;  
        try{  
        	oSolutionRepresentationClone = (SolutionRepresentation)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return oSolutionRepresentationClone;  
    
	}

}
