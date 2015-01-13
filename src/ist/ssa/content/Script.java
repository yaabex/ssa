package ist.ssa.content;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Script extends Story {
	private List<Line> _wholeScript;
	private List<DialogLine> _scriptDialogs;

	/**
	 * @param reader 
	 * @throws IOException
	 */
	public Script(StoryVisitor reader) throws IOException {
		accept(reader);
	}

	/**
	 * @see ist.ssa.content.Story#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		_wholeScript = new ArrayList<>();
		_scriptDialogs = new ArrayList<>();
	}

	/**
	 * TODO: FIXME: should probably be the same as before (superclass)
	 * 
	 * @see ist.ssa.content.Story#size()
	 */
	@Override
	public int size() {
		return _wholeScript.size();
	}

	public boolean nameOfCharacter(String line) {
		return line.matches("^\\s+[\\p{Lu}]{2,}.*");
	}

	public Line getContextFromLineNumberOfWord(int i) {
		return getWholeStory().get(getDialogs().get(i).getIndex());
	}

	public List<DialogLine> getDialogs() {
		return _scriptDialogs;
	}

	public List<Line> getWholeStory() {
		return _wholeScript;
	}

	/**
	 * @see ist.ssa.content.Story#accept(ist.ssa.content.StoryVisitor)
	 */
	@Override
	public void accept(StoryVisitor visitor) throws IOException {
		visitor.processScript(this);
	}

	/**
	 * @param scriptLine
	 */
	public void addLine(Line scriptLine) {
		_wholeScript.add(scriptLine);
	}

	/**
	 * @param scriptDialog
	 */
	public void addDialogLine(DialogLine scriptDialog) {
		_scriptDialogs.add(scriptDialog);
	}

}
