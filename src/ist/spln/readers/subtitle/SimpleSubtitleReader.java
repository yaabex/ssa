package ist.spln.readers.subtitle;

import ist.spln.readers.Reader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class SimpleSubtitleReader implements Reader {
    private String subtitleLocation;
    private List<String> wholeSubtitleFile;
    private List<String> textList;
    private List<SubtitleLine> subtitleLines;

    public SimpleSubtitleReader(String subtitleLocation) {
        this.subtitleLocation = subtitleLocation;
    }

    @Override
    public void read() throws IOException {
        List<String> wholeSubtitleFile = new LinkedList<>();
        List<String> textList = new ArrayList<>();
        List<SubtitleLine> subtitleLines = new ArrayList<>();
        List<String> utterances = new ArrayList<>();
        Charset encoding = Charset.defaultCharset();
        Path path = Paths.get(this.subtitleLocation);
        try (Scanner scanner = new Scanner(path, encoding.name())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                wholeSubtitleFile.add(line);
                if (line.trim().isEmpty()) {
                    continue;
                }
                line = scanner.nextLine();
                wholeSubtitleFile.add(line);
                String[] times = line.split("-->");
                String startTime = times[0].trim();
                String endTime = times[1].trim();
                while (scanner.hasNext() && line.trim().isEmpty()) {
                    wholeSubtitleFile.add(scanner.nextLine());
                }
                String utterance = "";
                //while it is not a blank line
                while (scanner.hasNext() && !(line = scanner.nextLine()).trim().isEmpty()) {
                    utterances.add(line);
                }
                if (isTwoPersonDialogue(utterances)) {
                    String utterance1 = utterances.get(0);
                    String utterance2 = utterances.get(1);
                    wholeSubtitleFile.add(utterance1);
                    wholeSubtitleFile.add(utterance2);
                    utterance1 = utterance1.replaceFirst("-", "").trim();
                    utterance2 = utterance2.replaceFirst("-", "").trim();
                    textList.add(utterance1);
                    textList.add(utterance2);
                    subtitleLines.add(new SubtitleLine(utterance1, wholeSubtitleFile.size() - 2, new TimeInfo(startTime, endTime)));
                    subtitleLines.add(new SubtitleLine(utterance2, wholeSubtitleFile.size() - 1, new TimeInfo(startTime, endTime)));
                } else {
                    wholeSubtitleFile.add(StringUtils.join(utterances, "\n"));
                    utterance = StringUtils.join(utterances, " ").trim();
                    textList.add(utterance);
                    subtitleLines.add(new SubtitleLine(utterance, wholeSubtitleFile.size() - 1, new TimeInfo(startTime, endTime)));
                }
                if (scanner.hasNext()) {
                    wholeSubtitleFile.add(line);
                    utterances.clear();
                }
            }
        }
        this.wholeSubtitleFile = wholeSubtitleFile;
        this.textList = textList;
        this.subtitleLines = subtitleLines;
    }

    private boolean isTwoPersonDialogue(List<String> utterances) {
        return (utterances.size() == 2) &&
                utterances.get(0).trim().startsWith("-") &&
                utterances.get(1).trim().startsWith("-");
    }

    public String getContextFromLineNumberOfWord(int i) {
        return this.getWholeSubtitleFile().get(this.getSubtitleLines().get(i).getLineNumber());
    }

    public List<String> getWholeSubtitleFile() {
        return wholeSubtitleFile;
    }

    @Override
    public List getTextList() {
        return textList;
    }

    public List<SubtitleLine> getSubtitleLines() {
        return subtitleLines;
    }

    public void printWholeSubtitleFile() {
        for (String line : getWholeSubtitleFile()) {
            System.out.println(line);
        }
    }

    public void writeWholeSubtitleFile(String newFilesLocation) throws IOException {
        File file = new File(newFilesLocation);
        file.mkdirs();
        file = new File(newFilesLocation + "/sub.srt");
        FileUtils.writeLines(file, this.wholeSubtitleFile);
    }
}
