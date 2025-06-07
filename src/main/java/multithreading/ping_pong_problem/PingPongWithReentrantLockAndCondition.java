package multithreading.ping_pong_problem;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PingPongWithReentrantLockAndCondition {

    /**
     * 🧩 What is Condition in Java?
     * A Condition is used with a Lock (like ReentrantLock) to allow threads to wait for some condition to become true. It is the more flexible replacement for wait(), notify(), and notifyAll() used with synchronized.
     *
     * You can think of a Condition as a waiting room for threads.
     *
     * 🔧 How to use it?
     * await() — similar to wait()
     *
     * signal() — similar to notify()
     *
     * signalAll() — similar to notifyAll()
     *
     * Unlike wait()/notify(), you can create multiple conditions per lock — each acting like its own "channel".
     */

    private final Lock lock = new ReentrantLock();
    private final Condition pingTurn = lock.newCondition();
    private final Condition pongTurn = lock.newCondition();

    private boolean isPingTurn = true;

    public static void main(String[] args) {
        new PingPongWithReentrantLockAndCondition().startGame();
    }

    public void startGame() {
        new PingThread().start();
        new PongThread().start();
    }

    class PingThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock();
                try {
                    while (!isPingTurn) {
                        pingTurn.await(); // Wait for Ping's turn
                    }
                    System.out.println("Ping");
                    isPingTurn = false;
                    pongTurn.signal(); // Wake up Pong
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class PongThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock();
                try {
                    while (isPingTurn) {
                        pongTurn.await(); // Wait for Pong's turn
                    }
                    System.out.println("Pong");
                    isPingTurn = true;
                    pingTurn.signal(); // Wake up Ping
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
