package de.uni_ko.fitnessstudio.util;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

public class ModelIO {

	public static EObject loadModel(String model) {
		registerPackage();
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createURI(model), true);
		if (resource == null) {
			System.err.println("Model can not be loaded!");
			return null;
		}
		return resource.getContents().get(0);
	}

	public static void registerPackage() {
		HenshinPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl());
		m.put("henshin", new XMIResourceFactoryImpl());
	}

	public static void saveProducedRuleSet(Set<Rule> content, int iteration, double cra, String prefix) {
		Module module = HenshinFactory.eINSTANCE.createModule();
		module.getUnits().addAll(content);
		HenshinResourceSet rs = new HenshinResourceSet();
		rs.saveEObject(module, prefix +"\\"+ + iteration + "_rules_cra" + (Math.round(cra * 10.0) / 10.0) + ".henshin");
	}
	

	public static void saveProducedModel(EObject cm, int runNo, double cra, String prefix) {
		HenshinResourceSet rs = new HenshinResourceSet();
		rs.saveEObject(cm, prefix + "run" +runNo + "_cra" + (Math.round(cra * 100.0) / 100.0) + ".xmi");
	}

}
