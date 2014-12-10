package ist.ssa.content;

/**
 * A script line containing a character's name.
 */
public class CharacterNameLine extends Line {

    /**
     * @param text
     * @param lineNumber
     */
    public CharacterNameLine(String text, int lineNumber) {
        super(text, lineNumber, false);
    }
}
