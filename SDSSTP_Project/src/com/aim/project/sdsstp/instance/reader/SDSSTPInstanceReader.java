package com.aim.project.sdsstp.instance.reader;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aim.project.sdsstp.SDSSTPObjectiveFunction;
import com.aim.project.sdsstp.instance.SDSSTPInstance;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceReaderInterface;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 * Methods needing to be implemented:
 * - public SDSSTPInstanceInterface readSDSSTPInstance(Path path, Random random)
 */
public class SDSSTPInstanceReader implements SDSSTPInstanceReaderInterface {

	private static SDSSTPInstanceReader oInstance;
	
	private SDSSTPInstanceReader() {
		
	}
	
	public static SDSSTPInstanceReader getInstance() {
		
		if(oInstance == null) {
			
			oInstance = new SDSSTPInstanceReader();
		}
		
		return oInstance;
	}
	
	
	@Override
	public SDSSTPInstanceInterface readSDSSTPInstance(Path path, Random random) {
		int[][] aiTimeDistanceMatrix;

		int[] aiTimeDistancesFromTourOffice = null;

		int[] aiTimeDistancesToTourOffice = null;

		int[] aiVisitingDurations = null;
		
		String[] sTourOfficePosition = null;
		
		
        SDSSTPLocation[] aoLandmarks = null;

		String aiName = null;
		
		int aiLandMarks = 0;
		
		List<List<Integer>> LLcoordinate = new ArrayList<List<Integer>>();
		List<String> lines = null;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < lines.size(); i++) {
			
			// NAME
			if(lines.get(i).equals("NAME:")) {
				i++;
				aiName = lines.get(i);
			}
			
			// LANDMARKS
			if(lines.get(i).equals("LANDMARKS:")) {
				i++;
				aiLandMarks = Integer.valueOf(lines.get(i));
			}
			
			//TIME_MATRIX
			if(lines.get(i).equals("TIME_MATRIX:")) {
				i++;
				while(!lines.get(i).equals("TIME_FROM_OFFICE:")) {
					List<Integer> row = new ArrayList<Integer>();
					String line = lines.get(i);
					String[] sNum = line.split(" ");
					for(String snum : sNum) {
						row.add(Integer.valueOf(snum));
					}
					LLcoordinate.add(row);
					i++;
				}
			}
			
			// TIME_FROM_OFFICE 
			if(lines.get(i).equals("TIME_FROM_OFFICE:")) {
				i++;
				String[] sNum = lines.get(i).split(" ");
				aiTimeDistancesFromTourOffice = new int[sNum.length];
				for (int j = 0; j < aiTimeDistancesFromTourOffice.length; j++) {
					aiTimeDistancesFromTourOffice[j] = Integer.valueOf(sNum[j]);
				}
			}
			
			// TIME_TO_OFFICE
			if(lines.get(i).equals("TIME_TO_OFFICE:")) {
				i++;
				String[] sNum = lines.get(i).split(" ");
				aiTimeDistancesToTourOffice = new int[sNum.length];
				for (int j = 0; j < aiTimeDistancesToTourOffice.length; j++) {
					aiTimeDistancesToTourOffice[j] = Integer.valueOf(sNum[j]);
				}
			}
			
			// VISIT_DURATION
			if(lines.get(i).equals("VISIT_DURATION:")) {
				i++;
				String line = lines.get(i);
				String[] sNum = line.split(" ");
				aiVisitingDurations = new int[sNum.length];
				for (int j = 0; j < aiVisitingDurations.length; j++) {
					aiVisitingDurations[j] = Integer.valueOf(sNum[j]);
				}
			}
			
			// OFFICE_LOCATION
			if(lines.get(i).equals("OFFICE_LOCATION:")) {
				i++;
				sTourOfficePosition= lines.get(i).split(" ");
			}
			
			// LANDMARK_LOCATIONS
			
			if(lines.get(i).equals("LANDMARK_LOCATIONS:")) {
				i++;
				aoLandmarks = new SDSSTPLocation[aiLandMarks];
				for (int j = 0; j < aiLandMarks; j++) {
					String[] nums = lines.get(i).split(" ");
					aoLandmarks[j] = new SDSSTPLocation(Double.valueOf(nums[0]), Double.valueOf(nums[1]));
					i++;
				}
				break;
			}

		}
		
		//TIME_MATRIX
		int width = LLcoordinate.size();
		int height = LLcoordinate.get(0).size();
		aiTimeDistanceMatrix = new int[width][height];
		for (int i = 0; i < aiTimeDistanceMatrix.length; i++) {
			for (int j = 0; j < aiTimeDistanceMatrix[i].length; j++) {
				aiTimeDistanceMatrix[i][j] = LLcoordinate.get(i).get(j);
			}
		}
		
		// office coordinate
		com.aim.project.sdsstp.instance.SDSSTPLocation oTourOffice = 
				new SDSSTPLocation(Double.valueOf(sTourOfficePosition[0]), Double.valueOf(sTourOfficePosition[1]));
		
		SDSSTPObjectiveFunction aiSDSSTPObjectiveFunctionobj = new SDSSTPObjectiveFunction(aiTimeDistanceMatrix, aiTimeDistancesFromTourOffice, aiTimeDistancesToTourOffice, aiVisitingDurations);
		
		return new SDSSTPInstance(aiName, aiLandMarks, oTourOffice, aoLandmarks, random, aiSDSSTPObjectiveFunctionobj);
	}
}
