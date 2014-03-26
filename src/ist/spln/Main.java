package ist.spln;

import ist.spln.needleman.NeedlemanWunch;
import ist.spln.readers.Reader;
import ist.spln.readers.script.SimpleScriptReader;
import ist.spln.readers.subtitle.SimpleSubtitleReader;
import ist.spln.stringmodifiers.Lemmatizer;

import java.io.IOException;

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
        System.out.println(lemmatizer.modify(scriptReader.getTextList()).length);
        System.out.println(lemmatizer.modify(subtitleReader.getTextList()).length);
        //needlemanWunch.run(lemmatizer.modify(scriptReader.getTextList()), lemmatizer.modify(subtitleReader.getTextList()));
    }
}