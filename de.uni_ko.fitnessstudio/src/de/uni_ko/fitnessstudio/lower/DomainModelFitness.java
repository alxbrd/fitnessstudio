package de.uni_ko.fitnessstudio.lower;

import com.lagodiuk.Fitness;

/**
 *  Fitness function, which calculates difference two chromosomes.
 * @author strueber
 *
 */
	public interface DomainModelFitness extends Fitness<DomainModel, Double> {
		@Override
		public Double calculate(DomainModel chromosome);
	}
