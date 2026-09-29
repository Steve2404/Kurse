package ch5_methods.drills.exercises;

import ch5_methods.ExerciseChecker;
import ch5_methods.drills.Team;

import java.util.Arrays;
import java.util.List;

/**
 * DRILL 05 - Kata melange : tout le chapitre 5 sans indice de forme
 * =================================================================
 *
 * Mode d'emploi : voir Drill01_DeclarationsAndVarargs. Ici, PAS de
 * crochet : a toi de choisir varargs, static, copie, boxing... Fais ce
 * drill seulement quand les drills 01 a 04 passent. Ne modifie jamais
 * les tableaux de Team : copie-les.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : nextTicket()             un numero de ticket qui augmente a chaque appel : "T-1", "T-2"...
 * TODO 2  : bestPlayer()             le nom du meilleur score de Team -> "Grace".
 * TODO 3  : totalWithBonus()         somme des SCORES + somme des BONUS presents -> 51.
 * TODO 4  : scoresPlusBonus()        un NOUVEAU tableau scores + bonus (null compte 0) -> {15, 7, 20, 9}.
 * TODO 5  : addToAll(values, extra)  ajoute extra a chaque case du tableau recu (l'appelant le voit).
 * TODO 6  : format(separator, values...) -> format("|", 12, 7) -> "12|7".
 * TODO 7  : namesAbove(threshold)    les noms dont le score depasse threshold (via une lambda) -> 10 : [Ada, Grace].
 * TODO 8  : sameScore(a, b)          compare deux Integer par valeur, null-safe.
 * TODO 9  : safeParse(text)          un Integer ou null si le texte n'est pas un nombre.
 * TODO 10 : frozenNames()            une liste NON modifiable des noms.
 * TODO 11 : rank(name)               le rang (1 = meilleur) d'un joueur -> Grace 1, Ada 2, Alan 3, Linus 4.
 * TODO 12 : describe(values...)      "vide" si aucun argument, sinon "n valeurs, max m" -> describe(3, 9) -> "2 valeurs, max 9".
 */
public class Drill05_MixedKata {

    private static int tickets;

    public static String nextTicket() {
        throw new UnsupportedOperationException("TODO 1 : implementer nextTicket()");
    }

    public static String bestPlayer() {
        throw new UnsupportedOperationException("TODO 2 : implementer bestPlayer()");
    }

    public static int totalWithBonus() {
        throw new UnsupportedOperationException("TODO 3 : implementer totalWithBonus()");
    }

    public static int[] scoresPlusBonus() {
        throw new UnsupportedOperationException("TODO 4 : implementer scoresPlusBonus()");
    }

    public static void addToAll(int[] values, int extra) {
        throw new UnsupportedOperationException("TODO 5 : implementer addToAll()");
    }

    public static String format(String separator, int... values) {
        throw new UnsupportedOperationException("TODO 6 : implementer format()");
    }

    public static List<String> namesAbove(int threshold) {
        throw new UnsupportedOperationException("TODO 7 : implementer namesAbove()");
    }

    public static boolean sameScore(Integer a, Integer b) {
        throw new UnsupportedOperationException("TODO 8 : implementer sameScore()");
    }

    public static Integer safeParse(String text) {
        throw new UnsupportedOperationException("TODO 9 : implementer safeParse()");
    }

    public static List<String> frozenNames() {
        throw new UnsupportedOperationException("TODO 10 : implementer frozenNames()");
    }

    public static int rank(String name) {
        throw new UnsupportedOperationException("TODO 11 : implementer rank()");
    }

    public static String describe(int... values) {
        throw new UnsupportedOperationException("TODO 12 : implementer describe()");
    }

    public static void main(String[] args) {
        tickets = 0;
        ExerciseChecker.check("1  nextTicket : T-1 puis T-2", nextTicket().equals("T-1") && nextTicket().equals("T-2"));
        ExerciseChecker.check("2  bestPlayer() == Grace", bestPlayer().equals("Grace"));
        ExerciseChecker.check("3  totalWithBonus() == 51", totalWithBonus() == 51);
        ExerciseChecker.check("4  scoresPlusBonus() et SCORES intact",
                Arrays.equals(scoresPlusBonus(), new int[] {15, 7, 20, 9}) && Team.SCORES[0] == 12);
        int[] copy = {1, 2};
        addToAll(copy, 10);
        ExerciseChecker.check("5  addToAll : {11, 12}", Arrays.equals(copy, new int[] {11, 12}));
        ExerciseChecker.check("6  format", format("|", 12, 7).equals("12|7") && format("|").isEmpty());
        ExerciseChecker.check("7  namesAbove(10) == [Ada, Grace]", namesAbove(10).equals(List.of("Ada", "Grace")));
        ExerciseChecker.check("8  sameScore", sameScore(1000, 1000) && sameScore(null, null) && !sameScore(1, null));
        ExerciseChecker.check("9  safeParse", Integer.valueOf(15).equals(safeParse("15")) && safeParse("x") == null);
        boolean frozen;
        try {
            frozenNames().add("Tim");
            frozen = false;
        } catch (UnsupportedOperationException e) {
            frozen = true;
        }
        ExerciseChecker.check("10 frozenNames non modifiable", frozen && frozenNames().size() == 4);
        ExerciseChecker.check("11 rank : 1, 2, 3, 4",
                rank("Grace") == 1 && rank("Ada") == 2 && rank("Alan") == 3 && rank("Linus") == 4);
        ExerciseChecker.check("12 describe", describe().equals("vide") && describe(3, 9).equals("2 valeurs, max 9"));

        ExerciseChecker.summary();
    }
}
