package ist.spln.needleman.valueobject;

public class NeedlemanArrayValueObject {
    private String string;

    public NeedlemanArrayValueObject(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NeedlemanArrayValueObject that = (NeedlemanArrayValueObject) o;

        if (string != null ? !string.equals(that.string) : that.string != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return string != null ? string.hashCode() : 0;
    }
}
