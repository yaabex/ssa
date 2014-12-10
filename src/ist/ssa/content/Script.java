package ist.ssa.content;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Script extends Story {
	private List<Line> wholeScript;
	private List<DialogLine> scriptDialogs;

	public Script(String filename) throws IOException {
		this(filename, true);
	}

	public Script(String filename, boolean load) throws IOException {
		super(filename);
		if (load)
			read();
	}

	@Override
	public void read() throws IOException {
		List<Line> wholeScript = new ArrayList<>();
		List<DialogLine> scriptDialogs = new ArrayList<>();
		Charset encoding = StandardCharsets.ISO_8859_1;

		try (Scanner scanner = new Scanner(getPath(), encoding.name())) {
			boolean previousLineIsEmpty = false;
			boolean dialogue = false;
			String wholeLine = "";
			String characterName = "";
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.trim().isEmpty()) {
					previousLineIsEmpty = true;
					dialogue = false;
					if (!wholeLine.isEmpty()) {
						String trimmedWholeLine = wholeLine.trim();
						DialogLine scriptDialog = new DialogLine(trimmedWholeLine,
								wholeScript.size(), characterName);
						wholeScript.add(scriptDialog);
						append(new Line(trimmedWholeLine, -1));
						scriptDialogs.add(scriptDialog);
						wholeLine = "";
					} else {
						// adds empty lines. probably useless
						wholeScript.add(new EmptyLine(line, -1));
					}
					continue;
				} else {
					if (dialogue) {
						wholeLine += line.trim() + " ";
					} else {
						if (previousLineIsEmpty && nameOfCharacter(line)) {
							characterName = line.trim();
							dialogue = true;
							wholeScript.add(new CharacterNameLine(line, -1));
						} else {
							wholeScript.add(new GeneralTextLine(line, -1));
						}
					}
				}
			}

			String trimmedWholeLine = wholeLine.trim();
			if (!trimmedWholeLine.isEmpty()) {
				DialogLine scriptDialog = new DialogLine(trimmedWholeLine,
						wholeScript.size(), characterName);
				wholeScript.add(scriptDialog);
				append(new Line(trimmedWholeLine, -1));
				scriptDialogs.add(scriptDialog);
			}
		}
		wholeScript = wholeScript;
		scriptDialogs = scriptDialogs;
	}

	private boolean nameOfCharacter(String line) {
		return line.matches("^\\s+[\\p{Lu}]{2,}.*");
	}

	public Line getContextFromLineNumberOfWord(int i) {
		return getWholeStory().get(getDialogs().get(i).getLineNumber());
	}

	public List<DialogLine> getDialogs() {
		return scriptDialogs;
	}

	public void writeWholeScriptAndTranslate(String location) throws Exception {
		File file = new File(location);
		file.mkdirs();
		file = new File(location + "/script.txt");

		Translate.setClientId("ssa9000");
		Translate.setClientSecret("q7a4z1w8s5x2q7a4z1w8s5x2");
		List<String> translatedScript = new ArrayList<>();
		for (Line line : getWholeStory()) {
			if (line.isTranslatable()) {
				if (line instanceof DialogLine) {
					line.setText("BT: " + line);
				}
				translatedScript.add(Translate.execute(line.toString(), Language.ENGLISH,
						Language.PORTUGUESE));
			} else {
				translatedScript.add(line.toString());
			}
		}
		FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), translatedScript);
	}

	public String getStoryName() {
		return "script.txt";
	}

	public List<Line> getWholeStory() {
		return wholeScript;
	}

}
