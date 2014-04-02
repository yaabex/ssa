package ist.spln.readers.script;

public class ScriptLine {
    private String line;
    private int lineNumber;
    private String characterName;

    public ScriptLine(String line, int lineNumber, String characterName) {
        this.line = line;
        this.lineNumber = lineNumber;
        this.characterName = characterName;
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
}
