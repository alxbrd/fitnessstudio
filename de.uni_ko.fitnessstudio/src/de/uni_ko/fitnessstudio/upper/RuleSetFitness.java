package de.uni_ko.fitnessstudio.upper;

import org.eclipse.emf.ecore.EObject;

import com.lagodiuk.Fitness;

import de.uni_ko.fitnessstudio.lower.DomainModelDistanceCalculator;
import de.uni_ko.fitnessstudio.lower.DomainModelFitness;
import de.uni_ko.fitnessstudio.lower.DomainModelInit;
import de.uni_ko.fitnessstudio.lower.DomainModelMutator;
import de.uni_ko.fitnessstudio.lower.LowerGAManager;
import de.uni_ko.fitnessstudio.util.GAConfiguration;

/**
 * Fitness function, which calculates difference two chromosomes.
 */
public class RuleSetFitness implements Fitness<RuleSet, Double> {

	// Map<RuleSet, Double> cache = new HashMap<RuleSet, Double>();
	EObject model;
	DomainModelFitness fitness;
	DomainModelInit init;
	DomainModelDistanceCalculator distanceCalculator;
	GAConfiguration configurationLower;
	private ConstraintChecker constraintChecker;
	
	public RuleSetFitness(DomainModelFitness fitness, DomainModelDistanceCalculator distanceCalculator, DomainModelInit init, EObject model,
			GAConfiguration configurationLower, ConstraintChecker constraintChecker) {
		this.model = model;
		this.fitness = fitness;
		this.distanceCalculator = distanceCalculator;
		this.init = init;
		this.configurationLower = configurationLower;
		this.constraintChecker = constraintChecker;
	}

	@Override
	public Double calculate(RuleSet chromosome) {
		if (chromosome.getConstraintChecker().satisfiesMutationConstraints(chromosome.getContent())) {
			DomainModelMutator mutator = new DomainModelMutator(chromosome.getContent());
			return getResultWithTimeout(mutator);
		} else {
			return 10000.0;
		}
	}

	private Double getResultWithTimeout(DomainModelMutator mutator) {

		LowerGAManager manager = new LowerGAManager(mutator, fitness, distanceCalculator, init, configurationLower, constraintChecker);
		return  manager.runGA();
	}

}