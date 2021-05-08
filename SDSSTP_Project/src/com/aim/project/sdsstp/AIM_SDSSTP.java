package com.aim.project.sdsstp;


import java.awt.Color;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import com.aim.project.sdsstp.heuristics.AdjacentSwap;
import com.aim.project.sdsstp.heuristics.CX;
import com.aim.project.sdsstp.heuristics.DavissHillClimbing;
import com.aim.project.sdsstp.heuristics.InversionMutation;
import com.aim.project.sdsstp.heuristics.NextDescent;
import com.aim.project.sdsstp.heuristics.OX;
import com.aim.project.sdsstp.heuristics.Reinsertion;
import com.aim.project.sdsstp.hyperheuristics.MyHyperHeuristic;
import com.aim.project.sdsstp.hyperheuristics.SR_IE_HH;
import com.aim.project.sdsstp.instance.InitialisationMode;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.instance.reader.SDSSTPInstanceReader;
import com.aim.project.sdsstp.interfaces.HeuristicInterface;
import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.Visualisable;
import com.aim.project.sdsstp.interfaces.XOHeuristicInterface;
import com.aim.project.sdsstp.visualiser.SDSSTPView2;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

/**
 * Problem Domain Object for the 
 * Socially Distanced Sight-Seeing Tour Problem
 */
