package ist.spln.readers.script;

import ist.spln.readers.subtitle.TimeInfo;

public class ScriptLine {
    private String line;
    private int lineNumber;

    public ScriptLine(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public String getLine() {
        return line;
    }

    public void addTimeInfoToLine(TimeInfo timeInfo) {
        this.line = timeInfo.getStartTime() + this.line + timeInfo.getEndTime();
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
