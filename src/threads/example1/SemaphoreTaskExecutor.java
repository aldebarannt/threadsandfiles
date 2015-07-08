package threads.example1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class SemaphoreTaskExecutor implements Runnable {

	private static final Map<String, Semaphore> lockMap = new ConcurrentHashMap<String, Semaphore>(new HashMap<String, Semaphore>());
	private final String taskName;
	
	public SemaphoreTaskExecutor(final String taskName) {
		this.taskName = taskName;
	}
	
	@Override
	public void run() {
		
		final Semaphore semaphore = getSemaphore(taskName);
		
		try {
			semaphore.acquire();
			System.out.println(semaphore);

			execute();
			
			semaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void execute() {
		System.out.println("Start task " + taskName);
		
		try { Thread.sleep(10000); }
		catch (InterruptedException e) { e.printStackTrace(); }
		
		System.out.println("Task " + taskName + " finished");
	}
	
	private Semaphore getSemaphore(final String taskName) {
		synchronized (lockMap) {
			if (!lockMap.containsKey(taskName)) {
				lockMap.put(taskName, new Semaphore(1));
			}
			
			return lockMap.get(taskName);
		}
	}

}
