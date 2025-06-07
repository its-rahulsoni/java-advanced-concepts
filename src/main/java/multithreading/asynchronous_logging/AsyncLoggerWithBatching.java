package multithreading.asynchronous_logging;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncLoggerWithBatching {
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final ExecutorService loggerExecutor = Executors.newSingleThreadExecutor();
    private final File logFile = new File("async-logs.txt");
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    // Batching params
    private static final int BATCH_SIZE = 5;
    private static final int BATCH_TIME_MS = 2000;

    public AsyncLoggerWithBatching() {
        loggerExecutor.submit(this::consumeLogs);
    }

    public void log(String message) {
        logQueue.offer(formatLog(message));
    }

    public void shutdown() {
        isRunning.set(false);
        loggerExecutor.shutdown();
    }

    private void consumeLogs() {
        List<String> buffer = new ArrayList<>();
        long lastFlushTime = System.currentTimeMillis();

        try (FileWriter writer = new FileWriter(logFile, true)) {
            while (isRunning.get() || !logQueue.isEmpty()) {
                String msg = logQueue.poll(500, TimeUnit.MILLISECONDS);

                if (msg != null) {
                    buffer.add(msg);
                    System.out.println(msg); // ✅ Console log
                }

                long now = System.currentTimeMillis();
                if (buffer.size() >= BATCH_SIZE || (now - lastFlushTime >= BATCH_TIME_MS && !buffer.isEmpty())) {
                    for (String line : buffer) {
                        writer.write(line + "\n");
                    }
                    writer.flush();
                    buffer.clear();
                    lastFlushTime = now;
                }
            }

            // Flush remaining logs on shutdown
            for (String line : buffer) {
                writer.write(line + "\n");
            }
            writer.flush();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String formatLog(String message) {
        return "[" + Thread.currentThread().getName() + "][" + System.currentTimeMillis() + "] " + message;
    }

    public static void main(String[] args) throws InterruptedException {
        AsyncLogger logger = new AsyncLogger();

        ExecutorService appThreads = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            int id = i;
            appThreads.submit(() -> logger.log("Message from Thread " + id + " Thread Name: "  + Thread.currentThread().getName()));
        }

        appThreads.shutdown();
        appThreads.awaitTermination(5, TimeUnit.SECONDS);

        Thread.sleep(3000); // wait for logger to flush everything
        logger.shutdown();
    }
}
