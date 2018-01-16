package de.uni_ko.fitnessstudio.lower;

import com.lagodiuk.GA;
import com.lagodiuk.GAIterationListener;
import com.lagodiuk.GAPopulation;

import de.uni_ko.fitnessstudio.upper.ConstraintChecker;
import de.uni_ko.fitnessstudio.util.GAConfiguration;

public class LowerGAManager {
	private DomainModel result = null;
	private DomainModelMutator mutator;
	private DomainModelInit domainModelInit;
	private DomainModelFitness fitness;
	private DomainModelDistanceCalculator distanceCalculator;

	private ConstraintChecker constraintChecker; 
	private GAConfiguration configuration;
	private GA<DomainModel, Double> ga;

	public LowerGAManager(DomainModelMutator mutator, DomainModelFitness fitness, DomainModelDistanceCalculator distanceCalculator, DomainModelInit init, GAConfiguration configuration, ConstraintChecker constraintChecker) {
		this.mutator = mutator;
		this.domainModelInit = init;
		this.fitness = fitness;
		this.distanceCalculator = distanceCalculator;
		this.configuration = configuration;
		this.constraintChecker = constraintChecker;
	}

	public double runGA() {
		domainModelInit.setMutator(mutator);
		GAPopulation<DomainModel> population = domainModelInit.createPopulation(configuration.getPopulationSize());
		ga = new GA<DomainModel, Double>(population, fitness, distanceCalculator);
		addListener(ga);
		ga.evolve(configuration.getIterations());
		DomainModel best = ga.getBest();
		double fitnessValue = ga.fitness(best);
		ga.clearCache();
		ga.getPopulation().trim(0);
		if (result== null || !constraintChecker.satisfiesWellformednessConstraint(best.getContent()))
			return -10000.0;
		else
			return fitnessValue;
	}

	/**
	 * After each iteration Genetic algorithm notifies listener
	 */
	private void addListener(GA<DomainModel, Double> ga) {
		// Lets add listener, which prints best chromosome after each iteration
		ga.addIterationListener(new GAIterationListener<DomainModel, Double>() {

			@Override
			public void update(GA<DomainModel, Double> ga) {
				DomainModel best = ga.getBest();
				double bestFit = ga.fitness(best);
				int iteration = ga.getIteration();

				if (configuration.getPrintInfoAfterIteration())
					System.out.println(String.format("%s\t%s\t%s", iteration, bestFit, best));

//				CRAIndexCalculator.printGeneralInfo(best.getContent());
				
//				if (!CRAIndexCalculator.isCorrect(best.getContent())) {
//					ga.terminate();
//					result = null;
//				}
//				else
					result = best;
			}
		});
	}

	public DomainModelMutator getMutator() {
		return mutator;
	}

	public void setMutator(DomainModelMutator mutator) {
		this.mutator = mutator;
	}


	public DomainModel getResult() {
		return result;
	}

	public void setResult(DomainModel result) {
		this.result = result;
	}

	
	public void close() {
		ga.getPopulation().trim(0);
		ga.clearCache();
	}
}
