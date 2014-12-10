package ist.ssa.textanalysis;

public class TextNormalizer {

	public static String normPunctLCase(String words) {
		return words.replaceAll("\\p{Punct}+", "").toLowerCase().trim();
	}

	public static String normPunctLCaseDMarks(String words) {
		return java.text.Normalizer.normalize(normPunctLCase(words), java.text.Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String normDMarks(String words) {
		return java.text.Normalizer.normalize(words, java.text.Normalizer.Form.NFD).replaceAll(
				"\\p{InCombiningDiacriticalMarks}+", "");
	}
}
