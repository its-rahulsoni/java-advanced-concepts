Great! Let's dive into `CountDownLatch` in Java, one of the most useful synchronization utilities in `java.util.concurrent`.

---

## 🔒 What is `CountDownLatch`?

A `CountDownLatch` is used to make one or more threads wait until a **set of operations** being performed by other threads completes.

---

### 💡 Real-Life Analogy

Imagine you're in a rocket launch control room. The launch can’t happen until:

* ✅ Fuel system is ready
* ✅ Navigation system is ready
* ✅ Weather system is clear

Only after all 3 subsystems report “ready” can the rocket launch.

This is exactly what `CountDownLatch` helps with.

---

## 🧪 Code Example: Waiting for 3 Workers to Finish

### ✅ Worker threads do their job and call `countDown()`.

### ✅ Main thread waits on `await()` until count reaches 0.

```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

    public static void main(String[] args) throws InterruptedException {
        int numberOfWorkers = 3;
        CountDownLatch latch = new CountDownLatch(numberOfWorkers);

        for (int i = 1; i <= numberOfWorkers; i++) {
            final int id = i;
            new Thread(() -> {
                System.out.println("Worker " + id + " started working...");
                try {
                    Thread.sleep(1000 * id); // Simulate different work time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Worker " + id + " finished.");
                latch.countDown(); // Signal that this thread is done
            }).start();
        }

        System.out.println("Main thread waiting for workers to finish...");
        latch.await(); // Wait until count reaches 0
        System.out.println("✅ All workers finished. Main thread proceeding.");
    }
}
```

---

### 🟢 Output:

```
Main thread waiting for workers to finish...
Worker 1 started working...
Worker 2 started working...
Worker 3 started working...
Worker 1 finished.
Worker 2 finished.
Worker 3 finished.
✅ All workers finished. Main thread proceeding.
```

---

## 📌 Key Methods

| Method        | Description                                                   |
| ------------- | ------------------------------------------------------------- |
| `countDown()` | Decreases the count by 1. Called by workers.                  |
| `await()`     | Blocks until count reaches 0. Called by main/awaiting thread. |

---

## 🧠 Use Cases

* Waiting for multiple services or modules to initialize before starting main logic.
* Unit testing where you need to wait for async tasks to complete.
* Simulating countdowns or phases in workflows.

---

Would you like to see a use case with multiple phases (like loading, preparing, and starting a game)? Or using `CountDownLatch` with thread pools?
