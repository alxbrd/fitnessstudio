package com.lagodiuk;

import java.util.Collection;

public interface DistanceCalculator<C extends Chromosome<C>, T extends Comparable<T>> {

	public T[][] calculate(Collection<C> chromosomes);

}