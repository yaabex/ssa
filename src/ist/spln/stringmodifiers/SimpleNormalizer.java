package ist.spln.stringmodifiers;

import java.text.Normalizer;

public class SimpleNormalizer {

    /**
     * contains:
     * 	-remove all '!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~'  = \p{Punct}+
     *  -lowercase
     *  -trim
     *
     * @param words: single word or sentence
     * @return single word or sentence normalized
     */
    public String normPunctLCase(String words){
        return words.replaceAll("\\p{Punct}+", "").toLowerCase().trim();
    }

    /**
     * contains:
     * 	-remove all !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~  = \p{Punct}+
     *  -lowercase
     *  -trim
     *  -remove all diacritical marks (´`~^, etc)
     *
     * @param words
     * @return
     */
    public String normPunctLCaseDMarks(String words){
        return Normalizer.normalize(normPunctLCase(words), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * remove all diacritical marks (´`~^, etc)
     * @param words
     * @return
     */
    public String normDMarks(String words){
        return Normalizer.normalize(words, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
