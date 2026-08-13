/**
Solution 1: Dynamic List + Binary Search:
    Time: hit() - O(1) amortized, get() - O(logN)
    Space: O(n) - Poor for long-running systems
Solution 2: Fixed-Size Bucket (Circular Array)
    Time: O(300) ~= O(1), Production-ready
    Space: O(300) != O(1)
*/

package com.leetcode;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DesignHitCounter {

    private final int[] times;
    private final int[] hits;

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public DesignHitCounter() {
        this.times = new int[300];
        this.hits = new int[300];
    }

    /** Record a hit. */
    public void hit(int timestamp) {
        rwLock.writeLock().lock();
        try {
            int idx = timestamp % 300;
            if (times[idx] != timestamp) {
                // New timestamp mapping to this bucket: reset bucket
                times[idx] = timestamp;
                hits[idx] = 1;
            } else {
                // Same timestamp already in bucket: aggregate hits
                hits[idx]++;
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /** Return the number of hits in the past 5 minutes (300 seconds). */
    public int getHits(int timestamp) {
        rwLock.readLock().lock();
        try {
            int totalHits = 0;
            for (int i = 0; i < 300; i++) {
                // Include bucket count only if timestamp is within the last 300 seconds
                if (timestamp - times[i] < 300) {
                    totalHits += hits[i];
                }
            }
            return totalHits;
        } finally {
            rwLock.readLock().unlock();
        }
    }
}

/*
 * 
 * ### Evaluation of Using `synchronized` on `hit()` and `getHits()`
 * 
 * Using `synchronized` on both methods **is functionally correct** and prevents
 * race conditions, memory visibility issues, and data corruption. However, in
 * terms of **performance under high concurrency**, there are major trade-offs
 * and potential bottlenecks.
 * 
 * ---
 * 
 * ### 1. The Problems / Performance Bottlenecks
 * 
 * 1. **Coarse-Grained Locking & High Lock Contention**
 * Using `synchronized` on both instance methods locks on `this` (the entire
 * `DesignHitCounter` instance).
 * **Write-Write Contention:** All concurrent incoming traffic threads trying to
 * record a `hit()` must queue up sequentially behind a single lock.
 * **Read-Write Contention:** Calling `getHits()` loops 300 times while holding
 * the monitor lock on `this`. While `getHits()` is executing its 300-step loop,
 * **all incoming `hit()` requests are completely blocked**.
 * 
 * 2. **Poor Read/Write Scalability**
 * `synchronized` is a **mutual exclusion lock (mutex)**: Readers block writers,
 * writers block readers, and readers block other readers.
 * In metrics monitoring systems, reads (`getHits`) and writes (`hit`) happen
 * constantly. Blocking writers during reads leads to latency spikes in logging
 * throughput.
 * 
 * ---
 * 
 * ### 2. How to Improve Performance & Scale Concurrency
 * 
 * Depending on the scale you want to explain to your interviewer, here are the
 * standard optimizations from good to best:
 * 
 * ---
 * 
 * #### Option A: `ReentrantReadWriteLock` (Better Read Throughput)
 * Allows multiple concurrent readers (`getHits`) to run simultaneously, while
 * ensuring exclusive access for writers (`hit`).
 * 
 * ```java
 * import java.util.concurrent.locks.ReentrantReadWriteLock;
 * 
 * public class DesignHitCounter {
 * private final int[] times = new int[300];
 * private final int[] hits = new int[300];
 * private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
 * 
 * public void hit(int timestamp) {
 * rwLock.writeLock().lock();
 * try {
 * int idx = timestamp % 300;
 * if (times[idx] != timestamp) {
 * times[idx] = timestamp;
 * hits[idx] = 1;
 * } else {
 * hits[idx]++;
 * }
 * } finally {
 * rwLock.writeLock().unlock();
 * }
 * }
 * 
 * public int getHits(int timestamp) {
 * rwLock.readLock().lock();
 * try {
 * int totalHits = 0;
 * for (int i = 0; i < 300; i++) {
 * if (timestamp - times[i] < 300) {
 * totalHits += hits[i];
 * }
 * }
 * return totalHits;
 * } finally {
 * rwLock.readLock().unlock();
 * }
 * }
 * }
 * ```
 * 
 * ---
 * 
 * #### Option B: Lock-Free Atomic Bucket Arrays (`AtomicIntegerArray`) (Best
 * Write Throughput)
 * For extreme write traffic (e.g. hundreds of thousands of QPS), lock
 * contention—even with `ReentrantReadWriteLock`—is expensive. We can use atomic
 * arrays or `AtomicIntegerArray` / `LongAdder` per bucket.
 * 
 * ```java
 * import java.util.concurrent.atomic.AtomicIntegerArray;
 * 
 * public class DesignHitCounter {
 * private final AtomicIntegerArray times = new AtomicIntegerArray(300);
 * private final AtomicIntegerArray hits = new AtomicIntegerArray(300);
 * 
 * public void hit(int timestamp) {
 * int idx = timestamp % 300;
 * // Spin/CAS loop to update timestamp and hit count atomically per bucket
 * while (true) {
 * int curTime = times.get(idx);
 * if (curTime != timestamp) {
 * if (times.compareAndSet(idx, curTime, timestamp)) {
 * hits.set(idx, 1);
 * break;
 * }
 * } else {
 * hits.incrementAndGet(idx);
 * break;
 * }
 * }
 * }
 * 
 * public int getHits(int timestamp) {
 * int totalHits = 0;
 * for (int i = 0; i < 300; i++) {
 * int time = times.get(i);
 * if (timestamp - time < 300) {
 * totalHits += hits.get(i);
 * }
 * }
 * return totalHits;
 * }
 * }
 * ```
 * 
 * ---
 * 
 * ### Key Takeaways for your Technical Deep Dive Discussion
 * 
 * 1. **Correctness vs Performance**: Mention that `synchronized` achieves
 * **thread safety**, but sacrifices **throughput** due to coarse locking on
 * `this`.
 * 2. **Read-Write Blocking**: Explain that `getHits()` looping 300 times
 * holding the lock blocks high-frequency `hit()` callers.
 * 3. **Locking Strategies**: Frame `ReentrantReadWriteLock` as the standard
 * step-up, and `AtomicIntegerArray` as the lock-free production strategy for
 * ultra-high throughput telemetry systems.
 */