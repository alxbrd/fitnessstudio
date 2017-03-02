package de.uni_ko.fitnessstudio.lower;

import com.lagodiuk.GAPopulation;

public interface DomainModelInit {

	public GAPopulation<DomainModel> createPopulation(int populationSize);

	public void setMutator(DomainModelMutator mutator);

	
}
