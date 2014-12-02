package ist.ssa;

import ist.ssa.needleman.Alignment;
import ist.ssa.needleman.NWResults;
import ist.ssa.needleman.NeedlemanWunch;
import ist.ssa.needleman.TimeVO;
import ist.ssa.readers.ScriptDialogLine;
import ist.ssa.readers.SimpleScript;
import ist.ssa.readers.SimpleSRT;
import ist.ssa.readers.SubtitleLine;
import ist.ssa.readers.TimeInfo;
import ist.ssa.textanalysis.Lemmatizer;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SSA {

    public static void main(String[] args) throws Exception {

    	if (args.length != 4) return;

    	String subtitleLocation = args[0];
    	String scriptLocation = args[1];
    	String newFilesLocation = args[2];
    	String subtitleOtherLanguageLocation = args[3];
    	
        //Translate.setClientId(Configuration.TRANSLATE_CLIENT_ID);
        //Translate.setClientSecret(Configuration.TRANSLATE_CLIENT_SECRET);
        //System.out.println(Translate.execute("bom", Language.PORTUGUESE, Language.ENGLISH));
        
        //TODO make a Script and a Subtitle object
        SimpleScript scriptReader = new SimpleScript(scriptLocation);
        SimpleSRT subtitleReader = new SimpleSRT(subtitleLocation);
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

        scriptReader.writeWholeScript(newFilesLocation);
        subtitleReader.writeWholeSubtitleFile(newFilesLocation);

//        SimpleSubtitleReader subtitleOtherLanguageReader = new SimpleSubtitleReader(xmlParser.getSubtitleOtherLanguageLocation());
//        subtitleOtherLanguageReader.read();
//
//        NWResults nwResultsTime = needlemanWunch.run(toValueObjectArray1(scriptReader.getScriptDialogs()),
//                toValueObjectArray2(subtitleOtherLanguageReader.getSubtitleLines()), 1, 0, 999999);
//
//        alignment.align(nwResultsTime.getValueObjectPairs());
//        alignment.translateScript(scriptReader, subtitleOtherLanguageReader);
//
//        scriptReader.writeWholeScriptAndTranslate(xmlParser.getNewFilesLocation());
    }

    private static TimeVO[] toValueObjectArray1(List<ScriptDialogLine> scriptDialogs) {
        List<TimeVO> timeValueObjects = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss,SSS");
        for (int i = 0; i < scriptDialogs.size(); i++) {
            ScriptDialogLine line = scriptDialogs.get(i);
            for (TimeInfo timeInfo : line.getTimeInfos()) {
                DateTime start = formatter.parseDateTime(timeInfo.getStartTime());
                DateTime end = formatter.parseDateTime(timeInfo.getEndTime());
                timeValueObjects.add(new TimeVO(i, new Interval(start, end)));
            }
        }
        TimeVO[] timeValueObjectsArray = new TimeVO[timeValueObjects.size()];
        return timeValueObjects.toArray(timeValueObjectsArray);
    }

    private static TimeVO[] toValueObjectArray2(List<SubtitleLine> subtitleLines) {
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