package multithreading.thread_safe_singleton;

public class SimpleSingletonWithoutSync {

    private static SimpleSingletonWithoutSync instance;

    private SimpleSingletonWithoutSync() {}

    /**
     * Problem:
     * In multithreaded scenarios, two threads could simultaneously enter getInstance() and both create new instances.
     */
    public static SimpleSingletonWithoutSync getInstance() {
        if (instance == null) {
            instance = new SimpleSingletonWithoutSync();
        }
        return instance;
    }

}
