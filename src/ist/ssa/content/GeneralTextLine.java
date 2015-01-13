package ist.ssa.content;

/**
 * A script line containing scene descriptions (in general, any line not
 * containing names or dialog text).
 */
public class GeneralTextLine extends Line {

	/**
	 * @param text
	 * @param lineNumber
	 */
	public GeneralTextLine(String text, int lineNumber) {
		super(text, lineNumber, true);
	}
}
