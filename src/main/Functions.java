package main;

import java.util.ArrayList;
import java.util.Random;

public class Functions {
	
	public ArrayList<Chromosome> evaluation(ArrayList<Chromosome> chromos) {
		ArrayList<Chromosome> goodChromos = new ArrayList<Chromosome>();
		boolean chromoCheck;
		
		for (int i=0; i<chromos.size(); i++) {
			chromoCheck = chromos.get(i).evaluateChromo();
			if (chromoCheck==true)
				goodChromos.add(chromos.get(i));
		}
		
		return goodChromos;
	}
	
	public Chromosome rouletteWheelSelection (ArrayList<Chromosome> goodChromos) {//roulette-like selection with different weight for every child
		int sum=0;
		Chromosome luckyChromo = new Chromosome();
		
		for (int i=0; i<goodChromos.size(); i++)
			sum = sum + goodChromos.get(i).getEvaluation();
		
		int spin = new Random().nextInt(sum)+1;
		
		sum=0;
		for (int i=0; i<goodChromos.size(); i++) {
			sum = sum + goodChromos.get(i).getEvaluation();
			if (spin<=sum) {
				luckyChromo = goodChromos.get(i);
				break;
			}
				
		}
		return luckyChromo;
	}
	
	public ArrayList<Chromosome> twoPointCrossover (Chromosome parent1, Chromosome parent2) {//horizontal split between employee 10 and 20
		ArrayList<Chromosome> childChromos = new ArrayList<Chromosome>();
		ArrayList<Employee> employees1 = new ArrayList<Employee>();
		ArrayList<Employee> employees2 = new ArrayList<Employee>();
		
		for (int j=0; j<parent1.numOfEmployees; j++) {
			if (j<10 || j>19) {
				employees1.add(parent1.getEmployees().get(j));
				employees2.add(parent2.getEmployees().get(j));
			}else {
				employees1.add(parent2.getEmployees().get(j));
				employees2.add(parent1.getEmployees().get(j));
			}
		}
		
		Chromosome child1 = new Chromosome();
		Chromosome child2 = new Chromosome();
		
		child1.setEmployees(employees1);
		child2.setEmployees(employees2);			
		child1.correctingHours();
		child2.correctingHours();
		childChromos.add(child1);
		childChromos.add(child2);
		return childChromos;
	}
	
	public ArrayList<Chromosome> uniformCrossover (Chromosome parent1, Chromosome parent2) {//vertical split between shifts randomly; one child takes one day's shifts
		ArrayList<Chromosome> childChromos = new ArrayList<Chromosome>();				//from one parent and the other child takes them from another
		ArrayList<Employee> employees1 = new ArrayList<Employee>();
		ArrayList<Employee> employees2 = new ArrayList<Employee>();
		int[] shifts1 = new int[parent1.numOfDays];
		int[] shifts2 = new int[parent1.numOfDays];
		int[] tmp1 = new int[parent1.numOfDays];
		int[] tmp2 = new int[parent1.numOfDays];
		int[] rand = new int[parent1.numOfDays];
			
		for (int j=0; j<parent1.numOfDays; j++)
			rand[j] = new Random().nextInt(2);
			
		for (int k=0; k<parent1.numOfEmployees; k++) {
			tmp1 = parent1.getEmployees().get(k).getShift().clone();
			tmp2 = parent2.getEmployees().get(k).getShift().clone();
			for (int j=0; j<parent1.numOfDays; j++) {
				if (rand[j]==1) {
					shifts1[j] = tmp2[j];
					shifts2[j] = tmp1[j];
				}else {
					shifts1[j] = tmp1[j];
					shifts2[j] = tmp2[j];
				}
			}
			employees1.add(new Employee());
			employees1.get(k).setShift(shifts1);
			employees2.add(new Employee());
			employees2.get(k).setShift(shifts2);
		}
		
		Chromosome child1 = new Chromosome();
		Chromosome child2 = new Chromosome();
		
		child1.setEmployees(employees1);
		child2.setEmployees(employees2);
		child1.correctingHours();
		child2.correctingHours();
		childChromos.add(child1);
		childChromos.add(child2);

		return childChromos;
	}
	
	public ArrayList<Chromosome> swapMutation (ArrayList<Chromosome> childChromos) {//swap of two random numbers within chromosome
		ArrayList<Chromosome> mutatedChromos = new ArrayList<Chromosome>();
		int randEmp1, randShift1, randEmp2, randShift2, holdShift;
		int[] shifts1 = new int[childChromos.get(0).numOfDays];
		int[] shifts2 = new int[childChromos.get(0).numOfDays];
		
		for (int i=0; i<childChromos.size(); i++) {
			randEmp1 = new Random().nextInt(childChromos.get(i).numOfEmployees);
			randShift1 = new Random().nextInt(childChromos.get(i).numOfDays);			
			shifts1 = childChromos.get(i).getEmployees().get(randEmp1).getShift();
			holdShift = shifts1[randShift1];
			
			randEmp2 = new Random().nextInt(childChromos.get(i).numOfEmployees);
			randShift2 = new Random().nextInt(childChromos.get(i).numOfDays);
			shifts2 = childChromos.get(i).getEmployees().get(randEmp2).getShift();
			
			shifts1[randShift1]=shifts2[randShift2];
			shifts2[randShift2]=holdShift;
			
			mutatedChromos.add(childChromos.get(i));
		}
		
		return mutatedChromos;
	}
	
	public ArrayList<Chromosome> inversionMutation (ArrayList<Chromosome> childChromos) {//inversion of one employee's shifts from a random point to another
		ArrayList<Chromosome> mutatedChromos = new ArrayList<Chromosome>();
		int randEmp, randShift, randLength, inversionPoint, holder;
		int[] shifts = new int[childChromos.get(0).numOfDays];
		
		for (int i=0; i<childChromos.size(); i++) {
			randEmp = new Random().nextInt(childChromos.get(i).numOfEmployees);
			randShift = new Random().nextInt(childChromos.get(i).numOfDays-1);
			randLength = new Random().nextInt(childChromos.get(i).numOfDays-randShift);
			if (randLength==0)//inversion will happen no matter what
				randLength++;
			
			inversionPoint = randShift+randLength;
			shifts = childChromos.get(i).getEmployees().get(randEmp).getShift();

			while (randShift<inversionPoint) {
				holder=shifts[randShift];
				shifts[randShift]=shifts[inversionPoint];
				shifts[inversionPoint]=holder;
				randShift++;
				inversionPoint--;
			}
		}
		
		return mutatedChromos;
	}
}
