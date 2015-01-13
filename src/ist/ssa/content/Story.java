package ist.ssa.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Story {

	/** File contents. */
	private List<Line> _storyLines = new ArrayList<>();

	/**
     *
	 */
	public Story() {
	}

	/**
	 * 
	 */
	public void reset() {
		_storyLines = new ArrayList<>();
	}

	public Line getLine(int i) {
		return _storyLines.get(i);
	}

	public List<Line> getStoryLines() {
		return _storyLines;
	}

	public void setStoryLines(List<Line> storyLines) {
		_storyLines = storyLines;
	}

	public int size() {
		return _storyLines.size();
	}

	public void append(Line line) {
		_storyLines.add(line);
	}

	public void printWholeStory() {
		for (Line line : getStoryLines()) {
			System.out.println(line);
		}
	}

	/**
	 * @return list of lines in story
	 */
	public List<?> getWholeStory() {
		return getStoryLines();
	}

	/**
	 * @param lines
	 * @return dialog text at given lines.
	 */
	@SuppressWarnings("nls")
	public String getDialogTextAt(List<Integer> lines) {
		String text = "";
		for (Integer line : lines)
			text += ((Line) getDialogs().get(line)).getText() + " ";
		return text;
	}

	/**
	 * @return dialog lines in the story.
	 */
	public abstract List<?> getDialogs();

	/**
	 * @param visitor
	 * @throws IOException
	 */
	public abstract void accept(StoryVisitor visitor) throws IOException;

}
