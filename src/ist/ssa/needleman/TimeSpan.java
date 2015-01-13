package ist.ssa.needleman;

import org.joda.time.Interval;

/**
 * Source time spans.
 */
public class TimeSpan extends Span {
	/** Time interval in this span. */
	private Interval _interval;

	/**
	 * @param line 
	 * @param time
	 */
	public TimeSpan(int line, Interval time) {
		super(line);
		_interval = time;
	}

	/**
	 * @return the time interval in this span.
	 */
	public Interval getInterval() {
		return _interval;
	}

	/**
	 * Two time spans match if they share a significant overlap.
	 * 
	 * @see ist.ssa.needleman.Span#matches(ist.ssa.needleman.Span)
	 */
	@Override
	public boolean matches(Span span) {
		if (span instanceof TimeSpan) {
			Interval other = ((TimeSpan) span).getInterval();
			Interval overlap = _interval.overlap(other);
			if (overlap != null) {
				double mean = (_interval.toDurationMillis() + other.toDurationMillis()) / 2d;
				if (overlap.toDurationMillis() / mean > 0.50) {
					return true;
				}
			}
		}
		return false;
	}

}
