package de.uni_ko.fitnessstudio.upper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.RuleApplication;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.RuleApplicationImpl;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.Unit;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

import de.uni_ko.fitnessstudio.util.EngineFactory;
import de.uni_ko.fitnessstudio.util.ModelIO;

public class RuleSetMutator {

	private static List<Rule> mutationRules;
	private static boolean initialized = false;
//	private static 
	public static RuleSet mutate(RuleSet myRuleSet, EPackage metaModel) {
		Engine engine = EngineFactory.createEngine();

		initMutationRules();
		RuleSet domainRules = copyRuleSet(myRuleSet);

		for (Rule domainRule : new HashSet<Rule>(domainRules.getContent())) {
			double fate = Math.random();
			if (fate < 0.33 && myRuleSet.getContent().size() > 2) {
				domainRules.getContent().remove(domainRule);
			} else if (fate > 0.8) {
				domainRules.getContent().add(EcoreUtil.copy(domainRule));
			}
		}

		// for (Rule r : mutated.getContent())
		// if (Math.random() > 0.5)
		// graph.addTree(r);

		for (Rule domainRule : domainRules.getContent()) {
				EGraph graph = new EGraphImpl();
				graph.addTree(metaModel);
				graph.addTree(domainRule);
				for (Rule mutationRule : mutationRules) {
					if (Math.random() > 0.8) {
						// int apps = (int) (2*Math.random());
						// for (int i = 0; i < apps; i++) {
						RuleApplication app = new RuleApplicationImpl(engine, graph, mutationRule, null);
						boolean applied = app.execute(null);
						if (applied) {
							if (!domainRules.getConstraintChecker().satisfiesMutationConstraints(domainRule)) {
								app.undo(null);
							} else {
								addMutationInfoToDescription(domainRule, mutationRule);
							}
						}
						// System.out.println(done);
						// identifyDangling(graph);

					}
				}
				graph.clear();
		}

		// if (mutated.getContent().size() > 1)
		// System.out.println();
		return domainRules;
	}

	private static void addMutationInfoToDescription(Rule domainRule, Rule mutationRule) {
		StringBuilder sb = new StringBuilder();
		sb.append(domainRule.getDescription());
		sb.append(',');
		sb.append(' ');
		sb.append(mutationRule.getName());
		domainRule.setDescription(sb.toString());
	}

	// private static void identifyDangling(EGraph graph) {
	// List<EObject> domain =
	// graph.getDomain(HenshinPackage.eINSTANCE.getEdge(), true);
	// for (EObject e : domain) {
	// if (e.eContainer() == null)
	// System.out.println("Found dangling edge: " + e);
	// }
	// }

	private static RuleSet copyRuleSet(RuleSet myRuleSet) {
		Set<Rule> rules = new HashSet<Rule>();
		myRuleSet.getContent().stream().forEach(r -> rules.add(EcoreUtil.copy(r)));
		return new RuleSet(rules, myRuleSet.getMetaModel(), myRuleSet.getConstraintChecker());
	}

	public static void initMutationRules() {
		if (initialized)
			return;
		ModelIO.registerPackage();

		HenshinResourceSet resSet = new HenshinResourceSet();
		Resource res1 = resSet.getResource(
				URI.createURI("..\\de.uni_ko.fitnessstudio\\transformation\\genetic\\simple.henshin"), true);
		Resource res2 = resSet.getResource(
				URI.createURI("..\\de.uni_ko.fitnessstudio\\transformation\\genetic\\advanced.henshin"), true);
		Module mod1 = (Module) res1.getContents().get(0);
		Module mod2 = (Module) res2.getContents().get(0);
		mutationRules = new ArrayList<Rule>();
		for (Unit u : mod1.getUnits()) {
			if (u instanceof Rule && !u.getName().startsWith(RuleSetInit.RULE_NAME_EMPTY)
					&& !u.getName().startsWith(RuleSetInit.INITIALIZATION_PREFIX) && u.isActivated())
				mutationRules.add(((Rule) u));
		}
		for (Unit u : mod2.getUnits()) {
			if (u instanceof Rule && u.isActivated())
				mutationRules.add(((Rule) u));
		}
		initialized = true;
	}

}
