package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.MultiValueMap;

import tools.analizers.AbstractAnalizer.Data;
import tools.analizers.filters.FilterChain;
import tools.analizers.filters.IFilter;
import tools.analizers.filters.IFilterChain;
import tools.analizers.filters.LastLineFilter;
import tools.analizers.filters.MaxLinesFilter;
import tools.analizers.filters.RootCauseFilter;
import tools.analizers.ExceptionAnalizer;
import tools.analizers.ExceptionFilteredAnalizer;
import tools.analizers.ExceptionRootCauseAnalizer;
import tools.analizers.ExceptionWithNextLineRootCauseAnalizer;
import tools.analizers.IAnalizer;
import tools.log.LogFile;

public class Main {
	
	private static final String DOCUMENT_EXCEPTION_UNKNOWN_PROTOCOL_REGEX = "(^)(org\\.dom4j\\.DocumentException\\: unknown protocol)(.*?)($)";
	private static final String DOWNLOAD_MEDIA_BIN_ASSET_VALIDATION_EXCEPTION__REGEX = "(^)(.*?)(DownloadMediaBinAsset\\$ValidationException)(.*?)($)";
	private static final String NO_SUCH_ELEMENT_EXCEPTION_REGEX = "(^)(.*?)(ERROR)(.*?)(NoSuchElementException)(.*?)($)";
	private static final String NUMBER_FORMAT_EXCEPTION_REGEX = "(^)(.*?)(java.lang.NumberFormatException: For input string:)(.*?)($)";
	private static final String NOT_SERIALIZABLE_EXCEPTION_REGEX = "(^)(java.io.NotSerializableException)(.*?)($)";
	private static final String RUNTIME_EXCEPTION_REGEX = "(^)(java\\.lang\\.RuntimeException\\: )(.*?)($)";
	private static final String DOCUMENT_EXCEPTION_REGEX = "(^)(org\\.dom4j\\.DocumentException\\: )(\\/)(.*?)($)";
	private static final List<IAnalizer> analizers = new ArrayList<IAnalizer>();
	private static int numberOfLinesInOneFile;
	private static int numberOfLines;
	private static int depth = 1;
	
	static {
//		analizers.add(new ExceptionRootCauseAnalizer("(^)(java\\.lang\\.RuntimeException\\: Failed to execute rules and externals)(.*?)($)", "$0", 3));
//		analizers.add(new ExceptionRootCauseAnalizer("(^)(org\\.dom4j\\.DocumentException\\: unknown protocol)(.*?)($)", "$2", 3));
//		analizers.add(new ExceptionRootCauseAnalizer("(^)(org\\.dom4j\\.DocumentException\\: \\/)(.*?)($)", "$2", 2));
//		analizers.add(new ExceptionRootCauseAnalizer("(^)(.*?)(java.lang.NumberFormatException: For input string:)(.*?)($)", "$3", 3));
//		analizers.add(new ExceptionWithNextLineRootCauseAnalizer("(^)(.*?)(ERROR)(.*?)(Exception in writing targetting log file)(.*?)($)", "$3$4$5", 3));
//		analizers.add(new ExceptionRootCauseAnalizer("(^)(.*?)(ERROR)(.*?)(NoSuchElementException)(.*?)($)", "$3$4$5", 3));
//		analizers.add(new ExceptionRootCauseAnalizer("(^)(.*?)(DownloadMediaBinAsset\\$ValidationException)(.*?)($)", "$3$4", 3));
//		analizers.add(new ExceptionRootCauseAnalizer("(^)(java.io.NotSerializableException)(.*?)($)", "$0", 3));
		
//		analizers.add(new ExceptionRootCauseAnalizer(RUNTIME_EXCEPTION_REGEX, "$0", 0));
//		analizers.add(new ExceptionAnalizer(DOCUMENT_EXCEPTION_REGEX, "$0", 0));
		
//		analizers.add(new ExceptionAnalizer(RUNTIME_EXCEPTION_REGEX, "$0", 2));
//		analizers.add(new ExceptionAnalizer("(^)(org\\.dom4j\\.DocumentException\\: unknown protocol)(.*?)($)", "$2", 3));
//		analizers.add(new ExceptionAnalizer(DOCUMENT_EXCEPTION_REGEX, "$2", 3));
//		analizers.add(new ExceptionAnalizer("(^)(.*?)(java.lang.NumberFormatException: For input string:)(.*?)($)", "$3", 3));
//		analizers.add(new ExceptionAnalizer("(^)(.*?)(ERROR)(.*?)(NoSuchElementException)(.*?)($)", "$3$4$5", 3));
//		analizers.add(new ExceptionAnalizer("(^)(.*?)(DownloadMediaBinAsset\\$ValidationException)(.*?)($)", "$3$4", 3));
//		analizers.add(new ExceptionAnalizer("(^)(java.io.NotSerializableException)(.*?)($)", "$0", 3));
		
//		analizers.add(new ExceptionRootCauseAnalizer(RUNTIME_EXCEPTION_REGEX, "$0", 0));
//		analizers.add(new ExceptionRootCauseAnalizer(DOCUMENT_EXCEPTION_UNKNOWN_PROTOCOL_REGEX, "$2", depth));
//		analizers.add(new ExceptionRootCauseAnalizer(DOCUMENT_EXCEPTION_REGEX, "$2<some xpath>", 10));
//		analizers.add(new ExceptionRootCauseAnalizer(NUMBER_FORMAT_EXCEPTION_REGEX, "$3", depth));
//		analizers.add(new ExceptionRootCauseAnalizer(NO_SUCH_ELEMENT_EXCEPTION_REGEX, "$3$4$5", depth));
//		analizers.add(new ExceptionAnalizer(DOWNLOAD_MEDIA_BIN_ASSET_VALIDATION_EXCEPTION__REGEX, "$2$3$4", depth));
//		analizers.add(new ExceptionAnalizer(NOT_SERIALIZABLE_EXCEPTION_REGEX, "$0", depth));
		
//		final FilterChain filterChain = new FilterChain();
//		filterChain.add(new RootCauseFilter());
//		filterChain.add(new MaxLinesFilter(8));
//		filterChain.add(new LastLineFilter());
//		analizers.add(new ExceptionFilteredAnalizer(DOCUMENT_EXCEPTION_REGEX, "$2<some xpath>", filterChain));
		
		final FilterChain filterChain = new FilterChain();
//		filterChain.add(new RootCauseFilter());
		filterChain.add(new MaxLinesFilter(0));
//		filterChain.add(new LastLineFilter());
		analizers.add(new ExceptionFilteredAnalizer(RUNTIME_EXCEPTION_REGEX, "$0", filterChain));
		analizers.add(new ExceptionFilteredAnalizer(DOCUMENT_EXCEPTION_UNKNOWN_PROTOCOL_REGEX, "$2", filterChain));
		analizers.add(new ExceptionFilteredAnalizer(DOCUMENT_EXCEPTION_REGEX, "$2<some xpath>", filterChain));
		analizers.add(new ExceptionFilteredAnalizer(NUMBER_FORMAT_EXCEPTION_REGEX, "$3", filterChain));
		analizers.add(new ExceptionFilteredAnalizer(NO_SUCH_ELEMENT_EXCEPTION_REGEX, "$3$4$5", filterChain));
		analizers.add(new ExceptionFilteredAnalizer(DOWNLOAD_MEDIA_BIN_ASSET_VALIDATION_EXCEPTION__REGEX, "$2$3$4", filterChain));
		analizers.add(new ExceptionFilteredAnalizer(NOT_SERIALIZABLE_EXCEPTION_REGEX, "$0", filterChain));
		analizers.add(new ExceptionFilteredAnalizer("(^)(java.io.FileNotFoundException)(.*?)(\\}\\/)(.*)($)", "$2 - $5", filterChain));
	}
	
