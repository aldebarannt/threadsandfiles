package threads.example1;

import java.io.File;
import java.util.concurrent.CompletionService;
import java.util.concurrent.atomic.AtomicLong;

public class FileIterator {
	
	private final AtomicLong iterations = new AtomicLong(0);
	
	void iterate(final File root, final CompletionService<Long> executorService) {
		iterations.incrementAndGet();
		
		Counter.count.incrementAndGet();
		if (root.isFile()) {
			executorService.submit(new CheckFileSize(root));
		} else {
			final File[] files = root.listFiles();
			for (final File file : files) {
				iterate(file, executorService);
			}
		}
	}
	
	public Long getIterations() {
		return iterations.get();
	}

}
