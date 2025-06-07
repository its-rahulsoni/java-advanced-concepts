# 🧨 What Is a Race Condition?

A **Race Condition** occurs when:

> **Two or more threads access shared data concurrently, and the outcome depends on the timing of their execution.**

This leads to **unexpected or incorrect results**, because one thread might **override or interfere** with another thread's work.

---

## 🧪 Real-Life Analogy

Imagine two bank customers accessing the same ATM account at the same time:

* Balance: ₹500
* Both try to withdraw ₹300
* Both check balance before withdrawal
* Both see ₹500
* Both withdraw ₹300
* Final balance should be ₹200, but it becomes **₹-100**

**Why?** Because both operations read and wrote to shared balance without proper synchronization.

---

# 🧱 Code Example: Race Condition in Java

```java
public class RaceConditionExample {
    private int counter = 0;

    public void increment() {
        counter++; // NOT atomic
    }

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) throws InterruptedException {
        RaceConditionExample example = new RaceConditionExample();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                example.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                example.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Counter: " + example.getCounter());
    }
}
```

### 💥 Expected Output:

```
Final Counter: 20000
```

### ⚠️ But You Might Get:

```
Final Counter: 15836, 17492, or any random number < 20000
```

---

## 🧠 Why Does This Happen?

`counter++` is **not atomic**. It is actually 3 operations:

1. Read the value of `counter`
2. Add 1
3. Write the new value back

If two threads execute this sequence simultaneously, they can **interfere** with each other’s result.

---

# ✅ Solution 1: Synchronize the Method

Make the `increment()` method synchronized:

```java
public synchronized void increment() {
    counter++;
}
```

### ✅ Now output will always be:

```
Final Counter: 20000
```

But `synchronized` has **some performance overhead**.

---

# ✅ Solution 2: Use `ReentrantLock`

More flexible than `synchronized`:

```java
import java.util.concurrent.locks.ReentrantLock;

public class RaceConditionWithLock {
    private int counter = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }

    // rest same as earlier
}
```

🔐 Useful when you want finer control: tryLock, timeouts, or interruptible locking.

---

# ✅ Solution 3: Use Atomic Classes

Java provides `AtomicInteger` for atomic operations:

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicExample {
    private final AtomicInteger counter = new AtomicInteger(0);

    public void increment() {
        counter.incrementAndGet(); // Atomic
    }

    public int getCounter() {
        return counter.get();
    }

    // rest same
}
```

✅ This is very fast and thread-safe without explicit locking.

---

# 🔁 Summary

| Approach        | Thread-Safe | Performance | Recommended When... |
| --------------- | ----------- | ----------- | ------------------- |
| `synchronized`  | ✅           | Moderate    | Simple cases        |
| `ReentrantLock` | ✅           | Good        | Advanced locking    |
| `AtomicInteger` | ✅           | Best        | Simple counters     |
