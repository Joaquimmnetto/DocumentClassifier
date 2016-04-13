package br.ufrgs.inf.docclass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrgs.inf.vocabbuilder.ProbClass;

public class TenFoldValidator {

	private Map<ProbClass, File[]> sets = new HashMap<>();

	private File[][][] tenFoldDivision = new File[10][][];

	public void putSet(ProbClass c, File[] docs) {
		List<File> docsList = Arrays.asList(docs);
		Collections.shuffle(docsList);
		sets.put(c, docsList.toArray(docs));
	}

	// perguntar mais sobre!
	private void generateTenFold() {

		for (int i = 0; i < 10; i++) {
			tenFoldDivision[i] = new File[ProbClass.count()][];
			for (ProbClass c : ProbClass.values()) {
				int partSize = sets.get(c).length / 10;
				File[] subpart = Arrays.copyOfRange(sets.get(c), i * partSize, (i + 1) * partSize);
				tenFoldDivision[i][c.ordinal()] = subpart;
			}

		}
	}

	public ConfusionTable[] tenFoldValidation(Validator.MetricsCalc metrics) throws IOException {
		generateTenFold();
		ConfusionTable[] results = new ConfusionTable[10];
		Validator v = new Validator();
		for (int i = 0; i < 10; i++) {
			File[][] trainingSet = new File[ProbClass.count()][];
			File[][] validationSet = tenFoldDivision[i];

			List<List<File>> trainingList = new ArrayList<>();

			for (ProbClass c : ProbClass.values()) {
				trainingList.add(c.ordinal(), new ArrayList<>());
			}
			for (int j = 0; j < 10; j++) {
				if (j != i) {
					for (ProbClass c : ProbClass.values()) {
						trainingList.get(c.ordinal()).addAll(Arrays.asList(tenFoldDivision[j][c.ordinal()]));
					}
				}
			}
			for (ProbClass c : ProbClass.values()) {
				List<File> cList = trainingList.get(c.ordinal());
				trainingSet[c.ordinal()] = cList.toArray(new File[cList.size()]);
			}
			trainingList.clear();
			System.err.println("Cross-validation step "+i+":");
			ConfusionTable t = v.validate(trainingSet, validationSet, metrics);
			results[i] = t;
			

		}
		return results;
	}

}
