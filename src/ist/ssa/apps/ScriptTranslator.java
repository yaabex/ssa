package ist.ssa.apps;

import ist.ssa.Alignment;
import ist.ssa.Configuration;
import ist.ssa.content.DialogLine;
import ist.ssa.content.Script;
import ist.ssa.content.Subtitles;
import ist.ssa.content.SubtitlesLine;
import ist.ssa.content.TimeInfo;
import ist.ssa.io.IMSDBScriptReader;
import ist.ssa.io.SRTSubtitlesReader;
import ist.ssa.io.TranslateAndWrite;
import ist.ssa.io.WholeStoryWriter;
import ist.ssa.needleman.NWResult;
import ist.ssa.needleman.NeedlemanWunsch;
import ist.ssa.needleman.Span;
import ist.ssa.needleman.TimeSpan;
import ist.ssa.textanalysis.TextAnalyzer;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ScriptTranslator {

	public static void main(String[] args) throws Exception {
		if (args.length != 4)
			return;

		String scriptLocation = args[0];
		String l1TranscriptLocation = args[1];
		String l2TranscriptLocation = args[2];
		String basename = args[3];

		Script script = new Script(new IMSDBScriptReader(scriptLocation));
		Subtitles l1Transcript = new Subtitles(new SRTSubtitlesReader(l1TranscriptLocation));

		NeedlemanWunsch nw = new NeedlemanWunsch();

		// ==== DO TEXT ALIGNMENT ====

		TextAnalyzer analyzer = new TextAnalyzer(Configuration.ANALYZER_PROPERTIES);
		Span[] scriptSpans = analyzer.modify(script);
		Span[] l1TranscriptSpans = analyzer.modify(l1Transcript);
		NWResult nwTextResults = nw.run(scriptSpans, l1TranscriptSpans, 1, 0, 999999);

		Alignment alignment = new Alignment();
		alignment.alignTextSpans(nwTextResults.getPairs(), analyzer, script, l1Transcript);
		alignment.enhanceScript(script, l1Transcript);
		alignment.enhanceSubtitles(script, l1Transcript);

		WholeStoryWriter wholeStoryWriter = new WholeStoryWriter(basename, scriptLocation);
		script.accept(wholeStoryWriter);
		l1Transcript.accept(wholeStoryWriter);

		// ==== DO TIME ALIGNMENT ====

		Subtitles l2Transcript = new Subtitles(new SRTSubtitlesReader(l2TranscriptLocation));

		NWResult nwTimeResults = nw.run(scriptToTimeSpans(script.getDialogs()),
				transcriptToTimeSpans(l2Transcript.getDialogs()), 1, 0, 999999);

		alignment.alignTimeSpans(nwTimeResults.getPairs());
		alignment.translateScript(script, l2Transcript);

		TranslateAndWrite writer = new TranslateAndWrite(basename, l2TranscriptLocation);
		script.accept(writer);
	}

	/**
	 * @param scriptDialogs
	 * @return conversion
	 */
	private static TimeSpan[] scriptToTimeSpans(List<DialogLine> scriptDialogs) {
		List<TimeSpan> timeSpans = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
		for (int i = 0; i < scriptDialogs.size(); i++) {
			DialogLine line = scriptDialogs.get(i);
			for (TimeInfo timeInfo : line.getTimeInfos()) {
				DateTime start = formatter.parseDateTime(timeInfo.getStartTime());
				DateTime end = formatter.parseDateTime(timeInfo.getEndTime());
				timeSpans.add(new TimeSpan(i, new Interval(start, end)));
			}
		}
		return timeSpans.toArray(new TimeSpan[timeSpans.size()]);
	}

	/**
	 * @param transcriptLines
	 * @return conversion
	 */
	private static TimeSpan[] transcriptToTimeSpans(List<SubtitlesLine> transcriptLines) {
		List<TimeSpan> timeSpans = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
		for (int i = 0; i < transcriptLines.size(); i++) {
			SubtitlesLine line = transcriptLines.get(i);
			DateTime start = formatter.parseDateTime(line.getTimeInfo().getStartTime());
			DateTime end = formatter.parseDateTime(line.getTimeInfo().getEndTime());
			timeSpans.add(new TimeSpan(i, new Interval(start, end)));
		}
		return timeSpans.toArray(new TimeSpan[timeSpans.size()]);
	}
}