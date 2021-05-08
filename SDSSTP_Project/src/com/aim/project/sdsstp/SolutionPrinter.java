package com.aim.project.sdsstp;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import com.aim.project.sdsstp.instance.SDSSTPLocation;

public class SolutionPrinter {

	public SolutionPrinter() {
		
	}
	
	/**
	 * 
	 * @param routeLocations The array of Locations ordered in route order.
	 */
	public static void printSolution(List<SDSSTPLocation> routeLocations) {
		
		OutputStream os;
		try {
			os = new FileOutputStream("out.csv");
			PrintStream printStream = new PrintStream(os);
			routeLocations.forEach( l -> {
				printStream.println(l.getX() + "," + l.getY());
			});
			printStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
