package ist.spln.readers.script;

public abstract class ScriptLine {
    private String line;
    private boolean toTranslate;

    public ScriptLine(String line, boolean toTranslate) {
        this.line = line;
        this.toTranslate = toTranslate;
    }
    public String getLine() {
        return line;
    }

    public void setToTranslate(boolean toTranslate) {
        this.toTranslate = toTranslate;
    }

    public boolean isToTranslate() {
        return toTranslate;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return line;
    }
}
