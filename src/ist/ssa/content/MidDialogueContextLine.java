package ist.ssa.content;

/**
 * A script line containing mid-dialogue context.
 * Example from Her (2013): "(re: letter on his screen)".
 *
 * 				PAUL
 * 		Even more mesmerizing stuff today.
 *		(re: letter on his screen)
 *		Who knew you could rhyme so many
 *		words with the name Penelope?
 *		Badass.
 *
 */
public class MidDialogueContextLine extends Line {

    /**
     * @param text
     * @param lineNumber
     */
    public MidDialogueContextLine(String text, int lineNumber) {
        super(text, lineNumber, false);
    }
}
