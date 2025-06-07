package multithreading.thread_safe_singleton;

public class EagerSingleton {
    /**
     * ✅ Thread-safe?
     * ✔ Yes. JVM ensures class loading is thread-safe.
     *
     * ❌ Downside:
     * Instance is created even if never used (eagerly initialized).
     */
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return instance;
    }
}
