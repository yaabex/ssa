package ist.ssa.content;

/**
 * This class represents a general line in a story.
 * 
 * Lines have text, a number, and may be translated.
 */
public class Line {
	/** The raw text in this line. */
	private String _text;
	
	/** The line number. */
	private int _index;
	
	/** Should this line be translated? */
	private boolean _translatable;

	/**
	 * By default, lines are not translated.
	 * 
	 * @param text
	 * @param number
	 */
	public Line(String text, int number) {
		this(text, number, false);
	}

	/**
	 * @param text
	 * @param index
	 * @param translatable
	 */
	public Line(String text, int index, boolean translatable) {
		_text = text;
		_index = index;
		_translatable = translatable;
	}

	/**
	 * @return text in this line.
	 */
	public String getText() {
		return _text;
	}

	/**
	 * @param line
	 */
	public void setText(String line) {
		_text = line;
	}

	/**
	 * @return index of this line (line number).
	 */
	public int getIndex() {
		return _index;
	}

	/**
	 * @param index
	 */
	public void setIndex(int index) {
		_index = index;
	}

	/**
	 * @param translatable
	 */
	public void setTranslatable(boolean translatable) {
		_translatable = translatable;
	}

	/**
	 * @return translatable status 
	 */
	public boolean isTranslatable() {
		return _translatable;
	}

	/**
	 * @param text
	 * @return this
	 */
	public Line prepend(String text) {
		_text = text + _text;
		return this;
	}

	/**
	 * @param text
	 * @return this
	 */
	public Line append(String text) {
		_text += text;
		return this;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return _text;
	}

}
