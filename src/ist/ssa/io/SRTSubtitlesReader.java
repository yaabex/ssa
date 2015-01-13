/**
 * 
 */
package ist.ssa.io;

import ist.ssa.content.StoryVisitor;
import ist.ssa.content.Subtitles;
import ist.ssa.content.SubtitlesLine;
import ist.ssa.content.TimeInfo;

import java.io.File;
import java.io.FileNotFoundException;
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

/**
 * 
 */
public class SRTSubtitlesReader extends StoryVisitor {

	private String _filename;
	private File _file;

	/**
	 * @param filename
	 */
	public SRTSubtitlesReader(String filename) {
		_filename = filename;
		_file = new File(_filename);
	}

	private boolean isTwoPersonDialogue(List<String> utterances) {
		return (utterances.size() == 2) && utterances.get(0).trim().startsWith("-")
				&& utterances.get(1).trim().startsWith("-");
	}

	/**
	 * @see ist.ssa.content.StoryVisitor#processScript(ist.ssa.content.Script)
	 */
	@Override
	@SuppressWarnings("nls")
	public void processSubtitles(Subtitles subtitles) throws FileNotFoundException {
		subtitles.reset();
		List<String> wholeSubtitleFile = new LinkedList<String>();
		List<String> utterances = new ArrayList<String>();
		Charset encoding = StandardCharsets.ISO_8859_1;
		try (Scanner scanner = new Scanner(_file, encoding.name())) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				wholeSubtitleFile.add(line);

				if (line.trim().isEmpty())
					continue;

				line = scanner.nextLine();
				wholeSubtitleFile.add(line);

				String[] times = line.split("-->");
				String startTime = times[0].trim();
				String endTime = times[1].trim();

				while (scanner.hasNext() && line.trim().isEmpty())
					wholeSubtitleFile.add(scanner.nextLine());

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

					SubtitlesLine uttLine1 = new SubtitlesLine(utterance1,
							wholeSubtitleFile.size() - 2, new TimeInfo(startTime,
									formatter.print(mid)));
					SubtitlesLine uttLine2 = new SubtitlesLine(utterance2,
							wholeSubtitleFile.size() - 1, new TimeInfo(formatter.print(mid),
									endTime));

					subtitles.append(uttLine1);
					subtitles.append(uttLine2);

					subtitles.addTranscriptLine(uttLine1);
					subtitles.addTranscriptLine(uttLine2);
				} else {
					wholeSubtitleFile.add(StringUtils.join(utterances, "\n"));
					String utterance = StringUtils.join(utterances, " ").trim();
					SubtitlesLine uttLine = new SubtitlesLine(utterance,
							wholeSubtitleFile.size() - 1, new TimeInfo(startTime, endTime));
					subtitles.append(uttLine);
					subtitles.addTranscriptLine(uttLine);
				}
				if (scanner.hasNext()) {
					wholeSubtitleFile.add(line);
					utterances.clear();
				}
			}
		}

	}

}
