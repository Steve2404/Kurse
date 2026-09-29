package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

/**
 * EXERCICE 2 - Varargs en pratique : zero, un ou plusieurs arguments, un tableau, et le piege du null (niveau : difficile)
 * =====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un varargs "int... values", c'est un sac : on peut y mettre zero,
 * un ou plusieurs nombres, ou donner directement un sac tout pret (un
 * int[]). DANS la methode, values est toujours un tableau (jamais null
 * si on a passe des valeurs, un tableau VIDE si on n'a rien passe).
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   countArgs()                          -> 0   (tableau vide, pas null)
 *   countArgs(1, "a", null)              -> 3
 *   countArgs(new String[] {"x", "y"})   -> 2   (un String[] EST un Object[] : il devient le sac lui-meme)
 *        javac avertit : warning: non-varargs call of varargs method with inexact argument type for last parameter;
 *        on ecrit donc countArgs((Object[]) new String[] {"x", "y"}) pour le dire explicitement.
 *   countArgs(new int[] {1, 2, 3})       -> 1   (un int[] N'EST PAS un Object[] : il devient UN objet du sac)
 *   countArgs((Object) null)             -> 1   (un sac contenant null)
 *   countArgs((Object[]) null)           -> le sac lui-meme est null : args.length lancerait NullPointerException
 *
 *
 * ==================================================================
 * TODO 1 : sum(values...)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   sum() -> 0 ; sum(4) -> 4 ; sum(1, 2, 3) -> 6 ; sum(new int[] {5, 5}) -> 10
 *
 * -- Le plan --
 *
 *   1. for-each sur values (un tableau), additionner.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : join(separator, parts...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un parametre normal PUIS le varargs (toujours en dernier). On colle
 * les morceaux avec le separateur, sans separateur au debut ni a la fin.
 *
 * -- Essayons a la main --
 *
 *   join("-", "a", "b", "c") -> "a-b-c"      join("-") -> ""      join(", ", "seul") -> "seul"
 *
 * -- Le plan --
 *
 *   1. StringBuilder ; pour i de 0 a parts.length - 1 : si i > 0, ajouter separator ; ajouter parts[i].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : max(first, rest...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "Au moins un nombre" : un varargs seul accepterait max() sans rien.
 * En mettant un premier parametre obligatoire, max() ne compile meme
 * plus. C'est le compilateur qui protege.
 *
 * -- Essayons a la main --
 *
 *   max(7) -> 7 ; max(3, 9, 4) -> 9 ; max(-5, -2) -> -2
 *
 * -- Le plan --
 *
 *   1. best = first ; pour chaque v de rest : best = Math.max(best, v).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : countArgs(args...)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Si args est null (le sac lui-meme), rendre -1 ; sinon args.length.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Tout l'interet est dans main() : regarde bien les 6 appels.
 *
 *
 * ==================================================================
 * TODO 5 : average(values...)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   average(2, 4) -> 3.0 ; average() -> 0.0 (pas de division par zero)
 *
 * -- Le plan --
 *
 *   1. Si values.length == 0 -> 0.0 ; sinon somme / values.length.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : sentence(pattern, values...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On FAIT SUIVRE le sac a une autre methode varargs : String.format
 * attend (String, Object...). Donner values tel quel passe le MEME
 * tableau (pas un tableau dans un tableau).
 *
 * -- Essayons a la main --
 *
 *   sentence("%s a %d ans", "Ada", 36) -> "Ada a 36 ans"
 *
 * -- Le plan --
 *
 *   1. Rendre String.format(pattern, values).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Un varargs se manipule exactement comme un tableau : values.length, values[i], for-each.
 */
public class Exercise02_VarargsInPractice {

    public static int sum(int... values) {
        throw new UnsupportedOperationException("TODO 1 : implementer sum()");
    }

    public static String join(String separator, String... parts) {
        throw new UnsupportedOperationException("TODO 2 : implementer join()");
    }

    public static int max(int first, int... rest) {
        throw new UnsupportedOperationException("TODO 3 : implementer max()");
    }

    public static int countArgs(Object... args) {
        throw new UnsupportedOperationException("TODO 4 : implementer countArgs()");
    }

    public static double average(double... values) {
        throw new UnsupportedOperationException("TODO 5 : implementer average()");
    }

    public static String sentence(String pattern, Object... values) {
        throw new UnsupportedOperationException("TODO 6 : implementer sentence()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("sum : 0, 4, 6, 10 (tableau passe directement)",
                sum() == 0 && sum(4) == 4 && sum(1, 2, 3) == 6 && sum(new int[] {5, 5}) == 10);
        ExerciseChecker.check("join : a-b-c, \"\", seul",
                join("-", "a", "b", "c").equals("a-b-c") && join("-").isEmpty() && join(", ", "seul").equals("seul"));
        ExerciseChecker.check("max : 7, 9, -2", max(7) == 7 && max(3, 9, 4) == 9 && max(-5, -2) == -2);
        ExerciseChecker.check("countArgs() == 0 et countArgs(1, \"a\", null) == 3", countArgs() == 0 && countArgs(1, "a", null) == 3);
        ExerciseChecker.check("countArgs(String[] de 2) == 2 : le String[] DEVIENT le sac", countArgs((Object[]) new String[] {"x", "y"}) == 2);
        ExerciseChecker.check("countArgs(int[] de 3) == 1 : le int[] est UN objet dans le sac", countArgs(new int[] {1, 2, 3}) == 1);
        ExerciseChecker.check("countArgs((Object) null) == 1 et countArgs((Object[]) null) == -1",
                countArgs((Object) null) == 1 && countArgs((Object[]) null) == -1);
        ExerciseChecker.check("average : 3.0 et 0.0", average(2, 4) == 3.0 && average() == 0.0);
        ExerciseChecker.check("sentence : \"Ada a 36 ans\"", sentence("%s a %d ans", "Ada", 36).equals("Ada a 36 ans"));

        ExerciseChecker.summary();
    }
}
