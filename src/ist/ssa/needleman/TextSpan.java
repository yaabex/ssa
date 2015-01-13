package ist.ssa.needleman;

import ist.ssa.textanalysis.TextNormalizer;

/**
 * Source text spans.
 */
public class TextSpan extends Span {
	/** Text in this span. */
	private String _text;

	/**
	 * @param line
	 * @param text
	 */
	public TextSpan(int line, String text) {
		super(line);
		_text = text;
	}

	/**
	 * @return the text in the span.
	 */
	public String getText() {
		return _text;
	}

	/**
	 * Two text spans match if they are equal after normalization.
	 * 
	 * @see ist.ssa.needleman.Span#matches(ist.ssa.needleman.Span)
	 */
	@Override
	public boolean matches(Span span) {
		if (span instanceof TextSpan) {
			TextSpan other = ((TextSpan) span);
			if (TextNormalizer.normPunctLCaseDMarks(_text).equals(
					TextNormalizer.normPunctLCaseDMarks(other.getText()))) {
				return true;
			}
		}
		return false;
	}

}
