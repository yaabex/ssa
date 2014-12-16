package ist.ssa.needleman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeedlemanWunch {

	public NWResults run(NWVOArray[] seq1, NWVOArray[] seq2, int gap, int match, int mismatch) {

		Value[][] nw = new Value[seq1.length + 1][seq2.length + 1];
		nw[0][0] = new Value(0, new Direction(false, false, true));

		for (int i = 1; i < nw.length; i++) {
			nw[i][0] = new Value(i * gap, new Direction(true, false, false));
		}

		for (int j = 1; j < nw[0].length; j++) {
			nw[0][j] = new Value(j * gap, new Direction(false, true, false));
		}

		for (int i = 1; i < nw.length; i++) {
			for (int j = 1; j < nw[i].length; j++) {
				int l = nw[i][j - 1].getValue() + gap;
				int u = nw[i - 1][j].getValue() + gap;
				int c = nw[i - 1][j - 1].getValue()
						+ calcMatch(seq1[i - 1], seq2[j - 1], match, mismatch);

				int min = Math.min(l, Math.min(u, c));

				boolean up = false;
				boolean left = false;
				boolean corner = false;

				if (min == c) {
					corner = true;
				}
				if (min == l) {
					left = true;
				}
				if (min == u) {
					up = true;
				}
				nw[i][j] = new Value(min, new Direction(up, left, corner));
			}
		}
		return returnOptimalAlignment(nw, seq1, seq2);
	}

	private int calcMatch(NWVOArray value1, NWVOArray value2, int match, int mismatch) {
		if (value1.isEquivalentTo(value2))
			return match;
		return mismatch;
	}

	private NWResults returnOptimalAlignment(Value[][] nw, NWVOArray[] seq1, NWVOArray[] seq2) {
		int matches = 0;
		int total = 0;
		int i = seq1.length;
		int j = seq2.length;
		List<VOPair> valueObjects = new ArrayList<VOPair>();
		while (!(i == 0 && j == 0)) {
			total++;
			Direction dir = nw[i][j].getDirection();
			if (dir.isLeft()) {
				j--;
			} else if (dir.isUp()) {
				i--;
			} else if (dir.isCorner()) {
				// TODO check for null values in the rest of the code
				valueObjects.add(new VOPair((i != 0) ? seq1[i - 1] : null, (j != 0) ? seq2[j - 1]
						: null));
				i--;
				j--;
				matches++;
			}
		}
		// System.out.println("Word Matches/Total Words = " +
		// matches/(float)total);
		Collections.reverse(valueObjects);
		return new NWResults(valueObjects, nw[seq1.length][seq2.length].getValue());
	}

	private void printMatrix(Value[][] nw) {
		for (int i = 0; i < nw.length; i++) {
			for (int j = 0; j < nw[i].length; j++) {
				System.out.print(nw[i][j].getValue() + "\t");
			}
			System.out.println();
		}
	}
}
