package multithreading.producer_consumer;

import java.util.LinkedList;

public class SharedResource {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int capacity = 2;

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {

            /**
             * It means the current thread has acquired the lock on the this object (here, the shared SharedResource instance).
             * Only one thread can execute inside this block at a time.
             */
            synchronized (this) {

                /**
                 * Case 1: List is full
                 *
                 *
                 * The producer checks if the list is full (capacity == 2).
                 *
                 * If yes, then:
                 *
                 * It prints a message.
                 *
                 * Calls notify() — to wake up the consumer, in case it's waiting for items to consume.
                 *
                 * Then calls wait() — the producer now waits, releasing the lock so the consumer can enter and consume an item.
                 *
                 * Important: Even though notify() is called, the consumer won’t proceed immediately. It must wait for the producer to wait() and release the lock.
                 */
                if (list.size() == capacity) {
                    System.out.println("List is full, producer is waiting...");
                    notify();
                    wait();
                }

                /**
                 * Case 2: List has space
                 *
                 * The producer adds a new value to the list.
                 *
                 * Prints what it produced.
                 *
                 * Calls notify() — to wake up the consumer, in case it’s waiting due to an empty list.
                 *
                 * Then sleeps for 1 second to simulate processing time.
                 */
                list.add(value);
                System.out.println("Producer produced - " + value++);
                notify();
                Thread.sleep(1000);
            }
        }
    }

    public void consume() throws InterruptedException {
        while (true) {

            /**
             * It means the current thread has acquired the lock on the this object (here, the shared SharedResource instance).
             * Only one thread can execute inside this block at a time.
             */
            synchronized (this) {
                if (list.isEmpty()) {
                    System.out.println("List is empty, consumer is waiting...");

                    /**
                     * 1. notify();
                     * It wakes up one thread that is waiting on the same object (this) — likely the Producer, if it was previously put to wait
                     * (e.g., when the list was full).
                     *
                     * However, the awakened thread won't immediately start running — it has to wait until the current thread (Consumer) exits the
                     * synchronized block and releases the lock.
                     *
                     * 2. wait();
                     * This puts the current thread (Consumer) into the waiting state, and it releases the lock on this.
                     *
                     * The awakened Producer thread can now enter the synchronized block and continue producing.
                     */
                    notify(); // notify() affects the other thread (Producer), if it’s waiting — tells it: "You can run soon."
                    wait(); // wait() affects the current thread (Consumer), putting it into a waiting state until another thread calls notify() and it reacquires the lock.
                }

                int value = list.removeFirst();
                System.out.println("Consumer consumed - " + value);
                notify();
                Thread.sleep(1000);
            }
        }
    }
}
