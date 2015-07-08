package threads.example1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirService {
	
	private Long visits = new Long(0); 
	
	void accept(final File root, final IDirVisitor visitor) {
		visits++;
		visitor.visit(root);

		final File[] files = root.listFiles();
		for (final File file : files) {
			if (file.isDirectory()) {
				accept(file, visitor);
			}
		}
	}
	
	void accept(final File root, final IFileListVisitor visitor) {
		final List<File> fileList = new ArrayList<File>();
		
		final File[] files = root.listFiles();
		for (final File file : files) {
			Counter.count.incrementAndGet();
			if (file.isFile()) {
				fileList.add(file);
			} else {
				accept(file, visitor);
			}
		}
		
		if (!fileList.isEmpty()) {
			visits++;
			visitor.visit(fileList);
		}
	}

	public Long getVisits() {	
		return visits;
	}

}
