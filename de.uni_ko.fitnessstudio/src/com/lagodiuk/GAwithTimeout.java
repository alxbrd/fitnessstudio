package com.lagodiuk;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class GAwithTimeout<C extends Chromosome<C>, T extends Comparable<T>>  extends GA<C, T>  {
	int timeoutSeconds = 0;
	
	public GAwithTimeout(GAPopulation<C> population, Fitness<C, T> fitnessFunc, DistanceCalculator<C, T> distanceCalculator, int timeoutSeconds) {
		super(population, fitnessFunc, distanceCalculator);
		this.timeoutSeconds = timeoutSeconds;
	}

	public void evolve() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> future = null;
		@SuppressWarnings("unused")
		Boolean result = null;
		try {
			future = executor
					.submit(new EvolveTask<C, T>(this));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			result = future.get(timeoutSeconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(true);
		}
		executor.shutdown();
	}
	
	public void evolveSuper() {
		super.evolve();
	}
}


class EvolveTask<C extends Chromosome<C>, T extends Comparable<T>>  implements Callable<Boolean> {
	GAwithTimeout<C, T> ga;
	
	public EvolveTask(GAwithTimeout<C, T> ga) {
		this.ga = ga;
	}

	@Override
	public Boolean call() throws Exception {
		 ga.evolveSuper();
		 return true;
	}
}