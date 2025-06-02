package multithreading.reader_write_problem;

// Main class to test
public class ReaderWriterProblem {
    public static void main(String[] args) {
        SharedData sharedData = new SharedData();

        // Start multiple reader threads
        for (int i = 1; i <= 3; i++) {
            Thread reader = new Thread(new Reader(sharedData, "Reader-" + i));
            reader.start();
        }

        // Start writer thread after a short delay
        new Thread(() -> {
            try {
                Thread.sleep(500); // Delay to simulate concurrent access
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            new Thread(new Writer(sharedData, "Writer-1", 100)).start();
        }).start();

        // Another reader after writer
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Wait for previous operations
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            new Thread(new Reader(sharedData, "Reader-4")).start();
        }).start();
    }
}