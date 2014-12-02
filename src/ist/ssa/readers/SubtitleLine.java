package ist.ssa.readers;

public class SubtitleLine extends Line {
    private TimeInfo timeInfo;

    public SubtitleLine(String text, int lineNumber, TimeInfo timeInfo) {
    	super(text, lineNumber);
        this.timeInfo = timeInfo;
    }

    public TimeInfo getTimeInfo() {
        return timeInfo;
    }
}
