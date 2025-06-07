package multithreading.thread_safe_singleton;

public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {}

    /**
     * ✅ Why volatile?
     * Without volatile, instruction reordering could result in:
     *
     * 1. Allocating memory
     *
     * 2. Assigning reference
     *
     * 3. Then calling the constructor
     *
     * Other threads could see a partially constructed object.
     *
     * ✅ Efficient?
     * ✔ Yes — synchronization only occurs once, during the first call.
     */
    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * 📌 Step-by-Step Explanation
     * Let’s break it down:
     *
     * 🔹 Step 1: if (instance == null) — First Check
     * Why?
     * Avoid locking unless absolutely necessary. Improves performance when instance is already initialized.
     *
     * Multithreaded problem?
     * Yes — two threads might reach this point at the same time, both find instance == null, and both try to create the object!
     *
     * 🔹 Step 2: synchronized Block
     * This ensures that only one thread at a time can initialize the singleton.
     *
     * 🔹 Step 3: if (instance == null) — Second Check
     * After acquiring the lock, check again.
     *
     * Why?
     * Because another thread might have already created the instance while we were waiting to acquire the lock.
     *
     * 🔹 Step 4: instance = new DBConnectionManager();
     * Safely initialize the singleton only once.
     *
     * ⚠️ Why Is volatile Needed?
     * Without volatile, this pattern is broken.
     *
     * Here’s why:
     *
     * 🔧 Object creation in Java is not atomic (it's 3 steps):
     * instance = new DBConnectionManager();
     * Behind the scenes, this can be broken into:
     *
     * 1. Allocate memory
     *
     * 2. Call constructor
     *
     * 3. Assign reference to instance
     *
     * Because of instruction reordering (done by JVM or CPU for optimization), step 3 could happen before step 2, meaning:
     *
     * A thread might see a non-null instance that is not fully constructed yet!
     *
     * This results in partially initialized object access, causing hard-to-find bugs.
     *
     * ✅ volatile fixes this by:
     * Preventing instruction reordering.
     *
     * Ensuring visibility — when one thread writes to volatile, other threads immediately see it.
     *
     * ❌ What if we don’t use volatile?
     * This bug might happen in rare timing-sensitive conditions:
     *
     * Thread A starts creating the instance.
     *
     * Thread A assigns it to the instance variable before it's fully constructed (due to reordering).
     *
     * Thread B checks instance != null (first check passes) and returns the half-constructed object.
     *
     * Boom: ❌ unpredictable behavior.
     *
     * This might not happen every time — which makes the bug extremely hard to reproduce and debug.
     */
}
