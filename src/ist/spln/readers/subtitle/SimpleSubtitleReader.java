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
    private List<SubtitleLine> subtitleLines;

    public SimpleSubtitleReader() {
        XmlParser xmlParser = new XmlParser();
        xmlParser.parse(Reader.configLocation);
        this.subtitleLocation = xmlParser.getSubtitleLocation();
    }

    @Override
    public void read() throws IOException {
        List<String> textList = new ArrayList<>();
        List<SubtitleLine> subtitleLines = new ArrayList<>();
        Charset encoding = Charset.defaultCharset();
        Path path = Paths.get(this.subtitleLocation);
        try (Scanner scanner = new Scanner(path, encoding.name())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] times = scanner.nextLine().split("-->");
                String startTime = times[0];
                String endTime = times[1];
                while (scanner.hasNext() && line.trim().isEmpty()) {
                    scanner.nextLine();
                }
                String utterance = "";
                //while it is not a blank line
                while (scanner.hasNext() && !(line = scanner.nextLine()).trim().isEmpty()) {
                    utterance += line + " ";
                }
                System.out.println(utterance);
                textList.add(utterance.trim());
                subtitleLines.add(new SubtitleLine(utterance.trim(), new TimeInfo(startTime, endTime)));
            }
        }
        this.textList = textList;
        this.subtitleLines = subtitleLines;
    }

    @Override
    public List getTextList() {
        return textList;
    }

    public List<SubtitleLine> getSubtitleLines() {
        return subtitleLines;
    }
}
