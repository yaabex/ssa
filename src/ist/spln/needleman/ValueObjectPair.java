package ist.spln.needleman;

import ist.spln.needleman.valueobject.NeedlemanArrayValueObject;

public class ValueObjectPair {
    private NeedlemanArrayValueObject valueObject1;
    private NeedlemanArrayValueObject valueObject2;

    public ValueObjectPair(NeedlemanArrayValueObject valueObject1, NeedlemanArrayValueObject valueObject2) {
        this.valueObject1 = valueObject1;
        this.valueObject2 = valueObject2;
    }

    public NeedlemanArrayValueObject getValueObject1() {
        return valueObject1;
    }

    public NeedlemanArrayValueObject getValueObject2() {
        return valueObject2;
    }
}
