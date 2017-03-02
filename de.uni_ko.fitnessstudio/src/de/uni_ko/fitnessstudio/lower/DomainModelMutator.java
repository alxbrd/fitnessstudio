package de.uni_ko.fitnessstudio.lower;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
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

public class DomainModelMutator {
	static boolean initialized = false;
	static Set<Rule> defaultRules;

	Set<Rule> rules;
	Engine engine = EngineFactory.createEngine();

	public DomainModelMutator() {
		initDefaultRules();
		this.rules = defaultRules;
	}

	public DomainModelMutator(Set<Rule> rules) {
		this.rules = rules;
	}

	public DomainModel mutate(DomainModel domainModel) {
		EObject mutated = (EObject) EcoreUtil.copy(domainModel.getContent());
		EGraph graph = new EGraphImpl(mutated);

		for (Rule rule : rules) {
			if (Math.random() > 0.4) {
				RuleApplication app = new RuleApplicationImpl(engine, graph, rule, null);
				app.execute(null);
			}
		}
		graph.clear();
		DomainModel result = new DomainModel(mutated, domainModel.getMutator(), domainModel.getCrossover(), domainModel.getFitness());
		return result;
	}

	private void initDefaultRules() {
		if (initialized)
			return;
		HenshinResourceSet resourceSet = new HenshinResourceSet();
		loadDefaultRules(resourceSet);
		initialized = true;
	}

	public void loadDefaultRules(ResourceSet resSet) {
		Resource resource = resSet.getResource(URI.createURI("transformation\\example.henshin"), true);
		Module module = (Module) resource.getContents().get(0);
		defaultRules = new HashSet<Rule>();
		for (Unit u : module.getUnits()) {
			if (u instanceof Rule)
				defaultRules.add(((Rule) u));
		}
	}

	@Override
	public String toString() {
		return super.toString() + ", " + rules.size() + " rules: " + rules;
	}
}
