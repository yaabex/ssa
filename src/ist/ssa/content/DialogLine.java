package ist.ssa.content;

import java.util.ArrayList;
import java.util.List;

/**
 * A script line containing a dialog line.
 */
public class DialogLine extends Line {
	/**
	 * The character the line belongs to.
	 */
	@SuppressWarnings("nls")
	private String _characterName = "";

	/**
	 * This line may be timed, given a movie made from the script.
	 */
	private List<TimeInfo> _timeInfos = new ArrayList<TimeInfo>();

	/**
	 * @param text
	 * @param lineNumber
	 * @param characterName
	 */
	public DialogLine(String text, int lineNumber, String characterName) {
		super(text, lineNumber, true);
		_characterName = characterName;
		_timeInfos = new ArrayList<>();
	}

	/**
	 * @return the character's name
	 */
	public String getCharacterName() {
		return _characterName;
	}

	/**
	 * @return the time information associated with the dialog line.
	 */
	public List<TimeInfo> getTimeInfos() {
		return _timeInfos;
	}

	/**
	 * Add time information to the dialog line.
	 * 
	 * @param timeInfo
	 */
	public void addTimeInfo(TimeInfo timeInfo) {
		_timeInfos.add(timeInfo);
	}
}
