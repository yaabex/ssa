package ist.ssa.readers;

import java.util.ArrayList;
import java.util.List;

public class ScriptDialogLine extends ScriptLine {
    private String characterName;
    private List<TimeInfo> timeInfos;

    public ScriptDialogLine(String line, int lineNumber, String characterName) {
        super(line, lineNumber, true);
        this.characterName = characterName;
        this.timeInfos = new ArrayList<>();
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
