package threads.example1;

import java.io.File;
import java.util.concurrent.*;

public class CheckFileSize implements Callable<Long> {
	
	private final File file;
	
	public CheckFileSize(final File file) {
		this.file = file;
	}

	@Override
	public Long call() throws Exception {
		return file.length();
	}

}
