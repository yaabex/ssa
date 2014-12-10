package ist.ssa.content;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

public abstract class Story {

	/** File name. */
	private String _location;

	/** Low-level location. */
	private Path _path;

	/** File contents. */
	private List<Line> _storyLines;

	/**
	 * @param location
	 *            file name
	 */
	public Story(String location) {
		_location = location;
		setPath(Paths.get(location));
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return _location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		_location = location;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return _path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(Path path) {
		_path = path;
	}

	public List<Line> getStoryLines() {
		return _storyLines;
	}

	public void setStoryLines(List<Line> storyLines) {
		_storyLines = storyLines;
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
	 * @return filename for story
	 */
	public abstract String getStoryName();

	/**
	 * @param basename
	 * @throws IOException
	 */
	public final void writeWholeStory(String basename) throws IOException {
		File file = new File(basename);
		file.mkdirs();
		file = new File(basename + "/" + getStoryName());
		FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), getWholeStory());
	}

	public abstract void read() throws IOException;

}
