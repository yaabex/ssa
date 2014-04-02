package ist.spln.needleman.valueobject;

import ist.spln.stringmodifiers.SimpleNormalizer;

public class NeedlemanArrayValueObject {
    private String string;

    public NeedlemanArrayValueObject(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public boolean isEquivalentTo(NeedlemanArrayValueObject valueObject) {
        SimpleNormalizer normalizer = new SimpleNormalizer();
        return normalizer.normPunctLCaseDMarks(this.string).equals(normalizer.normPunctLCaseDMarks(valueObject.getString()));
    }

    @Override
    public int hashCode() {
        return string != null ? string.hashCode() : 0;
    }
}
