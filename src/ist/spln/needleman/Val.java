package ist.spln.needleman;

public class Val {

	private int value;
	private Dir dir;

	public Val(int value, Dir dir) {
		this.value = value;
		this.dir = dir;
	}

	public int getValue() {
		return value;
	}

	public Dir getDir() {
		return dir;
	}
}
