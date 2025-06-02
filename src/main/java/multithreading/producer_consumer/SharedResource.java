package multithreading.producer_consumer;

import java.util.LinkedList;

public class SharedResource {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int capacity = 2;

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (this) {
                if (list.size() == capacity) {
                    System.out.println("List is full, producer is waiting...");
                    notify();
                    wait();
                }

                list.add(value);
                System.out.println("Producer produced - " + value++);
                notify();
                Thread.sleep(1000);
            }
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (list.isEmpty()) {
                    System.out.println("List is empty, consumer is waiting...");
                    notify();
                    wait();
                }

                int value = list.removeFirst();
                System.out.println("Consumer consumed - " + value);
                notify();
                Thread.sleep(1000);
            }
        }
    }
}
