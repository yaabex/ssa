package ist.ssa.needleman;

/**
 * Direction in Needleman-Wunsch.
 */
public class Direction {
	/** Up case. */
	private final boolean _up;
	/** Left case. */
	private final boolean _left;
	/** Corner case. */
	private final boolean _corner;

	/**
	 * @param up
	 * @param left
	 * @param corner
	 */
	Direction(boolean up, boolean left, boolean corner) {
		_up = up;
		_left = left;
		_corner = corner;
	}

	/**
	 * @return up case
	 */
	public boolean isUp() {
		return _up;
	}

	/**
	 * @return left case
	 */
	public boolean isLeft() {
		return _left;
	}

	/**
	 * @return corner case
	 */
	public boolean isCorner() {
		return _corner;
	}

	/**
	 * @param seq1
	 * @param seq2
	 * @param i
	 * @param j
	 * @return string pair
	 */
	@SuppressWarnings("nls")
	public String[] strings(TextSpan[] seq1, TextSpan[] seq2, int i, int j) {
		if (_up)
			return new String[] { seq1[i].getText(), "_" };
		if (_left)
			return new String[] { "_", seq2[j].getText() };
		if (_corner)
			return new String[] { seq1[i].getText(), seq2[j].getText() };

		return new String[] { "_", "_" }; // DAVID FIXME
	}

}
