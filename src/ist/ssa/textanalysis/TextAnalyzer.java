package ist.ssa.textanalysis;

import ist.ssa.content.Story;
import ist.ssa.needleman.Span;
import ist.ssa.needleman.TextSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TextAnalyzer {
	private StanfordCoreNLP _pipeline;

	// creates a StanfordCoreNLP object, with POS tagging, lemmatization
	public TextAnalyzer(String properties) {
		Properties props = new Properties();
		props.put("annotators", properties);
		_pipeline = new StanfordCoreNLP(props);
	}

	public List<CoreMap> analyze(String text) {
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		_pipeline.annotate(document);
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		return document.get(CoreAnnotations.SentencesAnnotation.class);
	}

	/**
	 * @param text
	 * @return lemmatized text
	 */
	public List<String> lemmatize(String text) {
		List<String> lemmas = new ArrayList<>();
		List<CoreMap> sentences = analyze(text);
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
				if (!lemma.matches(".*\\p{L}.*")) { // \p{L} = any letter
					continue;
				}
				lemmas.add(lemma.toLowerCase());
			}
		}
		return lemmas;
	}

	/**
	 * should not have Needleman stuff, but its faster this way... i think...
	 * 
	 * @param story
	 * @return spans array
	 */
	public Span[] modify(Story story) {
		List<Span> spans = new ArrayList<>();
		for (int i = 0; i < story.size(); i++) {
			List<CoreMap> sentences = analyze(story.getLine(i).getText());
			for (CoreMap sentence : sentences) {
				// traversing the words in the current sentence
				// a CoreLabel is a CoreMap with additional token-specific
				// methods
				for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
					String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
					if (!lemma.matches(".*\\p{L}.*")) {
						// \p{L} = any letter
						continue;
					}
					spans.add(new TextSpan(i, lemma.toLowerCase()));
				}
			}
		}
		return spans.toArray(new Span[spans.size()]);
	}

}
