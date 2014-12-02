package ist.ssa.readers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class Story {
	
	/** File name. */
	private String location;
	
	/** Low-level location. */
	private Path path;
	
	/** File contents. */
	private List<String> textList;

	/**
	 * @param location file name
	 */
	public Story(String location) {
		this.location = location;
		this.setPath(Paths.get(location));
	}
	
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	public List<String> getTextList() {
		return textList;
	}

	public void setTextList(List<String> textList) {
		this.textList = textList;
	}

	public void append(String text) {
		textList.add(text);
	}
	
	public abstract void read() throws IOException;

}
