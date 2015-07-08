package threads.example1;

import java.io.File;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FileVisitorMain {
	
	public static void main(String[] args) {
		final long start = System.nanoTime();
		
		final File root = new File("d:\\dev");
		
		final ExecutorService executorService = Executors.newFixedThreadPool(100);
		
		final CompletionService<Long> taskCompletionService = new ExecutorCompletionService<Long>(executorService);
		
		final Files files = new Files();
		
		try {
		
			files.accept(root, new IFileVisitor() {
				@Override
				public void visit(File file) {
					taskCompletionService.submit(new CheckFileSize(file));
				}
			});

			Integer reads = 0;
			Future<Long> future;
			Long count = new Long(0);
			while ((future = taskCompletionService.poll(5, TimeUnit.MILLISECONDS)) != null) {
				reads++;
				count += future.get();
			}
			
			System.out.println("count: " + count + " in " + (System.nanoTime() - start)/1000 + " miliseconds");
			System.out.println("iterations: " + files.getVisits());
			System.out.println("reads: " + reads);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
	}

}
