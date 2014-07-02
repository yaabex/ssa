package ist.spln.needleman.valueobject;

import ist.spln.needleman.NeedlemanWunch;
import ist.spln.stringmodifiers.SimpleNormalizer;

public class NeedlemanArrayValueObjectWithMoreInfo extends NeedlemanArrayValueObject {
    private int lineWhereItCameFrom; //so ugly...
    private String string;

    public NeedlemanArrayValueObjectWithMoreInfo(String string, int lineWhereItCameFrom) {
        this.string = string;
        this.lineWhereItCameFrom = lineWhereItCameFrom;
    }

    public int getLineWhereItCameFrom() {
        return lineWhereItCameFrom;
    }

    public String getString() {
        return string;
    }

    @Override
    public boolean isEquivalentTo(NeedlemanArrayValueObject valueObject) {
        SimpleNormalizer normalizer = new SimpleNormalizer();
        NeedlemanArrayValueObjectWithMoreInfo otherObject = ((NeedlemanArrayValueObjectWithMoreInfo)valueObject);
        if (normalizer.normPunctLCaseDMarks(this.string).equals(normalizer.normPunctLCaseDMarks(otherObject.getString()))) {
            return true;
        } else {
            if (this.getString().length() < 5) {
                /*return (new NeedlemanWunch()).run(toValueObjectArray(this.getString()),
                        toValueObjectArray(otherObject.getString()), 1, 0, 1).getRes() < 2;*/
                return false;
            } else {
                return (new NeedlemanWunch()).run(toValueObjectArray(this.getString()),
                        toValueObjectArray(otherObject.getString()), 1, 0, 1).getRes() < 3;
            }
        }
    }

    private MEDValueObject[] toValueObjectArray(String s) {
        char[] chars = s.toCharArray();
        MEDValueObject[] medValueObjects = new MEDValueObject[chars.length];
        for (int i = 0; i < chars.length; i++) {
            medValueObjects[i] = new MEDValueObject(chars[i]);
        }
        return medValueObjects;
    }
}
