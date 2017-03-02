package de.uni_ko.fitnessstudio.instance.cra.customized;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.lagodiuk.GAPopulation;

import architectureCRA.ArchitectureCRAFactory;
import architectureCRA.Class;
import architectureCRA.ClassModel;
import architectureCRA.Feature;
import de.uni_ko.fitnessstudio.lower.DomainModel;
import de.uni_ko.fitnessstudio.lower.DomainModelInit;
import de.uni_ko.fitnessstudio.lower.DomainModelMutator;

public class ClassModelInit implements DomainModelInit {

	EObject inputModel;
	DomainModelMutator mutator;
	ClassModelCrossover crossover;
	ClassModelFitness fitness;
	

	public ClassModelInit(EObject inputModel, DomainModelMutator mutator) {
		super();
		this.inputModel = inputModel;
		this.mutator = mutator;
		this.crossover = new ClassModelCrossover();
		this.fitness = new ClassModelFitness();
	}
	
	public ClassModelInit(EObject inputModel, DomainModelMutator mutator, ClassModelCrossover crossover,
			ClassModelFitness fitness) {
		super();
		this.inputModel = inputModel;
		this.mutator = mutator;
		this.crossover = crossover;
		this.fitness = fitness;
	}

	
	public GAPopulation<DomainModel> createPopulation(int populationSize) {
		GAPopulation<DomainModel> result = new GAPopulation<DomainModel>();
		for (int i = 0; i < populationSize; i++) {
//			 if (i % 20 == 0)
//			 result.addChromosome(createInitialWithGodClass());
//			 else
			result.addChromosome(createInitialWithSingletons());
		}
		return result;
	}

	private  DomainModel createInitialWithSingletons() {
		ClassModel fresh = (ClassModel) EcoreUtil.copy(inputModel);

		for (Feature f : fresh.getFeatures()) {
			Class c = ArchitectureCRAFactory.eINSTANCE.createClass();
			fresh.getClasses().add(c);
			f.setIsEncapsulatedBy(c);
			c.setName(f.getName());
		}
		return new DomainModel(fresh, mutator, crossover, fitness);
	}

//	private  DomainModel createInitialWithGodClass() {
//		ClassModel fresh = (ClassModel) EcoreUtil.copy(inputModel);
//		Class c1 = ArchitectureCRAFactory.eINSTANCE.createClass();
//		c1.setName("c1");
//		fresh.getClasses().add(c1);
//		for (Feature f : fresh.getFeatures()) {
//			f.setIsEncapsulatedBy(c1);
//			c1.getEncapsulates().add(f);
//		}
//		return new DomainModel(fresh, mutator, crossover, fitness);
//	}

	@Override
	public void setMutator(DomainModelMutator mutator) {
		this.mutator = mutator;
	}

}
