package ist.spln.needleman.valueobject;

public class NeedlemanArrayValueObjectWithMoreInfo extends NeedlemanArrayValueObject {
    private int lineWhereItCameFrom; //so ugly...

    public NeedlemanArrayValueObjectWithMoreInfo(String string, int lineWhereItCameFrom) {
        super(string);
        this.lineWhereItCameFrom = lineWhereItCameFrom;
    }

    public int getLineWhereItCameFrom() {
        return lineWhereItCameFrom;
    }
}
