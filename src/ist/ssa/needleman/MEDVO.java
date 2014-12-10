package ist.ssa.needleman;

public class MEDVO extends NWVOArray {
	private char _char;

	public MEDVO(char c) {
		_char = c;
	}

	public char getChar() {
		return _char;
	}

	@Override
	public boolean isEquivalentTo(NWVOArray vo) {
		return _char == ((MEDVO) vo).getChar();
	}
}
