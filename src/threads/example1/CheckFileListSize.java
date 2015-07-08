package threads.example1;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

public class CheckFileListSize implements Callable<Long> {
	
	private final List<File> fileList;
	
	public CheckFileListSize(final List<File> fileList) {
		this.fileList = fileList;
	}

	@Override
	public Long call() throws Exception {
		Long count = new Long(0);
		
		for (final File file : fileList) {
			count += file.length();
		}
		
		return count;
	}

}
