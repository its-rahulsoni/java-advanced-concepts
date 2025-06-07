package multithreading.print_numbers_sequentially_with_multiple_threads;

public class PrintNumbersUsingSyncAndWaitNotify {
    private static final int MAX = 30;
    private static int number = 1;
    private static final Object lock = new Object();
    private static int turn = 0; // Thread turn tracker

    /**
     * 🧱 Core Idea:
     * Each thread:
     * 1. Enters a synchronized block on a shared lock object.
     *
     * 2. Checks if it's its turn to print.
     *
     * 3. If not, it waits.
     *
     * 4. If yes, it prints, increments the number, and notifies others.
     */
    public static void main(String[] args) {
        int totalThreads = 3;

        for (int i = 0; i < totalThreads; i++) {
            int threadId = i;
            Thread thread = new Thread(() -> printNumbers(threadId, totalThreads));
            thread.start();
        }
    }

    private static void printNumbers(int threadId, int totalThreads) {
        while (true) {
            synchronized (lock) {
                while (number <= MAX && turn != threadId) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ignored) {}
                }

                if (number > MAX) {
                    // Important: wake up all to let them check and exit
                    lock.notifyAll();
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " printed: " + number++);
                turn = (turn + 1) % totalThreads;

                lock.notifyAll(); // Wake up all waiting threads
            }
        }
    }
}
