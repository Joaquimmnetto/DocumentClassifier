package br.ufrgs.inf.docclass;

import br.ufrgs.inf.vocabbuilder.Index;
import br.ufrgs.inf.vocabbuilder.ProbClass;

public class Trainer {

	private ClassifierTable table;

	public Trainer() {
		this.table = new ClassifierTable();
	}

	public ClassifierTable train(Index index) {
		for (ProbClass c : ProbClass.values()) {
			table.putClassDocCount(c, index.getClassDocCount(c));
		}

		for (String w : index.getAllWords()) {
			trainWord(index, w);
		}

		return table;
	}

	private void trainWord(Index index, String w) {
		for (ProbClass c : ProbClass.values()) {
			table.putValue(w, p(index, w, c), c);
		}
	}

	private double p(Index index, String w, ProbClass c) {
		double nwc = index.getVocabCount(w, c);
		double nc = index.getClassWordCount(c);
		double wordCount = index.getWordCount();

		double result = (nwc + 1) / (nc + wordCount);

		return result;

	}

}
