package ist.spln.sentencealignment;

import ist.spln.needleman.ValueObjectPair;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObjectWithMoreInfo;
import ist.spln.readers.script.SimpleScriptReader;
import ist.spln.readers.subtitle.SimpleSubtitleReader;

import java.util.ArrayList;
import java.util.List;

public class Alignment {
    private List<List<Integer>> subAlign;
    private List<List<Integer>> scriptAlign;

    public void align(List<ValueObjectPair> valueObjectPairs) {
        List<List<Integer>> subStuff = new ArrayList<>();
        List<List<Integer>> scriptStuff = new ArrayList<>();
        for (int i = 0; i < valueObjectPairs.size(); i++) {
            ValueObjectPair pair = valueObjectPairs.get(i);

            NeedlemanArrayValueObjectWithMoreInfo n1 = (NeedlemanArrayValueObjectWithMoreInfo) pair.getValueObject1();
            NeedlemanArrayValueObjectWithMoreInfo n2 = (NeedlemanArrayValueObjectWithMoreInfo) pair.getValueObject2();

            List<Integer> subLines = new ArrayList<>();
            List<Integer> scriptLines = new ArrayList<>();

            int subLine = n2.getLineWhereItCameFrom();
            int scriptLine = n1.getLineWhereItCameFrom();

            subLines.add(subLine);
            scriptLines.add(scriptLine);

            while (true) {
                i++;
                if (i < valueObjectPairs.size()) {
                    ValueObjectPair newPair = valueObjectPairs.get(i);

                    NeedlemanArrayValueObjectWithMoreInfo newN1 = (NeedlemanArrayValueObjectWithMoreInfo) newPair.getValueObject1();
                    NeedlemanArrayValueObjectWithMoreInfo newN2 = (NeedlemanArrayValueObjectWithMoreInfo) newPair.getValueObject2();

                    int newSubLine = newN2.getLineWhereItCameFrom();
                    int newScriptLine = newN1.getLineWhereItCameFrom();

                    if (newScriptLine == scriptLine || newSubLine == subLine) {
                        if (!subLines.contains(newSubLine)) {
                            subLines.add(newSubLine);
                        }
                        if (!scriptLines.contains(newScriptLine)) {
                            scriptLines.add(newScriptLine);
                        }
                    } else {
                        subStuff.add(subLines);
                        scriptStuff.add(scriptLines);
                        i--;
                        break;
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

    public void enhanceScript(SimpleScriptReader scriptReader, SimpleSubtitleReader subtitleReader) {
        assert this.subAlign != null;
        assert this.scriptAlign != null;
        assert this.subAlign.size() == this.scriptAlign.size();

        for (int i = 0; i < subAlign.size(); i++) {
            List<Integer> subLines = subAlign.get(i);
            List<Integer> scriptLines = scriptAlign.get(i);
            for (Integer line : scriptLines) {
                String startTime = subtitleReader.getSubtitleLines().get(subLines.get(0)).getTimeInfo().getStartTime();
                String endTime = subtitleReader.getSubtitleLines().get(subLines.get(subLines.size() - 1)).getTimeInfo().getEndTime();
                scriptReader.getWholeScript().set(scriptReader.getScriptLines().get(line).getLineNumber(),
                        "<" + startTime + ">" +
                                scriptReader.getContextFromLineNumberOfWord(line) +
                                "<" + endTime + ">");
            }
        }
    }

    public void enhanceSubtitles(SimpleScriptReader scriptReader, SimpleSubtitleReader subtitleReader) {
        assert this.subAlign != null;
        assert this.scriptAlign != null;
        assert this.subAlign.size() == this.scriptAlign.size();

        for (int i = 0; i < subAlign.size(); i++) {
            List<Integer> subLines = subAlign.get(i);
            List<Integer> scriptLines = scriptAlign.get(i);
            for (Integer line : subLines) {
                String characterName = scriptReader.getScriptLines().get(scriptLines.get(0)).getCharacterName();
                int realLineNumber = subtitleReader.getSubtitleLines().get(line).getLineNumber();
                subtitleReader.getWholeSubtitleFile().set(realLineNumber,
                        characterName + ":" + subtitleReader.getContextFromLineNumberOfWord(line));
            }
        }
    }
}
