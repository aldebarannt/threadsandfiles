package threads.example1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutorMain {

	private static final String[] tasks = "t1,t1,t2,t3,t4,t5,t6,t7,t8".split(",");
	
	public static void main(String[] args) {
		
		final ExecutorService executorService = Executors.newFixedThreadPool(4);
		
		for (String task: tasks) {
			executorService.execute(new SemaphoreTaskExecutor(task));
		}
		
	}
	
}
