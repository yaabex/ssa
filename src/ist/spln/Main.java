package ist.spln;

import ist.spln.needleman.NeedlemanWunch;
import ist.spln.needleman.ValueObjectPair;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObjectWithMoreInfo;
import ist.spln.readers.Reader;
import ist.spln.readers.script.SimpleScriptReader;
import ist.spln.readers.subtitle.SimpleSubtitleReader;
import ist.spln.sentencealignment.Alignment;
import ist.spln.stringmodifiers.Lemmatizer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String ANALYZER_PROPERTIES = "tokenize, ssplit, pos, lemma";

    public static void main(String[] args) {
        SimpleScriptReader scriptReader = new SimpleScriptReader(); //TODO make a Script and a Subtitle object
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

        Alignment alignment = new Alignment();
        alignment.align(valueObjectPairs);
        alignment.enhanceScript(scriptReader, subtitleReader);
        alignment.enhanceSubtitles(scriptReader, subtitleReader);

        scriptReader.printWholeScript();
        subtitleReader.printWholeSubtitleFile();
    }
}