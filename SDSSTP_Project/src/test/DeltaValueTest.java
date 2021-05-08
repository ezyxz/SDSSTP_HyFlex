package test;

import java.util.Arrays;

import com.aim.project.sdsstp.AIM_SDSSTP;

public class DeltaValueTest {
	public static void main(String[] args) {
		long seed = 527893l;
		long timeLimit = 10_000;
		AIM_SDSSTP tsp = new AIM_SDSSTP(seed);
		tsp.loadInstance( 0 );
		tsp.setMemorySize(3);
		
		tsp.initialiseSolution(0);
		tsp.initialiseSolution(1);
		tsp.setIntensityOfMutation(1);
		tsp.setDepthOfSearch(1);
		solutionPrinter(tsp, 2);
		tsp.applyHeuristic(4, 0, 1);
		solutionPrinter(tsp, 2);

		for (int i = 0; i < 10; i++) {
			tsp.applyHeuristic(4, 0, 1);
			solutionPrinter(tsp, 2);
		}
		
		
	}
	
	public static void solutionPrinter(AIM_SDSSTP tsp, int num) {
		System.out.println("========Solution===========");
		for (int i = 0; i < num; i++) {
			int[] iaRep = tsp.getSolution(i).getSolutionRepresentation().getSolutionRepresentation();
			int value = (int) tsp.getFunctionValue(i);
			System.out.println(Arrays.toString(iaRep)+" "+value);
		}
		
	}

}
