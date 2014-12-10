package ist.ssa.content;

public class TimeInfo {
	private String _start;
	private String _end;

	public TimeInfo(String start, String end) {
		_start = start;
		_end = end;
	}

	public String getStartTime() {
		return _start;
	}

	public String getEndTime() {
		return _end;
	}
}
