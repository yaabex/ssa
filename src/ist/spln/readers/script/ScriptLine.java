package ist.spln.readers.script;

import ist.spln.readers.subtitle.TimeInfo;

import java.util.ArrayList;
import java.util.List;

public class ScriptLine {
    private String line;
    private int lineNumber;
    private String characterName;
    private List<TimeInfo> timeInfos;

    public ScriptLine(String line, int lineNumber, String characterName) {
        this.line = line;
        this.lineNumber = lineNumber;
        this.characterName = characterName;
        this.timeInfos = new ArrayList<>();
    }
    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getCharacterName() {
        return characterName;
    }

    public List<TimeInfo> getTimeInfos() {
        return timeInfos;
    }

    public void addTimeInfo(TimeInfo timeInfo) {
        this.timeInfos.add(timeInfo);
    }
}
