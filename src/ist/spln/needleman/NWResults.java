package ist.spln.needleman;

import java.util.List;

public class NWResults {
    private List<ValueObjectPair> valueObjectPairs;
    private long res;

    public NWResults(List<ValueObjectPair> valueObjectPairs, long res) {
        this.valueObjectPairs = valueObjectPairs;
        this.res = res;
    }

    public List<ValueObjectPair> getValueObjectPairs() {
        return valueObjectPairs;
    }

    public long getRes() {
        return res;
    }
}
