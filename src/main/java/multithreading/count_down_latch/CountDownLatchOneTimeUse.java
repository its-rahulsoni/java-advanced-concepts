package multithreading.count_down_latch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchOneTimeUse {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        Runnable task = () -> {
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished work.");
            latch.countDown();  // Decrease count
        };

        for (int i = 0; i < 3; i++) {
            new Thread(task).start();
        }

        latch.await();  // Main thread waits
        System.out.println("All workers done. Proceeding...");

        // Try reusing the same latch (won't work)
        latch.await();  // This will instantly return since count is already 0
        System.out.println("Trying to reuse latch (this happens immediately).");
    }
}

