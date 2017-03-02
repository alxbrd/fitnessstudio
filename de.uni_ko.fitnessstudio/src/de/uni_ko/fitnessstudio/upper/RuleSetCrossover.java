package de.uni_ko.fitnessstudio.upper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.henshin.model.Rule;

public class RuleSetCrossover {

	public static List<RuleSet> crossover(RuleSet one, RuleSet two) {
		List<RuleSet> result = new ArrayList<RuleSet>();
		
		Set<Rule> set1 = new HashSet<Rule>();
		Set<Rule> set2 = new HashSet<Rule>();
		
		int counter = 0;
		for (Rule r1 : one.getContent()) {
			if (counter % 2 == 0)
				set1.add(EcoreUtil.copy(r1));
			else
				set2.add(EcoreUtil.copy(r1));
			counter++;
		}
		
		counter = 0;
		for (Rule r1 : two.getContent()) {
			if (counter % 2 == 0)
				set1.add(EcoreUtil.copy(r1));
			else
				set2.add(EcoreUtil.copy(r1));
			counter++;
		}
		
		result.add(new RuleSet(set1, one.getMetaModel(), one.getConstraintChecker()));
		result.add(new RuleSet(set2, one.getMetaModel(), one.getConstraintChecker()));
		
		return result;
	}
}
