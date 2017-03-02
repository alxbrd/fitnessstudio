package de.uni_ko.fitnessstudio.instance.cra.customized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.model.Edge;
import org.eclipse.emf.henshin.model.Mapping;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;

import architectureCRA.ArchitectureCRAPackage;
import architectureCRA.ClassModel;
import de.uni_ko.fitnessstudio.upper.ConstraintChecker;

public class CRAConstraintChecker implements ConstraintChecker {

	private static EClass[] fC = { ArchitectureCRAPackage.eINSTANCE.getClassModel(),
			ArchitectureCRAPackage.eINSTANCE.getMethod(), ArchitectureCRAPackage.eINSTANCE.getAttribute() };
	private static EReference[] fR = { ArchitectureCRAPackage.eINSTANCE.getClassModel_Features(),
			ArchitectureCRAPackage.eINSTANCE.getMethod_DataDependency(),
			ArchitectureCRAPackage.eINSTANCE.getMethod_FunctionalDependency() };
	private static Set<EClass> fixedClasses = new HashSet<EClass>(Arrays.asList(fC));
	private static Set<EReference> fixedReferences = new HashSet<EReference>(Arrays.asList(fR));

	public boolean satisfiesMutationConstraints(Collection<Rule> content) {
		for (Rule rule : content) {
			if (!satisfiesMutationConstraints(rule))
				return false;
		}

		return true;
	}

	public boolean satisfiesMutationConstraints(Rule rule) {
		if (creationOrDeletionViolatesConstraints(rule))
			return false;

		if (!satisfiesMutationConstraints(rule.getMultiRules())) {
			return false;
		}
		return true;
	}

	private static boolean creationOrDeletionViolatesConstraints(Rule rule) {
		Set<Node> deletionNodes = new HashSet<Node>(rule.getLhs().getNodes());
		Set<Node> creationNodes = new HashSet<Node>(rule.getRhs().getNodes());
		Map<Node, Node> preservedNodesLhs2Rhs = new HashMap<Node, Node>();
		Map<Node, Node> preservedNodesRhs2Lhs = new HashMap<Node, Node>();
		for (Mapping m : rule.getMappings()) {
			deletionNodes.remove(m.getOrigin());
			creationNodes.remove(m.getImage());
			preservedNodesLhs2Rhs.put(m.getOrigin(), m.getImage());
			preservedNodesRhs2Lhs.put(m.getImage(), m.getOrigin());
		}

		for (Node n : deletionNodes) {
			if (fixedClasses.contains(n.getType()))
				return true;
		}
		for (Node n : creationNodes) {
			if (fixedClasses.contains(n.getType()))
				return true;
		}

		if (createOrDeleteEdgesViolateConstraints(deletionNodes, preservedNodesLhs2Rhs))
			return true;

		if (createOrDeleteEdgesViolateConstraints(creationNodes, preservedNodesRhs2Lhs))
			return true;

		return false;
	}

	private static boolean createOrDeleteEdgesViolateConstraints(Set<Node> nodes, Map<Node, Node> graph2graph) {
		// An edge is <<delete>> or <<create>>, respectively:
		// (i) if its source or target node is <<delete>> (or <<create>)
		for (Node n : nodes) {
			for (Edge e : n.getOutgoing())
				if (fixedReferences.contains(e.getType()))
					return true;
			for (Edge e : n.getIncoming())
				if (fixedReferences.contains(e.getType()))
					return true;
		}

		// (ii) If its source and target nodes, x1 and x2, are
		// <<preserve>>, but the edge itself, e, has no
		// counterpart between the source and target node counterparts,
		// y1 and y2
		for (Node x1 : graph2graph.keySet()) {
			for (Edge e : x1.getOutgoing()) {
				Node x2 = e.getTarget();
				Node y1 = graph2graph.get(x1);
				Node y2 = graph2graph.get(x2);
				if (y1 != null && y2 != null && y1.getOutgoing(e.getType(), y2) == null)
					if (fixedReferences.contains(e.getType()))
						return true;
			}
		}
		return false;
	}

	public boolean satisfiesWellformednessConstraint(EObject model) {
		return CRAIndexCalculator.isCorrect((ClassModel) model);
	}

}
