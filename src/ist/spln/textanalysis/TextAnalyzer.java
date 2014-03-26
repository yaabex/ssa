package ist.spln.textanalysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

public class TextAnalyzer {
    private StanfordCoreNLP pipeline;

    public TextAnalyzer(String properties) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization
        Properties props = new Properties();
        props.put("annotators", properties);
        pipeline = new StanfordCoreNLP(props);
    }

    public List<CoreMap> analyze(String text) {

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        return document.get(CoreAnnotations.SentencesAnnotation.class);
    }
}
