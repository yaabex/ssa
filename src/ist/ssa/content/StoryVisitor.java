/**
 * 
 */
package ist.ssa.content;

import java.io.IOException;

/**
 * 
 */
public abstract class StoryVisitor {

	/**
	 * @param script
	 * @throws IOException
	 */
	public void processScript(Script script) throws IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param subtitles
	 * @throws IOException
	 */
	public void processSubtitles(Subtitles subtitles) throws IOException {
		throw new UnsupportedOperationException();
	}

}
