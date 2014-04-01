package ist.spln;

import ist.spln.needleman.NeedlemanWunch;
import ist.spln.needleman.ValueObjectPair;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObjectWithMoreInfo;
import ist.spln.readers.Reader;
import ist.spln.readers.script.SimpleScriptReader;
import ist.spln.readers.subtitle.SimpleSubtitleReader;
import ist.spln.stringmodifiers.Lemmatizer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String ANALYZER_PROPERTIES = "tokenize, ssplit, pos, lemma";

    public static void main(String[] args) {
        SimpleScriptReader scriptReader = new SimpleScriptReader();
        SimpleSubtitleReader subtitleReader = new SimpleSubtitleReader();
        try {
            scriptReader.read();
            subtitleReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Lemmatizer lemmatizer = new Lemmatizer();
        NeedlemanWunch needlemanWunch = new NeedlemanWunch();
        List<ValueObjectPair> valueObjectPairs  = needlemanWunch.run(lemmatizer.modify(scriptReader.getTextList()),
                lemmatizer.modify(subtitleReader.getTextList()));

        for (ValueObjectPair pair : valueObjectPairs) {
            NeedlemanArrayValueObjectWithMoreInfo n1 = (NeedlemanArrayValueObjectWithMoreInfo) pair.getValueObject1();
            NeedlemanArrayValueObjectWithMoreInfo n2 = (NeedlemanArrayValueObjectWithMoreInfo) pair.getValueObject2();
            if (n1 != null) {
                System.out.println("1 - " + n1.getString() + " - " + scriptReader.getWholeScript().get(scriptReader.getScriptLines().get(n1.getLineWhereItCameFrom()).getLineNumber()));
            }
            if (n2 != null) {
                System.out.println("2 - " + n2.getString() + " - " + subtitleReader.getSubtitleLines().get(n2.getLineWhereItCameFrom()).getLine());
            }
            System.out.println();

        }
    }
}