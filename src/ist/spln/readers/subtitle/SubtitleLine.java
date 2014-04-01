package ist.spln.readers.subtitle;

public class SubtitleLine {
    private String line;
    private TimeInfo timeInfo;

    public SubtitleLine(String line, TimeInfo timeInfo) {
        this.line = line;
        this.timeInfo = timeInfo;
    }

    public String getLine() {
        return line;
    }

    public TimeInfo getTimeInfo() {
        return timeInfo;
    }
}
