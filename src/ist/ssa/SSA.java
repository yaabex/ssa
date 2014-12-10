package ist.ssa;

import ist.ssa.content.DialogLine;
import ist.ssa.content.Script;
import ist.ssa.content.SimpleSubtitle;
import ist.ssa.content.SubtitleLine;
import ist.ssa.content.TimeInfo;
import ist.ssa.needleman.Alignment;
import ist.ssa.needleman.NWResults;
import ist.ssa.needleman.NWVOArray;
import ist.ssa.needleman.NeedlemanWunch;
import ist.ssa.needleman.TimeVO;
import ist.ssa.textanalysis.TextAnalyzer;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class SSA {

	public static void main(String[] args) throws Exception {
		if (args.length != 4)
			return;

		String scriptLocation = args[0];
		String subtitleLocation = args[1];
		String basename = args[2];
		String subtitleOtherLanguageLocation = args[3];

		Script script = new Script(scriptLocation);
		SimpleSubtitle subtitle = new SimpleSubtitle(subtitleLocation);

		TextAnalyzer analyzer = new TextAnalyzer(Configuration.ANALYZER_PROPERTIES);
		NeedlemanWunch needlemanWunch = new NeedlemanWunch();
		NWVOArray[] scriptVOs = analyzer.modify(script.getStoryLines());
		NWVOArray[] subtitlesVOs = analyzer.modify(subtitle.getStoryLines());
		NWResults nwResults = needlemanWunch.run(scriptVOs, subtitlesVOs, 1, 0, 999999);

		Alignment alignment = new Alignment();
		alignment.align(nwResults.getVOPairs(), analyzer, script, subtitle);
		alignment.enhanceScript(script, subtitle);
		alignment.enhanceSubtitles(script, subtitle);

		script.writeWholeStory(basename);
		subtitle.writeWholeStory(basename);

		Translate.setClientId(Configuration.TRANSLATE_CLIENT_ID);
		Translate.setClientSecret(Configuration.TRANSLATE_CLIENT_SECRET);
		System.out.println(Translate.execute("bom", Language.PORTUGUESE, Language.ENGLISH));

		SimpleSubtitle subtitleOtherLanguage = new SimpleSubtitle(subtitleOtherLanguageLocation);

		NWResults nwResultsTime = needlemanWunch.run(toVOArray1(script.getDialogs()),
				toVOArray2(subtitleOtherLanguage.getDialogs()), 1, 0, 999999);

		alignment.align(nwResultsTime.getVOPairs());
		alignment.translateScript(script, subtitleOtherLanguage);

		script.writeWholeScriptAndTranslate(basename);
	}

	private static TimeVO[] toVOArray1(List<DialogLine> scriptDialogs) {
		List<TimeVO> timeValueObjects = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
		for (int i = 0; i < scriptDialogs.size(); i++) {
			DialogLine line = scriptDialogs.get(i);
			for (TimeInfo timeInfo : line.getTimeInfos()) {
				DateTime start = formatter.parseDateTime(timeInfo.getStartTime());
				DateTime end = formatter.parseDateTime(timeInfo.getEndTime());
				timeValueObjects.add(new TimeVO(i, new Interval(start, end)));
			}
		}
		TimeVO[] timeValueObjectsArray = new TimeVO[timeValueObjects.size()];
		return timeValueObjects.toArray(timeValueObjectsArray);
	}

	private static TimeVO[] toVOArray2(List<SubtitleLine> subtitleLines) {
		List<TimeVO> timeValueObjects = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
		for (int i = 0; i < subtitleLines.size(); i++) {
			SubtitleLine line = subtitleLines.get(i);
			DateTime start = formatter.parseDateTime(line.getTimeInfo().getStartTime());
			DateTime end = formatter.parseDateTime(line.getTimeInfo().getEndTime());
			timeValueObjects.add(new TimeVO(i, new Interval(start, end)));
		}
		TimeVO[] timeValueObjectsArray = new TimeVO[timeValueObjects.size()];
		return timeValueObjects.toArray(timeValueObjectsArray);
	}
}