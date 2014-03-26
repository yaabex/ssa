package ist.spln.stringmodifiers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import ist.spln.Main;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObjectWithMoreInfo;
import ist.spln.textanalysis.TextAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class Lemmatizer {

    public NeedlemanArrayValueObjectWithMoreInfo[] modify(List<String> text) { //should not have needleman stuff, but its faster this way... i think...
        TextAnalyzer textAnalyzer = new TextAnalyzer(Main.ANALYZER_PROPERTIES);

        List<NeedlemanArrayValueObjectWithMoreInfo> valueObjects = new ArrayList<>();
        for (int i = 0; i < text.size(); i++) {
            List<CoreMap> sentences = textAnalyzer.analyze(text.get(i));
            for (CoreMap sentence : sentences) {
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token : sentences.get(0).get(CoreAnnotations.TokensAnnotation.class)) {
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    if (lemma.length() == 1 && !Character.isLetterOrDigit(lemma.charAt(0))) {
                        continue;
                    }
                    valueObjects.add(new NeedlemanArrayValueObjectWithMoreInfo(lemma, i));
                }
            }
        }
        NeedlemanArrayValueObjectWithMoreInfo[] valueObjectsArray = new NeedlemanArrayValueObjectWithMoreInfo[valueObjects.size()];
        return valueObjects.toArray(valueObjectsArray);
    }
}
