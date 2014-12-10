package ist.ssa.content;

public class SubtitleLine extends Line {
	private TimeInfo _timeInfo;

	public SubtitleLine(String text, int lineNumber, TimeInfo timeInfo) {
		super(text, lineNumber);
		_timeInfo = timeInfo;
	}

	public TimeInfo getTimeInfo() {
		return _timeInfo;
	}
}
