package ist.spln;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import ist.spln.needleman.NeedlemanWunch;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObject;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObjectWithMoreInfo;
import ist.spln.readers.Reader;
import ist.spln.readers.script.SimpleScriptReader;
import ist.spln.readers.subtitle.SimpleSubtitleReader;
import ist.spln.stringmodifiers.Lemmatizer;
import ist.spln.textanalysis.TextAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String ANALYZER_PROPERTIES = "tokenize, ssplit, pos, lemma";

    public static void main(String[] args) {
        Reader scriptReader = new SimpleScriptReader();
        Reader subtitleReader = new SimpleSubtitleReader();
        try {
            scriptReader.read();
            subtitleReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Lemmatizer lemmatizer = new Lemmatizer();
        NeedlemanWunch needlemanWunch = new NeedlemanWunch();
        needlemanWunch.run(lemmatizer.modify(scriptReader.getTextList()), lemmatizer.modify(subtitleReader.getTextList()));
    }
}