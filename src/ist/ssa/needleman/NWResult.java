package ist.ssa.needleman;

import java.util.List;

public class NWResult {
	private List<Pair> _pairs;
	private long _result;

	public NWResult(List<Pair> pairs, long result) {
		_pairs = pairs;
		_result = result;
	}

	public List<Pair> getPairs() {
		return _pairs;
	}

	public long getResult() {
		return _result;
	}
}
