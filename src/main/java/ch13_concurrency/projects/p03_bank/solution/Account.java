package ch13_concurrency.projects.p03_bank.solution;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SOLUTION - un compte et SON verrou. Le solde n'est lu ou modifie que verrou tenu.
 */
public class Account {

    private final int id;
    private final Lock lock = new ReentrantLock();
    private long balance;

    public Account(int id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public int id() {
        return id;
    }

    public Lock lock() {
        return lock;
    }

    // Appelees seulement par du code qui TIENT le verrou du compte.
    void add(long amount) {
        balance += amount;
    }

    public long balance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
