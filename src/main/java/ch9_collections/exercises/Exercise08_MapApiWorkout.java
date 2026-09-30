package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * EXERCICE 8 - L'API de Map a fond : merge, computeIfAbsent, computeIfPresent... et ce qu'elles rendent (niveau : difficile)
 * ==========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une Map, c'est un meuble a tiroirs etiquetes (cle -> valeur). Depuis
 * Java 8, le meuble sait faire des gestes complets en UNE ligne :
 *
 *   getOrDefault(k, d)            regarder, avec une valeur de secours
 *   putIfAbsent(k, v)             ranger seulement si le tiroir est vide
 *   computeIfAbsent(k, k -> v)    creer le contenu seulement si le tiroir est vide
 *   computeIfPresent(k, (k, v) -> ...)  retravailler un tiroir plein
 *   merge(k, v, (ancien, v) -> ...)     fusionner (le geste du compteur)
 *   replaceAll((k, v) -> ...)     retravailler tous les tiroirs
 *
 * Regle d'or piegeuse : quand la fonction de compute... ou de merge
 * rend null, le tiroir est SUPPRIME.
 *
 *
 * ==================================================================
 * TODO 1 : countWords(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Compter combien de fois chaque mot apparait, dans l'ordre alphabetique.
 *
 * -- Essayons a la main --
 *
 *   [b, a, b] -> {a=1, b=2}
 *
 * -- Le plan --
 *
 *   1. Un TreeMap vide.
 *   2. Pour chaque mot : merge(mot, 1, somme).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : Integer::sum est deja la boite.
 *
 *
 * ==================================================================
 * TODO 2 : groupByLength(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ranger les mots par longueur : un tiroir par longueur, qui contient
 * la liste des mots de cette longueur (dans l'ordre d'arrivee).
 *
 * -- Essayons a la main --
 *
 *   [ab, c, de] -> {1=[c], 2=[ab, de]}
 *
 * -- Le plan --
 *
 *   1. Un TreeMap vide.
 *   2. Pour chaque mot : computeIfAbsent(longueur, cree une liste vide), puis add(mot).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : sell(stock, item, quantity)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Vendre : enlever quantity au stock de item. Si le stock tombe a 0 ou
 * moins, le tiroir disparait. Un article inconnu ne cree rien.
 *
 * -- Essayons a la main --
 *
 *   {pomme=5}, pomme, 2 -> {pomme=3}
 *   {pomme=5}, pomme, 5 -> {}
 *   {pomme=5}, kiwi, 1  -> {pomme=5}
 *
 * -- Le plan --
 *
 *   1. computeIfPresent(item, ...) : nouveau = ancien - quantity.
 *   2. Rendre null si nouveau <= 0 (le tiroir est alors supprime).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : returned(method, old)   et   TODO 5 : stored(method, old)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * old est la valeur dans le tiroir "k" avant l'appel (null = tiroir
 * absent). Pour chaque methode ci-dessous, dire ce que l'appel REND
 * (TODO 4) et ce qui est dans le tiroir APRES (TODO 5, null = absent).
 * main() fait les memes appels sur une vraie HashMap et compare.
 *
 *   "put"               map.put("k", 5)
 *   "putIfAbsent"       map.putIfAbsent("k", 5)
 *   "getOrDefault"      map.getOrDefault("k", 0)
 *   "remove"            map.remove("k")
 *   "replace"           map.replace("k", 5)
 *   "computeIfAbsent"   map.computeIfAbsent("k", key -> 5)
 *   "computeIfPresent"  map.computeIfPresent("k", (key, v) -> v * 10)
 *   "merge"             map.merge("k", 5, Integer::sum)
 *   "mergeToNull"       map.merge("k", 5, (a, b) -> null)
 *
 * -- Essayons a la main --
 *
 *   put, old 3        -> rend 3 (l'ANCIENNE valeur), stocke 5
 *   putIfAbsent, old 3 -> rend 3, stocke 3 (rien ne change)
 *   replace, absent   -> rend null, stocke null (replace n'ajoute JAMAIS)
 *   merge, old 3      -> rend 8 (la NOUVELLE valeur), stocke 8
 *   mergeToNull, old 3 -> rend null, stocke null (tiroir supprime)
 *   mergeToNull, absent -> rend 5, stocke 5 (la fonction n'est meme pas appelee)
 *
 * -- Le plan --
 *
 *   1. Un switch sur method.
 *   2. Dans chaque cas, distinguer old == null et old != null.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Astuce a retenir : put, putIfAbsent, remove, replace rendent
 * l'ANCIENNE valeur ; compute..., merge rendent la NOUVELLE.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - counts.merge(word, 1, Integer::sum);
 *   - groups.computeIfAbsent(w.length(), k -> new ArrayList<>()).add(w);
 *   - stock.computeIfPresent(item, (k, v) -> v - quantity <= 0 ? null : v - quantity);
 *   - switch (method) { case "put": return old; ... }
 */
public class Exercise08_MapApiWorkout {

    public static Map<String, Integer> countWords(List<String> words) {
        throw new UnsupportedOperationException("TODO 1 : implementer countWords()");
    }

    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer groupByLength()");
    }

    public static Map<String, Integer> sell(Map<String, Integer> stock, String item, int quantity) {
        throw new UnsupportedOperationException("TODO 3 : implementer sell()");
    }

    public static Integer returned(String method, Integer old) {
        throw new UnsupportedOperationException("TODO 4 : implementer returned()");
    }

    public static Integer stored(String method, Integer old) {
        throw new UnsupportedOperationException("TODO 5 : implementer stored()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("countWords([b, a, b, c]) -> {a=1, b=2, c=1}",
                countWords(List.of("b", "a", "b", "c")).toString().equals("{a=1, b=2, c=1}"));
        ExerciseChecker.check("groupByLength([ab, c, de]) -> {1=[c], 2=[ab, de]}",
                groupByLength(List.of("ab", "c", "de")).toString().equals("{1=[c], 2=[ab, de]}"));
        ExerciseChecker.check("sell : 5 - 2 = 3 ; 5 - 5 -> supprime ; article inconnu -> rien",
                sell(new TreeMap<>(Map.of("pomme", 5)), "pomme", 2).equals(Map.of("pomme", 3))
                        && sell(new TreeMap<>(Map.of("pomme", 5)), "pomme", 5).isEmpty()
                        && sell(new TreeMap<>(Map.of("pomme", 5)), "kiwi", 1).equals(Map.of("pomme", 5)));

        List<String> methods = List.of("put", "putIfAbsent", "getOrDefault", "remove", "replace",
                "computeIfAbsent", "computeIfPresent", "merge", "mergeToNull");
        int agreeReturned = 0;
        int agreeStored = 0;
        for (String m : methods) {
            for (Integer old : new Integer[]{null, 3}) {
                Map<String, Integer> map = new HashMap<>();
                if (old != null) {
                    map.put("k", old);
                }
                Integer realReturn = call(map, m);
                if (Objects.equals(returned(m, old), realReturn)) {
                    agreeReturned++;
                }
                if (Objects.equals(stored(m, old), map.get("k"))) {
                    agreeStored++;
                }
            }
        }
        ExerciseChecker.check("returned == vraie HashMap sur 18 appels (" + agreeReturned + " d'accord)", agreeReturned == 18);
        ExerciseChecker.check("stored == vraie HashMap sur 18 appels (" + agreeStored + " d'accord)", agreeStored == 18);

        ExerciseChecker.summary();
    }

    // ---- Le juge : fait le vrai appel (ne pas modifier) ----

    static Integer call(Map<String, Integer> map, String method) {
        switch (method) {
            case "put":
                return map.put("k", 5);
            case "putIfAbsent":
                return map.putIfAbsent("k", 5);
            case "getOrDefault":
                return map.getOrDefault("k", 0);
            case "remove":
                return map.remove("k");
            case "replace":
                return map.replace("k", 5);
            case "computeIfAbsent":
                return map.computeIfAbsent("k", key -> 5);
            case "computeIfPresent":
                return map.computeIfPresent("k", (key, v) -> v * 10);
            case "merge":
                return map.merge("k", 5, Integer::sum);
            default:
                return map.merge("k", 5, (a, b) -> null);
        }
    }
}
