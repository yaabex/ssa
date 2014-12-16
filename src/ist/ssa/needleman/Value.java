package ist.ssa.needleman;

public class Value {

	private int _value;
	private Direction _direction;

	public Value(int value, Direction direction) {
		_value = value;
		_direction = direction;
	}

	public int getValue() {
		return _value;
	}

	public Direction getDirection() {
		return _direction;
	}
}
