package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * EXERCICE 24 - Spliterator : couper, avancer, vider, decouper recursivement (niveau : difficile)
 * ==============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Spliterator, en une phrase --
 *
 * Un Spliterator (SPLIT + iTERATOR) est un "sac" d'elements qu'on peut :
 *   - PARTAGER : trySplit() retire une partie du sac (en general la
 *     1re moitie) et la rend dans un NOUVEAU Spliterator ; le sac
 *     d'origine garde le reste. Rend null si le sac refuse de se
 *     couper.
 *   - PIOCHER UN element : tryAdvance(action) applique action au
 *     prochain element et rend true, ou rend false si le sac est vide.
 *   - VIDER : forEachRemaining(action) applique action a TOUT ce qui
 *     reste.
 *   - MESURER : estimateSize() = combien il reste (une estimation ;
 *     Long.MAX_VALUE si inconnu, par exemple pour un stream infini).
 * C'est la mecanique cachee qui permet aux streams paralleles de
 * partager le travail entre plusieurs threads.
 *
 * Dans cet exercice, on travaille sur des ArrayList (dont trySplit
 * coupe au milieu : pour 5 elements, il rend les 2 premiers et garde
 * les 3 derniers ; pour 7, il rend 3 et garde 4).
 *
 *
 * ==================================================================
 * TODO 1 : splitInHalf(items)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu as un sac de bonbons et tu en donnes la moitie a ton frere :
 * trySplit() rend le sac du frere, et ton sac a toi a maigri. Ensuite
 * chacun vide son sac dans sa propre liste.
 *
 * -- Essayons a la main --
 *
 *   [a, b, c, d, e] -> frere : [a, b] ; toi : [c, d, e]
 *   -> [[a, b], [c, d, e]]
 *
 * -- Le plan --
 *
 *   1. Obtenir le Spliterator d'une ArrayList copiee de items.
 *   2. trySplit() -> le sac du frere.
 *   3. Vider le sac du frere dans une liste, puis le tien dans une autre.
 *   4. Rendre la liste des 2 listes.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * L'etape "vider un Spliterator dans une liste" revient 2 fois (et
 * reservira au TODO 3) : ecris une methode privee generique
 * drain(Spliterator<T>) -> List<T>.
 *
 *
 * ==================================================================
 * TODO 2 : take(spliterator, n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu pioches au plus n bonbons, UN PAR UN, et tu t'arretes si le sac
 * est vide avant. tryAdvance est parfait : il pioche un seul element et
 * te dit (true/false) s'il y en avait un. Le sac garde ce que tu n'as
 * pas pioche : un 2e appel continue la ou le 1er s'est arrete.
 *
 * -- Essayons a la main --
 *
 *   sac [a, b, c, d, e]
 *   take(sac, 2)  -> [a, b], estimateSize() == 3
 *   take(sac, 10) -> [c, d, e] (le sac s'est vide avant 10)
 *   sac.tryAdvance(...) -> false
 *
 * -- Le plan --
 *
 *   1. Liste vide.
 *   2. Tant que la liste a moins de n elements ET que tryAdvance
 *      reussit (en ajoutant l'element a la liste) : continuer.
 *   3. Rendre la liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Attention a l'ORDRE des conditions du while : si tu appelles
 * tryAdvance AVANT de verifier la taille, tu pioches un element de
 * trop (et il est perdu).
 *
 *
 * ==================================================================
 * TODO 3 : chunks(spliterator, maxChunkSize)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On doit repartir un gros sac en petits sacs d'au plus maxChunkSize
 * bonbons, en ne sachant que "couper en deux". Recette recursive
 * (une boite magique qui s'appelle elle-meme) :
 *   - si le sac est assez petit (estimateSize() <= max) : on le vide
 *     dans UN morceau ;
 *   - sinon on le coupe (trySplit) : on traite D'ABORD le morceau
 *     retire (la 1re partie), PUIS le reste, avec la meme recette ;
 *   - si trySplit rend null (le sac refuse), on le vide tel quel.
 *
 * -- Essayons a la main --
 *
 *   [0..9] (10), max 3
 *   10 > 3 -> coupe en [0..4] (5) et [5..9] (5)
 *     [0..4] : 5 > 3 -> [0, 1] (2 <= 3, un morceau) et [2, 3, 4] (3, un morceau)
 *     [5..9] : 5 > 3 -> [5, 6] et [7, 8, 9]
 *   -> [[0, 1], [2, 3, 4], [5, 6], [7, 8, 9]]
 *
 * -- Le plan --
 *
 *   1. Creer la liste de resultats.
 *   2. Appeler une methode privee recursive (spliterator, max, resultats).
 *   3. Rendre les resultats.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : la recette recursive "traiter un sac" se raconte seule ET se
 * rappelle elle-meme. Elle reutilise drain (TODO 1).
 *
 *
 * ==================================================================
 * TODO 4 : shareFood(food)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'exemple du livre OCP. Un sac de nourriture pour animaux, 3
 * personnes :
 *   1. Emma prend la 1re moitie du sac (trySplit) et la vide entierement.
 *   2. Jill prend la 1re moitie de ce qui RESTE (trySplit a nouveau),
 *      en pioche UN element (tryAdvance), puis vide le reste de SON sac.
 *   3. On vide ce qui reste dans le sac d'origine.
 * On rend "emma=... jill1=... jill2=... reste=...".
 *
 * -- Essayons a la main --
 *
 *   [bird-, bunny-, cat-, dog-, fish-, lamb-, mouse-] (7)
 *   Emma : trySplit -> 3 premiers : [bird-, bunny-, cat-] ; il reste 4
 *   Jill : trySplit des 4 -> [dog-, fish-] ; pioche [dog-], puis vide [fish-]
 *   reste : [lamb-, mouse-]
 *   -> "emma=[bird-, bunny-, cat-] jill1=[dog-] jill2=[fish-] reste=[lamb-, mouse-]"
 *
 * -- Le plan --
 *
 *   Suivre les 3 etapes ci-dessus, en rangeant ce que chacun obtient
 *   dans une liste, puis assembler la chaine.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * drain (TODO 1) sert 3 fois ici.
 *
 *
 * ==================================================================
 * TODO 5 : describeCharacteristics(collection)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque sac porte des etiquettes (caracteristiques) : ORDERED (les
 * elements ont un ordre defini), SIZED (on connait la taille exacte),
 * DISTINCT (pas de doublons), SORTED (tries). hasCharacteristics(X)
 * repond oui/non pour chaque etiquette. Les streams s'en servent pour
 * optimiser (par exemple : distinct() ne fait rien si le sac est deja
 * DISTINCT).
 *
 * -- Essayons a la main --
 *
 *   ArrayList -> "ORDERED=true,SIZED=true,DISTINCT=false,SORTED=false"
 *   HashSet   -> "ORDERED=false,SIZED=true,DISTINCT=true,SORTED=false"
 *   TreeSet   -> "ORDERED=true,SIZED=true,DISTINCT=true,SORTED=true"
 *
 * -- Le plan --
 *
 *   1. Obtenir le Spliterator de la collection.
 *   2. Interroger les 4 etiquettes, assembler la chaine.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier :
 *
 *   splitInHalf([a, b, c, d, e]) == [[a, b], [c, d, e]]
 *   take : [a, b] puis estimateSize == 3, puis [c, d, e], puis tryAdvance == false
 *   chunks([0..9], 3) == [[0, 1], [2, 3, 4], [5, 6], [7, 8, 9]]
 *   chunks([x, y], 5) == [[x, y]]
 *   shareFood == "emma=[bird-, bunny-, cat-] jill1=[dog-] jill2=[fish-] reste=[lamb-, mouse-]"
 *   describeCharacteristics : voir TODO 5
 *   (et main() montre qu'un stream infini a un estimateSize() de Long.MAX_VALUE)
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Spliterator<String> sp = new ArrayList<>(items).spliterator();
 *   - Spliterator<String> other = sp.trySplit();
 *   - private static <T> List<T> drain(Spliterator<T> sp) {
 *         List<T> out = new ArrayList<>(); sp.forEachRemaining(out::add); return out; }
 *   - while (out.size() < n && sp.tryAdvance(out::add)) { }
 *   - private static <T> void split(Spliterator<T> sp, int max, List<List<T>> out)
 *   - sp.hasCharacteristics(Spliterator.ORDERED) (idem SIZED, DISTINCT, SORTED)
 */
public class Exercise24_SpliteratorBasics {

    public static List<List<String>> splitInHalf(List<String> items) {
        throw new UnsupportedOperationException("TODO 1 : implementer splitInHalf()");
    }

    public static List<String> take(Spliterator<String> spliterator, int n) {
        throw new UnsupportedOperationException("TODO 2 : implementer take()");
    }

    public static <T> List<List<T>> chunks(Spliterator<T> spliterator, int maxChunkSize) {
        throw new UnsupportedOperationException("TODO 3 : implementer chunks()");
    }

    public static String shareFood(List<String> food) {
        throw new UnsupportedOperationException("TODO 4 : implementer shareFood()");
    }

    public static String describeCharacteristics(Collection<?> collection) {
        throw new UnsupportedOperationException("TODO 5 : implementer describeCharacteristics()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("splitInHalf([a, b, c, d, e]) == [[a, b], [c, d, e]]",
                splitInHalf(List.of("a", "b", "c", "d", "e")).equals(List.of(List.of("a", "b"), List.of("c", "d", "e"))));

        Spliterator<String> bag = new ArrayList<>(List.of("a", "b", "c", "d", "e")).spliterator();
        ExerciseChecker.check("take(sac, 2) == [a, b]", take(bag, 2).equals(List.of("a", "b")));
        ExerciseChecker.check("... puis estimateSize() == 3", bag.estimateSize() == 3);
        ExerciseChecker.check("take(sac, 10) == [c, d, e]", take(bag, 10).equals(List.of("c", "d", "e")));
        ExerciseChecker.check("... puis tryAdvance rend false (sac vide)", !bag.tryAdvance(x -> { }));

        List<Integer> zeroToNine = new ArrayList<>(IntStream.range(0, 10).boxed().toList());
        ExerciseChecker.check("chunks([0..9], 3) == [[0, 1], [2, 3, 4], [5, 6], [7, 8, 9]]",
                chunks(zeroToNine.spliterator(), 3).equals(List.of(List.of(0, 1), List.of(2, 3, 4), List.of(5, 6), List.of(7, 8, 9))));
        ExerciseChecker.check("chunks([x, y], 5) == [[x, y]]",
                chunks(new ArrayList<>(List.of("x", "y")).spliterator(), 5).equals(List.of(List.of("x", "y"))));

        ExerciseChecker.check("shareFood (exemple du livre)",
                shareFood(List.of("bird-", "bunny-", "cat-", "dog-", "fish-", "lamb-", "mouse-"))
                        .equals("emma=[bird-, bunny-, cat-] jill1=[dog-] jill2=[fish-] reste=[lamb-, mouse-]"));

        ExerciseChecker.check("describeCharacteristics(ArrayList)",
                describeCharacteristics(new ArrayList<>(List.of(1, 2))).equals("ORDERED=true,SIZED=true,DISTINCT=false,SORTED=false"));
        ExerciseChecker.check("describeCharacteristics(HashSet)",
                describeCharacteristics(new HashSet<>(List.of(1, 2))).equals("ORDERED=false,SIZED=true,DISTINCT=true,SORTED=false"));
        ExerciseChecker.check("describeCharacteristics(TreeSet)",
                describeCharacteristics(new TreeSet<>(List.of(1, 2))).equals("ORDERED=true,SIZED=true,DISTINCT=true,SORTED=true"));

        Spliterator<Integer> infinite = Stream.iterate(1, x -> x + 1).spliterator();
        ExerciseChecker.check("(demo) stream infini : estimateSize() == Long.MAX_VALUE et pas SIZED",
                infinite.estimateSize() == Long.MAX_VALUE && !infinite.hasCharacteristics(Spliterator.SIZED));

        ExerciseChecker.summary();
    }
}
