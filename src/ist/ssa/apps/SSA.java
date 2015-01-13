package ist.ssa.apps;

import ist.ssa.Alignment;
import ist.ssa.Configuration;
import ist.ssa.content.Script;
import ist.ssa.content.Subtitles;
import ist.ssa.io.IMSDBScriptReader;
import ist.ssa.io.SRTSubtitlesReader;
import ist.ssa.io.WholeStoryWriter;
import ist.ssa.needleman.NWResult;
import ist.ssa.needleman.NeedlemanWunsch;
import ist.ssa.needleman.Span;
import ist.ssa.textanalysis.TextAnalyzer;

/**
 * Simple script-transcript aligner.
 */
public class SSA {

	/**
	 * This application loads a script and a transcript and aligns them. After
	 * alignment, data from one is shared with the other.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3)
			return;

		String scriptLocation = args[0];
		String transcriptLocation = args[1];
		String destinationDirectory = args[2];

		System.err.println("=== READING ===");
		
		Script script = new Script(new IMSDBScriptReader(scriptLocation));
		Subtitles subtitles = new Subtitles(new SRTSubtitlesReader(transcriptLocation));

		System.err.println("=== ANALYSING (TAGGING) ===");
		
		TextAnalyzer analyzer = new TextAnalyzer(Configuration.ANALYZER_PROPERTIES);
		Span[] scriptSpans = analyzer.modify(script);
		Span[] subtitlesSpans = analyzer.modify(subtitles);

		System.err.println("=== DOING NEEDLEDMAN-WUNCH ===");
		
		NeedlemanWunsch nw = new NeedlemanWunsch();
		NWResult nwTextResults = nw.run(scriptSpans, subtitlesSpans, 1, 0, 999999);

		System.err.println("=== ENHANCING ===");
		
		Alignment alignment = new Alignment();
		alignment.alignTextSpans(nwTextResults.getPairs(), analyzer, script, subtitles);
		alignment.enhanceScript(script, subtitles);
		alignment.enhanceSubtitles(script, subtitles);

		System.err.println("=== WRITING ===");
		
		WholeStoryWriter wholeStoryWriter = new WholeStoryWriter(destinationDirectory, scriptLocation);
		script.accept(wholeStoryWriter);
		subtitles.accept(wholeStoryWriter);

		System.err.println("=== DONE! ===");
		
	}

}