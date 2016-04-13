package br.ufrgs.inf.docclass;

import java.io.File;
import java.io.IOException;

import br.ufrgs.inf.vocabbuilder.ProbClass;

public class Main {

	private static final String POS_FOLDER = "/home/joaquim/Downloads/IMDB/pos";
	private static final String NEG_FOLDER = "/home/joaquim/Downloads/IMDB/neg";

	public static void main(String[] args) throws IOException {
		TenFoldValidator validator = new TenFoldValidator();
		Validator.MetricsCalc metricsCalc = new Validator.MetricsCalc() {

			@Override
			public ConfusionTable updateTable(ConfusionTable metrics, ProbClass result, ProbClass expected) {
				if (result.equals(ProbClass.getClass("P"))) {
					if (expected.equals(ProbClass.getClass("P"))) {
						metrics.vp++;
					} else {
						metrics.fp++;
					}
				}
				if (result.equals(ProbClass.getClass("N"))) {
					if (expected.equals(ProbClass.getClass("N"))) {
						metrics.vn++;
					} else {
						metrics.fn++;
					}
				}
				return metrics;
			}
		};

		validator.putSet(ProbClass.getClass("P"), new File(POS_FOLDER).listFiles());
		validator.putSet(ProbClass.getClass("N"), new File(NEG_FOLDER).listFiles());

		ConfusionTable[] cTables = validator.tenFoldValidation(metricsCalc);
		
		ConfusionTable avg = new ConfusionTable(cTables);
		System.err.println("Results:");
		for (int i = 0; i < 10; i++) {
			ConfusionTable t = cTables[i];
			System.out.println("Cross-validation step " + i + " results: ");
			System.out.println(t);
			System.out.println();
		}
		
		System.err.println("Average confusion table and corresponding metrics: ");
		System.out.println(avg);
	}

}