public class AIM_SDSSTP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
		"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446"
	};
	
	private SDSSTPSolutionInterface[] solutions;
	
	public SDSSTPSolutionInterface bestSolution;
	
	public SDSSTPInstanceInterface instance;
	
	private HeuristicInterface[] heuristics;
	
	public ObjectiveFunctionInterface f;
	
	private long seed;
	
	public AIM_SDSSTP(long seed) {
		
		super(seed);

		this.seed = seed;
		
		setMemorySize(2);
		
		heuristics = new HeuristicInterface[getNumberOfHeuristics()];
		
		this.heuristics[0] = new AdjacentSwap(super.rng);
		this.heuristics[1] = new InversionMutation(super.rng);
		this.heuristics[2] = new Reinsertion(super.rng);
		this.heuristics[3] = new NextDescent(super.rng);
		this.heuristics[4] = new DavissHillClimbing(super.rng);
		this.heuristics[5] = new OX(super.rng);
		this.heuristics[6] = new CX(super.rng);
		
	}
	
	public SDSSTPSolutionInterface getSolution(int index) {
		
		return solutions[index];
	}
	
	public SDSSTPSolutionInterface getBestSolution() {
		
		return bestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		
		HeuristicInterface heuristic = heuristics[hIndex];
		
		if(currentIndex != candidateIndex) {
			copySolution(currentIndex, candidateIndex);
		}
		
		long lStartTime = System.nanoTime();
		double sol = heuristic.apply(getSolution(candidateIndex), depthOfSearch, intensityOfMutation);
		long lEndTime = System.nanoTime();
		
		updateBestSolution(candidateIndex);
		super.heuristicCallRecord[hIndex]++;
		super.heuristicCallTimeRecord[hIndex] += ((lEndTime - lStartTime) / 1e6);
		
		return sol;
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		
		HeuristicInterface heuristic = heuristics[hIndex];
		
		if(getSolution(candidateIndex) == null) {
			initialiseSolution(candidateIndex);
		}
		
		XOHeuristicInterface heuristic2 = (XOHeuristicInterface) heuristic; 
		
		long lStartTime = System.nanoTime();
		double sol = heuristic2.apply(getSolution(parent1Index), getSolution(parent2Index), getSolution(candidateIndex), depthOfSearch, intensityOfMutation);
		long lEndTime = System.nanoTime();
		
		updateBestSolution(candidateIndex);
		super.heuristicCallRecord[hIndex]++;
		super.heuristicCallTimeRecord[hIndex] += ((lEndTime - lStartTime) / 1e6);
		
		return sol;
	}

	@Override
	public String bestSolutionToString() {
		
		int[] representation = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		return Arrays.toString(representation);
	}

	@Override
	public boolean compareSolutions(int a, int b) {

		int[] aiSolutionA = getSolution(a).getSolutionRepresentation().getSolutionRepresentation();
		int[] aiSolutionB = getSolution(b).getSolutionRepresentation().getSolutionRepresentation();
		return Arrays.equals(aiSolutionA, aiSolutionB);
	}

	@Override
	public void copySolution(int a, int b) {

		solutions[b] = solutions[a].clone();
		
	}

	@Override
	public double getBestSolutionValue() {

		return bestSolution.getObjectiveFunctionValue();
	}
	
	@Override
	public double getFunctionValue(int index) {
		
		return getSolution(index).getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		
		
		int[] hs = new int[] {};
		
		switch(type) {
		case CROSSOVER:
			hs = IntStream.range(0, getNumberOfHeuristics()).filter( h -> heuristics[h].isCrossover()).toArray();
			break;
		case LOCAL_SEARCH:
			hs = getHeuristicsThatUseDepthOfSearch();
			break;
		case OTHER:
			break;
		case RUIN_RECREATE:
			break;
		case MUTATION:
			hs = getHeuristicsThatUseIntensityOfMutation();
			break;
		default:
			break;
		}
		
		return hs;
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		
		return IntStream.range(0, getNumberOfHeuristics()).filter( h -> heuristics[h].usesDepthOfSearch()).toArray();
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		
		return IntStream.range(0, getNumberOfHeuristics()).filter( h -> heuristics[h].usesIntensityOfMutation()).toArray();
	}

	@Override
	public int getNumberOfHeuristics() {

		// this has to be hard-coded due to the design of the HyFlex framework
		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		return instanceFiles.length;
	}

	@Override
	public void initialiseSolution(int index) {
		if(super.rng.nextBoolean())
			solutions[index] = instance.createSolution(InitialisationMode.RANDOM);
		else
			solutions[index] = instance.createSolution(InitialisationMode.CONSTRUCTIVE);
		updateBestSolution(index);
	}

	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "sdsstp" + SEP + instanceFiles[instanceId] + ".sdsstp";

		// create instance reader and problem instance
		Path path = Paths.get(instanceName);
		Random random = new Random(seed);
		
		SDSSTPInstanceReader tsp = SDSSTPInstanceReader.getInstance();
		instance = tsp.readSDSSTPInstance(path, random);

		f = instance.getSDSSTPObjectiveFunction();
		
		// set the objective function in each of the heuristics
		for(HeuristicInterface h : heuristics) {
			h.setObjectiveFunction(f);
		}
	
	}

	@Override
	public void setMemorySize(int size) {

		SDSSTPSolutionInterface[] tempSolutions = new SDSSTPSolutionInterface[size];
		
		
		if(this.solutions != null) {

			System.arraycopy(this.solutions, 0, tempSolutions, 0, Math.min(this.solutions.length, size));
		}

		this.solutions = tempSolutions; 
	}

	@Override
	public String solutionToString(int index) {

		int[] representation = getSolution(index).getSolutionRepresentation().getSolutionRepresentation();
		return Arrays.toString(representation);

	}

	@Override
	public String toString() {

		return "PSZWJ1's AIM SDSSTP";
	}
	
	private void updateBestSolution(int index) {
		
		if(getBestSolution() == null) {
			
			this.bestSolution = getSolution(index).clone();

		} else if(getSolution(index).getObjectiveFunctionValue() < getBestSolutionValue()) {
			
			bestSolution = getSolution(index).clone();
		}
		
	}
	
	@Override
	public SDSSTPInstanceInterface getLoadedInstance() {

		return this.instance;
	}

	@Override
	public SDSSTPLocation[] getRouteOrderedByLocations() {

		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		SDSSTPLocation[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForLandmark).toArray(SDSSTPLocation[]::new);
		return route;
	}

	public static void main(String [] args) {
		
		long seed = 527893l;
		long timeLimit = 10_000;
		AIM_SDSSTP sdsstp = new AIM_SDSSTP(seed);
		HyperHeuristic hh = new MyHyperHeuristic(seed);
		sdsstp.loadInstance( 3 );
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(sdsstp);
		hh.run();
		
		double best = hh.getBestSolutionValue();
		System.out.println(best);
		System.out.println(sdsstp.bestSolutionToString());
		ArrayList<SDSSTPLocation> routeLocations = new ArrayList<>();
		
		int[] landmarks = sdsstp.getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		
		for(int i = 0; i < landmarks.length; i++) {
			routeLocations.add(sdsstp.instance.getLocationForLandmark(landmarks[i]));
		}
		
		SolutionPrinter.printSolution(routeLocations);
		new SDSSTPView2(sdsstp.getLoadedInstance(), sdsstp, Color.black, Color.white);

		
	}
}