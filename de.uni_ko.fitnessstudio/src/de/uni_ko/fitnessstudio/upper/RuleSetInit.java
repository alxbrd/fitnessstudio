package de.uni_ko.fitnessstudio.upper;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.RuleApplicationImpl;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

import com.lagodiuk.GAPopulation;

import de.uni_ko.fitnessstudio.util.EngineFactory;
import de.uni_ko.fitnessstudio.util.ModelIO;

public class RuleSetInit {

	public static final String RULE_NAME_EMPTY = "rule";
	public static final String INITIALIZATION_PREFIX = "createFirst";
	public static final String RULE_NAME_MAPPED_FIRST = "createFirstNodeMapped";
	
	private static Rule emptyRule;
	private static Rule initialMutationRule;
	
	private static boolean initialized = false;
	private static Engine engine = EngineFactory.createEngine();

	public static GAPopulation<RuleSet> create(int populationSize, EPackage metaModel, ConstraintChecker checker) {
		resetEngine();
		initInitializationRules();

		int ruleSetSize = 4;
		
		GAPopulation<RuleSet> result = new GAPopulation<RuleSet>();
		for (int i = 0; i < populationSize; i++) {
			Set<Rule> rules = new HashSet<Rule>();
			for (int j = 0; j < ruleSetSize; j++) {
				Rule initialRule = EcoreUtil.copy(emptyRule);
				applyFirstRule(initialRule, metaModel);
				rules.add(initialRule);
			}
			result.addChromosome(RuleSetMutator.mutate(new RuleSet(rules, metaModel, checker), metaModel));
		}
		System.out.println("INITIALIZED POPULATION");
		return result;
	}

	private static void applyFirstRule(Rule initialRule, EPackage metaModel) {
		EGraphImpl graph = new EGraphImpl();
		graph.addTree(initialRule);
		graph.addTree(metaModel);
		new RuleApplicationImpl(engine, graph, initialMutationRule, null).execute(null);
		graph.clear();
	}

	public static Module loadModule(String model) {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createURI(model), true);
		if (resource == null) {
			System.err.println("Module can not be loaded!");
			return null;
		}
		EObject root = resource.getContents().get(0);
		if (!(root instanceof Module)) {
			System.err.println("Model is not a Henshin Module!");
			return null;
		}
		return (Module) resource.getContents().get(0);
	}

	

	private static  void initInitializationRules() {
		if (initialized)
			return;
		ModelIO.registerPackage();
		engine.getOptions().put(Engine.OPTION_DETERMINISTIC, false);
		
		HenshinResourceSet resSet = new HenshinResourceSet();
		Resource resource = resSet.getResource(URI.createURI("..\\de.uni_ko.fitnessstudio\\transformation\\genetic\\simple.henshin"), true);
		Module module = (Module) resource.getContents().get(0);
		emptyRule = (Rule) module.getUnit(RULE_NAME_EMPTY);
		initialMutationRule = (Rule) module.getUnit(RULE_NAME_MAPPED_FIRST);
		initialized = true;
	}
	
	public static void resetEngine() {
		engine = EngineFactory.createEngine();
	}
	
}
