package multithreading.thread_safe_singleton;

public class SynchronizedSingleton {
    private static SynchronizedSingleton instance;

    private SynchronizedSingleton() {}

    /**
     * ✅ Thread-safe?
     * ✔ Yes.
     *
     * ❌ Downside:
     * Synchronization happens every time getInstance() is called — even after the object is created.
     *
     * This leads to performance issues.
     */
    public static synchronized SynchronizedSingleton getInstance() {
        if (instance == null) {
            instance = new SynchronizedSingleton();
        }
        return instance;
    }
}
