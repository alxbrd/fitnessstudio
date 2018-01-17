/*******************************************************************************
 * Copyright 2012 Yuriy Lagodiuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.lagodiuk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class GA<C extends Chromosome<C>, T extends Comparable<T>> {

	private static final int ALL_PARENTAL_CHROMOSOMES = Integer.MAX_VALUE;

	private class ChromosomesComparator implements Comparator<C> {

		private final Map<C, T> cache = new WeakHashMap<C, T>();

		@Override
		public int compare(C chr1, C chr2) {
			T fit1 = this.fit(chr1);
			T fit2 = this.fit(chr2);
			int ret = fit2.compareTo(fit1);
			return ret;
		}

		public T fit(C chr) {
			T fit = this.cache.get(chr);
			if (fit == null) {
				fit = GA.this.fitnessFunc.calculate(chr);
				this.cache.put(chr, fit);
			}
			return fit;
		};

		public void clearCache() {
			this.cache.clear();
		}
	}

	private final ChromosomesComparator chromosomesComparator;

	private final Fitness<C, T> fitnessFunc;

	private final DistanceCalculator<C, T> distanceCalculator;

	public Fitness<C, T> getFitnessFunc() {
		return fitnessFunc;
	}

	private GAPopulation<C> population;

	// listeners of genetic algorithm iterations (handle callback afterwards)
	private final List<GAIterationListener<C, T>> iterationListeners = new LinkedList<GAIterationListener<C, T>>();

	private boolean terminate = false;

	// number of parental chromosomes, which survive (and move to new
	// population)
	private int parentChromosomesSurviveCount = ALL_PARENTAL_CHROMOSOMES;

	private int iteration = 0;

	public GA(GAPopulation<C> population, Fitness<C, T> fitnessFunc, DistanceCalculator<C, T> distanceCalculator) {
		this.population = population;
		this.fitnessFunc = fitnessFunc;
		this.distanceCalculator = distanceCalculator;
		this.chromosomesComparator = new ChromosomesComparator();
		this.population.sortPopulationByFitness(this.chromosomesComparator);
	}

	public void evolve() {
		int parentPopulationSize = this.population.getSize();

		GAPopulation<C> newPopulation = new GAPopulation<C>();


		for (int i = 0; i < parentPopulationSize; i++) {
			C chromosome = this.population.getChromosomeByIndex(i);
			C mutated = chromosome.mutate();
			newPopulation.addChromosome(mutated);
		}

		int populationSize = this.population.getSize();
		Double[][] distanceMatrix = (Double[][]) distanceCalculator.calculate(this.population.getChromosomes());
		for (int i = 0; i < populationSize; i++) {
			C chromosome = this.population.getChromosomeByIndex(i);
			C otherChromosome = getRandomDistantChromosome(i, populationSize, distanceMatrix, newPopulation.getChromosomes());
			// C otherChromosome = this.population.getRandomChromosome();
			List<C> crossovered = chromosome.crossover(otherChromosome);
			for (C c : crossovered) {
				newPopulation.addChromosome(c);
			}

		}


		for (int i = 0; (i < parentPopulationSize) && (i < this.parentChromosomesSurviveCount); i++) {
			newPopulation.addChromosome(this.population.getChromosomeByIndex(i));
		}
		
		newPopulation.sortPopulationByFitness(this.chromosomesComparator);
		newPopulation.trim(parentPopulationSize);
		this.population = newPopulation;
	}

	private C getRandomDistantChromosome(int i, int populationSize, Double[][] distanceMatrix, List<C> chromosomes) {
		double greatestDistance = 0.0;
		List<C> results = new ArrayList<C>();
		for (int j = 0; j <populationSize; j++) {
			if (i != j) {
//				System.out.println(i + " " + j);
				double dist = distanceMatrix[i][j];
				if (dist == greatestDistance) {
					results.add(chromosomes.get(j));
				} else if (dist > greatestDistance) {
					greatestDistance = dist;
					results.clear();
					results.add(chromosomes.get(j));
				}
			}
		}
		
		if (results.size() > 0)
			return results.get((int) Math.floor(results.size() * Math.random()));
		else
			return chromosomes.get(((int) (chromosomes.size() * Math.random())));
	}

	public void evolve(int count) {
		this.terminate = false;

		for (int i = 0; i < count; i++) {
			if (this.terminate) {
				break;
			}
			this.evolve();
			this.iteration = i;
			for (GAIterationListener<C, T> l : this.iterationListeners) {
				l.update(this);
			}
		}
	}

	public int getIteration() {
		return this.iteration;
	}

	public void terminate() {
		this.terminate = true;
	}

	public GAPopulation<C> getPopulation() {
		return this.population;
	}

	public C getBest() {
		return this.population.getChromosomeByIndex(0);
	}

	public C getWorst() {
		return this.population.getChromosomeByIndex(this.population.getSize() - 1);
	}

	public void setParentChromosomesSurviveCount(int parentChromosomesCount) {
		this.parentChromosomesSurviveCount = parentChromosomesCount;
	}

	public int getParentChromosomesSurviveCount() {
		return this.parentChromosomesSurviveCount;
	}

	public void addIterationListener(GAIterationListener<C, T> listener) {
		this.iterationListeners.add(listener);
	}

	public void removeIterationListener(GAIterationListener<C, T> listener) {
		this.iterationListeners.remove(listener);
	}

	public T fitness(C chromosome) {
		return this.chromosomesComparator.fit(chromosome);
	}

	public void clearCache() {
		this.chromosomesComparator.clearCache();
	}
}
