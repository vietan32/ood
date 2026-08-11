package com.hellointerview.lrucache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache {
    private class Node {
        String key;
        String value;
        Node prev;
        Node next;

        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Map<String, Node> map;
    private final int capacity;
    private int size;
    private final Node tail, head;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.map = new HashMap<>();
        this.capacity = capacity;
        this.size = 0;
        this.tail = new Node(null, null);
        this.head = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public String get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        lock.writeLock().lock();
        try {
            Node n = map.get(key);
            if (n == null)
                return null;
            remove(n);
            addAfterHead(n);
            return n.value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void put(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }
        lock.writeLock().lock();
        try {
            if (map.containsKey(key)) {
                Node n = map.get(key);
                n.value = value;
                remove(n);
                addAfterHead(n);
            } else {
                Node n2 = new Node(key, value);
                addAfterHead(n2);
                map.put(key, n2);
                size++;
                if (size > capacity) {
                    Node oldest = tail.prev;
                    remove(oldest);
                    map.remove(oldest.key);
                    size--;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    // --
    private void addAfterHead(Node n) {
        Node nodeAfter = head.next;
        head.next = n;
        n.prev = head;
        n.next = nodeAfter;
        nodeAfter.prev = n;
    }

    private Node remove(Node n) {
        Node nodeBefore = n.prev;
        Node nodeAfter = n.next;
        nodeBefore.next = nodeAfter;
        nodeAfter.prev = nodeBefore;
        n.next = n.prev = null;
        return n;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Running LRUCache verification tests...");

        // Test 1: Basic Put and Get
        LRUCache cache = new LRUCache(2);
        cache.put("k1", "v1");
        cache.put("k2", "v2");
        assert "v1".equals(cache.get("k1")) : "Expected v1 for k1";
        assert "v2".equals(cache.get("k2")) : "Expected v2 for k2";
        System.out.println("Test 1 Passed: Basic Put and Get");

        // Test 2: Eviction of LRU item
        // Accessing k1 makes k2 the least recently used
        cache.get("k1");
        // Inserting k3 should evict k2
        cache.put("k3", "v3");
        assert cache.get("k2") == null : "Expected k2 to be evicted";
        assert "v1".equals(cache.get("k1")) : "Expected v1 for k1";
        assert "v3".equals(cache.get("k3")) : "Expected v3 for k3";
        System.out.println("Test 2 Passed: Eviction of LRU item");

        // Test 3: Updating existing key
        // Current state: k1 (MRU), k3 (LRU). Updating k3 to "v3_updated" should bring
        // it to MRU.
        cache.put("k3", "v3_updated");
        assert "v3_updated".equals(cache.get("k3")) : "Expected v3_updated for k3";
        // Inserting k4 should evict k1 (since k3 was recently updated)
        cache.put("k4", "v4");
        assert cache.get("k1") == null : "Expected k1 to be evicted";
        assert "v3_updated".equals(cache.get("k3")) : "Expected v3_updated for k3";
        assert "v4".equals(cache.get("k4")) : "Expected v4 for k4";
        System.out.println("Test 3 Passed: Updating existing key");

        // Test 4: Missing Key
        assert cache.get("non_existing") == null : "Expected null for non_existing key";
        System.out.println("Test 4 Passed: Missing Key lookup");

        // Test 5: Edge Case - Invalid Capacity
        try {
            new LRUCache(0);
            assert false : "Expected IllegalArgumentException for capacity <= 0";
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5 Passed: Invalid capacity validation");
        }

        // Test 6: Edge Case - Null Key / Null Value Validation
        try {
            cache.put(null, "val");
            assert false : "Expected IllegalArgumentException for null key in put";
        } catch (IllegalArgumentException e) {
            System.out.println("Test 6a Passed: Null key validation in put");
        }

        try {
            cache.put("key", null);
            assert false : "Expected IllegalArgumentException for null value in put";
        } catch (IllegalArgumentException e) {
            System.out.println("Test 6b Passed: Null value validation in put");
        }

        try {
            cache.get(null);
            assert false : "Expected IllegalArgumentException for null key in get";
        } catch (IllegalArgumentException e) {
            System.out.println("Test 6c Passed: Null key validation in get");
        }

        // Test 7: Edge Case - Capacity of 1
        LRUCache cacheCap1 = new LRUCache(1);
        cacheCap1.put("a", "1");
        assert "1".equals(cacheCap1.get("a"));
        cacheCap1.put("b", "2");
        assert cacheCap1.get("a") == null : "Expected 'a' to be evicted";
        assert "2".equals(cacheCap1.get("b"));
        System.out.println("Test 7 Passed: Single element capacity");

        // Test 8: Multithreaded Concurrency Stress Test
        final LRUCache concurrentCache = new LRUCache(100);
        final int numThreads = 10;
        final int opsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < opsPerThread; j++) {
                        String key = "key_" + (j % 200);
                        String value = "val_" + threadId + "_" + j;
                        concurrentCache.put(key, value);
                        concurrentCache.get("key_" + ((j + 50) % 200));
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        assert concurrentCache.size <= 100 : "Cache size exceeds capacity under concurrency";
        System.out.println("Test 8 Passed: Multithreaded Concurrency Stress Test");

        System.out.println("All tests passed successfully!");
    }
}
