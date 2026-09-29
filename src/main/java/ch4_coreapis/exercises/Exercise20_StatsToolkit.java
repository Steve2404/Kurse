package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 20 - Petite boite a outils de statistiques : moyenne, mediane, ecart-type, pourcentages (niveau : avance)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise11_ArraysBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On a les notes d'une classe : {4, 8, 15, 16, 23, 42}. On veut les
 * chiffres qu'un professeur calcule : la moyenne, la note du milieu
 * (mediane), la dispersion (ecart-type), des pourcentages arrondis et
 * un petit histogramme en '#'. Tout avec Math, Arrays et String.repeat.
 *
 *
 * ==================================================================
 * TODO 1 : mean(values...)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (4 + 8 + 15 + 16 + 23 + 42) / 6 = 108 / 6 = 18.0      mean(1, 2) = 1.5 (pas 1 !)
 *
 * -- Le plan --
 *
 *   1. Somme en int, puis (double) somme / nombre (sinon division entiere).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : median(values...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On range les notes et on prend celle du milieu. Nombre pair : la
 * moyenne des deux du milieu. Piege : Arrays.sort trie le tableau DE
 * L'APPELANT. On trie donc une COPIE.
 *
 * -- Essayons a la main --
 *
 *   {23, 4, 42, 8, 16, 15} -> trie {4, 8, 15, 16, 23, 42} -> (15 + 16) / 2.0 = 15.5
 *   {9, 1, 5} -> {1, 5, 9} -> 5.0
 *
 * -- Le plan --
 *
 *   1. sorted = Arrays.copyOf(values, values.length) ; Arrays.sort(sorted).
 *   2. n impair -> sorted[n / 2] ; pair -> (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : stdDev(values...)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   moyenne 18 ; ecarts -14, -10, -3, -2, 5, 24 ; carres 196, 100, 9, 4, 25, 576 ;
 *   somme 910 ; / 6 = 151.67 ; racine = 12.315...
 *
 * -- Le plan --
 *
 *   1. m = mean(values).
 *   2. Somme des Math.pow(v - m, 2) ; diviser par n ; Math.sqrt.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : mean (TODO 1).
 *
 *
 * ==================================================================
 * TODO 4 : percent(part, total)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (1, 3) -> 33.33 -> 33     (2, 3) -> 66.67 -> 67     (1, 8) -> 12.5 -> 13
 *
 * -- Le plan --
 *
 *   1. Rendre (int) Math.round(100.0 * part / total).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Attention : 100 * part / total en int donnerait 12 pour (1, 8).
 *
 *
 * ==================================================================
 * TODO 5 : bar(value, max, width)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une barre de '#' proportionnelle : la valeur max remplit toute la
 * largeur, les autres une partie arrondie.
 *
 * -- Essayons a la main --
 *
 *   (42, 42, 10) -> 10 '#'     (21, 42, 10) -> 5 '#'     (4, 42, 10) -> 0.95 -> 1 '#'
 *
 * -- Le plan --
 *
 *   1. n = (int) Math.round(value * width / (double) max).
 *   2. Rendre "#".repeat(n).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : histogram(values...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une ligne par note : la note alignee a droite sur 3 cases, " | ",
 * puis sa barre (largeur 10, max = la plus grande note).
 *
 * -- Essayons a la main --
 *
 *   histogram(21, 42) -> " 21 | #####\n 42 | ##########"
 *
 * -- Le plan --
 *
 *   1. max = la plus grande valeur (Math.max dans une boucle).
 *   2. Pour chaque valeur : String.format("%3d | ", v) + bar(v, max, 10) ; lignes separees par "\n".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : bar (TODO 5).
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - int... values est un int[] : values.length, values[i], for-each.
 *   - Math.round(double) rend un long : cast (int) pour repeat.
 */
public class Exercise20_StatsToolkit {

    public static double mean(int... values) {
        throw new UnsupportedOperationException("TODO 1 : implementer mean()");
    }

    public static double median(int... values) {
        throw new UnsupportedOperationException("TODO 2 : implementer median()");
    }

    public static double stdDev(int... values) {
        throw new UnsupportedOperationException("TODO 3 : implementer stdDev()");
    }

    public static int percent(int part, int total) {
        throw new UnsupportedOperationException("TODO 4 : implementer percent()");
    }

    public static String bar(int value, int max, int width) {
        throw new UnsupportedOperationException("TODO 5 : implementer bar()");
    }

    public static String histogram(int... values) {
        throw new UnsupportedOperationException("TODO 6 : implementer histogram()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("mean : 18.0 et 1.5", mean(4, 8, 15, 16, 23, 42) == 18.0 && mean(1, 2) == 1.5);

        int[] notes = {23, 4, 42, 8, 16, 15};
        ExerciseChecker.check("median : 15.5 et 5.0", median(notes) == 15.5 && median(9, 1, 5) == 5.0);
        ExerciseChecker.check("median ne trie PAS le tableau de l'appelant", Arrays.equals(notes, new int[] {23, 4, 42, 8, 16, 15}));

        ExerciseChecker.check("stdDev(4, 8, 15, 16, 23, 42) vaut environ 12.315",
                Math.abs(stdDev(4, 8, 15, 16, 23, 42) - 12.315) < 0.001);
        ExerciseChecker.check("percent : 33, 67, 13", percent(1, 3) == 33 && percent(2, 3) == 67 && percent(1, 8) == 13);
        ExerciseChecker.check("bar : 10, 5, 1 '#'",
                bar(42, 42, 10).equals("##########") && bar(21, 42, 10).equals("#####") && bar(4, 42, 10).equals("#"));
        ExerciseChecker.check("histogram(21, 42)", histogram(21, 42).equals(" 21 | #####\n 42 | ##########"));

        ExerciseChecker.summary();
    }
}
