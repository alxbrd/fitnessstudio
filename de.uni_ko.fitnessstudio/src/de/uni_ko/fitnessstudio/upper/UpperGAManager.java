package de.uni_ko.fitnessstudio.upper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.lagodiuk.Fitness;
import com.lagodiuk.GA;
import com.lagodiuk.GAIterationListener;
import com.lagodiuk.GAPopulation;
import com.lagodiuk.GAwithTimeout;

import de.uni_ko.fitnessstudio.lower.DomainModelFitness;
import de.uni_ko.fitnessstudio.lower.DomainModelInit;
import de.uni_ko.fitnessstudio.util.GAConfiguration;
import de.uni_ko.fitnessstudio.util.ModelIO;

public class UpperGAManager {

	private String prefix = "output_rules\\"
			+ new SimpleDateFormat("HH_mm_ss").format(Calendar.getInstance().getTime()).toString();

	private EObject inputModel;
	private EPackage metaModel;
	private DomainModelFitness domainModelFitness;
	private DomainModelInit init;
	private ConstraintChecker constraintChecker;
	private GAConfiguration configurationUpper;
	private GAConfiguration configurationLower;
	private int timeoutSeconds;
	
	long time = 0;

	private GA<RuleSet, Double> ga;

	public UpperGAManager(DomainModelFitness domainModelFitness, DomainModelInit init, ConstraintChecker ruleSetChecker,
			EPackage metaModel, GAConfiguration configurationUpper, GAConfiguration configurationLower,
			EObject inputModel, int timeoutSeconds) {
		this.domainModelFitness = domainModelFitness;
		this.init = init;
		this.constraintChecker = ruleSetChecker;
		this.metaModel = metaModel;
		this.configurationUpper = configurationUpper;
		this.configurationLower = configurationLower;
		this.inputModel = inputModel;
		this.timeoutSeconds = timeoutSeconds;
	}

	public double runGA() {
		time = System.currentTimeMillis();
		GAPopulation<RuleSet> population = RuleSetInit.create(configurationUpper.getPopulationSize(), metaModel,
				constraintChecker);
		Fitness<RuleSet, Double> fitness = new RuleSetFitness(domainModelFitness, init, inputModel, configurationLower,
				constraintChecker);
		ga = new GAwithTimeout<RuleSet, Double>(population, fitness, timeoutSeconds);
		addListener(ga);
		ga.evolve(configurationUpper.getIterations());
		RuleSet best = ga.getBest();
		double fitnessVal = ga.fitness(best);
		ga.getPopulation().trim(0);
		ga.clearCache();
		time = System.currentTimeMillis();
		if (best == null || !constraintChecker.satisfiesMutationConstraints(best.getContent()))
			return -10000.0;
		else
			return fitnessVal;
	}

	/**
	 * Listener for printing best chromosome after each iteration
	 */
	private void addListener(GA<RuleSet, Double> ga) {
		// just for pretty print
		System.out.println(String.format("%s\t%s\t%s\t%s", "iter", "fit", "chromosome", "seconds"));

		// Lets add listener, which prints best chromosome after each iteration
		ga.addIterationListener(new GAIterationListener<RuleSet, Double>() {

			@Override
			public void update(GA<RuleSet, Double> ga) {
				time = (System.currentTimeMillis() - time)/1000;
				RuleSet best = ga.getBest();
				double bestFit = ga.fitness(best);
				int iteration = ga.getIteration();
//				ga.getFitnessFunc().clearCache();

				// Listener prints best achieved solution
				System.out.println(String.format("%s\t%s\t%s\t%s", iteration, bestFit, best, time));
				ModelIO.saveProducedRuleSet(best.getContent(), iteration, bestFit, prefix);
				time = System.currentTimeMillis();
			}
		});
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
