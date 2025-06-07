package multithreading.count_down_latch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

    public static void main(String[] args) throws InterruptedException {
        int numberOfWorkers = 5;
        CountDownLatch latch = new CountDownLatch(numberOfWorkers);

        for (int i = 1; i <= numberOfWorkers; i++) {
            final int id = i;
            new Thread(() -> {
                System.out.println("Worker " + id + " started working...");
                try {
                    Thread.sleep(2000 * id); // Simulate different work time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Worker " + id + " finished.");
                latch.countDown(); // Signal that this thread is done ....
            }).start();
        }

        System.out.println("Main thread waiting for workers to finish...");
        latch.await(); // Wait until count reaches 0
        System.out.println("✅ All workers finished. Main thread proceeding.");
    }
}
