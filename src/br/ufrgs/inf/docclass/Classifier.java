package br.ufrgs.inf.docclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import br.ufrgs.inf.vocabbuilder.ProbClass;

public class Classifier {

	public ProbClass classify(File toClassify, ClassifierTable table) throws FileNotFoundException {
		Scanner scanner = new Scanner(toClassify);
		double[] classProb = new double[ProbClass.count()];

		while (scanner.hasNext()) {
			String word = scanner.next();
			for (ProbClass c : ProbClass.values()) {
				if (!table.contains(word)) {
					continue;
				}
				double currentProb = classProb[c.ordinal()];
				currentProb = currentProb == 0 ? 1 : currentProb;

				currentProb += Math.log(table.getValue(word, c));

				classProb[c.ordinal()] = currentProb;
			}

		}

		scanner.close();
		int biggest = 0;

		for (ProbClass c : ProbClass.values()) {
			classProb[c.ordinal()] *= table.getAPriori(c);

			if (classProb[c.ordinal()] > classProb[biggest]) {

				biggest = c.ordinal();

			}
		}

		return ProbClass.values()[biggest];
	}

}