	public static void main(String[] args) {

//		try {
//			test();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		numberOfLines = 0;
		
		System.out.println("------------------------------------------------------------");
		System.out.println("Scanning");
		System.out.println("------------------------------------------------------------");
		System.out.println();
		
		scanLogFiles();
		
		System.out.println();
		System.out.println("------------------------------------------------------------");
		System.out.println("Results");
		System.out.println("------------------------------------------------------------");

		printResult("D:\\dev\\doosan\\logs\\prod.livesite\\loganalize.txt");
		
	}

	private static void test() {
		throw new RuntimeException(new NullPointerException());
	}

	private static void scanLogFiles() {
//		analizeLogFile("D:\\dev\\doosan\\tmp\\iwjboss.log");
//		analizeLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\test.livesite.runtime.log");
		
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log");
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log.2015-04-13");
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log.2015-04-12");
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log.2015-04-11");
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log.2015-04-10");
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log.2015-04-09");
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log.2015-04-08");
//		scanLogFile("D:\\dev\\doosan\\logs\\dev.livesite\\livesite.runtime.log.2015-04-07");

//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log");
//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log.2015-04-13");
//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log.2015-04-12");
//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log.2015-04-11");
//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log.2015-04-10");
//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log.2015-04-09");
//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log.2015-04-08");
//		scanLogFile("D:\\dev\\doosan\\logs\\uat.livesite\\livesite.runtime.log.2015-04-07");

//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\livesite.runtime.log");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\livesite.runtime.log.2015-03-16");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\livesite.runtime.log.2015-03-15");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\livesite.runtime.log.2015-03-14");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\livesite.runtime.log.2015-03-13");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\livesite.runtime.log.2015-03-12");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite\\livesite.runtime.log.2015-03-11");

//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log.2015-04-13");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log.2015-04-12");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log.2015-04-11");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log.2015-04-10");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log.2015-04-09");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log.2015-04-08");
//		scanLogFile("D:\\dev\\doosan\\logs\\prod.livesite.1\\livesite.runtime.log.2015-04-07");
		
		scanLogDirectory(Paths.get("D:\\dev\\doosan\\logs\\prod.livesite.2"));

	}
	
