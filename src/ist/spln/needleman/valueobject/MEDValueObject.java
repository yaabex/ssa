package ist.spln.needleman.valueobject;

import ist.spln.needleman.NeedlemanWunch;

public class MEDValueObject extends NeedlemanArrayValueObject {
    private char c;

    public MEDValueObject(char c) {
        this.c = c;
    }

    public char getChar() {
        return this.c;
    }

    @Override
    public boolean isEquivalentTo(NeedlemanArrayValueObject valueObject) {
        return this.c == ((MEDValueObject)valueObject).getChar();
    }
}
