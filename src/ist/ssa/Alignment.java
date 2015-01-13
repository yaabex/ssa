package ist.ssa;

import ist.ssa.content.Line;
import ist.ssa.content.Script;
import ist.ssa.content.Subtitles;
import ist.ssa.content.SubtitlesLine;
import ist.ssa.needleman.Pair;
import ist.ssa.needleman.TextSpan;
import ist.ssa.needleman.TimeSpan;
import ist.ssa.textanalysis.TextAnalyzer;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.util.StringUtils;

public class Alignment {

	/** Textual alignment indices (script). */
	private List<List<Integer>> _scriptAlign;

	/** Textual alignment indices (subtitles). */
	private List<List<Integer>> _subtitlesAlign;

	/** Time alignment indices (script). */
	private List<Integer> _scriptAlignTime;

	/** Time alignment indices (subtitles). */
	private List<List<Integer>> _subtitlesAlignTime;

	private final static int THRESHOLD = 50;

	/**
	 * @param pairs
	 * @param analyzer
	 * @param script
	 * @param transcript
	 */
	public void alignTextSpans(List<Pair> pairs, TextAnalyzer analyzer, Script script,
			Subtitles transcript) {
		_scriptAlign = new ArrayList<>();
		_subtitlesAlign = new ArrayList<>();

		System.err.println("Alignment.alignTextSpans: size is " + pairs.size()); //$NON-NLS-1$

		for (int i = 0; i < pairs.size(); i++) {
			System.err.println("Alignment.alignTextSpans: pair " + i); //$NON-NLS-1$

			TextSpan span1 = (TextSpan) pairs.get(i).first();
			TextSpan span2 = (TextSpan) pairs.get(i).second();

			List<Integer> scriptLines = new ArrayList<>();
			List<Integer> subtitlesLines = new ArrayList<>();

			int scriptLine = span1.getLine();
			int subtitlesLine = span2.getLine(); // horrible!

			scriptLines.add(scriptLine);
			subtitlesLines.add(subtitlesLine);

			int matchedWords = 1;

			while (true) {
				i++;
				if (i < pairs.size()) {
					TextSpan newN1 = (TextSpan) pairs.get(i).first();
					TextSpan newN2 = (TextSpan) pairs.get(i).second();

					int newScriptLine = newN1.getLine();
					int newSubtitlesLine = newN2.getLine();

					if (newScriptLine == scriptLine || newSubtitlesLine == subtitlesLine) {
						matchedWords++;
						if (!scriptLines.contains(newScriptLine))
							scriptLines.add(newScriptLine);
						if (!subtitlesLines.contains(newSubtitlesLine))
							subtitlesLines.add(newSubtitlesLine);
					} else {
						if (linesDoNotMatch(scriptLines, subtitlesLines, matchedWords, analyzer,
								script, transcript)) {
							i--;
							break;
						}
						// lines match
						_scriptAlign.add(scriptLines);
						_subtitlesAlign.add(subtitlesLines);
						i--;
						break;
					}
				} else {
					_scriptAlign.add(scriptLines);
					_subtitlesAlign.add(subtitlesLines);
					break;
				}
			}
		}
	}

	/**
	 * @param pairs
	 */
	public void alignTimeSpans(List<Pair> pairs) {
		_scriptAlignTime = new ArrayList<>();
		_subtitlesAlignTime = new ArrayList<>();

		System.err.println("Alignment.alignTimeSpans: size is " + pairs.size()); //$NON-NLS-1$

		for (int i = 0; i < pairs.size(); i++) {
			System.err.println("Alignment.alignTimeSpans: pair " + i); //$NON-NLS-1$

			TimeSpan n1 = (TimeSpan) pairs.get(i).first();
			TimeSpan n2 = (TimeSpan) pairs.get(i).second();

			List<Integer> transcriptLines = new ArrayList<>();

			int scriptLine = n1.getLine();
			int transcriptLine = n2.getLine();

			transcriptLines.add(transcriptLine);

			while (true) {
				i++;
				if (i < pairs.size()) {
					TimeSpan newN1 = (TimeSpan) pairs.get(i).first();
					TimeSpan newN2 = (TimeSpan) pairs.get(i).second();

					int newScriptLine = newN1.getLine();
					int newTranscriptLine = newN2.getLine();

					if (newScriptLine == scriptLine) {
						transcriptLines.add(newTranscriptLine);
					} else {
						_scriptAlignTime.add(scriptLine);
						_subtitlesAlignTime.add(transcriptLines);
						i--;
						break;
					}
				} else {
					_scriptAlignTime.add(scriptLine);
					_subtitlesAlignTime.add(transcriptLines);
					break;
				}
			}
		}
	}

