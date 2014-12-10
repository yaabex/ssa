package ist.ssa.content;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.stanford.nlp.util.StringUtils;

public class SimpleSubtitle extends Story {
	private List<SubtitleLine> _subtitleLines;

	public SimpleSubtitle(String location) throws IOException {
		this(location, true);
	}

	public SimpleSubtitle(String location, boolean loaddRead) throws IOException {
		super(location);
		if (loaddRead)
			read();
	}

	public List<SubtitleLine> getDialogs() {
		return _subtitleLines;
	}

	public String getStoryName() {
		return "sub.txt";
	}

	@Override
	public void read() throws IOException {
		List<String> wholeSubtitleFile = new LinkedList<String>();
		List<SubtitleLine> subtitleLines = new ArrayList<SubtitleLine>();
		List<String> utterances = new ArrayList<String>();
		Charset encoding = StandardCharsets.ISO_8859_1;
		try (Scanner scanner = new Scanner(getPath(), encoding.name())) {
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
				// while it is not a blank line
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

					DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
					DateTime start = formatter.parseDateTime(startTime);
					DateTime end = formatter.parseDateTime(endTime);
					long duration = end.getMillis() - start.getMillis();
					assert duration >= 0;
					String[] words1 = utterance1.split("\\s+");
					String[] words2 = utterance2.split("\\s+");
					long round = Math
							.round((words1.length / (words1.length + (double) words2.length))
									* duration);
					DateTime mid = start.plus(round);
					
					SubtitleLine uttLine1 = new SubtitleLine(utterance1, wholeSubtitleFile.size() - 2,
							new TimeInfo(startTime, formatter.print(mid)));
					SubtitleLine uttLine2 = new SubtitleLine(utterance2, wholeSubtitleFile.size() - 1,
							new TimeInfo(formatter.print(mid), endTime));
					
					append(uttLine1);
					append(uttLine2);

					subtitleLines.add(uttLine1);
					subtitleLines.add(uttLine2);
				} else {
					wholeSubtitleFile.add(StringUtils.join(utterances, "\n"));
					utterance = StringUtils.join(utterances, " ").trim();
					SubtitleLine uttLine = new SubtitleLine(utterance,
							wholeSubtitleFile.size() - 1, new TimeInfo(startTime, endTime));
					append(uttLine);
					subtitleLines.add(uttLine);
				}
				if (scanner.hasNext()) {
					wholeSubtitleFile.add(line);
					utterances.clear();
				}
			}
		}

		_subtitleLines = subtitleLines;
	}

	private boolean isTwoPersonDialogue(List<String> utterances) {
		return (utterances.size() == 2) && utterances.get(0).trim().startsWith("-")
				&& utterances.get(1).trim().startsWith("-");
	}

	public Line getContextFromLineNumberOfWord(int i) {
		return getStoryLines().get(_subtitleLines.get(i).getLineNumber());
	}

}
