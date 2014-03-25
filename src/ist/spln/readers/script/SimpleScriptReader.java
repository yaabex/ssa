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
    private List textList;

    public SimpleScriptReader() {
        XmlParser xmlParser = new XmlParser();
        xmlParser.parse(Reader.configLocation);
        this.scriptLocation = xmlParser.getScriptLocation();
    }

    @Override
    public void read() throws IOException {
        List<String> textList = new ArrayList<>();
        Charset encoding = StandardCharsets.ISO_8859_1;
        Path path = Paths.get(scriptLocation);
        try (Scanner scanner = new Scanner(path, encoding.name())){
            boolean previousLineIsEmpty = false;
            boolean dialogue = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.trim().isEmpty()) {
                    previousLineIsEmpty = true;
                    dialogue = false;
                    continue;
                }
                if(dialogue) {
                    System.out.println(line);
                    textList.add(line);
                }
                if(previousLineIsEmpty && nameOfCharacter(line)) {
                    dialogue = true;
                }
            }
        }
        this.textList = textList;
    }

    private boolean nameOfCharacter(String line) {
        return line.matches("^\\s+[\\p{Lu}]{2,}.*");
    }

    @Override
    public List getTextList() {
        return textList;
    }
}
