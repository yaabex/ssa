/**
 * 
 */
package ist.ssa.io;

import ist.ssa.Configuration;
import ist.ssa.content.Script;
import ist.ssa.content.Story;
import ist.ssa.content.StoryVisitor;
import ist.ssa.content.Subtitles;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

/**
 * 
 */
public class WholeStoryWriter extends StoryVisitor {

	private String _directory;
	private String _basename = Configuration.DEFAULT_SCRIPT_NAME;

	/**
	 * @param directory
	 * @param basename
	 *            base name of all related files
	 */
	public WholeStoryWriter(String directory, String basename) {
		_directory = directory;
		_basename = basename;
	}

	/**
	 * @param story
	 * @throws IOException
	 */
	@SuppressWarnings("nls")
	private void writeStory(Story story) throws IOException {
		File file = new File(_directory);
		file.mkdirs();
		file = new File(_directory + "/" + _basename);
		FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), story.getWholeStory());
	}

	@Override
	public final void processScript(Script script) throws IOException {
		writeStory(script);
	}

	@Override
	public final void processSubtitles(Subtitles transcript) throws IOException {
		writeStory(transcript);
	}

}
