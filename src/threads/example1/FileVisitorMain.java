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
		final CompletionService<Long> completionService = new ExecutorCompletionService<Long>(executorService);
		
		final FileService fileService = new FileService();
		
		try {
		
			fileService.accept(root, new IFileVisitor() {
				@Override
				public void visit(File file) {
					completionService.submit(new CheckFileSize(file));
				}
			});

			Integer reads = 0;
			Future<Long> future;
			Long directorySize = new Long(0);
			while ((future = completionService.poll(5, TimeUnit.MILLISECONDS)) != null) {
				reads++;
				directorySize += future.get();
			}
			
			System.out.println("count: " + directorySize + " in " + (System.nanoTime() - start)/1000 + " miliseconds");
			System.out.println("iterations: " + fileService.getVisits());
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
