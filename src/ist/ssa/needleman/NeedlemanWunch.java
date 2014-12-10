package ist.ssa.needleman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeedlemanWunch {

	public NWResults run(NWVOArray[] sequence1, NWVOArray[] sequence2, int gap,
			int match, int mismatch) {

		Value[][] needleman = new Value[sequence1.length + 1][sequence2.length + 1];
		needleman[0][0] = new Value(0, new Direction(false, false, true));

		for (int i = 1; i < needleman.length; i++) {
			needleman[i][0] = new Value(i * gap, new Direction(true, false, false));
		}

		for (int j = 1; j < needleman[0].length; j++) {
			needleman[0][j] = new Value(j * gap, new Direction(false, true, false));
		}

		for (int i = 1; i < needleman.length; i++) {
			for (int j = 1; j < needleman[i].length; j++) {
				int l = needleman[i][j - 1].getValue() + gap;
				int u = needleman[i - 1][j].getValue() + gap;
				int c = needleman[i - 1][j - 1].getValue()
						+ calcMatch(sequence1[i - 1], sequence2[j - 1], match, mismatch);

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
				needleman[i][j] = new Value(min, new Direction(up, left, corner));
			}
		}
		return returnOptimalAlignment(needleman, sequence1, sequence2);
	}

	private int calcMatch(NWVOArray value1, NWVOArray value2, int match, int mismatch) {
		if (value1.isEquivalentTo(value2))
			return match;
		return mismatch;
	}

	private NWResults returnOptimalAlignment(Value[][] needleman, NWVOArray[] sequence1,
			NWVOArray[] sequence2) {
		int matches = 0;
		int total = 0;
		int i = sequence1.length;
		int j = sequence2.length;
		List<VOPair> valueObjects = new ArrayList<VOPair>();
		while (!(i == 0 && j == 0)) {
			total++;
			Direction dir = needleman[i][j].getDirection();
			if (dir.isLeft()) {
				j--;
			} else if (dir.isUp()) {
				i--;
			} else if (dir.isCorner()) {
				// TODO check for null values in the rest of the code
				valueObjects.add(new VOPair((i != 0) ? sequence1[i - 1] : null,
						(j != 0) ? sequence2[j - 1] : null));
				i--;
				j--;
				matches++;
			}
		}
		// System.out.println("Word Matches/Total Words = " +
		// matches/(float)total);
		Collections.reverse(valueObjects);
		return new NWResults(valueObjects, needleman[sequence1.length][sequence2.length].getValue());
	}

	private void printMatrix(Value[][] needleman) {
		for (int i = 0; i < needleman.length; i++) {
			for (int j = 0; j < needleman[i].length; j++) {
				System.out.print(needleman[i][j].getValue() + "\t");
			}
			System.out.println();
		}
	}
}
