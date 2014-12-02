package ist.ssa.needleman;

public class VOPair {
	private NeedlemanVOArray first;
	private NeedlemanVOArray second;

	public VOPair(NeedlemanVOArray first, NeedlemanVOArray second) {
		this.first = first;
		this.second = second;
	}

	public NeedlemanVOArray first() {
		return first;
	}

	public NeedlemanVOArray second() {
		return second;
	}
}
