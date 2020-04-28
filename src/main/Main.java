package main;

import java.util.ArrayList;
import java.util.Random;

public class Main {
	public static void main(String args[]) {
		//int pop=2000, itermax=1500, psel=2;	
		//double pcross=0.1, pmut=0.2;
		int pop=3000, itermax=1000, psel=2;
		double pcross=0.05, pmut=0.5;
		
		for (int k=0; k<5; k++) {
			int counter=0;
			boolean finished = false;
			Functions funcs = new Functions();
			ArrayList<Chromosome> newGeneration = new ArrayList<Chromosome>();
			ArrayList<Chromosome> childChromos = new ArrayList<Chromosome>();
			ArrayList<Chromosome> mutatedChromos = new ArrayList<Chromosome>();
			ArrayList<Chromosome> checkChromos = new ArrayList<Chromosome>();
			
			Population popu = new Population(pop);//initialize population
			ArrayList<Chromosome> chromos = popu.getChromos();				
			ArrayList<Chromosome> goodChromos = funcs.evaluation(chromos);//evaluate chromosomes and store the good ones
			while (!finished && counter<itermax) {
				if (counter!=0) {
					goodChromos.clear();
					goodChromos= (ArrayList<Chromosome>) newGeneration.clone();
				}
				newGeneration.clear();
				if (goodChromos.isEmpty())
					break;
				
				funcs.genStats(goodChromos, k);
				
				for (int i=0; i<pop/2; i++) {
					childChromos.clear();
					mutatedChromos.clear();
					checkChromos.clear();
					Chromosome luckyChromo = funcs.rouletteWheelSelection(goodChromos, psel);//selection
					Chromosome luckyChromo2 = funcs.rouletteWheelSelection(goodChromos, psel);//selection
					
					double randNum = 1/pcross;
					int randCross = new Random().nextInt((int) randNum);
					randNum = 1/pmut;
					int randMut = new Random().nextInt((int) randNum);
					
					if (randCross==0) {
						//childChromos = funcs.twoPointCrossover(luckyChromo, luckyChromo2);//crossover function 1
						childChromos = funcs.uniformCrossover(luckyChromo, luckyChromo2);//crossover function 2
					}else {
						childChromos.add(luckyChromo);
						childChromos.add(luckyChromo2);
					}
					
					if (randMut==0) {
						//mutatedChromos = funcs.swapMutation(childChromos);//mutation function 1
						mutatedChromos = funcs.inversionMutation(childChromos);//mutation function 2
					}else {
						mutatedChromos.add(childChromos.get(0));
						mutatedChromos.add(childChromos.get(1));
					}
					checkChromos = funcs.evaluation(mutatedChromos);
					for (int j=0; j<checkChromos.size(); j++) {
						newGeneration.add(checkChromos.get(j));
					}
				}
				counter++;
				
				if (funcs.checkToEnd(newGeneration)) {//check if we are good with the new generation
					System.out.println("Found an excellent chromosome. Time to stop.");
					break;
				}
			}
			if (counter==itermax)
				System.out.println("Maximum number of iterations has been reached.");
			
			System.out.println("Final Generation:");
			funcs.genStats(newGeneration, k);
		}
	}	
}
