package ist.spln.sentencealignment;

import ist.spln.needleman.ValueObjectPair;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObjectWithMoreInfo;
import ist.spln.needleman.valueobject.TimeValueObject;
import ist.spln.readers.script.ScriptLine;
import ist.spln.readers.script.SimpleScriptReader;
import ist.spln.readers.subtitle.SimpleSubtitleReader;
import ist.spln.readers.subtitle.SubtitleLine;
import ist.spln.stringmodifiers.Lemmatizer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Alignment {
    private List<List<Integer>> subAlign;
    private List<List<Integer>> scriptAlign;
    private List<List<Integer>> subAlignTime;
    private List<Integer> scriptAlignTime;
    private final static int THRESHOLD = 50;

    public void align(List<ValueObjectPair> valueObjectPairs, Lemmatizer lemmatizer, SimpleScriptReader scriptReader, SimpleSubtitleReader subtitleReader) {
        List<List<Integer>> subStuff = new ArrayList<>();
        List<List<Integer>> scriptStuff = new ArrayList<>();
        for (int i = 0; i < valueObjectPairs.size(); i++) {
            ValueObjectPair pair = valueObjectPairs.get(i);

            NeedlemanArrayValueObjectWithMoreInfo n1 = (NeedlemanArrayValueObjectWithMoreInfo) pair.getValueObject1();
            NeedlemanArrayValueObjectWithMoreInfo n2 = (NeedlemanArrayValueObjectWithMoreInfo) pair.getValueObject2();

            List<Integer> subLines = new ArrayList<>();
            List<Integer> scriptLines = new ArrayList<>();

            int subLine = n2.getLineWhereItCameFrom(); //horrible!
            int scriptLine = n1.getLineWhereItCameFrom();

            subLines.add(subLine);
            scriptLines.add(scriptLine);

            int matchedWords = 1;

            while (true) {
                i++;
                if (i < valueObjectPairs.size()) {
                    ValueObjectPair newPair = valueObjectPairs.get(i);

                    NeedlemanArrayValueObjectWithMoreInfo newN1 = (NeedlemanArrayValueObjectWithMoreInfo) newPair.getValueObject1();
                    NeedlemanArrayValueObjectWithMoreInfo newN2 = (NeedlemanArrayValueObjectWithMoreInfo) newPair.getValueObject2();

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
                        if (linesDoNotMatch(subLines, scriptLines, matchedWords, lemmatizer, scriptReader, subtitleReader)) {
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

    public void align(List<ValueObjectPair> valueObjectPairs) {
        List<List<Integer>> subStuff = new ArrayList<>();
        List<Integer> scriptStuff = new ArrayList<>();

        for (int i = 0; i < valueObjectPairs.size(); i++) {
            ValueObjectPair pair = valueObjectPairs.get(i);

            TimeValueObject n1 = (TimeValueObject) pair.getValueObject1();
            TimeValueObject n2 = (TimeValueObject) pair.getValueObject2();

            List<Integer> subLines = new ArrayList<>();
            int scriptLine = n1.getLineWhereItCameFrom();
            int subLine = n2.getLineWhereItCameFrom();
            subLines.add(subLine);

            while (true) {
                i++;
                if (i < valueObjectPairs.size()) {
                    ValueObjectPair newPair = valueObjectPairs.get(i);

                    TimeValueObject newN1 = (TimeValueObject) newPair.getValueObject1();
                    TimeValueObject newN2 = (TimeValueObject) newPair.getValueObject2();

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

    private boolean linesDoNotMatch(List<Integer> subLines, List<Integer> scriptLines, int matchedWords, Lemmatizer lemmatizer, SimpleScriptReader scriptReader, SimpleSubtitleReader subtitleReader) {
        List<String> subLinesLemmatized = lemmatizer.lemmatize(getTextFromLineNumber(subLines, subtitleReader));
        List<String> scriptLinesLemmatized = lemmatizer.lemmatize(getTextFromLineNumber(scriptLines, scriptReader));
        int minWords = Math.min(subLinesLemmatized.size(), scriptLinesLemmatized.size());
        return matchedWords/(float)minWords < THRESHOLD/100f;
    }

    private String getTextFromLineNumber(List<Integer> subLines, SimpleSubtitleReader subtitleReader) {
        String subLinesText = "";
        for (Integer line : subLines) {
            subLinesText += subtitleReader.getSubtitleLines().get(line).getLine() + " ";
        }
        return subLinesText;
    }

    private String getTextFromLineNumber(List<Integer> subLines, SimpleScriptReader scriptReader) {
        String scriptLinesText = "";
        for (Integer line : subLines) {
            scriptLinesText += scriptReader.getScriptDialogs().get(line).getLine() + " ";
        }
        return scriptLinesText;
    }

    public void enhanceScript(SimpleScriptReader scriptReader, SimpleSubtitleReader subtitleReader) {
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
                    scriptReader.getScriptDialogs().get(line).addTimeInfo(subtitleLine.getTimeInfo());
                }
                String startTime = subtitleReader.getSubtitleLines().get(subLines.get(0)).getTimeInfo().getStartTime();
                String endTime = subtitleReader.getSubtitleLines().get(subLines.get(subLines.size() - 1)).getTimeInfo().getEndTime();
                scriptReader.getWholeScript().get(scriptReader.getScriptDialogs().get(line).getLineNumber()).setLine("BT: " +
                        scriptReader.getContextFromLineNumberOfWord(line));

            }
        }
        System.out.println("Script Line Matches/Total Lines = " + lineMatches/(float)scriptReader.getScriptDialogs().size());
    }

    public void enhanceSubtitles(SimpleScriptReader scriptReader, SimpleSubtitleReader subtitleReader) {
        assert this.subAlign != null;
        assert this.scriptAlign != null;
        assert this.subAlign.size() == this.scriptAlign.size();

        int lineMatches = 0;

        for (int i = 0; i < subAlign.size(); i++) {
            List<Integer> subLines = subAlign.get(i);
            List<Integer> scriptLines = scriptAlign.get(i);
            for (Integer line : subLines) {
                lineMatches++;
                String characterName = scriptReader.getScriptDialogs().get(scriptLines.get(0)).getCharacterName();
                int realLineNumber = subtitleReader.getSubtitleLines().get(line).getLineNumber();
                subtitleReader.getWholeSubtitleFile().set(realLineNumber,
                        characterName + ":" + subtitleReader.getContextFromLineNumberOfWord(line));
            }
        }
        System.out.println("Sub Line Matches/Total Lines = " + lineMatches/(float)subtitleReader.getSubtitleLines().size());
    }

    public void translateScript(SimpleScriptReader scriptReader, SimpleSubtitleReader subtitleReader) {
        assert this.subAlignTime != null;
        assert this.scriptAlignTime != null;
        assert this.subAlignTime.size() == this.scriptAlignTime.size();

        for (int i = 0; i < subAlignTime.size(); i++) {
            List<String> strings = new ArrayList<>();
            for (Integer integer : subAlignTime.get(i)) {
                strings.add(subtitleReader.getSubtitleLines().get(integer).getLine());
            }
            int scriptLine = scriptAlignTime.get(i);
            ScriptLine line = scriptReader.getWholeScript().get(scriptReader.getScriptDialogs().get(scriptLine).getLineNumber());
            line.setLine(
                    StringUtils.join(strings, "\n").trim());
            line.setToTranslate(false);
        }
    }
}
