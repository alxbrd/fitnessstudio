package de.uni_ko.fitnessstudio.lower;

import java.util.Collection;

import com.lagodiuk.DistanceCalculator;

public interface DomainModelDistanceCalculator extends DistanceCalculator<DomainModel, Double> {
	@Override
	public Double[][] calculate(Collection<DomainModel> chromosomes);
}
