package main;

import java.util.ArrayList;

public class Population {
	ArrayList<Chromosome> chromos = new ArrayList<Chromosome>();
	
	public Population (int pop) {
		for (int i=0; i<pop; i++) {
			Chromosome chromo = new Chromosome();
			chromos.add(chromo);
		}
	}

	public ArrayList<Chromosome> getChromos() {
		return chromos;
	}

	public void setChromos(ArrayList<Chromosome> chromos) {
		this.chromos = chromos;
	}
	
}
