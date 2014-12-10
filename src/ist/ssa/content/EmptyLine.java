package ist.ssa.content;

/**
 * A script line containing no text (used to maintain the original structure).
 */
public class EmptyLine extends Line {

	/**
	 * @param text
	 * @param lineNumber
	 */
	public EmptyLine(String text, int lineNumber) {
		super(text, lineNumber, false);
	}
}
