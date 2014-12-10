package ist.ssa.needleman;

import java.util.List;

public class NWResults {
	private List<VOPair> _voPairs;
	private long res;

	public NWResults(List<VOPair> voPairs, long res) {
		this._voPairs = voPairs;
		this.res = res;
	}

	public List<VOPair> getVOPairs() {
		return _voPairs;
	}

	public long getRes() {
		return res;
	}
}
