package threads.example1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.atomic.AtomicLong;

public class DirIterator {
	
	private final AtomicLong iterations = new AtomicLong(0); 
	
	void iterate(final File root, final CompletionService<Long> executorService) {
		iterations.incrementAndGet();
		
		final List<File> fileList = new ArrayList<File>();
		
		final File[] files = root.listFiles();
		for (final File file : files) {
			Counter.count.incrementAndGet();
			if (file.isFile()) {
				fileList.add(file);
			} else {
				iterate(file, executorService);
			}
		}
		
		if (!fileList.isEmpty()) {
			executorService.submit(new CheckFileListSize(fileList));
		}
	}
	
	public Long getIterations() {
		return iterations.get();
	}

}
