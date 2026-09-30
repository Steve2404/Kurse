package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeSet;

/**
 * EXERCICE 11 - Mini moteur de recherche : un index inverse en TreeMap de TreeSet (niveau : avance)
 * =================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * A la fin d'un livre, l'index dit pour chaque mot a quelles pages il
 * apparait. On fait pareil avec des documents numerotes 0, 1, 2... :
 *
 *   0 "Java aime les listes"   1 "les maps en java"   2 "une liste chainee"   3 "java map et set"
 *
 *   index : aime=[0] chainee=[2] en=[1] et=[3] java=[0, 1, 3] les=[0, 1] liste=[2] listes=[0] map=[3] maps=[1] set=[3] une=[2]
 *
 * TreeMap garde les mots tries (on pourra chercher par prefixe) et
 * TreeSet garde les numeros tries et sans doublon.
 *
 *
 * ==================================================================
 * TODO 1 : build(docs)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ["a b", "b b"] -> {a=[0], b=[0, 1]}  (b deux fois dans le doc 1 : un seul 1)
 *
 * -- Le plan --
 *
 *   1. Pour chaque document i, pour chaque mot (en minuscules, separe par des espaces) :
 *   2. prendre (ou creer) l'ensemble du mot, y ajouter i.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : computeIfAbsent fait "prendre ou creer".
 *
 *
 * ==================================================================
 * TODO 2 : searchAll(index, words)   (ET)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les documents qui contiennent TOUS les mots. Piege : si tu fais
 * retainAll directement sur l'ensemble de l'index, tu ABIMES l'index
 * (la prochaine recherche sera fausse). Travaille sur une copie.
 *
 * -- Essayons a la main --
 *
 *   [java, les] -> [0, 1] ; [java, inconnu] -> [] ; [] -> []
 *
 * -- Le plan --
 *
 *   1. Liste de mots vide -> ensemble vide.
 *   2. Copier l'ensemble du 1er mot (vide s'il est absent).
 *   3. Pour chaque autre mot : garder seulement les numeros communs.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2 : "ensemble d'un mot, vide s'il est absent" sert aussi au
 * TODO 3) : getOrDefault(mot, new TreeSet<>()) suffit.
 *
 *
 * ==================================================================
 * TODO 3 : searchAny(index, words)   (OU)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   [set, une] -> [2, 3]
 *
 * -- Le plan --
 *
 *   1. Un ensemble vide ; y ajouter tous les numeros de chaque mot.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : wordsWithPrefix(index, prefix)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tous les mots qui commencent par prefix, dans l'ordre. Comme les mots
 * sont TRIES, ils sont tous cote a cote : il suffit de decouper la
 * tranche [prefix ; prefix suivi du plus grand caractere possible].
 *
 * -- Essayons a la main --
 *
 *   "list" -> [liste, listes] ; "ma" -> [map, maps] ; "z" -> []
 *
 * -- Le plan --
 *
 *   1. subMap(prefix inclus, prefix + '￿' exclu).
 *   2. Rendre ses cles dans une liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : mostCommonWord(index)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le mot present dans le plus de documents. En cas d'egalite, le
 * premier dans l'ordre alphabetique (l'index est deja trie : garder le
 * premier trouve et ne remplacer que si c'est STRICTEMENT mieux).
 *
 * -- Essayons a la main --
 *
 *   -> java (3 documents)
 *
 * -- Le plan --
 *
 *   1. Parcourir entrySet() ; garder le meilleur (taille strictement plus grande).
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
 *   - doc.toLowerCase().split(" ")
 *   - index.computeIfAbsent(word, k -> new TreeSet<>()).add(i);
 *   - new TreeSet<>(index.getOrDefault(w, new TreeSet<>())) puis retainAll / addAll.
 *   - index.subMap(prefix, true, prefix + '￿', false).keySet()
 */
public class Exercise11_InvertedIndex {

    public static NavigableMap<String, TreeSet<Integer>> build(List<String> docs) {
        throw new UnsupportedOperationException("TODO 1 : implementer build()");
    }

    public static TreeSet<Integer> searchAll(NavigableMap<String, TreeSet<Integer>> index, List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer searchAll()");
    }

    public static TreeSet<Integer> searchAny(NavigableMap<String, TreeSet<Integer>> index, List<String> words) {
        throw new UnsupportedOperationException("TODO 3 : implementer searchAny()");
    }

    public static List<String> wordsWithPrefix(NavigableMap<String, TreeSet<Integer>> index, String prefix) {
        throw new UnsupportedOperationException("TODO 4 : implementer wordsWithPrefix()");
    }

    public static String mostCommonWord(NavigableMap<String, TreeSet<Integer>> index) {
        throw new UnsupportedOperationException("TODO 5 : implementer mostCommonWord()");
    }

    public static void main(String[] args) {
        List<String> docs = List.of("Java aime les listes", "les maps en java", "une liste chainee", "java map et set");

        ExerciseChecker.check("build([a b, b b]) -> {a=[0], b=[0, 1]}", build(List.of("a b", "b b")).toString().equals("{a=[0], b=[0, 1]}"));
        NavigableMap<String, TreeSet<Integer>> index = build(docs);
        ExerciseChecker.check("build(docs) : java=[0, 1, 3], 12 mots",
                index.get("java").toString().equals("[0, 1, 3]") && index.size() == 12);

        ExerciseChecker.check("searchAll([java, les]) -> [0, 1]", searchAll(index, List.of("java", "les")).toString().equals("[0, 1]"));
        ExerciseChecker.check("searchAll n'abime pas l'index (java toujours [0, 1, 3])", index.get("java").toString().equals("[0, 1, 3]"));
        ExerciseChecker.check("searchAll([java, inconnu]) -> [] et searchAll([]) -> []",
                searchAll(index, List.of("java", "inconnu")).isEmpty() && searchAll(index, List.of()).isEmpty());

        ExerciseChecker.check("searchAny([set, une]) -> [2, 3]", searchAny(index, List.of("set", "une")).toString().equals("[2, 3]"));
        ExerciseChecker.check("searchAny n'abime pas l'index (set toujours [3])", index.get("set").toString().equals("[3]"));

        ExerciseChecker.check("wordsWithPrefix(list) -> [liste, listes], (ma) -> [map, maps], (z) -> []",
                wordsWithPrefix(index, "list").equals(List.of("liste", "listes"))
                        && wordsWithPrefix(index, "ma").equals(List.of("map", "maps"))
                        && wordsWithPrefix(index, "z").isEmpty());

        ExerciseChecker.check("mostCommonWord -> java ; egalite -> le premier alphabetique",
                mostCommonWord(index).equals("java") && mostCommonWord(build(List.of("b a", "a b"))).equals("a"));

        ExerciseChecker.summary();
    }
}
