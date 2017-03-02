package de.uni_ko.fitnessstudio.lower;

import java.util.List;

public interface DomainModelCrossover {

	List<DomainModel> crossover(DomainModel domainModel, DomainModel anotherChromosome);

}
