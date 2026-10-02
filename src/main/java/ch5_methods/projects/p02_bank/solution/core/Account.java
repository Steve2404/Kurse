package ch5_methods.projects.p02_bank.solution.core;

import java.util.Arrays;

/**
 * SOLUTION - un compte. Chaque membre a le niveau d'acces le plus FAIBLE possible.
 */
public class Account {

    // static : un seul compteur partage par TOUS les comptes ; private : personne ne peut le fausser.
    private static int nextId = 1;
    private static int opened;

    // private : seul Account les modifie ; le reste passe par des methodes.
    private int id;
    private String owner;
    private long balance;
    private String[] history = new String[2];
    private int size;
    private int withdrawStreak;

    // protected : visible dans le paquet core ET dans les sous-classes d'un autre paquet (premium).
    protected long overdraft;
    protected String kind = "courant";

    // Pas de constructeur ecrit (chapitre 6) : une fabrique static cree et initialise le compte.
    public static Account open(String owner, long cents) {
        Account a = new Account();
        a.init(owner, cents);
        return a;
    }

    // protected : PremiumAccount (autre paquet) l'appelle sur SA propre reference.
    protected void init(String owner, long cents) {
        id = nextId++;
        opened++;
        this.owner = owner;
        balance = cents;
        record("ouverture " + Money.format(cents));
    }

    public static int opened() {
        return opened;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public long getBalance() {
        return balance;
    }

    public String getKind() {
        return kind;
    }

    // Package-private (pas de mot-cle) : seul Ledger, dans le meme paquet, fait bouger l'argent.
    void deposit(long cents) {
        balance += cents;
        withdrawStreak = 0;
        record("depot " + Money.format(cents));
    }

    boolean withdraw(long cents) {
        if (balance - cents < -overdraft) {
            return false;
        }
        balance -= cents;
        withdrawStreak++;
        record("retrait " + Money.format(cents));
        return true;
    }

    int withdrawStreak() {
        return withdrawStreak;
    }

    void applyInterest(long cents) {
        balance += cents;
        record("interets " + Money.format(cents));
    }

    // private : le tableau qui grandit est un detail interne (doublement de taille, comme ArrayList).
    private void record(String line) {
        if (size == history.length) {
            history = Arrays.copyOf(history, history.length * 2);
        }
        history[size++] = line;
    }

    public String statement() {
        StringBuilder sb = new StringBuilder("releve " + id + " " + owner + " (" + kind + ", solde " + Money.format(balance) + ") :");
        for (int i = 0; i < size; i++) {
            sb.append(i == 0 ? " " : " | ").append(history[i]);
        }
        return sb.toString();
    }
}
