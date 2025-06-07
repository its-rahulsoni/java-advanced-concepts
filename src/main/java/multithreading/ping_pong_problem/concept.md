Let's dive deep into **`Condition`** in Java.

---

## 🧩 What is `Condition` in Java?

A `Condition` is used **with a `Lock`** (like `ReentrantLock`) to allow threads to wait for some condition to become true. It is the **more flexible replacement** for `wait()`, `notify()`, and `notifyAll()` used with `synchronized`.

You can think of a `Condition` as a waiting room for threads.

### 🔧 How to use it?

* `await()` — similar to `wait()`
* `signal()` — similar to `notify()`
* `signalAll()` — similar to `notifyAll()`

> Unlike `wait()`/`notify()`, **you can create multiple conditions** per lock — each acting like its own "channel".

---

## 🧪 Simple Example: Bounded Buffer

Imagine a producer-consumer problem where:

* Producer waits if buffer is full.
* Consumer waits if buffer is empty.

We'll use `ReentrantLock` and `Condition` for coordination.

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 5;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();   // Wait when full
    private final Condition notEmpty = lock.newCondition();  // Wait when empty

    public void produce(int value) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                System.out.println("Buffer full. Producer waiting...");
                notFull.await(); // Wait until not full
            }
            queue.add(value);
            System.out.println("Produced: " + value);
            notEmpty.signal(); // Wake up one waiting consumer
        } finally {
            lock.unlock();
        }
    }

    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println("Buffer empty. Consumer waiting...");
                notEmpty.await(); // Wait until not empty
            }
            int value = queue.poll();
            System.out.println("Consumed: " + value);
            notFull.signal(); // Wake up one waiting producer
            return value;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer();

        // Producer thread
        Thread producer = new Thread(() -> {
            int value = 0;
            while (true) {
                try {
                    buffer.produce(value++);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    buffer.consume();
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
```

---

## 🔍 What’s Happening?

### When producer runs:

* If buffer is full, it waits on `notFull`.
* Else, it adds item and signals `notEmpty` to wake consumers.

### When consumer runs:

* If buffer is empty, it waits on `notEmpty`.
* Else, it removes item and signals `notFull` to wake producers.

---

## ✅ Why Use `Condition` over `wait()`/`notify()`?

| Feature                      | `wait()` / `notify()` | `Condition`                           |
| ---------------------------- | --------------------- | ------------------------------------- |
| Associated with              | `synchronized` block  | `ReentrantLock`                       |
| Multiple wait-queues         | ❌ Not supported       | ✅ Supported via multiple `Condition`s |
| Interruptible wait?          | Yes                   | Yes                                   |
| Fairness settings            | ❌                     | ✅ with fair `ReentrantLock`           |
| More control and flexibility | ❌                     | ✅                                     |

---

## 🔧 Real-World Use

* **Bounded queues**
* **Blocking resource access**
* **Thread pools**
* **Producer-consumer models**
* **Reader-writer locks**

---

Let's explore **why `Condition` was introduced in Java**, even though we already had `wait()` and `notify()`.

---

## 🔍 Quick Summary

| Feature                   | `wait()` / `notify()` (legacy) | `Condition` (modern)                                |
| ------------------------- | ------------------------------ | --------------------------------------------------- |
| Based on                  | `synchronized` keyword         | `Lock` interface (like `ReentrantLock`)             |
| Multiple wait queues?     | ❌ No                           | ✅ Yes — you can have multiple `Condition`s per lock |
| More flexible & readable? | ❌ No                           | ✅ Yes                                               |
| Interruptible waits?      | ✔️ Yes                         | ✔️ Yes                                              |
| Fine-grained control?     | ❌ No                           | ✅ Yes                                               |
| Spurious wakeups?         | ✔️ Yes, must use loops         | ✔️ Yes, must use loops                              |

---

## ✅ Reasons to Prefer `Condition` Over `wait()/notify()`

### 1. **Multiple wait queues per lock**

With `wait()`/`notify()`, all waiting threads share **one condition queue** per object lock. You cannot say *"wake only producers"* or *"wake only consumers"*. It leads to inefficiency.

But with `Condition`, you can have:

```java
Lock lock = new ReentrantLock();
Condition notEmpty = lock.newCondition();
Condition notFull = lock.newCondition();
```

This is especially useful in:

* Producer-consumer
* Bounded buffer
* Readers-writer locks

---

### 2. **Better control and structure**

* You use `lock.lock()` and `lock.unlock()` explicitly.
* You can separate concerns clearly:

    * `notFull.await()` → Producer waits when buffer is full
    * `notEmpty.await()` → Consumer waits when buffer is empty

Using `wait()/notify()`, you don’t have this kind of clear separation.

---

### 3. **No need to lock on the object itself**

With `wait()`/`notify()`, the object *is* the monitor.

```java
synchronized (obj) {
    obj.wait();
}
```

But with `Condition`, you can decouple locking and waiting logic:

```java
lock.lock();
try {
    notEmpty.await();
} finally {
    lock.unlock();
}
```

This makes the code **more modular and readable**.

---

### 4. **Fairness possible via `ReentrantLock(true)`**

With `Condition` (and `ReentrantLock`), you can enforce fairness in thread scheduling:

```java
Lock fairLock = new ReentrantLock(true); // true = fair scheduling
```

This isn't possible with `synchronized`.

---

### 5. **More explicit signaling**

Using `notify()` may wake up a thread not ready to proceed (wasteful).

But with separate `Condition`s, you can target exactly the right group of threads to wake up — no unnecessary wakeups.

---

## 🔚 Conclusion

### Use `Condition` when:

* You need **multiple conditions** (e.g., not full, not empty).
* You want **fine-grained thread coordination**.
* You need to **replace complex wait/notify code**.
* You use advanced concurrent APIs like **ReentrantLock**.

---
