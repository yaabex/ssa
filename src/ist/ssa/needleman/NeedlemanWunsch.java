package ist.ssa.needleman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Needleman-Wunsch alignment algorithm.
 */
public class NeedlemanWunsch {

	/**
	 * @param seq1
	 * @param seq2
	 * @param gap
	 * @param match
	 * @param mismatch
	 * @return alignment results
	 */
	public NWResult run(Span[] seq1, Span[] seq2, int gap, int match, int mismatch) {
		Value[][] nw = new Value[seq1.length + 1][seq2.length + 1];
		nw[0][0] = new Value(0, new Direction(false, false, true));

		System.err.println("NeedlemanWunch.run: " + (seq1.length + 1) + " x " + (seq2.length + 1));

		for (int i = 1; i < nw.length; i++)
			nw[i][0] = new Value(i * gap, new Direction(true, false, false));

		for (int j = 1; j < nw[0].length; j++)
			nw[0][j] = new Value(j * gap, new Direction(false, true, false));

		for (int i = 1; i < nw.length; i++) {
			for (int j = 1; j < nw[i].length; j++) {
				int left = nw[i][j - 1].value() + gap;
				int up = nw[i - 1][j].value() + gap;
				int corner = nw[i - 1][j - 1].value()
						+ (seq1[i - 1].matches(seq2[j - 1]) ? match : mismatch);

				int min = Math.min(left, Math.min(up, corner));
				nw[i][j] = new Value(min, new Direction(min == up, min == left, min == corner));
			}
		}

		System.err.println("NeedlemanWunch.run: preparing optimal alignment");

		return optimalAlignment(nw, seq1, seq2);
	}

	/**
	 * @param nw
	 * @param seq1
	 * @param seq2
	 * @return optimal alignment
	 */
	private NWResult optimalAlignment(Value[][] nw, Span[] seq1, Span[] seq2) {
		int matches = 0;
		int total = 0;
		int i = seq1.length;
		int j = seq2.length;

		List<Pair> pairs = new ArrayList<Pair>();
		while (i != 0 || j != 0) {
			Direction dir = nw[i][j].direction();
			if (dir.isLeft()) {
				j--;
			} else if (dir.isUp()) {
				i--;
			} else if (dir.isCorner()) {
				// TODO check for null values in the rest of the code
				pairs.add(new Pair(i != 0 ? seq1[i - 1] : null, j != 0 ? seq2[j - 1] : null));
				i--;
				j--;
				matches++;
			}
			total++;
		}
		System.err.println("Word Matches/Total Words = " + matches / (float) total);
		Collections.reverse(pairs);
		return new NWResult(pairs, nw[seq1.length][seq2.length].value());
	}

	private void printMatrix(Value[][] nw) {
		for (int i = 0; i < nw.length; i++) {
			for (int j = 0; j < nw[i].length; j++)
				System.out.print(nw[i][j].value() + "\t");
			System.out.println();
		}
	}
}