	/**
	 * @param scriptLines
	 * @param subtitlesLines
	 * @param matchedWords
	 * @param analyzer
	 * @param script
	 * @param subtitles
	 * @return decision
	 */
	private boolean linesDoNotMatch(List<Integer> scriptLines, List<Integer> subtitlesLines,
			int matchedWords, TextAnalyzer analyzer, Script script, Subtitles subtitles) {
		List<String> lemmatizedScript = analyzer.lemmatize(script.getDialogTextAt(scriptLines));
		List<String> lemmatizedTranscript = analyzer.lemmatize(subtitles
				.getDialogTextAt(subtitlesLines));
		int minWords = Math.min(lemmatizedTranscript.size(), lemmatizedScript.size());
		return matchedWords / (float) minWords < THRESHOLD / 100f;
	}

	/**
	 * @param script
	 * @param subtitles
	 */
	public void enhanceScript(Script script, Subtitles subtitles) {
		assert _scriptAlign != null && _subtitlesAlign != null;
		assert _subtitlesAlign.size() == _scriptAlign.size();

		int matches = 0;
		for (int i = 0; i < _subtitlesAlign.size(); i++) {
			List<Integer> scriptLines = _scriptAlign.get(i);
			List<Integer> subtitlesLines = _subtitlesAlign.get(i);
			for (Integer scriptLine : scriptLines) {
				matches++;
				for (Integer subtitlesLine : subtitlesLines) {
					SubtitlesLine subtitleLine = subtitles.getDialogs().get(subtitlesLine);
					script.getDialogs().get(scriptLine).addTimeInfo(subtitleLine.getTimeInfo());
				}
			}
		}
		System.err.println("Lines aligned = " + matches); //$NON-NLS-1$
	}

	/**
	 * @param script
	 * @param subtitles
	 */
	@SuppressWarnings("nls")
	public void enhanceSubtitles(Script script, Subtitles subtitles) {
		assert _scriptAlign != null && _subtitlesAlign != null;
		assert _subtitlesAlign.size() == _scriptAlign.size();

		int matches = 0;
		for (int i = 0; i < _subtitlesAlign.size(); i++) {
			List<Integer> scriptLines = _scriptAlign.get(i);
			List<Integer> subtitlesLines = _subtitlesAlign.get(i);
			for (Integer subtitlesLine : subtitlesLines) {
				matches++;
				String characterName = script.getDialogs().get(scriptLines.get(0))
						.getCharacterName();
				int realLineNumber = subtitles.getDialogs().get(subtitlesLine).getIndex();
				subtitles.getStoryLines().set(
						realLineNumber,
						subtitles.getContextFromLineNumberOfWord(subtitlesLine).prepend(
								characterName + ": "));
			}
		}

		System.out.println("Sub Line Matches/Total Lines = " + matches
				/ (float) subtitles.getDialogs().size());
	}

	/**
	 * @param script
	 * @param subtitle
	 */
	@SuppressWarnings("nls")
	public void translateScript(Script script, Subtitles subtitle) {
		assert _scriptAlignTime != null && _subtitlesAlignTime != null;
		assert _subtitlesAlignTime.size() == _scriptAlignTime.size();

		System.err.println("Subtitle translated lines: " + _subtitlesAlignTime.size()); //$NON-NLS-1$
		System.err.println("Total lines in script: " + script.getDialogs().size()); //$NON-NLS-1$

		for (int i = 0; i < _subtitlesAlignTime.size(); i++) {
			List<String> strings = new ArrayList<>();
			for (Integer index : _subtitlesAlignTime.get(i))
				strings.add(subtitle.getDialogs().get(index).getText());

			int scriptLine = _scriptAlignTime.get(i);
			Line line = script.getWholeStory().get(script.getDialogs().get(scriptLine).getIndex());
			line.setText(StringUtils.join(strings, "\n").trim());
			line.setTranslatable(false);
		}
	}
}
