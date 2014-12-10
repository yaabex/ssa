package ist.ssa.needleman;

public class Direction {
	private final boolean _up;
	private final boolean _left;
	private final boolean _corner;

	Direction(boolean up, boolean left, boolean corner) {
		_up = up;
		_left = left;
		_corner = corner;
	}

	public boolean isUp() {
		return _up;
	}

	public boolean isLeft() {
		return _left;
	}

	public boolean isCorner() {
		return _corner;
	}

	/**
	 * @param s1
	 * @param s2
	 * @param i
	 * @param j
	 * @return string pair
	 */
	public String[] getStrings(NWVOArrayWithMoreInfo[] s1, NWVOArrayWithMoreInfo[] s2, int i, int j) {
		if (_up)
			return new String[] { s1[i].getText(), "_" };
		if (_left)
			return new String[] { "_", s2[j].getText() };
		if (_corner)
			return new String[] { s1[i].getText(), s2[j].getText() };

		return new String[] { "_", "_" };  //DAVID FIXME
	}

}
