package multithreading.count_down_latch;

import java.util.concurrent.*;

public class LatchWithThreadPoolExample {

    public static void main(String[] args) throws InterruptedException {
        int numTasks = 5;
        CountDownLatch latch = new CountDownLatch(numTasks);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= numTasks; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " started.");
                try {
                    Thread.sleep(1000L * taskId);  // simulate variable task time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " done.");
                latch.countDown();
            });
        }

        System.out.println("Main thread waiting for all tasks...");
        latch.await();  // Wait for all tasks to finish
        System.out.println("✅ All tasks done. Shutting down executor.");
        executor.shutdown();
    }
}
