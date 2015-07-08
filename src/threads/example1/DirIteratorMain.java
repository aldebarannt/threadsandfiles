package threads.example1;

import java.io.File;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DirIteratorMain {

	private final static int THREAD_COUNT = 2;
	private final DirIterator dirIterator = new DirIterator();

	private void countDirectorySize(final File root) {
		final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
		final CompletionService<Long> taskCompletionService = new ExecutorCompletionService<Long>(executorService);

		try {
			System.out.println(DirIteratorMain.class.getName());

			final long start = System.nanoTime();

			dirIterator.iterate(root, taskCompletionService);
			
			System.out.println("iterate time: " + (System.nanoTime() - start));
			
			final long pollStart = System.nanoTime();
			
			Future<Long> future;
			Long count = new Long(0);
			while ((future = taskCompletionService.poll(5, TimeUnit.MILLISECONDS)) != null) {
				count += future.get();
			}
			
			System.out.println("poll time: " + (System.nanoTime() - pollStart));
			System.out.println("count: " + count + " in " + (System.nanoTime() - start)/1000 + " miliseconds");
			System.out.println("iterations: " + dirIterator.getIterations());
			System.out.println("file/dir check count: " + Counter.count.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
	}

	public static void main(String[] args) {
		final File root = new File("d:\\dev\\doosan");
		if (root.isDirectory()) {
			new DirIteratorMain().countDirectorySize(root);
		} else {
			System.out.println(String.format("File %s is not a directory.", root.getAbsolutePath()));
		}
	}

}
