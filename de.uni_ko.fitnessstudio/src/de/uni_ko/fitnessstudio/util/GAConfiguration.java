package de.uni_ko.fitnessstudio.util;

public class GAConfiguration {
	private int iterations;
	private int populationSize;
	private boolean printInfoAfterIteration;

	public GAConfiguration(int iterations, int populationSize) {
		super();
		this.iterations = iterations;
		this.populationSize = populationSize;
		this.printInfoAfterIteration = false;
	}

	public GAConfiguration(int iterations, int populationSize, boolean printInfoAfterIteration) {
		super();
		this.iterations = iterations;
		this.populationSize = populationSize;
		this.printInfoAfterIteration = printInfoAfterIteration;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public boolean getPrintInfoAfterIteration() {
		return printInfoAfterIteration;
	}

	public void setPrintInfoAfterIteration(boolean printInfoAfterIteration) {
		this.printInfoAfterIteration = printInfoAfterIteration;
	}
}
