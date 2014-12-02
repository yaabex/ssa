package ist.ssa.readers;

public abstract class ScriptLine extends Line {
    private boolean toTranslate;

    public ScriptLine(String line, int lineNumber, boolean toTranslate) {
    	super(line, lineNumber);
        this.toTranslate = toTranslate;
    }

    public void setToTranslate(boolean toTranslate) {
        this.toTranslate = toTranslate;
    }

    public boolean isToTranslate() {
        return toTranslate;
    }

}
