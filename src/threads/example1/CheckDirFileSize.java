package threads.example1;

import java.io.File;
import java.util.concurrent.*;

public class CheckDirFileSize implements Callable<Long> {
	
	private final File file;
	
	public CheckDirFileSize(final File file) {
		this.file = file;
	}

	@Override
	public Long call() throws Exception {
		Long count = new Long(0);
		
		final File[] files = file.listFiles();
		for (final File file : files) {
			Counter.count.incrementAndGet();
			if (file.isFile()) {
				count += file.length();
			}
		}
		
		return count;
	}

}
