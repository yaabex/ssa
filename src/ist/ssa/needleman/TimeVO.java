package ist.ssa.needleman;

import org.joda.time.Interval;

public class TimeVO extends NWVOArray {
	private int _sourceLine;
	private Interval time;

	public TimeVO(int sourceLine, Interval time) {
		this._sourceLine = sourceLine;
		this.time = time;
	}

	@Override
	public boolean isEquivalentTo(NWVOArray vo) {
		Interval otherTime = ((TimeVO) vo).getTime();
		Interval overlap = this.time.overlap(otherTime);
		double average = (this.time.toDurationMillis() + otherTime.toDurationMillis()) / 2d;
		if (overlap != null) {
			if (overlap.toDurationMillis() / average > 0.50) {
				return true;
			}
		}
		return false;
	}

	public Interval getTime() {
		return time;
	}

	public int getSourceLine() {
		return _sourceLine;
	}
}
