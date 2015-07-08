package threads.example1;

import java.io.File;
import java.util.List;

public interface IFileListVisitor {

	void visit(final List<File> fileList);
	
}
