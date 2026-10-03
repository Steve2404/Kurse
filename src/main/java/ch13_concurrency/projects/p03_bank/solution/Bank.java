package ch13_concurrency.projects.p03_bank.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SOLUTION - la banque. Pour eviter l'INTERBLOCAGE, on prend toujours les deux verrous dans le meme ordre
 * (le plus petit identifiant d'abord), quel que soit le sens du virement.
 */
public class Bank {

    private final List<Account> accounts = new ArrayList<>();
    private final OperationCounter counter = new OperationCounter();
    private final AtomicLong fees = new AtomicLong();
    private final AtomicLong biggest = new AtomicLong();

    public Bank(int n, long initial) {
        for (int i = 0; i < n; i++) {
            accounts.add(new Account(i, initial));
        }
    }

    public Account account(int id) {
        return accounts.get(id);
    }

    public void transfer(int from, int to, long amount, long fee) {
        Account first = accounts.get(Math.min(from, to));
        Account second = accounts.get(Math.max(from, to));
        first.lock().lock();
        try {
            second.lock().lock();
            try {
                accounts.get(from).add(-amount - fee);
                accounts.get(to).add(amount);
            } finally {
                second.lock().unlock();               // toujours dans un finally
            }
        } finally {
            first.lock().unlock();
        }
        counter.transfer();
        fees.addAndGet(fee);                          // atomique, sans verrou
        biggest.accumulateAndGet(amount, Math::max);  // "max" atomique : relance tant qu'un autre thread a change la valeur
    }

    // Variante qui ABANDONNE si le 2e verrou n'est pas obtenu a temps (au lieu d'attendre indefiniment).
    public boolean tryTransfer(Account a, Account b, long amount, long waitMs) throws InterruptedException {
        a.lock().lock();
        try {
            if (!b.lock().tryLock(waitMs, TimeUnit.MILLISECONDS)) {
                return false;
            }
            try {
                a.add(-amount);
                b.add(amount);
                return true;
            } finally {
                b.lock().unlock();
            }
        } finally {
            a.lock().unlock();
        }
    }

    public long total() {
        return accounts.stream().mapToLong(Account::balance).sum() + fees.get();
    }

    public List<Long> balances() {
        return accounts.stream().map(Account::balance).toList();
    }

    public String stats() {
        return counter.summary() + ", frais " + fees.get() + ", plus gros virement " + biggest.get();
    }
}
