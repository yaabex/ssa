package ist.spln.readers.script;

import ist.spln.config.XmlParser;
import ist.spln.readers.Reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class SimpleScriptReader implements Reader {
    private String scriptLocation;
    private List<String> wholeScript;
    private List textList;
    private List<ScriptLine> scriptLines;

    public SimpleScriptReader() {
        XmlParser xmlParser = new XmlParser();
        xmlParser.parse(Reader.configLocation);
        this.scriptLocation = xmlParser.getScriptLocation();
    }

    @Override
    public void read() throws IOException {
        List<String> wholeScript = new ArrayList<>();
        List<String> textList = new ArrayList<>();
        List<ScriptLine> scriptLines = new ArrayList<>();
        Charset encoding = StandardCharsets.ISO_8859_1;
        Path path = Paths.get(scriptLocation);
        try (Scanner scanner = new Scanner(path, encoding.name())){
            boolean previousLineIsEmpty = false;
            boolean dialogue = false;
            String wholeLine = "";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.trim().isEmpty()) {
                    previousLineIsEmpty = true;
                    dialogue = false;
                    if (!wholeLine.isEmpty()) {
                        String trimmedWholeLine = wholeLine.trim();
                        wholeScript.add(trimmedWholeLine);
                        textList.add(trimmedWholeLine);
                        scriptLines.add(new ScriptLine(trimmedWholeLine, wholeScript.size()-1));
                        wholeLine = "";
                    } else {
                        wholeScript.add(line); //adds empty lines. probably useless
                    }
                    continue;
                } else {
                    if(dialogue) {
                        System.out.println(line);
                        wholeLine += line.trim() + " ";
                    } else {
                        if(previousLineIsEmpty && nameOfCharacter(line)) {
                            dialogue = true;
                            wholeScript.add(line);
                        } else {
                            wholeScript.add(line.trim());
                        }
                    }
                }
            }
        }
        this.textList = textList;
        this.wholeScript = wholeScript;
        this.scriptLines = scriptLines;
    }

    private boolean nameOfCharacter(String line) {
        return line.matches("^\\s+[\\p{Lu}]{2,}.*");
    }

    public List<String> getWholeScript() {
        return wholeScript;
    }

    @Override
    public List getTextList() {
        return textList;
    }

    public List<ScriptLine> getScriptLines() {
        return scriptLines;
    }
}
