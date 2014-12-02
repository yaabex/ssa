package ist.ssa.needleman;

import org.joda.time.Interval;

public class TimeVO extends NeedlemanVOArray {
	private int lineWhereItCameFrom;
	private Interval time;

	public TimeVO(int lineWhereItCameFrom, Interval time) {
		this.lineWhereItCameFrom = lineWhereItCameFrom;
		this.time = time;
	}

	@Override
	public boolean isEquivalentTo(NeedlemanVOArray vo) {
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

	public int getLineWhereItCameFrom() {
		return lineWhereItCameFrom;
	}
}
