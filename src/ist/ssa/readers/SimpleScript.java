package ist.ssa.readers;

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

public class SimpleScript extends Story {
	private List<ScriptLine> wholeScript;
	private List<ScriptDialogLine> scriptDialogs;

	public SimpleScript(String scriptLocation) {
		super(scriptLocation);
	}

	@Override
	public void read() throws IOException {
		List<ScriptLine> wholeScript = new ArrayList<>();
		List<ScriptDialogLine> scriptDialogs = new ArrayList<>();
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
						ScriptDialogLine scriptDialog = new ScriptDialogLine(trimmedWholeLine,
								wholeScript.size(), characterName);
						wholeScript.add(scriptDialog);
						append(trimmedWholeLine);
						scriptDialogs.add(scriptDialog);
						wholeLine = "";
					} else {
						// adds empty lines. probably useless
						wholeScript.add(new ScriptEmptyLine(line, -1));
					}
					continue;
				} else {
					if (dialogue) {
						wholeLine += line.trim() + " ";
					} else {
						if (previousLineIsEmpty && nameOfCharacter(line)) {
							characterName = line.trim();
							dialogue = true;
							wholeScript.add(new ScriptCharacterNameLine(line, -1));
						} else {
							wholeScript.add(new ScriptOtherTextLine(line, -1));
						}
					}
				}
			}
			String trimmedWholeLine = wholeLine.trim();
			if (!trimmedWholeLine.isEmpty()) {
				ScriptDialogLine scriptDialog = new ScriptDialogLine(trimmedWholeLine,
						wholeScript.size(), characterName);
				wholeScript.add(scriptDialog);
				append(trimmedWholeLine);
				scriptDialogs.add(scriptDialog);
			}
		}
		this.wholeScript = wholeScript;
		this.scriptDialogs = scriptDialogs;
	}

	private boolean nameOfCharacter(String line) {
		return line.matches("^\\s+[\\p{Lu}]{2,}.*");
	}

	public ScriptLine getContextFromLineNumberOfWord(int i) {
		return this.getWholeScript().get(this.getScriptDialogs().get(i).getLineNumber());
	}

	public List<ScriptLine> getWholeScript() {
		return wholeScript;
	}

	public List<ScriptDialogLine> getScriptDialogs() {
		return scriptDialogs;
	}

	public void printWholeScript() {
		for (ScriptLine line : getWholeScript()) {
			System.out.println(line);
		}
	}

	public void writeWholeScript(String newFilesLocation) throws Exception {
		File file = new File(newFilesLocation);
		file.mkdirs();
		file = new File(newFilesLocation + "/script.txt");

		FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), getWholeScript());
	}

	public void writeWholeScriptAndTranslate(String newFilesLocation) throws Exception {
		File file = new File(newFilesLocation);
		file.mkdirs();
		file = new File(newFilesLocation + "/script.txt");

		Translate.setClientId("ssa9000");
		Translate.setClientSecret("q7a4z1w8s5x2q7a4z1w8s5x2");
		List<String> translatedScript = new ArrayList<>();
		for (ScriptLine line : getWholeScript()) {
			if (line.isToTranslate()) {
				if (line instanceof ScriptDialogLine) {
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
}
