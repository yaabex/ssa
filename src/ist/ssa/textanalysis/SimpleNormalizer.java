package ist.ssa.textanalysis;

import java.text.Normalizer;

public class SimpleNormalizer {

    public String normPunctLCase(String words){
        return words.replaceAll("\\p{Punct}+", "").toLowerCase().trim();
    }

    public String normPunctLCaseDMarks(String words){
        return Normalizer.normalize(normPunctLCase(words), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public String normDMarks(String words){
        return Normalizer.normalize(words, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
