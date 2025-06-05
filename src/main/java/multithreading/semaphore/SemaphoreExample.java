package multithreading.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample {
    private static final Semaphore semaphore = new Semaphore(3); // Only 3 threads allowed

    /**
     * What Is a Semaphore in Java?
     * A Semaphore is a concurrency utility that controls access to a resource by multiple threads.
     *
     * Semaphore semaphore = new Semaphore(int permits);
     * permits: number of allowed concurrent accesses (like number of parking spots or maximum no of threads allowed to use it.).
     *
     * Internals of Semaphore?
     * Internally, Semaphore uses a counter (number of permits).
     *
     * Each acquire() decrements the counter.
     *
     * If count < 0 → thread blocks.
     *
     * Each release() increments the counter and may wake a waiting thread.
     */
    public static void main(String[] args) {
        semaphoreWithoutTimeout();
       // semaphoreWithTimeout();
    }

    private static void semaphoreWithoutTimeout(){
        // Create 10 threads that try to access a resource
        for (int i = 1; i <= 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is trying to acquire permit...");
                    semaphore.acquire(); // Block if no permit available

                    System.out.println("Thread " + threadId + " acquired permit!");

                    // Simulate critical section
                    Thread.sleep(5000);

                    System.out.println("Thread " + threadId + " releasing permit.");
                    semaphore.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    private static void semaphoreWithTimeout(){
        // Create 10 threads that try to access a resource
        for (int i = 1; i <= 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is trying to acquire permit...");

                    if (semaphore.tryAcquire(2, TimeUnit.SECONDS)) {
                        System.out.println("Thread " + threadId + " acquired permit!");
                    } else {
                        System.out.println("Thread " + threadId + " Timed out!");
                    }

                    // Simulate critical section
                    Thread.sleep(5000);

                    System.out.println("Thread " + threadId + " releasing permit.");
                    semaphore.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

}
