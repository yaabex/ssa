package ist.ssa.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Subtitles extends Story {
	private List<SubtitlesLine> _transcriptLines;

	public Subtitles(StoryVisitor reader) throws IOException {
		accept(reader);
	}

	public void reset() {
		super.reset();
		_transcriptLines = new ArrayList<SubtitlesLine>();
	}

	public List<SubtitlesLine> getDialogs() {
		return _transcriptLines;
	}

	/**
	 * @param uttLine
	 */
	public void addTranscriptLine(SubtitlesLine uttLine) {
		_transcriptLines.add(uttLine);
	}

	public Line getContextFromLineNumberOfWord(int i) {
		System.err.println("Subtitles.getContextFromLineNumberOfWord(" + i + "): "
				+ getStoryLines().size() + " " + _transcriptLines.get(i).getIndex());
		return getStoryLines().get(_transcriptLines.get(i).getIndex());
	}

	/**
	 * @see ist.ssa.content.Story#accept(ist.ssa.content.StoryVisitor)
	 */
	@Override
	public void accept(StoryVisitor reader) throws IOException {
		reader.processSubtitles(this);
	}

}
