package ch10_streams.projects.p01_loandesk;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 1 (ne pas modifier). Lance-le quand tu veux : il
 * execute TON LoanDesk.main et compare sa sortie a EXPECTED, puis verifie que
 * tout l'API Optional est pratiquee (et que Optional.get() n'apparait nulle part).
 *
 * L'enonce et le tableau de bord sont dans TODO.md. EXPECTED est la sortie exacte, dans
 * l'ordre des commandes de Data.COMMANDS.
 *
 * Lance avec l'argument "solution" pour voir la solution passer.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "OK : Lea emprunte Dune (reste 2)",
            "REFUS : Lea a deja Dune",
            "REFUS : membre inconnu M9",
            "REFUS : livre inconnu B7",
            "OK : Hugo emprunte Le Petit Prince (reste 0)",
            "ATTENTE : Ines en position 1 pour Le Petit Prince",
            "ATTENTE : Tom en position 2 pour Le Petit Prince",
            "RETOUR : Hugo rend Le Petit Prince, penalite 1.50",
            "OK : Ines emprunte Le Petit Prince (reste 0)",
            "AVIS ines@biblio.org : Le Petit Prince vous attend",
            "RETOUR : Lea rend Dune, sans penalite",
            "REFUS : Lea n'a pas Dune",
            "CONTACT Hugo : par courrier",
            "CONTACT Tom : par courrier",
            "CONTACT Lea : lea@mail.fr",
            "REFUS : membre inconnu M7",
            "INFO B2 Fondation (Asimov) : 2 disponible(s), 0 en attente",
            "INFO B4 Le Petit Prince (Saint-Exupery) : 0 disponible(s), 1 en attente",
            "REFUS : aucun livre pour Silmarillion",
            "RETOUR : Ines rend Le Petit Prince, penalite 8.50",
            "OK : Tom emprunte Le Petit Prince (reste 0)",
            "AVIS par courrier a Tom : Le Petit Prince vous attend",
            "RETOUR : Tom rend Le Petit Prince, penalite 10.00",
            "REFUS : commande inconnue RENOUVELER",
            "BILAN : 3 penalite(s), total 20.00",
            "BILAN : retard max 40 jour(s) (Tom), moyen 16.5 jour(s)",
            "BILAN : joignables par email [lea@mail.fr, ines@biblio.org]",
            "BILAN : 0 emprunt(s) en cours");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Optional.ofNullable(", "Optional.of(", "Optional.empty()",
            ".map(", ".filter(", ".flatMap(", ".or(",
            ".orElse(", ".orElseGet(", ".orElseThrow(()", ".orElseThrow()",
            ".ifPresent(", ".ifPresentOrElse(", ".isPresent()", ".isEmpty()",
            "Optional::stream", "OptionalInt", ".getAsInt()", "OptionalDouble",
            "!.get()",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "LoanDesk", args, EXPECTED, API);
    }
}
