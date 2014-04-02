package ist.spln.readers.subtitle;

public class SubtitleLine {
    private String line;
    private int lineNumber;
    private TimeInfo timeInfo;

    public SubtitleLine(String line, int lineNumber, TimeInfo timeInfo) {
        this.line = line;
        this.lineNumber = lineNumber;
        this.timeInfo = timeInfo;
    }

    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public TimeInfo getTimeInfo() {
        return timeInfo;
    }
}
