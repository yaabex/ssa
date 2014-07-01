package ist.spln.readers.script;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import ist.spln.readers.Reader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class SimpleScriptReader implements Reader {
    private String scriptLocation;
    private List<ScriptLine> wholeScript;
    private List textList;
    private List<ScriptDialog> scriptDialogs;

    public SimpleScriptReader(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

    @Override
    public void read() throws IOException {
        List<ScriptLine> wholeScript = new ArrayList<>();
        List<String> textList = new ArrayList<>();
        List<ScriptDialog> scriptDialogs = new ArrayList<>();
        Charset encoding = StandardCharsets.ISO_8859_1;
        Path path = Paths.get(scriptLocation);
        try (Scanner scanner = new Scanner(path, encoding.name())){
            boolean previousLineIsEmpty = false;
            boolean dialogue = false;
            String wholeLine = "";
            String characterName = "";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.trim().isEmpty()) {
                    previousLineIsEmpty = true;
                    dialogue = false;
                    if (!wholeLine.isEmpty()) {
                        String trimmedWholeLine = wholeLine.trim();
                        ScriptDialog scriptDialog = new ScriptDialog(trimmedWholeLine, wholeScript.size(), characterName);
                        wholeScript.add(scriptDialog);
                        textList.add(trimmedWholeLine);
                        scriptDialogs.add(scriptDialog);
                        wholeLine = "";
                    } else {
                        wholeScript.add(new ScriptEmptyLine(line)); //adds empty lines. probably useless
                    }
                    continue;
                } else {
                    if(dialogue) {
                        wholeLine += line.trim() + " ";
                    } else {
                        if(previousLineIsEmpty && nameOfCharacter(line)) {
                            characterName = line.trim();
                            dialogue = true;
                            wholeScript.add(new ScriptOtherText(line));
                        } else {
                            wholeScript.add(new ScriptOtherText(line));
                        }
                    }
                }
            }
            String trimmedWholeLine = wholeLine.trim();
            if (!trimmedWholeLine.isEmpty()) {
                ScriptDialog scriptDialog = new ScriptDialog(trimmedWholeLine, wholeScript.size(), characterName);
                wholeScript.add(scriptDialog);
                textList.add(trimmedWholeLine);
                scriptDialogs.add(scriptDialog);
            }
        }
        this.textList = textList;
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

    @Override
    public List getTextList() {
        return textList;
    }

    public List<ScriptDialog> getScriptDialogs() {
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

        Translate.setClientId("ScriptSubtitleAlignment");
        Translate.setClientSecret("q7a4z1w8s5x2q7a4z1w8s5x2");
        List<String> translatedScript = new ArrayList<>();
        for (ScriptLine line : getWholeScript()) {
            if (line.isToTranslate()) {
                if (line instanceof ScriptDialog) {
                    line.setLine("BT: " + line.getLine());
                }
                translatedScript.add(Translate.execute(line.toString(), Language.ENGLISH, Language.PORTUGUESE));
            } else {
                translatedScript.add(line.toString());
            }
        }
        FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), translatedScript);
    }
}
