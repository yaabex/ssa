package ist.ssa.needleman;

public class Value {

	private int value;
	private Direction direction;

	public Value(int value, Direction direction) {
		this.value = value;
		this.direction = direction;
	}

	public int getValue() {
		return value;
	}

	public Direction getDirection() {
		return direction;
	}
}
