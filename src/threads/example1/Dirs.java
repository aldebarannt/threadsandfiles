package threads.example1;

import java.io.File;

public class Dirs {
	
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
	
	public Long getVisits() {	
		return visits;
	}

}
