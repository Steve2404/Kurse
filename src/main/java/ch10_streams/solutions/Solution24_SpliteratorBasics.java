package ch10_streams.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;

/**
 * Corrige de l'exercice 24. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise24_SpliteratorBasics.
 */
public class Solution24_SpliteratorBasics {

    public static List<List<String>> splitInHalf(List<String> items) {
        // trySplit retire la 1re moitie dans un NOUVEAU Spliterator ; l'original garde le reste.
        Spliterator<String> mine = new ArrayList<>(items).spliterator();
        Spliterator<String> brother = mine.trySplit();
        List<String> first = drain(brother);
        List<String> second = drain(mine);
        return List.of(first, second);
    }

    private static <T> List<T> drain(Spliterator<T> spliterator) {
        // forEachRemaining vide tout ce qui reste dans le Spliterator.
        List<T> out = new ArrayList<>();
        spliterator.forEachRemaining(out::add);
        return out;
    }

    public static List<String> take(Spliterator<String> spliterator, int n) {
        // Ordre des conditions : on verifie la taille AVANT tryAdvance, sinon on piocherait
        // un element de trop (et il serait perdu).
        List<String> out = new ArrayList<>();
        while (out.size() < n && spliterator.tryAdvance(out::add)) {
        }
        return out;
    }

    public static <T> List<List<T>> chunks(Spliterator<T> spliterator, int maxChunkSize) {
        // La vraie recette est dans split (recursive).
        List<List<T>> out = new ArrayList<>();
        split(spliterator, maxChunkSize, out);
        return out;
    }

    private static <T> void split(Spliterator<T> spliterator, int maxChunkSize, List<List<T>> out) {
        // Recursivite : trop gros -> on coupe et on traite la 1re partie puis le reste ;
        // assez petit (ou trySplit refuse : null) -> on vide dans un morceau.
        if (spliterator.estimateSize() > maxChunkSize) {
            Spliterator<T> prefix = spliterator.trySplit();
            if (prefix != null) {
                split(prefix, maxChunkSize, out);
                split(spliterator, maxChunkSize, out);
                return;
            }
        }
        out.add(drain(spliterator));
    }

    public static String shareFood(List<String> food) {
        // Exemple du livre : chaque trySplit prend la 1re moitie de ce qui reste dans le sac d'origine.
        Spliterator<String> original = new ArrayList<>(food).spliterator();
        List<String> emma = drain(original.trySplit());
        Spliterator<String> jillsBag = original.trySplit();
        List<String> jill1 = new ArrayList<>();
        jillsBag.tryAdvance(jill1::add);
        List<String> jill2 = drain(jillsBag);
        List<String> rest = drain(original);
        return "emma=" + emma + " jill1=" + jill1 + " jill2=" + jill2 + " reste=" + rest;
    }

    public static String describeCharacteristics(Collection<?> collection) {
        // Les caracteristiques sont des bits : hasCharacteristics teste chacun d'eux.
        Spliterator<?> sp = collection.spliterator();
        return "ORDERED=" + sp.hasCharacteristics(Spliterator.ORDERED)
                + ",SIZED=" + sp.hasCharacteristics(Spliterator.SIZED)
                + ",DISTINCT=" + sp.hasCharacteristics(Spliterator.DISTINCT)
                + ",SORTED=" + sp.hasCharacteristics(Spliterator.SORTED);
    }
}
