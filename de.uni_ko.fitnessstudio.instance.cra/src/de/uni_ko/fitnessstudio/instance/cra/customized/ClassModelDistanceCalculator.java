package de.uni_ko.fitnessstudio.instance.cra.customized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architectureCRA.Class;
import architectureCRA.ClassModel;
import architectureCRA.Feature;
import de.uni_ko.fitnessstudio.lower.DomainModel;
import de.uni_ko.fitnessstudio.lower.DomainModelDistanceCalculator;

public class ClassModelDistanceCalculator implements DomainModelDistanceCalculator {

	@Override
	public Double[][] calculate(Collection<DomainModel> chromosomes) {
		int size = chromosomes.size();
		
		List<List<Integer>> encoded = computeEncodings(chromosomes);
		
		Double[][] distances = new Double[size][size];
		for (int i=0; i<size; i++) {			
			List<Integer> encodedI = encoded.get(i);
			for (int j=0; j<size; j++) {
				List<Integer> encodedJ = encoded.get(j);
				int common = 0;
				int classes = Math.min(encodedI.size(), encodedJ.size());
				
				for (int k = 0; k<classes; k++) {
					if (encodedI.get(k) == encodedJ.get(k))
						common++;
				}
				
				distances[i][j] = 1.0 - (common / classes);
			}
		}
		
//		for (int i = 0; i < distances.length; i++) {
//		    for (int j = 0; j < distances[i].length; j++) {
//		        System.out.print(distances[i][j] + " ");
//		    }
//		    System.out.println();
//		}
//		System.out.println();
		return distances;
	}

	private List<List<Integer>> computeEncodings(Collection<DomainModel> chromosomes) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		for (DomainModel d : chromosomes) {
			List<Integer> resultEntry = new ArrayList<Integer>();
			ClassModel c = (ClassModel) d.getContent();
			List<Class> classes = new ArrayList<Class>(c.getClasses());
			Map<Class, Double> class2fitness = new HashMap<Class, Double>();
			for (Class cl : classes) {
				class2fitness.put(cl, CRAIndexCalculator.calculateCohesion(cl));
			}
			
			classes.sort(new Comparator<Class>() {
				@Override
				public int compare(Class o1, Class o2) {
					return Double.compare(class2fitness.get(o1), class2fitness.get(o2));
				}
			});
			
			Integer aggregatedHashCode = 0;
			for (Class cl : classes) {
				for (Feature en : cl.getEncapsulates()) {
					aggregatedHashCode += en.hashCode();
				}
			}
			resultEntry.add(aggregatedHashCode);
			result.add(resultEntry);
		}
		return result;
	}
	
	
}
