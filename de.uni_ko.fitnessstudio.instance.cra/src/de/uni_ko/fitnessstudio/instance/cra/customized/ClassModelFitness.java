package de.uni_ko.fitnessstudio.instance.cra.customized;

import architectureCRA.ClassModel;
import de.uni_ko.fitnessstudio.lower.DomainModel;
import de.uni_ko.fitnessstudio.lower.DomainModelFitness; 

/**
 *  Fitness function, which calculates difference two chromosomes.
 * @author strueber
 *
 */
	public class ClassModelFitness implements DomainModelFitness {
		@Override
		public Double calculate(DomainModel chromosome) {
			return CRAIndexCalculator.calculateCRAIndex((ClassModel) chromosome.getContent());
		}

	}
