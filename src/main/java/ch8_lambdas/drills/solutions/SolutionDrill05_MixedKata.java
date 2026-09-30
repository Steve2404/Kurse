package ch8_lambdas.drills.solutions;

import ch8_lambdas.drills.Words;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    public static List<String> filter(List<String> words, Predicate<String> rule) {
        // La regle est un parametre : le meme code filtre selon n'importe quel critere.
        List<String> kept = new ArrayList<>();
        for (String w : words) {
            if (rule.test(w)) {
                kept.add(w);
            }
        }
        return kept;
    }

    public static List<String> longWords() {
        // On reutilise filter : la regle est une simple lambda Predicate.
        return filter(Words.WORDS, w -> w.length() > 4);
    }

    public static List<String> mapAll(List<String> words, Function<String, String> f) {
        // La transformation est un parametre Function : le meme code sert a toutes les conversions.
        List<String> result = new ArrayList<>();
        for (String w : words) {
            result.add(f.apply(w));
        }
        return result;
    }

    public static List<String> tagAll() {
        // Deux fonctions collees avec andThen.
        Function<String, String> upper = String::toUpperCase;
        return mapAll(Words.WORDS, upper.andThen(s -> "#" + s));
    }

    public static UnaryOperator<String> applyTwice(UnaryOperator<String> f) {
        // Une fonction qui rend une fonction ; f est capture et appele deux fois.
        return s -> f.apply(f.apply(s));
    }

    public static Supplier<Integer> lazyLength(List<String> words) {
        // Rien n'est calcule avant get().
        return () -> {
            int total = 0;
            for (String w : words) {
                total += w.length();
            }
            return total;
        };
    }

    public static Map<Integer, List<String>> byLength(List<String> words) {
        // computeIfAbsent prend une Function qui cree la liste manquante.
        Map<Integer, List<String>> groups = new TreeMap<>();
        for (String w : words) {
            groups.computeIfAbsent(w.length(), k -> new ArrayList<>()).add(w);
        }
        return groups;
    }

    public static BiFunction<String, Integer, String> combiner() {
        // BiFunction : deux entrees de types differents ; String.join met le separateur entre les copies.
        return (word, n) -> {
            String[] copies = new String[n];
            java.util.Arrays.fill(copies, word);
            return String.join("-", copies);
        };
    }

    public static String firstMatching(List<String> words, Predicate<String> p) {
        // On s'arrete au premier mot qui passe ; la valeur par defaut couvre le cas vide.
        for (String w : words) {
            if (p.test(w)) {
                return w;
            }
        }
        return "aucun";
    }

    public static Predicate<String> rulesFor(int minLength) {
        // minLength est capture ; and et negate composent les regles.
        Predicate<String> longEnough = s -> s.length() >= minLength;
        Predicate<String> hasV = s -> s.contains("v");
        return longEnough.and(hasV.negate());
    }
}
