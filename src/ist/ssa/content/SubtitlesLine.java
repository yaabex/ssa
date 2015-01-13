package ist.ssa.content;

public class SubtitlesLine extends Line {
	private TimeInfo _timeInfo;

	/**
	 * @param text
	 * @param lineNumber
	 * @param timeInfo
	 */
	public SubtitlesLine(String text, int lineNumber, TimeInfo timeInfo) {
		super(text, lineNumber);
		_timeInfo = timeInfo;
	}

	/**
	 * @return time information
	 */
	public TimeInfo getTimeInfo() {
		return _timeInfo;
	}
}
