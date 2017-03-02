package de.uni_ko.fitnessstudio.upper;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.Rule;

public interface ConstraintChecker {

	public boolean satisfiesMutationConstraints(Rule domainRule);

	public boolean satisfiesMutationConstraints(Collection<Rule> rules);

	public boolean satisfiesWellformednessConstraint(EObject model);
}
