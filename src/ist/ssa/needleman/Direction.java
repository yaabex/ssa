package ist.ssa.needleman;

public class Direction {
	private final boolean up;
	private final boolean left;
	private final boolean corner;

	Direction(boolean up, boolean left, boolean corner) {
		this.up = up;
		this.left = left;
		this.corner = corner;
	}

	public String[] getStrings(NeedlemanVOArrayWithMoreInfo[] s1,
			NeedlemanVOArrayWithMoreInfo[] s2, int i, int j) {
		String[] strings = new String[2];
		if (this.up) {
			strings[0] = s1[i].getString();
			strings[1] = "_";
		} else if (this.left) {
			strings[0] = "_";
			strings[1] = s2[j].getString();
		} else if (this.corner) {
			strings[0] = s1[i].getString();
			strings[1] = s2[j].getString();
		}
		return strings;
	}

	public boolean isUp() {
		return up;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isCorner() {
		return corner;
	}
}
