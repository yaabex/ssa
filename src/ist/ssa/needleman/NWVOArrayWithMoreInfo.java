package ist.ssa.needleman;

import ist.ssa.textanalysis.TextNormalizer;

public class NWVOArrayWithMoreInfo extends NWVOArray {
	private int _sourceLine;
	private String _text;

	public NWVOArrayWithMoreInfo(String string, int sourceLine) {
		_text = string;
		_sourceLine = sourceLine;
	}

	public int getSourceLine() {
		return _sourceLine;
	}

	public String getText() {
		return _text;
	}

	@Override
	public boolean isEquivalentTo(NWVOArray vo) {
		NWVOArrayWithMoreInfo otherObject = ((NWVOArrayWithMoreInfo) vo);
		if (TextNormalizer.normPunctLCaseDMarks(_text).equals(
				TextNormalizer.normPunctLCaseDMarks(otherObject.getText()))) {
			return true;
		}
		/*
		 * else { if (getString().length() < 5) { return (new
		 * NeedlemanWunch()).run(toVOArray(getString()),
		 * toVOArray(otherObject.getString()), 1, 0, 1).getRes() < 3; } else {
		 * return (new NeedlemanWunch()).run(toVOArray(getString()),
		 * toVOArray(otherObject.getString()), 1, 0, 1).getRes() < 3; }
		 * 
		 * }
		 */
		return false;
	}

	private MEDVO[] toVOArray(String s) {
		char[] chars = s.toCharArray();
		MEDVO[] medValueObjects = new MEDVO[chars.length];
		for (int i = 0; i < chars.length; i++) {
			medValueObjects[i] = new MEDVO(chars[i]);
		}
		return medValueObjects;
	}
}
