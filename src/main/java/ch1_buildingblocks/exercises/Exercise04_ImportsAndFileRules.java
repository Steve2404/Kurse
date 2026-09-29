package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.util.*;
import java.sql.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * EXERCICE 4 - Packages, imports et regles de fichier : ce fichier est lui-meme l'exercice (niveau : moyen/difficile)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Regarde bien le HAUT de ce fichier --
 *
 *   package ch1_buildingblocks.exercises;          <- 1. toujours en premier
 *   import ch1_buildingblocks.ExerciseChecker;      <- 2. puis les imports
 *   import java.util.*;                             <-    (wildcard)
 *   import java.sql.Date;                           <-    (par nom)
 *   import java.util.concurrent.atomic.AtomicInteger;
 *   public class Exercise04_ImportsAndFileRules     <- 3. puis les classes
 *
 * et le BAS : une 2e classe, ReceiptLine, SANS "public", apres la
 * classe publique. Tout ce fichier compile : chaque TODO te fait
 * utiliser une de ces regles. Les versions FAUSSES sont dans le
 * Javadoc, avec le message REEL de javac 17 (verifie en direct).
 *
 * Les 5 regles :
 *   1. import paquet.* n'importe QUE les classes DIRECTEMENT dans le
 *      paquet, jamais celles d'un SOUS-paquet.
 *   2. Ordre STRICT : package, puis import, puis classes.
 *   3. Import PAR NOM contre import WILDCARD pour le meme nom simple :
 *      l'import par nom gagne. Deux imports wildcard qui contiennent
 *      le meme nom : ambigu des qu'on l'utilise. Deux imports par nom
 *      du meme nom simple : interdit tout de suite.
 *   4. java.lang (String, Math, Integer...) n'a jamais besoin d'import.
 *   5. Une classe public porte le nom du fichier ; un seul public par
 *      fichier ; d'autres classes non public peuvent suivre ; l'ordre
 *      des champs et methodes dans une classe est libre.
 *
 *
 * ==================================================================
 * TODO 1 : newCounter(start)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * AtomicInteger habite dans java.util.concurrent.atomic : c'est un
 * TIROIR DANS un tiroir de java.util. "import java.util.*" ouvre
 * seulement le premier tiroir, pas ceux qui sont dedans. C'est pour
 * ca que ce fichier importe AtomicInteger par son nom complet.
 *
 * Version fausse (seulement "import java.util.*;") :
 *   AtomicInteger a = new AtomicInteger();
 *   -> error: cannot find symbol  (symbol: class AtomicInteger)
 *
 * -- Essayons a la main --
 *
 *   newCounter(10) -> un compteur a 10 ; incrementAndGet() -> 11
 *
 * -- Le plan --
 *
 *   1. Fabriquer un AtomicInteger qui demarre a start et le rendre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : sqlDate(millis)  et  TODO 3 : utilDate(millis)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux enfants s'appellent "Date" : java.util.Date (dans le tiroir
 * java.util.*, ouvert en wildcard) et java.sql.Date (appele PAR SON
 * NOM en haut du fichier). Quand on dit juste "Date", c'est celui
 * qu'on a appele par son nom qui repond : java.sql.Date. Pour parler
 * a l'autre, il faut dire son nom COMPLET : java.util.Date.
 *
 * Versions fausses (verifiees) :
 *   import java.util.*; import java.sql.*;  puis  Date d;
 *   -> error: reference to Date is ambiguous
 *   import java.util.Date; import java.sql.Date;
 *   -> error: a type with the same simple name is already defined by the single-type-import of Date
 *
 * -- Essayons a la main --
 *
 *   sqlDate(0L).getClass().getName()  -> "java.sql.Date"
 *   utilDate(0L).getClass().getName() -> "java.util.Date"
 *
 * -- Le plan --
 *
 *   sqlDate  : construire un "Date" (le nom simple suffit : c'est java.sql.Date).
 *   utilDate : construire un java.util.Date en ecrivant son nom complet.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : lineTotalCents(line)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ReceiptLine est une 2e classe du MEME fichier, sans "public" : c'est
 * permis. Deux classes "public" dans un fichier, ou une classe public
 * qui ne porte pas le nom du fichier, ne compilent pas :
 *
 *   Fichier Wrong.java contenant "public class RightName { }"
 *   -> error: class RightName is public, should be declared in a file named RightName.java
 *   Fichier TwoPublic.java contenant 2 classes public
 *   -> error: class AlsoPublic is public, should be declared in a file named AlsoPublic.java
 *   import AVANT package :
 *   -> error: class, interface, enum, or record expected
 *
 * Regarde aussi ReceiptLine : sa methode label() est ecrite AVANT les
 * champs qu'elle utilise. Aucun probleme : Java connait toute la
 * classe avant d'executer quoi que ce soit, l'ordre des membres est
 * libre.
 *
 * -- Essayons a la main --
 *
 *   new ReceiptLine("pomme", 3, 50) -> 3 x 50 = 150 centimes
 *
 * -- Le plan --
 *
 *   1. Multiplier la quantite par le prix unitaire (en centimes) de la ligne.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : longestWord(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * List et ArrayList viennent de java.util.* (ouvert en wildcard) :
 * pas besoin de les importer une par une. String et Math viennent de
 * java.lang : jamais besoin de les importer.
 *
 * -- Essayons a la main --
 *
 *   ["pain", "pomme", "lait"] -> "pomme" ; [] -> ""
 *
 * -- Le plan --
 *
 *   1. Garder le mot le plus long rencontre (le premier en cas d'egalite).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new AtomicInteger(start)
 *   - new Date(millis) ici designe java.sql.Date ; new java.util.Date(millis)
 *   - line.quantity * line.unitPriceCents (champs accessibles dans le meme paquet)
 *   - for (String w : words) if (w.length() > best.length()) best = w;
 */
public class Exercise04_ImportsAndFileRules {

    public static AtomicInteger newCounter(int start) {
        throw new UnsupportedOperationException("TODO 1 : implementer newCounter()");
    }

    public static Date sqlDate(long millis) {
        throw new UnsupportedOperationException("TODO 2 : implementer sqlDate()");
    }

    public static java.util.Date utilDate(long millis) {
        throw new UnsupportedOperationException("TODO 3 : implementer utilDate()");
    }

    public static int lineTotalCents(ReceiptLine line) {
        throw new UnsupportedOperationException("TODO 4 : implementer lineTotalCents()");
    }

    public static String longestWord(List<String> words) {
        throw new UnsupportedOperationException("TODO 5 : implementer longestWord()");
    }

    public static void main(String[] args) {
        AtomicInteger counter = newCounter(10);
        ExerciseChecker.check("1 newCounter(10).incrementAndGet() == 11", counter.incrementAndGet() == 11);
        ExerciseChecker.check("2 sqlDate(0L) est un java.sql.Date (l'import par nom gagne)",
                sqlDate(0L).getClass().getName().equals("java.sql.Date"));
        ExerciseChecker.check("3 utilDate(0L) est un java.util.Date (nom complet)",
                utilDate(0L).getClass().getName().equals("java.util.Date"));
        ExerciseChecker.check("4 lineTotalCents(pomme x3 a 50) == 150", lineTotalCents(new ReceiptLine("pomme", 3, 50)) == 150);
        ExerciseChecker.check("4 (demo) label() ecrite avant les champs fonctionne",
                new ReceiptLine("pain", 1, 120).label().equals("pain x1"));
        ExerciseChecker.check("5 longestWord([pain, pomme, lait]) == pomme",
                longestWord(List.of("pain", "pomme", "lait")).equals("pomme"));
        ExerciseChecker.check("5 longestWord([]) == \"\"", longestWord(new ArrayList<>()).isEmpty());

        ExerciseChecker.summary();
    }
}

class ReceiptLine {

    String label() {
        return name + " x" + quantity;
    }

    final String name;
    final int quantity;
    final int unitPriceCents;

    ReceiptLine(String name, int quantity, int unitPriceCents) {
        this.name = name;
        this.quantity = quantity;
        this.unitPriceCents = unitPriceCents;
    }
}
