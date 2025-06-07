package multithreading.count_down_latch;

import java.util.concurrent.CountDownLatch;

public class GameLaunchExample {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        Thread assetLoader = new Thread(() -> {
            simulateWork("Loading assets", 2);
            latch.countDown();
        });

        Thread networkInit = new Thread(() -> {
            simulateWork("Initializing network", 3);
            latch.countDown();
        });

        Thread authService = new Thread(() -> {
            simulateWork("Authenticating users", 1);
            latch.countDown();
        });

        // Start all systems
        assetLoader.start();
        networkInit.start();
        authService.start();

        System.out.println("Game engine waiting for subsystems...");
        latch.await();  // Wait for all subsystems
        System.out.println("🚀 All systems ready. Game started!");
    }

    private static void simulateWork(String task, int seconds) {
        System.out.println(task + "...");
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(task + " ✅ done");
    }
}
