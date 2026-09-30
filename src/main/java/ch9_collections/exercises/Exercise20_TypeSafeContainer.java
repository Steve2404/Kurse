package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * EXERCICE 20 - Generiques avances : conteneur type par Class<T>, signatures PECS, record generique (niveau : avance)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les signatures sont DEJA ecrites : a toi d'ecrire les corps, puis de
 * relire chaque signature en te demandant pourquoi elle est comme ca.
 * main() appelle chaque methode avec des types "genants" (une liste
 * d'Apple la ou on attend des Fruit...) : si la signature etait naive,
 * main() ne compilerait meme pas.
 *
 *
 * ==================================================================
 * TODO 1 : Favorites.put(type, value) et Favorites.get(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un casier par TYPE : le casier "String" contient un String, le casier
 * "Integer" un Integer. La cle est l'objet Class lui-meme
 * (String.class). Dans la Map, tout est range en Object ; a la sortie,
 * type.cast(...) remet le bon type SANS avertissement "unchecked", et
 * verifie vraiment a l'execution.
 *
 * -- Essayons a la main --
 *
 *   put(String.class, "java") ; put(Integer.class, 17) -> get(Integer.class) == 17 ; get(Double.class) == null
 *
 * -- Le plan --
 *
 *   1. put : ranger value sous la cle type.
 *   2. get : lire le casier et le convertir avec type.cast.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : max(items)
 *          static <T extends Comparable<? super T>> T max(Collection<? extends T> items)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Apple est un Fruit, et les Fruit savent se comparer entre eux
 * (Comparable<Fruit>). Un Apple n'est donc PAS un Comparable<Apple> :
 * avec <T extends Comparable<T>>, max(pommes) serait refuse. Le
 * "? super T" dit : "il suffit que T sache se comparer a un de ses
 * ancetres".
 *
 * -- Essayons a la main --
 *
 *   pommes de 120, 180, 150 g -> la pomme de 180 g ; liste vide -> NoSuchElementException
 *
 * -- Le plan --
 *
 *   1. Vide -> NoSuchElementException.
 *   2. Garder le meilleur ; remplacer si compareTo(meilleur) > 0.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : argMax(map)
 *          static <K, V extends Comparable<? super V>> K argMax(Map<K, V> map)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La CLE dont la valeur est la plus grande (en cas d'egalite, la
 * premiere rencontree). Map vide -> null.
 *
 * -- Essayons a la main --
 *
 *   {ana=12, bob=17, cid=17} -> bob
 *
 * -- Le plan --
 *
 *   1. Parcourir entrySet ; remplacer seulement si STRICTEMENT plus grand.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : mapAll(items, f)
 *          static <T, R> List<R> mapAll(List<? extends T> items, Function<? super T, ? extends R> f)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Appliquer f a chaque element. La fonction peut accepter PLUS large
 * (? super T : une fonction sur Fruit marche pour des Apple) et rendre
 * PLUS precis (? extends R).
 *
 * -- Essayons a la main --
 *
 *   mapAll(pommes, Fruit::grams) -> [120, 180, 150]
 *
 * -- Le plan --
 *
 *   1. Une ArrayList vide ; y ajouter f.apply(x) pour chaque x.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : partition(items, test)
 *          static <T> Map<Boolean, List<T>> partition(Collection<? extends T> items, Predicate<? super T> test)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux tas : true (ceux qui passent le test) et false. Les deux cles
 * existent toujours, meme vides.
 *
 * -- Essayons a la main --
 *
 *   partition(pommes, f -> f.grams() > 130) -> {false=[120 g], true=[180 g, 150 g]}
 *
 * -- Le plan --
 *
 *   1. Une Map avec false -> liste vide et true -> liste vide.
 *   2. Chaque element va dans la liste de test.test(x).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : Pair.swap()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pair<A, B> est un record generique. swap() rend une Pair<B, A> : les
 * types s'echangent en meme temps que les valeurs.
 *
 * -- Essayons a la main --
 *
 *   new Pair<>("age", 17).swap() -> Pair[first=17, second=age]
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
 *   - private final Map<Class<?>, Object> values = new HashMap<>();
 *   - return type.cast(values.get(type));   (cast(null) rend null)
 *   - Iterator ou for-each : T best = null; for (T x : items) ...
 *   - new Pair<>(second, first)
 */
public class Exercise20_TypeSafeContainer {

    public static class Fruit implements Comparable<Fruit> {
        private final int grams;

        public Fruit(int grams) {
            this.grams = grams;
        }

        public int grams() {
            return grams;
        }

        @Override
        public int compareTo(Fruit other) {
            return Integer.compare(grams, other.grams);
        }

        @Override
        public String toString() {
            return grams + " g";
        }
    }

    public static class Apple extends Fruit {
        public Apple(int grams) {
            super(grams);
        }
    }

    public static class Favorites {
        private final Map<Class<?>, Object> values = new HashMap<>();

        public <T> void put(Class<T> type, T value) {
            throw new UnsupportedOperationException("TODO 1 : implementer put()");
        }

        public <T> T get(Class<T> type) {
            throw new UnsupportedOperationException("TODO 1 : implementer get()");
        }
    }

    public static <T extends Comparable<? super T>> T max(Collection<? extends T> items) {
        throw new UnsupportedOperationException("TODO 2 : implementer max()");
    }

    public static <K, V extends Comparable<? super V>> K argMax(Map<K, V> map) {
        throw new UnsupportedOperationException("TODO 3 : implementer argMax()");
    }

    public static <T, R> List<R> mapAll(List<? extends T> items, Function<? super T, ? extends R> f) {
        throw new UnsupportedOperationException("TODO 4 : implementer mapAll()");
    }

    public static <T> Map<Boolean, List<T>> partition(Collection<? extends T> items, Predicate<? super T> test) {
        throw new UnsupportedOperationException("TODO 5 : implementer partition()");
    }

    public record Pair<A, B>(A first, B second) {
        public Pair<B, A> swap() {
            throw new UnsupportedOperationException("TODO 6 : implementer swap()");
        }
    }

    public static void main(String[] args) {
        Favorites favorites = new Favorites();
        favorites.put(String.class, "java");
        favorites.put(Integer.class, 17);
        favorites.put(Class.class, Favorites.class);
        String s = favorites.get(String.class);
        int n = favorites.get(Integer.class);
        ExerciseChecker.check("Favorites : java, 17, Favorites.class, et null pour Double",
                s.equals("java") && n == 17 && favorites.get(Class.class) == Favorites.class && favorites.get(Double.class) == null);

        List<Apple> apples = List.of(new Apple(120), new Apple(180), new Apple(150));
        Apple heaviest = max(apples);
        ExerciseChecker.check("max(List<Apple>) -> 180 g (grace a Comparable<? super T>)", heaviest.grams() == 180);
        boolean thrown = false;
        try {
            max(List.<Apple>of());
        } catch (NoSuchElementException e) {
            thrown = true;
        }
        ExerciseChecker.check("max(vide) -> NoSuchElementException", thrown);

        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("ana", 12);
        scores.put("bob", 17);
        scores.put("cid", 17);
        ExerciseChecker.check("argMax({ana=12, bob=17, cid=17}) -> bob ; vide -> null",
                "bob".equals(argMax(scores)) && argMax(new HashMap<String, Integer>()) == null);

        Function<Fruit, Integer> weigh = Fruit::grams;
        List<Number> weights = mapAll(apples, weigh);
        ExerciseChecker.check("mapAll(List<Apple>, Function<Fruit, Integer>) -> List<Number> [120, 180, 150]",
                weights.equals(List.of(120, 180, 150)));

        Predicate<Object> notNull = o -> o != null;
        Map<Boolean, List<Fruit>> heavy = partition(apples, (Fruit f) -> f.grams() > 130);
        ExerciseChecker.check("partition(pommes, > 130 g) -> {false=[120 g], true=[180 g, 150 g]}",
                heavy.get(false).toString().equals("[120 g]") && heavy.get(true).toString().equals("[180 g, 150 g]"));
        ExerciseChecker.check("partition avec Predicate<Object> : les deux cles existent meme vides",
                partition(List.<String>of(), notNull).get(true).isEmpty() && partition(List.<String>of(), notNull).get(false).isEmpty());

        Pair<Integer, String> swapped = new Pair<>("age", 17).swap();
        ExerciseChecker.check("Pair(age, 17).swap() -> Pair[first=17, second=age]", swapped.toString().equals("Pair[first=17, second=age]"));

        ExerciseChecker.summary();
    }
}
