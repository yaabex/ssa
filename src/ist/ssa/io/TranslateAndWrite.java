/**
 * 
 */
package ist.ssa.io;

import ist.ssa.Configuration;
import ist.ssa.content.DialogLine;
import ist.ssa.content.Line;
import ist.ssa.content.Script;
import ist.ssa.content.StoryVisitor;
import ist.ssa.content.Subtitles;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.memetix.mst.MicrosoftTranslatorAPI;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * 
 */
public class TranslateAndWrite extends StoryVisitor {

	private final static String TRANSLATE_CLIENT_ID = "ssa9000";
	private final static String TRANSLATE_CLIENT_SECRET = "q7a4z1w8s5x2q7a4z1w8s5x2";

	private String _directory;
	private String _basename = Configuration.DEFAULT_SCRIPT_NAME;

	/**
	 * @param directory
	 * @param basename
	 *            base name of all related files
	 */
	public TranslateAndWrite(String directory, String basename) {
		_directory = directory;
		_basename = basename;
	}

	/**
	 * @see ist.ssa.content.StoryVisitor#processScript(ist.ssa.content.Script)
	 */
	@Override
	public void processScript(Script script) throws IOException {
		File file = new File(_directory);
		file.mkdirs();
		file = new File(_directory + "/" + Configuration.DEFAULT_SCRIPT_NAME);

		MicrosoftTranslatorAPI.setClientId(TRANSLATE_CLIENT_ID);
		MicrosoftTranslatorAPI.setClientSecret(TRANSLATE_CLIENT_SECRET);

		List<String> translatedScript = new ArrayList<>();
		for (Line line : script.getWholeStory()) {
			if (line.isTranslatable()) {
				try {
					if (line instanceof DialogLine)
						line.setText("BT: " + line);
					String translation = Translate.execute(line.toString(), Language.ENGLISH,
							Language.PORTUGUESE);
					translatedScript.add(translation);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1); // DAVID: FIXME
				}
			} else {
				translatedScript.add(line.toString());
			}
		}
		FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), translatedScript);
	}

	/**
	 * @see ist.ssa.content.StoryVisitor#processSubtitles(ist.ssa.content.Subtitles)
	 */
	@Override
	public void processSubtitles(Subtitles transcript) throws IOException {
		// TODO Auto-generated method stub

	}

}
