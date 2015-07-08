package threads.example1;

import java.io.File;

public class Files {
	
	private Long visits = new Long(0);
	
	void accept(final File root, final IFileVisitor visitor) {
		if (root.isFile()) {
			visits++;
			visitor.visit(root);
		} else {
			final File[] files = root.listFiles();
			for (final File file : files) {
				accept(file, visitor);
			}
		}
	}
	
	public Long getVisits() {
		return visits;
	}

}
