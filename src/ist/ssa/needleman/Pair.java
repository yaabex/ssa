package ist.ssa.needleman;

public class Pair {
	private Span _first;
	private Span _second;

	public Pair(Span first, Span second) {
		_first = first;
		_second = second;
	}

	public Span first() {
		return _first;
	}

	public Span second() {
		return _second;
	}
}
