package threads.example1;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

public class FileIteratorNoThreadsMain {

	private final AtomicLong iterations = new AtomicLong(0);
	
	private long dirSize = 0;
	
	void iterate(final File root) {
		iterations.incrementAndGet();
		
		Counter.count.incrementAndGet();
		if (root.isFile()) {
			dirSize += root.length();
		} else {
			final File[] files = root.listFiles();
			for (final File file : files) {
				iterate(file);
			}
		}
	}
	
	private void countDirectorySize(final File root) {
		
		System.out.println(this.getClass().getName());
		
		final long start = System.nanoTime();

		iterate(root);
		
		System.out.println("count: " + dirSize + " in " + (System.nanoTime() - start)/1000 + " miliseconds");
		System.out.println("iterations: " + iterations.get());
		System.out.println("file/dir check count: " + Counter.count.get());

	}
	
	public static void main(String[] args) {
		final File root = new File("d:\\dev\\doosan");
		if (root.isDirectory()) {
			new FileIteratorNoThreadsMain().countDirectorySize(root);
		} else {
			System.out.println(String.format("File %s is not a directory.", root.getAbsolutePath()));
		}
	}

}
