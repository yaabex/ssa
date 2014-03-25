package ist.spln.readers.subtitle;

import ist.spln.config.XmlParser;
import ist.spln.readers.Reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SimpleSubtitleReader implements Reader {
    private String subtitleLocation;
    private List textList;

    public SimpleSubtitleReader() {
        XmlParser xmlParser = new XmlParser();
        xmlParser.parse(Reader.configLocation);
        this.subtitleLocation = xmlParser.getSubtitleLocation();
    }

    @Override
    public void read() throws IOException {
        List<String> textList = new ArrayList<>();
        Charset encoding = StandardCharsets.UTF_8;
        Path path = Paths.get(subtitleLocation);
        try (Scanner scanner = new Scanner(path, encoding.name())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                scanner.nextLine();
                while (scanner.hasNext() && line.trim().isEmpty()) {
                    scanner.nextLine();
                }
                //while it is not a blank line
                while (scanner.hasNext() && !(line = scanner.nextLine()).trim().isEmpty()) {
                    System.out.println(line);
                    textList.add(line);
                }
            }
        }
        this.textList = textList;
    }

    @Override
    public List getTextList() {
        return textList;
    }
}
