package ist.ssa.needleman;

import ist.ssa.readers.ScriptLine;
import ist.ssa.readers.SimpleScript;
import ist.ssa.readers.SimpleSRT;
import ist.ssa.readers.SubtitleLine;
import ist.ssa.textanalysis.Lemmatizer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Alignment {
	private List<List<Integer>> subAlign;
	private List<List<Integer>> scriptAlign;
	private List<List<Integer>> subAlignTime;
	private List<Integer> scriptAlignTime;
	private final static int THRESHOLD = 50;

	public void align(List<VOPair> valueObjectPairs, Lemmatizer lemmatizer,
			SimpleScript scriptReader, SimpleSRT subtitleReader) {
		List<List<Integer>> subStuff = new ArrayList<>();
		List<List<Integer>> scriptStuff = new ArrayList<>();
		for (int i = 0; i < valueObjectPairs.size(); i++) {
			VOPair pair = valueObjectPairs.get(i);

			NeedlemanVOArrayWithMoreInfo n1 = (NeedlemanVOArrayWithMoreInfo) pair
					.first();
			NeedlemanVOArrayWithMoreInfo n2 = (NeedlemanVOArrayWithMoreInfo) pair
					.second();

			List<Integer> subLines = new ArrayList<>();
			List<Integer> scriptLines = new ArrayList<>();

			int subLine = n2.getLineWhereItCameFrom(); // horrible!
			int scriptLine = n1.getLineWhereItCameFrom();

			subLines.add(subLine);
			scriptLines.add(scriptLine);

			int matchedWords = 1;

			while (true) {
				i++;
				if (i < valueObjectPairs.size()) {
					VOPair newPair = valueObjectPairs.get(i);

					NeedlemanVOArrayWithMoreInfo newN1 = (NeedlemanVOArrayWithMoreInfo) newPair
							.first();
					NeedlemanVOArrayWithMoreInfo newN2 = (NeedlemanVOArrayWithMoreInfo) newPair
							.second();

					int newSubLine = newN2.getLineWhereItCameFrom();
					int newScriptLine = newN1.getLineWhereItCameFrom();

					if (newScriptLine == scriptLine || newSubLine == subLine) {
						matchedWords++;
						if (!subLines.contains(newSubLine)) {
							subLines.add(newSubLine);
						}
						if (!scriptLines.contains(newScriptLine)) {
							scriptLines.add(newScriptLine);
						}
					} else {
						if (linesDoNotMatch(subLines, scriptLines, matchedWords, lemmatizer,
								scriptReader, subtitleReader)) {
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
		this.scriptAlign = scriptStuff;
		this.subAlign = subStuff;
	}

	public void align(List<VOPair> valueObjectPairs) {
		List<List<Integer>> subStuff = new ArrayList<>();
		List<Integer> scriptStuff = new ArrayList<>();

		for (int i = 0; i < valueObjectPairs.size(); i++) {
			VOPair pair = valueObjectPairs.get(i);

			TimeVO n1 = (TimeVO) pair.first();
			TimeVO n2 = (TimeVO) pair.second();

			List<Integer> subLines = new ArrayList<>();
			int scriptLine = n1.getLineWhereItCameFrom();
			int subLine = n2.getLineWhereItCameFrom();
			subLines.add(subLine);

			while (true) {
				i++;
				if (i < valueObjectPairs.size()) {
					VOPair newPair = valueObjectPairs.get(i);

					TimeVO newN1 = (TimeVO) newPair.first();
					TimeVO newN2 = (TimeVO) newPair.second();

					int newSubLine = newN2.getLineWhereItCameFrom();
					int newScriptLine = newN1.getLineWhereItCameFrom();

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
		this.scriptAlignTime = scriptStuff;
		this.subAlignTime = subStuff;
	}

	private boolean linesDoNotMatch(List<Integer> subLines, List<Integer> scriptLines,
			int matchedWords, Lemmatizer lemmatizer, SimpleScript scriptReader,
			SimpleSRT subtitleReader) {
		List<String> subLinesLemmatized = lemmatizer.lemmatize(getTextFromLineNumber(subLines,
				subtitleReader));
		List<String> scriptLinesLemmatized = lemmatizer.lemmatize(getTextFromLineNumber(
				scriptLines, scriptReader));
		int minWords = Math.min(subLinesLemmatized.size(), scriptLinesLemmatized.size());
		return matchedWords / (float) minWords < THRESHOLD / 100f;
	}

	private String getTextFromLineNumber(List<Integer> subLines, SimpleSRT subtitleReader) {
		String subLinesText = "";
		for (Integer line : subLines) {
			subLinesText += subtitleReader.getSubtitleLines().get(line).getText() + " ";
		}
		return subLinesText;
	}

	private String getTextFromLineNumber(List<Integer> subLines, SimpleScript scriptReader) {
		String scriptLinesText = "";
		for (Integer line : subLines) {
			scriptLinesText += scriptReader.getScriptDialogs().get(line).getText() + " ";
		}
		return scriptLinesText;
	}

	public void enhanceScript(SimpleScript scriptReader, SimpleSRT subtitleReader) {
		assert this.subAlign != null;
		assert this.scriptAlign != null;
		assert this.subAlign.size() == this.scriptAlign.size();

		int lineMatches = 0;
		for (int i = 0; i < subAlign.size(); i++) {
			List<Integer> subLines = subAlign.get(i);
			List<Integer> scriptLines = scriptAlign.get(i);
			for (Integer line : scriptLines) {
				lineMatches++;
				for (Integer lineNumber : subLines) {
					SubtitleLine subtitleLine = subtitleReader.getSubtitleLines().get(lineNumber);
					scriptReader.getScriptDialogs().get(line)
							.addTimeInfo(subtitleLine.getTimeInfo());
				}
			}
		}
		System.out.println("Lines aligned = " + lineMatches);
	}

	public void enhanceSubtitles(SimpleScript scriptReader,
			SimpleSRT subtitleReader) {
		assert this.subAlign != null;
		assert this.scriptAlign != null;
		assert this.subAlign.size() == this.scriptAlign.size();

		int lineMatches = 0;

		for (int i = 0; i < subAlign.size(); i++) {
			List<Integer> subLines = subAlign.get(i);
			List<Integer> scriptLines = scriptAlign.get(i);
			for (Integer line : subLines) {
				lineMatches++;
				String characterName = scriptReader.getScriptDialogs().get(scriptLines.get(0))
						.getCharacterName();
				int realLineNumber = subtitleReader.getSubtitleLines().get(line).getLineNumber();
				subtitleReader.getWholeSubtitleFile().set(realLineNumber,
						characterName + ":" + subtitleReader.getContextFromLineNumberOfWord(line));
			}
		}
		// System.out.println("Sub Line Matches/Total Lines = " +
		// lineMatches/(float)subtitleReader.getSubtitleLines().size());
	}

	public void translateScript(SimpleScript scriptReader, SimpleSRT subtitleReader) {
		assert this.subAlignTime != null;
		assert this.scriptAlignTime != null;
		assert this.subAlignTime.size() == this.scriptAlignTime.size();

		System.out.println("Subtitle translated lines: " + subAlignTime.size());
		System.out.println("Total lines in script: " + scriptReader.getScriptDialogs().size());

		for (int i = 0; i < subAlignTime.size(); i++) {
			List<String> strings = new ArrayList<>();
			for (Integer integer : subAlignTime.get(i)) {
				strings.add(subtitleReader.getSubtitleLines().get(integer).getText());
			}
			int scriptLine = scriptAlignTime.get(i);
			ScriptLine line = scriptReader.getWholeScript().get(
					scriptReader.getScriptDialogs().get(scriptLine).getLineNumber());
			line.setText(StringUtils.join(strings, "\n").trim());
			line.setToTranslate(false);
		}
	}
}
