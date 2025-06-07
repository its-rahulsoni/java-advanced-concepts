package multithreading.starvation.avoiding_strategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FairThreadPoolExample {


    public static void main(String[] args) {

        /**
         * Java’s ExecutorService manages threads and prevents starvation by:
         *
         * 1. Managing a queue of tasks
         *
         * 2. Assigning tasks one at a time
         *
         * 3. Controlling the max number of threads
         */
        // Fixed thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 10 tasks submitted fairly
        for (int i = 1; i <= 10; i++) {
            executor.submit(new Task("Task-" + i));
        }

        executor.shutdown();
    }
}

class Task implements Runnable {
    private final String name;

    public Task(String name) {
        this.name = name;
    }

    /**
     * ✅ Why this prevents starvation:
     * 1. All tasks are queued and executed one by one
     *
     * 2. No task is given preferential treatment
     *
     * 3. Avoids manual thread creation, reducing CPU load and contention
     */
    @Override
    public void run() {
        System.out.println(name + " is starting work...");
        try {
            Thread.sleep(500); // Simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(name + " has finished work.");
    }
}