	private static void scanLogDirectory(final Path path) {
		final boolean isDir = Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
		if (isDir) {
			try {
				final DirectoryStream<Path> files = Files.newDirectoryStream(path, 
					p -> Files.isRegularFile(p, LinkOption.NOFOLLOW_LINKS));
				files.forEach(p -> scanLogFile(p.toFile().getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.print(path + " is not a directory");
		}
	}

	private static void scanLogFile(final String inputFileDir) {
		final LogFile logFile = new LogFile(inputFileDir);
		
		System.out.println("File: " + inputFileDir);
		
		logFile.accept((lineNumber, line, eof) -> {
			for (IAnalizer analizer : analizers) {
				if (eof) {
					analizer.close(lineNumber);
				} else {
					analizer.analize(lineNumber, line);
				}
			}
			numberOfLinesInOneFile = lineNumber;
		});
		
		numberOfLines += numberOfLinesInOneFile;
	}

	private static void printResult(
		final String outputFileDir)
	{
		
		final Map<Data, Collection<String>> innerMap = new TreeMap<Data, Collection<String>>((o1, o2) -> {
			return countsEqual(o1, o2) ? compareLines(o1, o2) : compareCounts(o1, o2);
		});

		final MultiValueMap<Data, String> sortedMap = MapUtils.<Data, String>multiValueMap(innerMap);

		analizers.forEach(analizer -> {
			analizer.getResult().forEach((key, value) -> {
				sortedMap.put(value, key);
			});
		});
		
		printResultToSystemOut(sortedMap);
		
		printResultToFile(outputFileDir, sortedMap);

	}

	private static void printResultToFile(
		final String outputFileDir,
		final MultiValueMap<Data, String> sortedMap)
	{
		BufferedWriter bufferedWriter = null;
		try {
			final File outputFile = new File(outputFileDir);
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			
			bufferedWriter = new BufferedWriter(new FileWriter(outputFile, false));
			
			writeResultEntryToFile(bufferedWriter, sortedMap);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try { bufferedWriter.close(); } catch (IOException e) { e.printStackTrace(); }
			}
		}
	}
	
	private static void printResultToSystemOut(
		final MultiValueMap<Data, String> sortedMap)
	{
		final StringBuilder builder = new StringBuilder();
		
		final Counter linesCounter = new Counter();
		sortedMap.forEach((data, object) -> {
			linesCounter.add(data.getLines());
			
			final Collection<String> collection = asCollection(object);		
			collection.forEach(message -> {
				formatMessage(builder, data, message);
				writeToSyso(builder);
			});
		});
		
		formatSummary(builder, linesCounter);
		writeToSyso(builder);
	}

	private static void formatMessage(
		final StringBuilder builder, 
		final Data data,
		final String message)
	{
		builder.setLength(0);
		builder.append("\n");
		builder.append(data.getCount() + " (" + data.getLines() + " lines)");
		builder.append("\n");
		builder.append(message);
	}

	private static void writeToSyso(
		final StringBuilder builder)
	{
		System.out.println(builder.toString());
	}

	@SuppressWarnings("unchecked")
	private static Collection<String> asCollection(Object object) {
		final Collection<String> collection = (Collection<String>) object;
		return collection;
	}

	private static void writeResultEntryToFile(
		final BufferedWriter bufferedWriter,
		final MultiValueMap<Data, String> sortedMap)
		throws IOException
	{
		final StringBuilder builder = new StringBuilder();
		
		final Counter linesCounter = new Counter();
		for (Map.Entry<Data, Object> entry : sortedMap.entrySet()) {
			linesCounter.add(entry.getKey().getLines());
			
			final Collection<String> collection = asCollection(entry.getValue());		
			for (String message : collection) {
				formatMessage(builder, entry.getKey(), message);
				writeToFile(bufferedWriter, builder);
			}
		}
		
		formatSummary(builder, linesCounter);
		writeToFile(bufferedWriter, builder);
	}

	private static void formatSummary(
		final StringBuilder builder,
		final Counter linesCounter)
	{
		builder.setLength(0);
		builder.append("\n------------------------------------------------------------");
		builder.append("\nSummary: ");
		builder.append(linesCounter.count);
		builder.append(" error lines in ");
		builder.append(numberOfLines);
		builder.append(" total lines (");
		builder.append(Math.round(100 * linesCounter.count / numberOfLines));
		builder.append("%)");
		builder.append("\n");
		builder.append("------------------------------------------------------------");
	}

	private static void writeToFile(
		final BufferedWriter bufferedWriter,
		final StringBuilder builder)
		throws IOException
	{
		bufferedWriter.write(builder.toString());
	}

	private static int compareLines(Data o1, Data o2) {
		return o2.getLines() - o1.getLines();
	}

	private static int compareCounts(Data o1, Data o2) {
		return o2.getCount() - o1.getCount();
	}

	private static boolean countsEqual(Data o1, Data o2) {
		return o2.getCount() == o1.getCount();
	}

	private static class Counter {
		int count = 0;
	
		public void add(int value) {
			count += value;
		}
	}

}
