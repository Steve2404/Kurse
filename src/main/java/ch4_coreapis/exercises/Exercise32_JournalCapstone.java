package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * EXERCICE 32 (CAPSTONE) - Le journal de bord : String, StringBuilder, Arrays, Math et java.time ensemble (niveau : capstone)
 * ===========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le serveur a ecrit son journal, mais les lignes sont dans le
 * desordre :
 *
 *   2024-03-11 08:00 INFO sauvegarde terminee
 *   2024-03-10 07:05 INFO demarrage du serveur
 *   2024-03-11 23:59 ERROR memoire insuffisante
 *   2024-03-10 07:42 WARN disque presque plein
 *   2024-03-10 09:15 ERROR connexion perdue
 *
 * On transforme chaque ligne en un petit objet Entry (quand, niveau,
 * message), puis on repond aux questions du chef : combien de lignes
 * par niveau ? quelle est la PREMIERE erreur dans le temps ? combien de
 * temps couvre le journal ? quels messages, par ordre alphabetique ? Et
 * on rend un rapport en texte.
 *
 *
 * ==================================================================
 * TODO 1 : parse(line)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "2024-03-10 09:15 ERROR connexion perdue"
 *     -> when = LocalDateTime.parse("2024-03-10T09:15") ; level = "ERROR" ; message = "connexion perdue"
 *
 * -- Le plan --
 *
 *   1. end = line.indexOf(' ', 17).
 *   2. when = LocalDateTime.parse(line.substring(0, 10) + "T" + line.substring(11, 16)).
 *   3. Rendre new Entry(when, line.substring(17, end), line.substring(end + 1)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non (c'est la meme decoupe qu'a l'Exercise06_LogLineParser).
 *
 *
 * ==================================================================
 * TODO 2 : countByLevel(entries)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Trois compteurs dans un tableau : case 0 pour INFO, 1 pour WARN,
 * 2 pour ERROR.
 *
 * -- Essayons a la main --
 *
 *   -> [2, 1, 2]
 *
 * -- Le plan --
 *
 *   1. counts = new int[3] ; pour chaque entree, un switch sur le niveau choisit la case.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : firstError(entries)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La premiere erreur DANS LE TEMPS, pas dans la liste : "memoire
 * insuffisante" est avant dans la liste, mais "connexion perdue" est
 * arrivee plus tot (le 10 a 09:15). null s'il n'y a aucune erreur.
 *
 * -- Le plan --
 *
 *   1. best = null ; pour chaque ERROR : si best == null ou e.when().isBefore(best.when()), best = e.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : span(entries)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   plus tot 2024-03-10T07:05, plus tard 2024-03-11T23:59 -> PT40H54M
 *
 * -- Le plan --
 *
 *   1. Chercher le plus tot et le plus tard (isBefore / isAfter).
 *   2. Rendre Duration.between(plusTot, plusTard).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : sortedMessages(entries)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. messages = new String[entries.length] ; remplir ; Arrays.sort(messages).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : report(lines)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   INFO=2 WARN=1 ERROR=2
 *   duree=40h54
 *   premiere erreur=2024-03-10T09:15 connexion perdue
 *   pourcentage erreurs=40
 *
 * -- Le plan --
 *
 *   1. entries = chaque ligne passee a parse.
 *   2. Un StringBuilder ; les 4 lignes separees par "\n" :
 *      - les compteurs (TODO 2) ;
 *      - la duree en "XhMM" : String.format("%dh%02d", d.toHours(), d.toMinutesPart()) ;
 *      - la premiere erreur : when + " " + message, ou "aucune" ;
 *      - le pourcentage d'erreurs arrondi : Math.round(100.0 * erreurs / total).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Il assemble les TODO 1 a 4 : c'est le capstone.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Entry est un record : e.when(), e.level(), e.message().
 *   - LocalDateTime.toString() donne "2024-03-10T09:15" (les secondes a 0 ne s'affichent pas).
 */
public class Exercise32_JournalCapstone {

    public record Entry(LocalDateTime when, String level, String message) {
    }

    public static final String[] LINES = {
            "2024-03-11 08:00 INFO sauvegarde terminee",
            "2024-03-10 07:05 INFO demarrage du serveur",
            "2024-03-11 23:59 ERROR memoire insuffisante",
            "2024-03-10 07:42 WARN disque presque plein",
            "2024-03-10 09:15 ERROR connexion perdue"};

    public static Entry parse(String line) {
        throw new UnsupportedOperationException("TODO 1 : implementer parse()");
    }

    public static int[] countByLevel(Entry[] entries) {
        throw new UnsupportedOperationException("TODO 2 : implementer countByLevel()");
    }

    public static Entry firstError(Entry[] entries) {
        throw new UnsupportedOperationException("TODO 3 : implementer firstError()");
    }

    public static Duration span(Entry[] entries) {
        throw new UnsupportedOperationException("TODO 4 : implementer span()");
    }

    public static String[] sortedMessages(Entry[] entries) {
        throw new UnsupportedOperationException("TODO 5 : implementer sortedMessages()");
    }

    public static String report(String[] lines) {
        throw new UnsupportedOperationException("TODO 6 : implementer report()");
    }

    public static void main(String[] args) {
        Entry first = parse(LINES[4]);
        ExerciseChecker.check("parse : 2024-03-10T09:15, ERROR, connexion perdue",
                first.when().equals(LocalDateTime.of(2024, 3, 10, 9, 15)) && first.level().equals("ERROR")
                        && first.message().equals("connexion perdue"));

        Entry[] entries = new Entry[LINES.length];
        for (int i = 0; i < LINES.length; i++) {
            entries[i] = parse(LINES[i]);
        }
        ExerciseChecker.check("countByLevel == [2, 1, 2]", Arrays.equals(countByLevel(entries), new int[] {2, 1, 2}));
        ExerciseChecker.check("firstError : la plus TOT (connexion perdue), pas la premiere de la liste",
                firstError(entries).message().equals("connexion perdue"));
        ExerciseChecker.check("firstError sans erreur -> null", firstError(new Entry[] {entries[0]}) == null);
        ExerciseChecker.check("span == PT40H54M", span(entries).toString().equals("PT40H54M"));
        ExerciseChecker.check("sortedMessages : ordre alphabetique",
                Arrays.equals(sortedMessages(entries), new String[] {"connexion perdue", "demarrage du serveur",
                        "disque presque plein", "memoire insuffisante", "sauvegarde terminee"}));
        ExerciseChecker.check("report(LINES)", report(LINES).equals(
                "INFO=2 WARN=1 ERROR=2\nduree=40h54\npremiere erreur=2024-03-10T09:15 connexion perdue\npourcentage erreurs=40"));

        ExerciseChecker.summary();
    }
}
