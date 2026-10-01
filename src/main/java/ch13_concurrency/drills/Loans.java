package ch13_concurrency.drills;

import java.util.List;

/**
 * Les donnees partagees par TOUS les drills du chapitre 13.
 * ========================================================
 *
 * Les emprunts de la bibliotheque du chapitre 10 : des taches a repartir
 * entre threads. Toujours les memes donnees : ton cerveau se concentre
 * sur les outils de concurrence.
 *
 *   12 emprunts, total des jours = 134 (le plus long : bob, Dune, 30 jours)
 *   par titre  : Dune 4, Hyperion 3, Solaris 3, Fondation 2
 *   par membre : ana 3, bob 3, cid 2, dan 2, eve 2
 *   en retard (plus de 14 jours) : ana/Hyperion (21) puis bob/Dune (30)
 */
public final class Loans {

    public record Loan(String member, String title, int days) {
    }

    public static final List<Loan> LOANS = List.of(
            new Loan("ana", "Dune", 14), new Loan("bob", "Fondation", 7), new Loan("ana", "Hyperion", 21),
            new Loan("cid", "Dune", 3), new Loan("bob", "Solaris", 10), new Loan("dan", "Dune", 7),
            new Loan("ana", "Solaris", 5), new Loan("cid", "Fondation", 14), new Loan("eve", "Hyperion", 2),
            new Loan("bob", "Dune", 30), new Loan("dan", "Hyperion", 9), new Loan("eve", "Solaris", 12));

    private Loans() {
    }
}
