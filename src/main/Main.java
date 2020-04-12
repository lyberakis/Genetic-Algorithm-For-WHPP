package main;

import java.util.ArrayList;

public class Main {
	public static void main(String args[]) {
		int pop=5000, itermax=1;
		double psel=0.5, pcross=0.5, pmut=0.5;
		
		Population popu = new Population(pop);
		ArrayList<Chromosome> chromos = popu.getChromos();
		Functions funcs = new Functions();
		
		ArrayList<Chromosome> goodChromos = funcs.evaluation(chromos);//evaluate chromosomes and store the good ones
		
		Chromosome luckyChromo = funcs.rouletteWheelSelection(goodChromos);//selection
		Chromosome luckyChromo2 = funcs.rouletteWheelSelection(goodChromos);//selection
		
		ArrayList<Chromosome> childChromos = funcs.twoPointCrossover(luckyChromo, luckyChromo2);//crossover function 1
		
		childChromos = funcs.uniformCrossover(luckyChromo, luckyChromo2);//crossover function 2
		
		ArrayList<Chromosome> mutatedChromos = funcs.swapMutation(childChromos);//mutation function 1
		
		mutatedChromos = funcs.inversionMutation(childChromos);//mutation function 2
		
	}	
}
