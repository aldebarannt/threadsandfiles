package threads.example1;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DirVisitorMain {
	
	private enum VISIT_METHOD {FILE, FILE_LIST};
	private final static int THREAD_NUM = 2;
	private final DirService dirService = new DirService();

	public static void main(String[] args) {
		final File root = new File("d:\\dev\\doosan");
		if (root.isDirectory()) {
			final DirVisitorMain dvm = new DirVisitorMain();
			dvm.evaluateDirectorySize(root, VISIT_METHOD.FILE);
			System.out.println();
			dvm.evaluateDirectorySize(root, VISIT_METHOD.FILE_LIST);
		} else {
			System.out.println(String.format("File % is not a directory", root.getAbsolutePath()));
		}
	}

	private void evaluateDirectorySize(final File root, final VISIT_METHOD method) {
		final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
		final CompletionService<Long> completionService = new ExecutorCompletionService<Long>(executorService);

		try {

			final long start = System.nanoTime();

			if (method == VISIT_METHOD.FILE) {
				dirService.accept(root, new IDirVisitor() {
					@Override
					public void visit(final File file) {
						completionService.submit(new CheckDirFileSize(file));
					}
				});
			} else {
				dirService.accept(root, new IFileListVisitor() {
					@Override
					public void visit(final List<File> fileList) {
						completionService.submit(new CheckFileListSize(fileList));
					}
				});
			}
			
			final long pollStart = System.nanoTime();

			Future<Long> future;
			Integer numberOfFilesRead = 0;
			Long directorySize = new Long(0);
			while ((future = completionService.poll(5, TimeUnit.MILLISECONDS)) != null) {
				numberOfFilesRead++;
				directorySize += future.get();
			}
			
			final long end = System.nanoTime();
			
			System.out.println(String.format("Class: %s", this.getClass().getSimpleName()));
			System.out.println(String.format("Method: %s", method));
			System.out.println(String.format("Visit time: %s[ms]", (pollStart - start)/1000));
			System.out.println(String.format("Poll time: %s[ms]", (end - pollStart)/1000));
			System.out.println(String.format("Time: %s[ms]", (end - start)/1000));
			System.out.println(String.format("Directory size: %s[kB]", directorySize/1024));
			System.out.println(String.format("Visits: %s", dirService.getVisits()));
			System.out.println(String.format("Reads: %s", numberOfFilesRead));
			System.out.println(String.format("File/dir check count: %s", Counter.count.get()));

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
	}

}
