package ist.ssa.textanalysis;

import ist.ssa.Configuration;
import ist.ssa.needleman.NeedlemanVOArray;
import ist.ssa.needleman.NeedlemanVOArrayWithMoreInfo;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

public class Lemmatizer {
	TextAnalyzer textAnalyzer;

	public Lemmatizer() {
		this.textAnalyzer = new TextAnalyzer(Configuration.ANALYZER_PROPERTIES);
	}

	public List<String> lemmatize(String text) {
		List<String> lemmas = new ArrayList<>();
		List<CoreMap> sentences = this.textAnalyzer.analyze(text);
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

	// should not have needleman stuff, but its faster this way... i think...
	public NeedlemanVOArray[] modify(List<String> text) {
		List<NeedlemanVOArray> valueObjects = new ArrayList<>();
		for (int i = 0; i < text.size(); i++) {
			List<CoreMap> sentences = this.textAnalyzer.analyze(text.get(i));
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
					valueObjects.add(new NeedlemanVOArrayWithMoreInfo(lemma.toLowerCase(),
							i));
				}
			}
		}
		NeedlemanVOArray[] valueObjectsArray = new NeedlemanVOArray[valueObjects
				.size()];
		return valueObjects.toArray(valueObjectsArray);
	}
}
