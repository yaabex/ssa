package ist.spln.needleman.valueobject;

import org.joda.time.Interval;

public class TimeValueObject extends NeedlemanArrayValueObject {
    private int lineWhereItCameFrom;
    private Interval time;

    public TimeValueObject(int lineWhereItCameFrom, Interval time) {
        this.lineWhereItCameFrom = lineWhereItCameFrom;
        this.time = time;
    }

    @Override
    public boolean isEquivalentTo(NeedlemanArrayValueObject valueObject) {
        Interval otherTime = ((TimeValueObject) valueObject).getTime();
        Interval overlap = this.time.overlap(otherTime);
        double average = (this.time.toDurationMillis() + otherTime.toDurationMillis()) / 2d;
        if (overlap != null) {
            if (overlap.toDurationMillis() / average > 0.30) {
                return true;
            }
        }
        return false;
    }

    public Interval getTime() {
        return time;
    }

    public int getLineWhereItCameFrom() {
        return lineWhereItCameFrom;
    }
}
