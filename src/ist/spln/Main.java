package ist.spln;

import ist.spln.config.XmlParser;
import ist.spln.needleman.NWResults;
import ist.spln.needleman.NeedlemanWunch;
import ist.spln.needleman.valueobject.TimeValueObject;
import ist.spln.readers.Reader;
import ist.spln.readers.script.ScriptDialog;
import ist.spln.readers.script.SimpleScriptReader;
import ist.spln.readers.subtitle.SimpleSubtitleReader;
import ist.spln.readers.subtitle.SubtitleLine;
import ist.spln.readers.subtitle.TimeInfo;
import ist.spln.sentencealignment.Alignment;
import ist.spln.stringmodifiers.Lemmatizer;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String ANALYZER_PROPERTIES = "tokenize, ssplit, pos, lemma";

    public static void main(String[] args) throws Exception {
        XmlParser xmlParser = new XmlParser();
        xmlParser.parse(Reader.configLocation);
        SimpleScriptReader scriptReader = new SimpleScriptReader(xmlParser.getScriptLocation()); //TODO make a Script and a Subtitle object
        SimpleSubtitleReader subtitleReader = new SimpleSubtitleReader(xmlParser.getSubtitleLocation());
        scriptReader.read();
        subtitleReader.read();

        Lemmatizer lemmatizer = new Lemmatizer();
        NeedlemanWunch needlemanWunch = new NeedlemanWunch();
        NWResults nwResults = needlemanWunch.run(lemmatizer.modify(scriptReader.getTextList()),
                lemmatizer.modify(subtitleReader.getTextList()), 1, 0, 999999);

        Alignment alignment = new Alignment();
        alignment.align(nwResults.getValueObjectPairs(), lemmatizer, scriptReader, subtitleReader);
        alignment.enhanceScript(scriptReader, subtitleReader);
        alignment.enhanceSubtitles(scriptReader, subtitleReader);

        scriptReader.writeWholeScript(xmlParser.getNewFilesLocation());
        subtitleReader.writeWholeSubtitleFile(xmlParser.getNewFilesLocation());

        SimpleSubtitleReader subtitleOtherLanguageReader = new SimpleSubtitleReader(xmlParser.getSubtitleOtherLanguageLocation());
        subtitleOtherLanguageReader.read();

        NWResults nwResultsTime = needlemanWunch.run(toValueObjectArray1(scriptReader.getScriptDialogs()),
                toValueObjectArray2(subtitleOtherLanguageReader.getSubtitleLines()), 1, 0, 999999);

        alignment.align(nwResultsTime.getValueObjectPairs());
        alignment.translateScript(scriptReader, subtitleOtherLanguageReader);

        scriptReader.writeWholeScript(xmlParser.getNewFilesLocation());
    }

    private static TimeValueObject[] toValueObjectArray1(List<ScriptDialog> scriptDialogs) {
        List<TimeValueObject> timeValueObjects = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
        for (int i = 0; i < scriptDialogs.size(); i++) {
            ScriptDialog line = scriptDialogs.get(i);
            for (TimeInfo timeInfo : line.getTimeInfos()) {
                DateTime start = formatter.parseDateTime(timeInfo.getStartTime());
                DateTime end = formatter.parseDateTime(timeInfo.getEndTime());
                timeValueObjects.add(new TimeValueObject(i, new Interval(start, end)));
            }
        }
        TimeValueObject[] timeValueObjectsArray = new TimeValueObject[timeValueObjects.size()];
        return timeValueObjects.toArray(timeValueObjectsArray);
    }

    private static TimeValueObject[] toValueObjectArray2(List<SubtitleLine> subtitleLines) {
        List<TimeValueObject> timeValueObjects = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
        for (int i = 0; i < subtitleLines.size(); i++) {
            SubtitleLine line = subtitleLines.get(i);
            DateTime start = formatter.parseDateTime(line.getTimeInfo().getStartTime());
            DateTime end = formatter.parseDateTime(line.getTimeInfo().getEndTime());
            timeValueObjects.add(new TimeValueObject(i, new Interval(start, end)));
        }
        TimeValueObject[] timeValueObjectsArray = new TimeValueObject[timeValueObjects.size()];
        return timeValueObjects.toArray(timeValueObjectsArray);
    }
}