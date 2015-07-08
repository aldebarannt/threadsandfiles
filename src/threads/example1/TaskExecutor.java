package threads.example1;

import java.util.HashMap;
import java.util.Map;

public class TaskExecutor implements Runnable {

	private static final Map<String, Object> lockMap = new HashMap<String, Object>();
	private final String taskName;
	
	public TaskExecutor(final String taskName) {
		this.taskName = taskName;
	}
	
	@Override
	public void run() {
		
		synchronized (acquireLock(taskName)) {
			execute();
		}
		
		releaseLock(taskName);
		
	}

	private void execute() {
		System.out.println("Start task " + taskName);
		
		try { Thread.sleep(10000); }
		catch (InterruptedException e) { e.printStackTrace(); }
		
		System.out.println("Task " + taskName + " finished");
	}
	
	private synchronized void releaseLock(final String taskName) {
		lockMap.remove(taskName);
	}

	private synchronized Object acquireLock(final String taskName) {
		if (!lockMap.containsKey(taskName)) {
			lockMap.put(taskName, new Object());
		}
		
		return lockMap.get(taskName);
	}

}
