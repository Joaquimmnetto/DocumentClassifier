package br.ufrgs.inf.docclass;

import java.io.File;
import java.io.IOException;

import br.ufrgs.inf.vocabbuilder.Index;
import br.ufrgs.inf.vocabbuilder.PreProcessor;
import br.ufrgs.inf.vocabbuilder.ProbClass;
import br.ufrgs.inf.vocabbuilder.PreProcessor.FileProcessingTypes;

public class Validator {

	interface MetricsCalc {
		ConfusionTable updateTable(ConfusionTable metrics, ProbClass result, ProbClass expected);
	}

	/**
	 * 
	 * @param trainingSet
	 *            - Training set to use in the training step, composed by class
	 *            x docs from that class.
	 * @param validationSet
	 *            - Training set used in the validation step, composed by class
	 *            x docs from that class
	 * @param metrics
	 *            - Listener that describes how to update the confusion table.
	 * @return ConfusionTable containing the metrics for this run.
	 * 
	 * @throws IOException
	 *             - If a File reading error occours.
	 */
	public ConfusionTable validate(File[][] trainingSet, File[][] validationSet, MetricsCalc metrics)
			throws IOException {
		PreProcessor pp = new PreProcessor(FileProcessingTypes.None);
		System.out.println("Creating index...");
		for (ProbClass c : ProbClass.values()) {
			pp.process(trainingSet[c.ordinal()], c);
		}
		Index index = pp.getIndex();
		System.out.println("Training dataset...");
		Trainer t = new Trainer();
		ClassifierTable table = t.train(index);

		ConfusionTable confTable = new ConfusionTable();
		Classifier cl = new Classifier();
		System.out.println("Doing classification and validation...");
		for (ProbClass c : ProbClass.values()) {
			// trocar por c.ordinal Q_Q
			File[] classDocs = validationSet[c.ordinal()];
			for (File doc : classDocs) {

				ProbClass res = cl.classify(doc, table);

				confTable = metrics.updateTable(confTable, res, c);
			}
		}
		return confTable;
	}

}
