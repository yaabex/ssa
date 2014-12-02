package ist.ssa.needleman;

import java.util.List;

public class NWResults {
	private List<VOPair> valueObjectPairs;
	private long res;

	public NWResults(List<VOPair> valueObjectPairs, long res) {
		this.valueObjectPairs = valueObjectPairs;
		this.res = res;
	}

	public List<VOPair> getValueObjectPairs() {
		return valueObjectPairs;
	}

	public long getRes() {
		return res;
	}
}
