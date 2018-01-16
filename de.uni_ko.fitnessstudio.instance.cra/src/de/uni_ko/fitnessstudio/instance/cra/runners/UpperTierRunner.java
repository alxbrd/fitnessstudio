package de.uni_ko.fitnessstudio.instance.cra.runners;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import architectureCRA.ArchitectureCRAPackage;
import de.uni_ko.fitnessstudio.instance.cra.customized.ClassModelCrossover;
import de.uni_ko.fitnessstudio.instance.cra.customized.ClassModelDistanceCalculator;
import de.uni_ko.fitnessstudio.instance.cra.customized.ClassModelFitness;
import de.uni_ko.fitnessstudio.instance.cra.customized.ClassModelInit;
import de.uni_ko.fitnessstudio.instance.cra.customized.CRAConstraintChecker;
import de.uni_ko.fitnessstudio.upper.UpperGAManager;
import de.uni_ko.fitnessstudio.util.GAConfiguration;
import de.uni_ko.fitnessstudio.util.ModelIO;

public class UpperTierRunner {
	private static String INPUT_MODEL_ID = "D";
	private static String INPUT_MODEL = "input\\TTC_InputRDG_" + INPUT_MODEL_ID + ".xmi";
	private static String OUTPUT_PREFIX = "output_rules\\" + INPUT_MODEL_ID + "\\"
			+ new SimpleDateFormat("HH_mm_ss").format(Calendar.getInstance().getTime()).toString() + "\\";

	private static int UPPER_TIER_ITERATIONS = 40;
	private static int UPPER_TIER_POPULATION_SIZE = 20;
	private static int LOWER_TIER_ITERATIONS = 20;
	private static int LOWER_TIER_POPULATION_SIZE = 2;
	private static int RUNS = 30;
	
	private static GAConfiguration configurationUpper = new GAConfiguration(UPPER_TIER_ITERATIONS, UPPER_TIER_POPULATION_SIZE, true);
	private static GAConfiguration configurationLower = new GAConfiguration(LOWER_TIER_ITERATIONS, LOWER_TIER_POPULATION_SIZE, false);

	public static void main(String[] args) {

		for (int i = 1; i <= RUNS; i++) {
			System.gc();
			System.out.println("============");
			System.out.println("Run " + i);
			System.out.println("============");

			long start = System.currentTimeMillis();

			ArchitectureCRAPackage.eINSTANCE.eClass();
			EPackage metaModel = ArchitectureCRAPackage.eINSTANCE;
			ClassModelFitness domainModelFitness = new ClassModelFitness();
			ClassModelDistanceCalculator distanceCalculator = new ClassModelDistanceCalculator();
			ClassModelCrossover domainModelCrossover = new ClassModelCrossover();
			CRAConstraintChecker mutationConstraintChecker = new CRAConstraintChecker();
			EObject inputModel = ModelIO.loadModel(INPUT_MODEL);

			ClassModelInit init = new ClassModelInit(inputModel, null, domainModelCrossover, domainModelFitness);
			
			UpperGAManager manager = new UpperGAManager(domainModelFitness, distanceCalculator, init, mutationConstraintChecker, metaModel,
					configurationUpper, configurationLower, inputModel, 15);
			manager.setPrefix(OUTPUT_PREFIX + "\\"+i);
			double result = manager.runGA();

			long end = System.currentTimeMillis();
			long time = end - start;
			createLogEntry(i, result, time);
		}
	}

	private static void createLogEntry(int i, double result, long time) {
		String line = i + " \t " + time + " \t " + result + "\n";
		String path = OUTPUT_PREFIX + "log.txt";

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
			bw.write(line);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
