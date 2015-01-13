package ist.ssa.needleman;

/**
 * Score value and direction.
 */
public class Value {

	/** Value. */
	private int _value;
	
	/** Direction. */
	private Direction _direction;

	/**
	 * @param value
	 * @param direction
	 */
	public Value(int value, Direction direction) {
		_value = value;
		_direction = direction;
	}

	/**
	 * @return value
	 */
	public int value() {
		return _value;
	}

	/**
	 * @return direction
	 */
	public Direction direction() {
		return _direction;
	}
}
