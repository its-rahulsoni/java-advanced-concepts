package multithreading.ping_pong_problem;

public class PingPongUsingThread {

    private final Object lock = new Object();
    private boolean isPingTurn = true; // Ping starts first

    public static void main(String[] args) {
        new PingPongUsingThread().startGame();
    }

    public void startGame() {
        new PingThread().start();
        new PongThread().start();
    }

    /**
     * Step-by-step Breakdown
     * 1. PingThread starts and acquires the lock.
     *
     * 2. isPingTurn == true, so it prints "Ping" and sets isPingTurn = false.
     *
     * 3. It calls notify() to wake PongThread, and goes back to wait().
     *
     * 4. PongThread wakes, sees isPingTurn == false, prints "Pong", sets isPingTurn = true.
     *
     * 5. This ping-pong continues.
     */
    class PingThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (!isPingTurn) {
                        try {
                            lock.wait(); // Not our turn
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("Ping");
                    isPingTurn = false;
                    lock.notify(); // Wake up Pong
                }
            }
        }
    }

    class PongThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (isPingTurn) {
                        try {
                            lock.wait(); // Not our turn
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("Pong");
                    isPingTurn = true;
                    lock.notify(); // Wake up Ping
                }
            }
        }
    }
}
