package ist.ssa.needleman;

/**
 * Source span.
 */
public abstract class Span {
	/** Source line number. */
	private int _line;

	/**
	 * @param line
	 */
	public Span(int line) {
		_line = line;
	}

	/**
	 * @return source line
	 */
	public int getLine() {
		return _line;
	}

	/**
	 * @param span
	 * @return whether two spans match.
	 */
	public abstract boolean matches(Span span);
}
