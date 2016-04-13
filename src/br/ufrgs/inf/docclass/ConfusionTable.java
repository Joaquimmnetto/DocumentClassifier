package br.ufrgs.inf.docclass;

public class ConfusionTable {

	public int vp;
	public int vn;
	public int fp;
	public int fn;

	public ConfusionTable() {
	}
	/**
	 * Constructor to calculate the average confusion table
	 * @param tables - Confusion tables from previous runs to do the average calculation.
	 */
	public ConfusionTable(ConfusionTable... tables) {
		for (ConfusionTable t : tables) {
			vp += t.vp;
			vn += t.vn;
			fp += t.fp;
			fn += t.fn;
		}
		
		
	}

	public double fMeasure() {
		return (2 * recall() * precision()) / (recall() + precision());
	}

	public double precision() {
		return vp / (double) (vp + fp);
	}

	public double recall() {
		return vp / (double) (vp + fn);
	}

	public double fpTax() {
		return fp / (double) (fp + vn);
	}

	@Override
	public String toString() {
		String table = "VP:" + vp + "\tVN:" + vn + "\n" + "FP:" + fp + "\tFN:" + fn;

		String metrics = "Precision:" + precision() + "\nRecall:" + recall() + "\nFPTax:" + fpTax() + "\nF-Measure:"
				+ fMeasure();

		return table + "\n" + metrics;

	}

}
