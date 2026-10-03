package ch13_concurrency.projects.p03_bank.solution;

/**
 * SOLUTION - un compteur protege par synchronized : un seul thread a la fois dans les methodes synchronized
 * d'un MEME objet (le verrou est l'objet lui-meme, "this").
 */
public class OperationCounter {

    private int transfers;

    public synchronized void transfer() {
        transfers++;                                  // ++ n'est PAS atomique : lire, ajouter, ecrire
    }

    public String summary() {
        synchronized (this) {                         // bloc synchronized : MEME verrou que les methodes synchronized
            return transfers + " virements";
        }
    }
}
