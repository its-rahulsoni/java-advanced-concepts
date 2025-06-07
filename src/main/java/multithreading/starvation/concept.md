## 🧠 Problem #6: Starvation in Java Multithreading

---

### ❓ What is Starvation?

**Starvation** happens when a thread is **perpetually denied access to resources** (like CPU time or locks) because other threads are constantly given preference.

In simpler terms:

> A thread is **ready to run**, but it never gets a chance to **actually run** because others keep cutting in line.

---

### 💡 Why does it happen?

* Threads with **higher priority** keep getting scheduled.
* **Fairness is not enforced** in locking mechanisms.
* One thread **holds a lock for too long**.
* Some threads are **never notified** or signaled.

---

## 🔬 Example of Starvation

We'll simulate a case where a **low-priority thread never gets to execute** because high-priority threads dominate CPU time.



### 🔍 What you'll observe:

Depending on your system, the **low-priority thread may run very little** or not at all — that’s starvation.

---

## ✅ Solution: Ensure Fair Access

### 1. Use **Fair Locks** (e.g., `ReentrantLock` with fairness)

* `ReentrantLock(true)` ensures that **threads get lock access in the order they requested it (FIFO)** — preventing starvation.
* 
---

### 2. Use **synchronized blocks carefully**

Ensure all threads have fair access to synchronized blocks and that **no thread hogs the lock** for too long.

---

### 3. Use **thread pool executors** with fairness

In concurrent applications, use thread pools (like `ExecutorService`) that control thread execution policy.

---

## 🧘 Summary

| Concept | Starvation                                                   |
| ------- | ------------------------------------------------------------ |
| Cause   | Thread is ready but never gets CPU or lock                   |
| Impact  | Thread becomes unresponsive                                  |
| Fix     | Fair locks, avoid priority abuse, thread pools with fairness |
