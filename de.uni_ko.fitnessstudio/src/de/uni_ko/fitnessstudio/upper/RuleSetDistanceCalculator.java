package de.uni_ko.fitnessstudio.upper;

import java.util.Collection;

import com.lagodiuk.DistanceCalculator;

public class RuleSetDistanceCalculator implements DistanceCalculator<RuleSet, Double> {

	@Override
	public Double[][] calculate(Collection<RuleSet> chromosomes) {
		int size = chromosomes.size();
		Double[][] distances = new Double[size][size];
		for (int i=0; i<size; i++)
			for (int j=0; j<size; j++)
				distances[i][j] = 0.0;
		return distances;
	}
	
}
