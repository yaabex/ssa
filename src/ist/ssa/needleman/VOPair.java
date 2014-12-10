package ist.ssa.needleman;

public class VOPair {
	private NWVOArray first;
	private NWVOArray second;

	public VOPair(NWVOArray first, NWVOArray second) {
		this.first = first;
		this.second = second;
	}

	public NWVOArray first() {
		return first;
	}

	public NWVOArray second() {
		return second;
	}
}
