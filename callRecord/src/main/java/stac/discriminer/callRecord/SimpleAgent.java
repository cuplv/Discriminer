package stac.discriminer.callRecord;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

public class SimpleAgent {

	public static void premain(String args, Instrumentation instrumentation) {
//		snapBuddyTransformer transformer = new snapBuddyTransformer();
//		gabFeedTransformer transformer = new gabFeedTransformer();
//		textCrunchrTransformer transformer = new textCrunchrTransformer();
//		benchmarkTransformer transformer = new benchmarkTransformer();
		malwareAnalysis transformer = new malwareAnalysis();
	    instrumentation.addTransformer(transformer);
	}
}
