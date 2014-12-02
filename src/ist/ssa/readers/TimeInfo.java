package ist.ssa.readers;

public class TimeInfo {
	private String startTime;
	private String endTime;

	public TimeInfo(String startTime, String endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
}
