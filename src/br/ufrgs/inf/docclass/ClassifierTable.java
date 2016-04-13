package br.ufrgs.inf.docclass;

import java.util.HashMap;
import java.util.Map;

import br.ufrgs.inf.vocabbuilder.ProbClass;

public class ClassifierTable {

	private Map<String, Double[]> probTable = new HashMap<>();
	private int[] classDocCount = new int[ProbClass.count()];

	public void putClassDocCount(ProbClass c, int count) {
		classDocCount[c.ordinal()] = count;

	}

	public void putValue(String word, double value, ProbClass c) {
		if (!probTable.containsKey(word)) {
			probTable.put(word, new Double[ProbClass.count()]);
		}

		Double[] values = probTable.get(word);
		values[c.ordinal()] = value;
	}

	public double getValue(String word, ProbClass c) {
		if (!probTable.containsKey(word)) {
			return -1;
		}

		return probTable.get(word)[c.ordinal()];

	}

	public boolean contains(String word) {
		return probTable.containsKey(word);
	}

	public double getAPriori(ProbClass c) {
		return classDocCount[c.ordinal()] / (double)getDocTotalCount();
	}

	public int getDocTotalCount() {
		int total = 0;
		for (ProbClass c : ProbClass.values()) {
			total += classDocCount[c.ordinal()];
		}

		return total;
	}

}
