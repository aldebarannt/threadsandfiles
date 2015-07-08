package tools.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class LogFile {
	
	private static final Logger log = Logger.getLogger(LogFile.class.getName());
	
	private final File file;
	
	public LogFile(final String fileDir) {
		file = new File(fileDir);
	}
	
	public void accept(final ILogLineVisitor logLineVisitor) {
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				
				int lineNumber = 0;
				String line;
				while ((line = reader.readLine()) != null) {
					lineNumber = lineNumber + 1;
					line = line.trim();
					
					if (lineNumber % 10000 == 0) {
//						System.out.print('.');
					}
					
					logLineVisitor.visit(lineNumber, line, false);
				}
				
				logLineVisitor.visit(lineNumber, "", true);
				
			} catch (IOException e) {
				log.severe(e.getMessage());
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
	}

}
