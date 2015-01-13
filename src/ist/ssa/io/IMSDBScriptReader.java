/**
 * 
 */
package ist.ssa.io;

import ist.ssa.Configuration;
import ist.ssa.content.CharacterNameLine;
import ist.ssa.content.DialogLine;
import ist.ssa.content.EmptyLine;
import ist.ssa.content.GeneralTextLine;
import ist.ssa.content.Line;
import ist.ssa.content.Script;
import ist.ssa.content.StoryVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 
 */
public class IMSDBScriptReader extends StoryVisitor {

	private String _filename;
	private File _file;

	/**
	 * @param filename
	 */
	public IMSDBScriptReader(String filename) {
		_filename = filename;
		_file = new File(_filename);
	}

	/**
	 * @see ist.ssa.content.StoryVisitor#processScript(ist.ssa.content.Script)
	 */
	@Override
	@SuppressWarnings("nls")
	public void processScript(Script script) throws IOException {
		script.reset();
		Charset encoding = StandardCharsets.ISO_8859_1;

		try (Scanner scanner = new Scanner(_file, encoding.name())) {
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
						DialogLine scriptDialog = new DialogLine(trimmedWholeLine, script.size(),
								characterName);
						script.addLine(scriptDialog);
						script.append(new Line(trimmedWholeLine, -1));
						script.addDialogLine(scriptDialog);
						wholeLine = "";
					} else {
						// adds empty lines. probably useless
						script.addLine(new EmptyLine(line, -1));
					}
					continue;
				} else {
					if (dialogue) {
						wholeLine += line.trim() + " ";
					} else {
						if (previousLineIsEmpty && script.nameOfCharacter(line)) {
							characterName = line.trim();
							dialogue = true;
							script.addLine(new CharacterNameLine(line, -1));
						} else {
							script.addLine(new GeneralTextLine(line, -1));
						}
					}
				}
			}

			String trimmedWholeLine = wholeLine.trim();
			if (!trimmedWholeLine.isEmpty()) {
				DialogLine scriptDialog = new DialogLine(trimmedWholeLine, script.size(),
						characterName);
				script.addLine(scriptDialog);
				script.append(new Line(trimmedWholeLine, -1));
				script.addDialogLine(scriptDialog);
			}
		}

	}

}
