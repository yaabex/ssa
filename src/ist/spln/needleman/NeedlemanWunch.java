package ist.spln.needleman;

import ist.spln.needleman.valueobject.NeedlemanArrayValueObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NeedlemanWunch {
    private final static int GAP = 1;
    private final static int MATCH = -1;
    private final static int MISMATCH = 1;

	public List<ValueObjectPair> run(NeedlemanArrayValueObject[] sequence1, NeedlemanArrayValueObject[] sequence2) {
		
		Val[][] needleman = new Val[sequence1.length+1][sequence2.length+1];
		needleman[0][0] = new Val(0, new Dir(false, false, true));

		for(int i = 1; i < needleman.length; i++) {
			needleman[i][0] = new Val(i*GAP, new Dir(true, false, false));
		}
		
		for(int j = 1; j < needleman[0].length; j++) {
			needleman[0][j] = new Val(j*GAP, new Dir(false, true, false));
		}
		
		for(int i = 1; i < needleman.length; i++) {
			for(int j = 1; j < needleman[i].length; j++) {
				int l = needleman[i][j-1].getValue() + GAP;
				int u = needleman[i-1][j].getValue() + GAP;
				int c = needleman[i-1][j-1].getValue() + calcMatch(sequence1[i-1], sequence2[j-1]);
				
				int min = Math.min(l, Math.min(u, c));

                boolean up = false;
                boolean left = false;
                boolean corner = false;

				if(min == c) {
                    corner = true;
				}
				if(min == l) {
					left = true;
				}
				if(min == u) {
                    up = true;
				}
                needleman[i][j] = new Val(min, new Dir(up, left, corner));
			}
		}
		
		System.out.println("Optimal score: " + needleman[sequence1.length][sequence2.length].getValue());
		return returnOptimalAlignment(needleman, sequence1, sequence2);
	}

	private int calcMatch(NeedlemanArrayValueObject value1, NeedlemanArrayValueObject value2) {
		if(value1.equals(value2)) {
			return MATCH;
		}
		return MISMATCH;
	}

	private List<ValueObjectPair> returnOptimalAlignment(Val[][] needleman, NeedlemanArrayValueObject[] sequence1, NeedlemanArrayValueObject[] sequence2) {
        int matches = 0;
        int total = 0;
		int i = sequence1.length;
		int j = sequence2.length;
        List<ValueObjectPair> valueObjects = new LinkedList<>();
		while (!(i == 0 && j == 0)) {
            valueObjects.add(new ValueObjectPair((i != 0) ? sequence1[i-1] : null, (j != 0) ? sequence2[j-1] : null));
            total++;
			Dir dir = needleman[i][j].getDir();
            if(dir.isCorner()) {
                i--;
                j--;
                matches++;
            } else if(dir.isLeft()) {
                j--;
            } else if(dir.isUp()) {
                i--;
            }
		}
        System.out.println("Matches/Total = " + matches/(float)total);
        Collections.reverse(valueObjects);
        return valueObjects;
    }

	private void printMatrix(Val[][] needleman) {
		for(int i = 0; i < needleman.length; i++) {
			for(int j = 0; j < needleman[i].length; j++) {
				System.out.print(needleman[i][j].getValue() + "\t");
			}
			System.out.println();
		}
	}
}
