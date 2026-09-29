package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * EXERCICE 15 - Streams primitifs avances : OptionalDouble, statistiques d'un stream vide, debordement, chars(), boxed(), interfaces fonctionnelles primitives (niveau : difficile)
 * ==========================================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Le rappel qui sert partout ici --
 *
 *   IntStream / LongStream / DoubleStream : des streams de NOMBRES
 *   BRUTS (pas d'objets Integer), avec des methodes que Stream<T> n'a
 *   pas : sum(), average(), summaryStatistics(), range()...
 *
 *   Leurs interfaces fonctionnelles sont PRIMITIVES aussi :
 *     IntStream.map(IntUnaryOperator)        int -> int
 *     IntStream.filter(IntPredicate)         int -> boolean
 *     IntStream.reduce(int, IntBinaryOperator)
 *     IntStream.mapToObj(IntFunction<R>)     int -> objet
 *     IntStream.asLongStream()               int -> long (elargissement)
 *     IntStream.boxed()                      int -> Integer
 *
 *
 * ==================================================================
 * TODO 1 : averageLength(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La longueur moyenne des mots. Stream<String> n'a PAS de average() :
 * il faut d'abord passer en IntStream (mapToInt). Et average() rend un
 * OptionalDouble (boite), car la moyenne de ZERO mot n'existe pas. Ici
 * on rend la boite telle quelle a l'appelant.
 *
 * -- Essayons a la main --
 *
 *   ["a", "bbb"] -> longueurs 1, 3 -> moyenne 2.0 -> OptionalDouble[2.0]
 *   []           -> OptionalDouble.empty
 *
 * -- Le plan --
 *
 *   1. Transformer chaque mot en sa longueur (int).
 *   2. Calculer la moyenne.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : describeStats(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * summaryStatistics() calcule count, min, max, somme et moyenne EN UN
 * SEUL passage (un stream ne se parcourt qu'une fois !). Mais que vaut
 * le min d'une liste VIDE ? IntSummaryStatistics ne lance pas
 * d'exception et ne rend pas de boite : il rend des valeurs
 * "sentinelles" surprenantes. Le min demarre a Integer.MAX_VALUE (pour
 * que n'importe quel vrai nombre soit plus petit) et le max a
 * Integer.MIN_VALUE. La moyenne d'un vide vaut 0.0.
 *
 * -- Essayons a la main --
 *
 *   [2, 4, 9] -> "count=3,min=2,max=9,avg=5.0"
 *   []        -> "count=0,min=2147483647,max=-2147483648,avg=0.0"
 *
 * -- Le plan --
 *
 *   1. Calculer les statistiques du tableau en un passage.
 *   2. Construire "count=" + ... + ",min=" + ... + ",max=" + ... + ",avg=" + ...
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. N'utilise PAS toString() de IntSummaryStatistics : son format
 * depend de la langue de la machine (la moyenne peut s'afficher
 * "0,000000" en francais). Utilise les getters.
 *
 *
 * ==================================================================
 * TODO 3 : sumOfSquares(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * 1*1 + 2*2 + ... + n*n. Pour n = 100 000, le resultat
 * (333 338 333 350 000) est BEAUCOUP trop grand pour un int (max
 * environ 2,1 milliards). Un int deborde EN SILENCE : pas d'erreur,
 * juste un resultat faux (avec IntStream on obtiendrait 1626540144).
 * Il faut travailler en long DES LE DEBUT (x*x deborde deja pour x
 * proche de 100 000 en int).
 *
 * -- Essayons a la main --
 *
 *   n = 3       -> 1 + 4 + 9 = 14
 *   n = 100000  -> 333338333350000
 *
 * -- Le plan --
 *
 *   1. Produire les entiers de 1 a n INCLUS, en long.
 *   2. Mettre chacun au carre.
 *   3. Faire la somme.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : countVowels(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * String.chars() transforme une chaine en IntStream : chaque lettre
 * devient son CODE numerique ('a' = 97). On peut alors filtrer ces
 * codes. On compte les voyelles "aeiouy", sans tenir compte des
 * majuscules. count() rend un long.
 *
 * -- Essayons a la main --
 *
 *   "Bonjour Java" -> o, o, u, a, a -> 5
 *   "xyz"          -> y -> 1
 *   ""             -> 0
 *
 * -- Le plan --
 *
 *   1. Passer le texte en minuscules.
 *   2. Le transformer en stream de codes de caracteres.
 *   3. Garder ceux qui sont dans "aeiouy".
 *   4. Compter.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : top3Desc(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * IntStream.sorted() ne prend AUCUN argument : il ne sait trier que
 * dans l'ordre croissant. Pour trier en decroissant, il faut revenir
 * dans le monde des objets (boxed() : int -> Integer), ou
 * sorted(Comparator.reverseOrder()) est permis.
 *
 * -- Essayons a la main --
 *
 *   [5, 1, 9, 3, 7] -> decroissant : 9, 7, 5, 3, 1 -> [9, 7, 5]
 *
 * -- Le plan --
 *
 *   1. Passer le tableau en IntStream puis en Stream<Integer>.
 *   2. Trier en decroissant, garder 3 elements, rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : transform(values, op, keep)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On recoit la recette de transformation (IntUnaryOperator) et la
 * regle de tri (IntPredicate) de l'exterieur. Ce sont les versions
 * "int" de UnaryOperator<Integer> et Predicate<Integer> : pas de
 * boxing, et les methodes s'appellent applyAsInt / test.
 *
 * -- Essayons a la main --
 *
 *   values = [1, 2, 3, 4, 5, 6], op = x*x, keep = pair
 *   carres : 1, 4, 9, 16, 25, 36 -> pairs : [4, 16, 36]
 *
 * -- Le plan --
 *
 *   1. Appliquer op a chaque valeur.
 *   2. Garder celles qui passent keep.
 *   3. Rendre un int[].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : product(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Il n'y a pas de "product()" tout fait : on utilise reduce, avec 1
 * comme valeur de depart (l'element neutre de la multiplication, comme
 * 0 pour l'addition). Un produit grandit TRES vite (20! depasse
 * largement un int) -> on elargit en long avec asLongStream() avant
 * de multiplier.
 *
 * -- Essayons a la main --
 *
 *   [2, 3, 4]      -> 1 * 2 * 3 * 4 = 24
 *   [1, 2, ..., 20] -> 20! = 2432902008176640000
 *   []             -> 1 (rien a multiplier, on garde la valeur de depart)
 *
 * -- Le plan --
 *
 *   1. Passer le tableau en IntStream, puis l'elargir en LongStream.
 *   2. Reduire par multiplication en partant de 1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : totalRevenue(lines)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque ligne de facture = quantite x prix unitaire. mapToDouble
 * transforme chaque ligne en un double, et DoubleStream.sum() fait le
 * total.
 *
 * -- Essayons a la main --
 *
 *   (2 x 1.5) + (4 x 0.25) + (1 x 10.0) = 3.0 + 1.0 + 10.0 = 14.0
 *
 * -- Le plan --
 *
 *   1. Transformer chaque ligne en son montant.
 *   2. Faire la somme.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 9 : initials(names)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut les initiales en majuscules : ["alice", "Bob", "chloe"] ->
 * "ABC". Le voyage des types est le vrai exercice :
 *   String --mapToInt--> int (code de la 1re lettre)
 *   int    --map-------> int (code en majuscule : Character.toUpperCase(int))
 *   int    --mapToObj--> String (il faut CASTER en char, sinon
 *                                String.valueOf(65) donne "65", pas "A" !)
 *   puis on colle tout avec Collectors.joining().
 *
 * -- Essayons a la main --
 *
 *   alice -> 'a' (97) -> 'A' (65) -> "A"
 *   Bob   -> 'B' (66) -> 66       -> "B"
 *   chloe -> 'c' (99) -> 'C' (67) -> "C"
 *   -> "ABC"
 *
 * -- Le plan --
 *
 *   1. Prendre le code de la 1re lettre de chaque nom.
 *   2. Le passer en majuscule.
 *   3. Le retransformer en String d'un caractere.
 *   4. Coller le tout.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier :
 *
 *   averageLength([a, bbb]) == OptionalDouble[2.0] ; averageLength([]) vide
 *   describeStats([2, 4, 9]) == "count=3,min=2,max=9,avg=5.0"
 *   describeStats([]) == "count=0,min=2147483647,max=-2147483648,avg=0.0"
 *   sumOfSquares(3) == 14 ; sumOfSquares(100000) == 333338333350000
 *   countVowels("Bonjour Java") == 5 ; ("xyz") == 1 ; ("") == 0
 *   top3Desc([5, 1, 9, 3, 7]) == [9, 7, 5]
 *   transform([1..6], carre, pair) == [4, 16, 36]
 *   product([2, 3, 4]) == 24 ; product([1..20]) == 2432902008176640000 ; product([]) == 1
 *   totalRevenue == 14.0
 *   initials([alice, Bob, chloe]) == "ABC"
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - words.stream().mapToInt(String::length).average()
 *   - IntSummaryStatistics s = IntStream.of(values).summaryStatistics();
 *     s.getCount(), s.getMin(), s.getMax(), s.getAverage()
 *   - LongStream.rangeClosed(1, n).map(x -> x * x).sum()
 *   - text.toLowerCase().chars().filter(c -> "aeiouy".indexOf(c) >= 0).count()
 *   - IntStream.of(values).boxed().sorted(Comparator.reverseOrder()).limit(3).toList()
 *   - IntStream.of(values).map(op).filter(keep).toArray()
 *   - IntStream.of(values).asLongStream().reduce(1L, (a, b) -> a * b)
 *   - lines.stream().mapToDouble(l -> l.quantity() * l.unitPrice()).sum()
 *   - mapToInt(n -> n.charAt(0)).map(Character::toUpperCase)
 *       .mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining())
 */
public class Exercise15_PrimitiveStreamsAdvanced {

    public record Line(int quantity, double unitPrice) {
    }

    public static OptionalDouble averageLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 1 : implementer averageLength()");
    }

    public static String describeStats(int[] values) {
        throw new UnsupportedOperationException("TODO 2 : implementer describeStats()");
    }

    public static long sumOfSquares(int n) {
        throw new UnsupportedOperationException("TODO 3 : implementer sumOfSquares()");
    }

    public static long countVowels(String text) {
        throw new UnsupportedOperationException("TODO 4 : implementer countVowels()");
    }

    public static List<Integer> top3Desc(int[] values) {
        throw new UnsupportedOperationException("TODO 5 : implementer top3Desc()");
    }

    public static int[] transform(int[] values, IntUnaryOperator op, IntPredicate keep) {
        throw new UnsupportedOperationException("TODO 6 : implementer transform()");
    }

    public static long product(int[] values) {
        throw new UnsupportedOperationException("TODO 7 : implementer product()");
    }

    public static double totalRevenue(List<Line> lines) {
        throw new UnsupportedOperationException("TODO 8 : implementer totalRevenue()");
    }

    public static String initials(List<String> names) {
        throw new UnsupportedOperationException("TODO 9 : implementer initials()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("averageLength([a, bbb]) == 2.0",
                averageLength(List.of("a", "bbb")).equals(OptionalDouble.of(2.0)));
        ExerciseChecker.check("averageLength([]) est vide", averageLength(List.of()).isEmpty());

        ExerciseChecker.check("describeStats([2, 4, 9])",
                describeStats(new int[]{2, 4, 9}).equals("count=3,min=2,max=9,avg=5.0"));
        ExerciseChecker.check("describeStats([]) montre les valeurs sentinelles",
                describeStats(new int[]{}).equals("count=0,min=2147483647,max=-2147483648,avg=0.0"));

        ExerciseChecker.check("sumOfSquares(3) == 14", sumOfSquares(3) == 14L);
        ExerciseChecker.check("sumOfSquares(100000) == 333338333350000 (pas de debordement)",
                sumOfSquares(100000) == 333338333350000L);

        ExerciseChecker.check("countVowels(\"Bonjour Java\") == 5", countVowels("Bonjour Java") == 5);
        ExerciseChecker.check("countVowels(\"xyz\") == 1", countVowels("xyz") == 1);
        ExerciseChecker.check("countVowels(\"\") == 0", countVowels("") == 0);

        ExerciseChecker.check("top3Desc([5, 1, 9, 3, 7]) == [9, 7, 5]",
                top3Desc(new int[]{5, 1, 9, 3, 7}).equals(List.of(9, 7, 5)));

        ExerciseChecker.check("transform([1..6], carre, pair) == [4, 16, 36]",
                Arrays.equals(transform(new int[]{1, 2, 3, 4, 5, 6}, x -> x * x, x -> x % 2 == 0), new int[]{4, 16, 36}));

        ExerciseChecker.check("product([2, 3, 4]) == 24", product(new int[]{2, 3, 4}) == 24L);
        ExerciseChecker.check("product([1..20]) == 20! == 2432902008176640000",
                product(IntStream.rangeClosed(1, 20).toArray()) == 2432902008176640000L);
        ExerciseChecker.check("product([]) == 1", product(new int[]{}) == 1L);

        ExerciseChecker.check("totalRevenue == 14.0",
                totalRevenue(List.of(new Line(2, 1.5), new Line(4, 0.25), new Line(1, 10.0))) == 14.0);

        ExerciseChecker.check("initials([alice, Bob, chloe]) == ABC",
                initials(List.of("alice", "Bob", "chloe")).equals("ABC"));

        ExerciseChecker.summary();
    }
}
