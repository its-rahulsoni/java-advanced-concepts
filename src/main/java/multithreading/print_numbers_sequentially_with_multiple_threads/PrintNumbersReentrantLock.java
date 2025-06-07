package multithreading.print_numbers_sequentially_with_multiple_threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintNumbersReentrantLock {

    private static final int THREAD_COUNT = 3;
    private static final int MAX_NUM = 30;
    private int number = 1;
    private int turn = 0; // 0 → T1, 1 → T2, 2 → T3

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition[] conditions = new Condition[THREAD_COUNT];

    public static void main(String[] args) {
        PrintNumbersReentrantLock obj = new PrintNumbersReentrantLock();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int id = i;

            /**
             * 🔹 new Thread(...)
             * You're creating a new instance of the Thread class in Java.
             *
             * 🔹 () -> print(threadId)
             * This is a lambda expression — shorthand for implementing a Runnable interface.
             *
             * It means:
             *
             * new Runnable() {
             *     public void run() {
             *         print(threadId);
             *     }
             * }
             * So when the thread starts, it will call the print(threadId) method.
             *
             * 🔹 threadId
             * This is a unique number passed into each thread (0, 1, or 2) to determine which thread prints which number in round-robin order.
             *
             * 🔹 "Thread-" + (threadId + 1)
             * This gives the thread a custom name, like:
             *
             * "Thread-1" for threadId = 0
             *
             * "Thread-2" for threadId = 1
             *
             * "Thread-3" for threadId = 2
             *
             * Giving threads names helps debug and identify them in logs or outputs.
             *
             * 🔹 Full Meaning. So this line is:
             *
             * ✅ Creating a new thread
             * ✅ Giving it a print(threadId) task to run
             * ✅ Naming it "Thread-1", "Thread-2", etc.
             * ✅ Saving it to the thread variable so we can .start() it later
             *
             * 💬 Equivalent Without Lambda:
             *
             * Thread thread = new Thread(new Runnable() {
             *     @Override
             *     public void run() {
             *         print(threadId);
             *     }
             * }, "Thread-" + (threadId + 1));
             */
            new Thread(() -> obj.print(id), "Thread-" + (id + 1)).start();
        }
    }

    public PrintNumbersReentrantLock() {
        for (int i = 0; i < THREAD_COUNT; i++) {
            conditions[i] = lock.newCondition();
        }
    }

    public void print(int threadId) {
        while (true) {
            lock.lock();
            try {
                while (turn != threadId) {
                    conditions[threadId].await(); // wait if not this thread's turn
                }

                /**
                 * What this block does:
                 *
                 * 1. Check if we've finished printing all numbers.
                 * 2. Wake up all threads.
                 * 3. Break out of the current thread’s while-loop.
                 */
                if (number > MAX_NUM) {
                    // Signal next and break to avoid deadlock
                    for (Condition c : conditions) {
                        c.signal();
                    }
                    break;
                }

                System.out.println("Thread-" + (threadId + 1) + " printed: " + number++);
                turn = (turn + 1) % THREAD_COUNT; // Next thread's turn
                conditions[turn].signal(); // Wake up the next thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }
}
