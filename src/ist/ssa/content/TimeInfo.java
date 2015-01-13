package ist.ssa.content;


/**
 * Time interval.
 */
public class TimeInfo {
	
	/** Start instant. */
	private String _start;
	
	/** End instant. */
	private String _end;

	/**
	 * @param start
	 * @param end
	 */
	public TimeInfo(String start, String end) {
		_start = start;
		_end = end;
	}

	/**
	 * @return start
	 */
	public String getStartTime() {
		return _start;
	}

	/**
	 * @return end
	 */
	public String getEndTime() {
		return _end;
	}
}
