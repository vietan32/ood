package com.concurrency;

class Bank {
    private double[] accounts;

    public synchronized void transfer(int from, int to, int amount)
            throws InterruptedException {
        while (accounts[from] < amount)
            wait(); // wait on intrinsic object lock's single condition
        accounts[from] -= amount;
        accounts[to] += amount;
        notifyAll(); // notify all threads waiting on the condition
    }

    public synchronized double getTotalBalance() {
        return 0.0;
    }
}
