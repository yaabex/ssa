package ist.spln.readers.script;

import ist.spln.readers.subtitle.TimeInfo;

import java.util.ArrayList;
import java.util.List;

public class ScriptDialog extends ScriptLine {
    private int lineNumber;
    private String characterName;
    private List<TimeInfo> timeInfos;

    public ScriptDialog(String line, int lineNumber, String characterName) {
        super(line, true);
        this.lineNumber = lineNumber;
        this.characterName = characterName;
        this.timeInfos = new ArrayList<>();
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
