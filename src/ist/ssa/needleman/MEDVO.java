package ist.ssa.needleman;

public class MEDVO extends NeedlemanVOArray {
	private char c;

	public MEDVO(char c) {
		this.c = c;
	}

	public char getChar() {
		return this.c;
	}

	@Override
	public boolean isEquivalentTo(NeedlemanVOArray valueObject) {
		return this.c == ((MEDVO) valueObject).getChar();
	}
}
