package de.uni_ko.fitnessstudio.upper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

import com.lagodiuk.Chromosome;


/**
 * Encapsulates a rule set as an individual during evolution.
 * @author strueber
 *
 */
public class RuleSet implements Chromosome<RuleSet> {
	Set<Rule> content;
	ConstraintChecker checker;
	EPackage metaModel;

	public RuleSet(Set<Rule> content, EPackage metaModel, ConstraintChecker checker) {
		super();
		this.content = content;
		this.metaModel = metaModel;
		this.checker = checker;
	}

	public RuleSet(Rule rule, EPackage metaModel, ConstraintChecker checker) {
		super();
		this.metaModel = metaModel;
		this.content = new HashSet<Rule>();
		this.checker = checker;
		content.add(rule);
	}


	@Override
	public RuleSet mutate() {
		return RuleSetMutator.mutate(this, metaModel);
	}
	
	public static EObject loadModel(String model) {
		ResourceSet resSet = new HenshinResourceSet();
		Resource resource = resSet.getResource(URI.createURI(model), true);
		if(resource == null) {
			System.err.println("Model can not be loaded!");
			return null;
		}
		return resource.getContents().get(0);
	}
	public static void registerPackage() {
//		ArchitectureCRAPackage.eINSTANCE.eClass();
		HenshinPackage.eINSTANCE.eClass();
		   
	    Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
	    Map<String, Object> m = reg.getExtensionToFactoryMap();
	    m.put("xmi", new XMIResourceFactoryImpl());
	}

	public Set<Rule> getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return content.toString();
	}

	@Override
	public List<RuleSet> crossover(RuleSet anotherChromosome) {
		return RuleSetCrossover.crossover(this, anotherChromosome);
	}


	public ConstraintChecker getConstraintChecker() {
		return checker;
	}

	public void setChecker(ConstraintChecker checker) {
		this.checker = checker;
	}

	public void setContent(Set<Rule> content) {
		this.content = content;
	}

	public EPackage getMetaModel() {
		return metaModel;
	}
}
