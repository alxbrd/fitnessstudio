package de.uni_ko.fitnessstudio.instance.cra.customized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.util.EcoreUtil;

import architectureCRA.ArchitectureCRAFactory;
import architectureCRA.Class;
import architectureCRA.ClassModel;
import architectureCRA.Feature;
import de.uni_ko.fitnessstudio.lower.DomainModel;
import de.uni_ko.fitnessstudio.lower.DomainModelCrossover;

public class ClassModelCrossover implements DomainModelCrossover {

	public List<DomainModel> crossover(DomainModel parent1, DomainModel parent2) {
		List<DomainModel> result = new ArrayList<DomainModel>();
		for (int i = 0; i < 2; i++)
			result.add(createCrossover(parent1, parent2));

		return result;
	}

	private DomainModel createCrossover(DomainModel parent1, DomainModel parent2) {
		ClassModel p1 = (ClassModel) EcoreUtil.copy(parent1.getContent());
		ClassModel p2 = (ClassModel) EcoreUtil.copy(parent2.getContent());
		ClassModel child = EcoreUtil.copy(p1);
		child.getClasses().clear();
		child.getFeatures().stream().forEach(f -> f.setIsEncapsulatedBy(null));

		boolean turn = false;
		while (!(p1.getClasses().isEmpty()  && p2.getClasses().isEmpty())) {
			if (turn && !p1.getClasses().isEmpty()) {
				moveSelectedClass(p1, p2, child);
			} else if (!turn && !p2.getClasses().isEmpty()) {
				moveSelectedClass(p2, p1, child);
			}
			turn = !turn;
		}
		return new DomainModel(child, parent1.getMutator(), parent1.getCrossover(), parent1.getFitness());
	}

	private void moveSelectedClass(ClassModel dominant, ClassModel recessive, ClassModel child) {
		dominant.getClasses().removeAll(
				dominant.getClasses().stream().filter(c -> c.getEncapsulates().isEmpty()).collect(Collectors.toSet()));
		if (!dominant.getClasses().isEmpty()) {
			Class selected = selectClass(dominant);
			Class selected_ = ArchitectureCRAFactory.eINSTANCE.createClass();
			selected_.setName(selected.getEncapsulates().get(0).getName());
			child.getClasses().add(selected_);
			Set<String> encapsulated = selected.getEncapsulates().stream().map(e -> e.getName())
					.collect(Collectors.toSet());
			// Remove all features from the recessive class
			for (Feature f : new HashSet<Feature>(child.getFeatures())) {
				if (encapsulated.contains(f.getName()))
					f.setIsEncapsulatedBy(selected_);
			}
			for (Feature f : new HashSet<Feature>(recessive.getFeatures())) {
				if (encapsulated.contains(f.getName()))
					f.setIsEncapsulatedBy(null);
			}
			dominant.getClasses().remove(selected);
		}
		recessive.getClasses().removeAll(
				recessive.getClasses().stream().filter(c -> c.getEncapsulates().isEmpty()).collect(Collectors.toSet()));
	}

	private Class selectClass(ClassModel model) {
		return model.getClasses().get((int) (Math.random() * model.getClasses().size()));
	}

}
