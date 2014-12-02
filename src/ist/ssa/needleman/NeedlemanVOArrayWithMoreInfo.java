package ist.ssa.needleman;

import ist.ssa.textanalysis.SimpleNormalizer;

public class NeedlemanVOArrayWithMoreInfo extends NeedlemanVOArray {
    private int lineWhereItCameFrom; //so ugly...
    private String string;

    public NeedlemanVOArrayWithMoreInfo(String string, int lineWhereItCameFrom) {
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
    public boolean isEquivalentTo(NeedlemanVOArray vo) {
        SimpleNormalizer normalizer = new SimpleNormalizer();
        NeedlemanVOArrayWithMoreInfo otherObject = ((NeedlemanVOArrayWithMoreInfo)vo);
        if (normalizer.normPunctLCaseDMarks(this.string).equals(normalizer.normPunctLCaseDMarks(otherObject.getString()))) {
            return true;
        } /*else {
            if (this.getString().length() < 5) {
                return (new NeedlemanWunch()).run(toVOArray(this.getString()),
                        toVOArray(otherObject.getString()), 1, 0, 1).getRes() < 3;
            } else {
                return (new NeedlemanWunch()).run(toVOArray(this.getString()),
                        toVOArray(otherObject.getString()), 1, 0, 1).getRes() < 3;
            }

        }*/
        return false;
    }

    private MEDVO[] toVOArray(String s) {
        char[] chars = s.toCharArray();
        MEDVO[] medValueObjects = new MEDVO[chars.length];
        for (int i = 0; i < chars.length; i++) {
            medValueObjects[i] = new MEDVO(chars[i]);
        }
        return medValueObjects;
    }
}
