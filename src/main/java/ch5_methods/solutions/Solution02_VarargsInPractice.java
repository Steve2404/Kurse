package ch5_methods.solutions;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise02_VarargsInPractice.
 */
public class Solution02_VarargsInPractice {

    public static int sum(int... values) {
        // values est un int[] : vide (pas null) quand on n'a rien passe.
        int total = 0;
        for (int v : values) {
            total += v;
        }
        return total;
    }

    public static String join(String separator, String... parts) {
        // Le separateur seulement ENTRE deux morceaux.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    public static int max(int first, int... rest) {
        // Le parametre obligatoire garantit au moins une valeur (max() ne compile pas).
        int best = first;
        for (int v : rest) {
            best = Math.max(best, v);
        }
        return best;
    }

    public static int countArgs(Object... args) {
        // Le sac lui-meme peut etre null si on passe explicitement (Object[]) null.
        return args == null ? -1 : args.length;
    }

    public static double average(double... values) {
        // Tableau vide : on evite 0.0 / 0 (qui donnerait NaN).
        if (values.length == 0) {
            return 0.0;
        }
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    public static String sentence(String pattern, Object... values) {
        // On fait suivre le MEME tableau a String.format(String, Object...).
        return String.format(pattern, values);
    }
}
