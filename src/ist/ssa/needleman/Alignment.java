package ist.ssa.needleman;

import ist.ssa.content.Line;
import ist.ssa.content.Script;
import ist.ssa.content.SimpleSubtitle;
import ist.ssa.content.Story;
import ist.ssa.content.SubtitleLine;
import ist.ssa.textanalysis.TextAnalyzer;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.util.StringUtils;

public class Alignment {
	private List<List<Integer>> _subAlign;
	private List<List<Integer>> _scriptAlign;
	private List<List<Integer>> _subAlignTime;
	private List<Integer> _scriptAlignTime;
	private final static int THRESHOLD = 50;

	public void align(List<VOPair> voPairs, TextAnalyzer analyzer, Script script,
			SimpleSubtitle subtitle) {
		List<List<Integer>> subStuff = new ArrayList<>();
		List<List<Integer>> scriptStuff = new ArrayList<>();
		for (int i = 0; i < voPairs.size(); i++) {
			VOPair pair = voPairs.get(i);

			NWVOArrayWithMoreInfo n1 = (NWVOArrayWithMoreInfo) pair.first();
			NWVOArrayWithMoreInfo n2 = (NWVOArrayWithMoreInfo) pair.second();

			List<Integer> subLines = new ArrayList<>();
			List<Integer> scriptLines = new ArrayList<>();

			int subLine = n2.getSourceLine(); // horrible!
			int scriptLine = n1.getSourceLine();

			subLines.add(subLine);
			scriptLines.add(scriptLine);

			int matchedWords = 1;

			while (true) {
				i++;
				if (i < voPairs.size()) {
					VOPair newPair = voPairs.get(i);

					NWVOArrayWithMoreInfo newN1 = (NWVOArrayWithMoreInfo) newPair.first();
					NWVOArrayWithMoreInfo newN2 = (NWVOArrayWithMoreInfo) newPair.second();

					int newSubLine = newN2.getSourceLine();
					int newScriptLine = newN1.getSourceLine();

					if (newScriptLine == scriptLine || newSubLine == subLine) {
						matchedWords++;
						if (!subLines.contains(newSubLine)) {
							subLines.add(newSubLine);
						}
						if (!scriptLines.contains(newScriptLine)) {
							scriptLines.add(newScriptLine);
						}
					} else {
						if (linesDoNotMatch(subLines, scriptLines, matchedWords, analyzer, script,
								subtitle)) {
							i--;
							break;
						} else {
							subStuff.add(subLines);
							scriptStuff.add(scriptLines);
							i--;
							break;
						}
					}
				} else {
					subStuff.add(subLines);
					scriptStuff.add(scriptLines);
					break;
				}
			}
		}
		_scriptAlign = scriptStuff;
		_subAlign = subStuff;
	}

	public void align(List<VOPair> voPairs) {
		List<List<Integer>> subStuff = new ArrayList<>();
		List<Integer> scriptStuff = new ArrayList<>();

		for (int i = 0; i < voPairs.size(); i++) {
			VOPair pair = voPairs.get(i);

			TimeVO n1 = (TimeVO) pair.first();
			TimeVO n2 = (TimeVO) pair.second();

			List<Integer> subLines = new ArrayList<>();
			int scriptLine = n1.getSourceLine();
			int subLine = n2.getSourceLine();
			subLines.add(subLine);

			while (true) {
				i++;
				if (i < voPairs.size()) {
					VOPair newPair = voPairs.get(i);

					TimeVO newN1 = (TimeVO) newPair.first();
					TimeVO newN2 = (TimeVO) newPair.second();

					int newSubLine = newN2.getSourceLine();
					int newScriptLine = newN1.getSourceLine();

					if (newScriptLine == scriptLine) {
						subLines.add(newSubLine);
					} else {
						subStuff.add(subLines);
						scriptStuff.add(scriptLine);
						i--;
						break;
					}
				} else {
					subStuff.add(subLines);
					scriptStuff.add(scriptLine);
					break;
				}
			}
		}
		_scriptAlignTime = scriptStuff;
		_subAlignTime = subStuff;
	}

	private boolean linesDoNotMatch(List<Integer> subLines, List<Integer> scriptLines,
			int matchedWords, TextAnalyzer analyzer, Script script, SimpleSubtitle subtitle) {
		List<String> subLinesLemmatized = analyzer.lemmatize(subtitle.getDialogTextAt(subLines));
		List<String> scriptLinesLemmatized = analyzer
				.lemmatize(script.getDialogTextAt(scriptLines));
		int minWords = Math.min(subLinesLemmatized.size(), scriptLinesLemmatized.size());
		return matchedWords / (float) minWords < THRESHOLD / 100f;
	}

	public void enhanceScript(Script script, SimpleSubtitle subtitle) {
		assert _subAlign != null;
		assert _scriptAlign != null;
		assert _subAlign.size() == _scriptAlign.size();

		int lineMatches = 0;
		for (int i = 0; i < _subAlign.size(); i++) {
			List<Integer> subLines = _subAlign.get(i);
			List<Integer> scriptLines = _scriptAlign.get(i);
			for (Integer line : scriptLines) {
				lineMatches++;
				for (Integer lineNumber : subLines) {
					SubtitleLine subtitleLine = subtitle.getDialogs().get(lineNumber);
					script.getDialogs().get(line).addTimeInfo(subtitleLine.getTimeInfo());
				}
			}
		}
		System.out.println("Lines aligned = " + lineMatches);
	}

	public void enhanceSubtitles(Script script, SimpleSubtitle subtitle) {
		assert _subAlign != null;
		assert _scriptAlign != null;
		assert _subAlign.size() == _scriptAlign.size();

		int lineMatches = 0;

		for (int i = 0; i < _subAlign.size(); i++) {
			List<Integer> subLines = _subAlign.get(i);
			List<Integer> scriptLines = _scriptAlign.get(i);
			for (Integer line : subLines) {
				lineMatches++;
				String characterName = script.getDialogs().get(scriptLines.get(0))
						.getCharacterName();
				int realLineNumber = subtitle.getDialogs().get(line).getLineNumber();
				subtitle.getStoryLines()
						.set(realLineNumber,
								subtitle.getContextFromLineNumberOfWord(line).prepend(
										characterName + ": "));
			}
		}

		System.out.println("Sub Line Matches/Total Lines = " + lineMatches
				/ (float) subtitle.getDialogs().size());
	}

	public void translateScript(Script script, SimpleSubtitle subtitle) {
		assert _subAlignTime != null;
		assert _scriptAlignTime != null;
		assert _subAlignTime.size() == _scriptAlignTime.size();

		System.out.println("Subtitle translated lines: " + _subAlignTime.size());
		System.out.println("Total lines in script: " + script.getDialogs().size());

		for (int i = 0; i < _subAlignTime.size(); i++) {
			List<String> strings = new ArrayList<>();
			for (Integer integer : _subAlignTime.get(i)) {
				strings.add(subtitle.getDialogs().get(integer).getText());
			}
			int scriptLine = _scriptAlignTime.get(i);

			Line line = script.getWholeStory().get(
					script.getDialogs().get(scriptLine).getLineNumber());
			line.setText(StringUtils.join(strings, "\n").trim());
			line.setTranslatable(false);
		}
	}
}
