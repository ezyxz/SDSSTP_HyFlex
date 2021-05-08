package com.aim.project.sdsstp.visualiser;

import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

public interface SDSSTPViewerInterface {

	public void updateSolution(SDSSTPSolutionInterface[] current, SDSSTPSolutionInterface[] candidate,
			SDSSTPSolutionInterface best);
}
