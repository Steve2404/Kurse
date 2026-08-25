package concurrency.exercises;

import concurrency.ExerciseChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EXERCICE 8 - Les collections concurrentes : ConcurrentHashMap et CopyOnWriteArrayList (niveau : difficile)
 * =========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un HashMap ou ArrayList "normal" n'est PAS concu pour etre modifie
 * par plusieurs threads en meme temps - ca peut carrement corrompre sa
 * structure interne, ou lancer une ConcurrentModificationException si
 * on l'itere pendant qu'un autre thread le modifie. Le Concurrency API
 * offre des versions "deja blindees" pour ca.
 *
 *
 * ==================================================================
 * TODO 1 : countOccurrencesConcurrent(words, threadCount)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ConcurrentHashMap.merge(cle, 1, Integer::sum) fait TOUT en une seule
 * operation ATOMIQUE : "si la cle n'existe pas encore, la creer avec
 * 1 ; sinon, ajouter 1 a sa valeur actuelle" - meme si PLUSIEURS
 * threads appellent merge() sur la MEME cle EN MEME TEMPS, aucun
 * comptage n'est jamais perdu (contrairement a un HashMap normal, qui
 * pourrait corrompre son etat interne dans ce cas).
 *
 * -- Le plan --
 *
 *   1. Preparer une ConcurrentHashMap<String, Integer> vide (le
 *      "carnet de comptage" partage).
 *   2. Repartir 'words' en 'threadCount' morceaux a peu pres egaux
 *      (par exemple, le mot d'indice i va dans le morceau numero
 *      i % threadCount).
 *   3. Lancer 'threadCount' threads, chacun parcourant SON morceau, et
 *      appelant map.merge(mot, 1, Integer::sum) pour chaque mot.
 *   4. Attendre TOUS les threads (join()), puis renvoyer la map.
 *
 *
 * ==================================================================
 * TODO 2 : buildCopyOnWriteSnapshotIterator(list)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * CopyOnWriteArrayList fabrique une COPIE ENTIERE de son tableau
 * interne a CHAQUE modification (add/remove...), plutot que de
 * modifier le tableau existant sur place. Consequence directe : un
 * iterateur DEJA CREE continue de pointer vers l'ANCIEN tableau (une
 * "photo" figee au moment ou list.iterator() a ete appele) - il ne
 * voit JAMAIS les modifications faites APRES sa creation, et ne lance
 * JAMAIS de ConcurrentModificationException (contrairement a
 * ArrayList).
 *
 * -- Le plan --
 *
 *   1. Renvoyer list.iterator() - une seule ligne, mais le moment OU
 *      cet appel a lieu est ce qui determine la "photo" prise.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre methode.
 *
 * Exemple a verifier : countOccurrencesConcurrent sur 4000 mots
 * repartis sur 6 threads donne EXACTEMENT le meme resultat qu'un
 * comptage sequentiel classique. Un iterateur pris sur ["a","b","c"],
 * PUIS "d" ajoute a la liste, ne parcourt QUE ["a","b","c"] - meme si
 * la liste elle-meme contient bien 4 elements juste apres.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - map.merge(cle, valeur, fonction) : si cle absente, pose
 *     'valeur' ; sinon, pose fonction.apply(ancienneValeur, valeur).
 *   - CopyOnWriteArrayList est ideal pour BEAUCOUP de lectures/peu
 *     d'ecritures (chaque ecriture est couteuse, car elle recopie tout
 *     le tableau) - jamais l'inverse.
 */
public class Exercise08_ConcurrentCollections {

    public static Map<String, Integer> countOccurrencesConcurrent(List<String> words, int threadCount)
            throws InterruptedException {
        throw new UnsupportedOperationException("TODO 1 : implementer countOccurrencesConcurrent()");
    }

    public static Iterator<String> buildCopyOnWriteSnapshotIterator(CopyOnWriteArrayList<String> list) {
        throw new UnsupportedOperationException("TODO 2 : implementer buildCopyOnWriteSnapshotIterator()");
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> words = new ArrayList<>();
        String[] vocab = {"a", "b", "c", "d"};
        Random random = new Random(42);
        for (int i = 0; i < 4000; i++) {
            words.add(vocab[random.nextInt(vocab.length)]);
        }
        Map<String, Integer> expected = new HashMap<>();
        for (String word : words) {
            expected.merge(word, 1, Integer::sum);
        }

        Map<String, Integer> actual = countOccurrencesConcurrent(words, 6);
        ExerciseChecker.check("le comptage concurrent donne le MEME resultat qu'un comptage sequentiel",
                actual.equals(expected));

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("a", "b", "c"));
        Iterator<String> snapshotIterator = buildCopyOnWriteSnapshotIterator(list);
        list.add("d");
        List<String> seen = new ArrayList<>();
        while (snapshotIterator.hasNext()) {
            seen.add(snapshotIterator.next());
        }
        ExerciseChecker.check("l'iterateur ne voit PAS l'ajout fait apres sa creation (photo figee)",
                seen.equals(List.of("a", "b", "c")));
        ExerciseChecker.check("la liste elle-meme contient bien le nouvel element", list.size() == 4);

        ExerciseChecker.summary();
    }
}
