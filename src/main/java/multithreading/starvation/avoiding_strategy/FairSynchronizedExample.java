package multithreading.starvation.avoiding_strategy;

public class FairSynchronizedExample {

    public static void main(String[] args) {
        SharedPrinter printer = new SharedPrinter();

        /**
         * Note: This is the way to create multiple threads using a for-loop ....
         */
        for (int i = 1; i <= 10; i++) {
            final String name = "Thread-" + i;
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    printer.print(name);
                }
            }).start();
        }
    }
}

class SharedPrinter {
    private final Object lock = new Object();

    /**
     * ✅ Why this prevents starvation:
     * 1. Lock is held only during critical section
     *
     * 2. Each thread gets repeated fair access
     *
     * 3. No nested locks or unnecessary delays
     */
    public void print(String threadName) {
        synchronized (lock) {
            System.out.println(threadName + " is printing...");
            try {
                Thread.sleep(100); // Simulate short work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(threadName + " done printing.");
        }
    }
}