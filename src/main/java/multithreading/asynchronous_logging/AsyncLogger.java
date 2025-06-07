package multithreading.asynchronous_logging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

public class AsyncLogger {
    /**
     * A thread-safe queue where producer threads put log messages.
     *
     * We're using LinkedBlockingQueue, which can grow dynamically.
     *
     * BlockingQueue handles synchronization internally.
     */
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final ExecutorService loggerExecutor = Executors.newSingleThreadExecutor();

    /**
     * 1. A flag used to signal the logger thread to shut down gracefully.
     * 2. Declared volatile so all threads see updated value immediately.
     */
    private volatile boolean isRunning = true;

    /**
     * Name of the file where logs will be written.
     */
    private final String logFile = "logs.txt";

    /**
     * Contructor:
     * When the AsyncLogger is instantiated, it immediately starts the logger thread using submit(), passing the method reference consumeLogs.
     */
    public AsyncLogger() {

        /**
         * Normally, ExecutorService.submit() expects an object of type Runnable (or Callable).
         *
         * QUES: So how does this::consumeLogs work?
         * ANS: this::consumeLogs is a method reference, and Java 8+ allows you to pass it wherever a Runnable is expected as long as the
         * method signature matches.
         *
         * In our case:
         * private void consumeLogs()
         * 1. Has No arguments
         * 2. Returns void
         *
         * This matches the Runnable.run() method signature:
         * @FunctionalInterface
         * public interface Runnable {
         *     void run();
         * }
         *
         * ✅ Behind the scenes, Java turns:
         * loggerExecutor.submit(this::consumeLogs);
         *
         * Into something like:
         * loggerExecutor.submit(new Runnable() {
         *     public void run() {
         *         consumeLogs();
         *     }
         * });
         *
         * Or even:
         * loggerExecutor.submit(() -> consumeLogs());
         */
        System.out.println("AsyncLogger constructor gets called.");
        loggerExecutor.submit(this::consumeLogs); // using another Thread to start log consumption ....

        /**
         * 🚫 Calling consumeLogs() Directly
         * If you write:
         * consumeLogs();
         *
         * You're simply calling the method in the main thread (or whatever thread instantiated the object). That means:
         *
         * 1. The main thread will block and run the consumeLogs() method.
         *
         * 2. If consumeLogs() is a long-running or infinite loop (like in an async logger), your application may hang or become unresponsive.
         *
         * 3. It defeats the purpose of making logging asynchronous.
         *
         * ✅ Using loggerExecutor.submit(this::consumeLogs);
         *
         * This:
         * loggerExecutor.submit(this::consumeLogs);
         *
         * 1. Submits the method to a background thread, separating log consumption from the main flow.
         *
         * 2. Allows your main application to continue running while log entries are processed in parallel.
         *
         * 3. Is non-blocking to the constructor or caller.
         *
         * 4. Enables asynchronous logging, which improves performance and responsiveness.
         *
         * 📌 Analogy
         * Think of:
         *
         * 1. consumeLogs() as a waiter refilling glasses.
         *
         * 2. submit(this::consumeLogs) as assigning that task to a new staff member (thread).
         *
         * 3. consumeLogs() directly — you're doing the refilling yourself and stopping everything else.
         */
    }

    /**
     * 1. This is what producer threads will call.
     *
     * 2. put() blocks if the queue is full (we're not setting a limit here, so it's unlikely).
     *
     * 3. InterruptedException is handled to restore the interrupt flag.
     */
    public void log(String message) {
        try {
            // This Queue is a LinkedBlockingQueue ....
            logQueue.put(message); // blocks if full (backpressure)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void consumeLogs() {
        System.out.println("consumeLogs() thread started: " + Thread.currentThread().getName());

        try (FileWriter writer = new FileWriter(logFile, true)) {

            /**
             * QUES: Why It's Still Processing Logs After logger.shutdown() (main method)?
             * ANS: You call logger.log(...) → messages go into the logQueue.
             *
             * You call logger.shutdown() → isRunning = false.
             *
             * But app threads may still be running, continuing to call logger.log(...) and adding more messages to the queue after shutdown was initiated.
             *
             * Since !logQueue.isEmpty() is true, the consumer loop keeps going.
             *
             * This happens because:
             *
             * You shut down the logger but not the producer threads.
             *
             * The queue is still being filled.
             */
            /**
             * This INFINITE LOOP is responsible for this Consumer running till either the Executor Service Shuts down
             * or the Queue is Empty ....
             */
            while (isRunning || !logQueue.isEmpty()) {
                String msg = logQueue.poll(1, TimeUnit.SECONDS); // timeout avoid permanent block
                System.out.println("msg from logQueue: " + msg);
                if (msg != null) {
                    writer.write(msg + "\n");
                    writer.flush(); // ensure it reaches disk
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        isRunning = false;
        loggerExecutor.shutdown();
    }

    // Usage example ....
    public static void main(String[] args) throws InterruptedException {
        AsyncLogger logger = new AsyncLogger();

        // Simulate multiple threads logging
        ExecutorService appThreads = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            int id = i;

            /**
             * () -> logger.log(...) is a lambda expression that implements Runnable.
             *
             * So this lambda is treated as a Runnable object.
             *
             * The submit() method then schedules it for execution in a thread from the pool.
             *
             * Equivalent to:
             * appThreads.submit(new Runnable() {
             *     public void run() {
             *         logger.log("Log message from thread " + id);
             *     }
             * });
             */
            appThreads.submit(() -> logger.log("Log message from thread " + id + " Thread Name: "  + Thread.currentThread().getName()));
        }

        appThreads.shutdown();
        appThreads.awaitTermination(5, TimeUnit.SECONDS);

        /**
         * Tell the consumer thread to stop accepting new log entries.
         *
         * Finish processing anything remaining in the queue.
         *
         * Exit the thread cleanly.
         */
        logger.shutdown();
        System.out.println("Logging completed.");
    }
}

