package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * EXERCICE 3 - Cache LRU generique avec LinkedHashMap (niveau : difficile)
 * ==========================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java. Les 3 questions : ca se raconte
 * seul (Q1) ? ca revient plusieurs fois (Q2) ? ca cache sa propre
 * recette (Q3) ?
 *
 *
 * ==================================================================
 * TODO : completer LRUCache<K,V>
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une petite etagere qui n'a de la place que pour 3 jouets.
 * Regle du jeu : chaque fois que tu JOUES avec un jouet (meme s'il
 * etait deja sur l'etagere), tu le reposes tout a droite, comme pour
 * dire "celui-la, je l'ai touche en dernier". Quand l'etagere est
 * pleine et que tu veux poser un NOUVEAU jouet, tu dois d'abord
 * enlever celui qui est tout a gauche - celui que tu n'as pas touche
 * depuis le plus longtemps (pas forcement le premier arrive, celui
 * qu'on a le plus "oublie").
 *
 * C'est exactement ca, un cache LRU (Least Recently Used = "utilise
 * le moins recemment") : une etagere a capacite limitee qui jette
 * automatiquement le jouet le plus oublie pour faire de la place.
 *
 * -- Essayons a la main --
 *
 * Etagere vide, capacite 3. Tu poses 1, puis 2, puis 3 : etagere =
 * [1, 2, 3] (de gauche/oublie a droite/recent).
 *
 * Tu joues avec le jouet 1 : il repart tout a droite. Etagere =
 * [2, 3, 1].
 *
 * Tu poses un nouveau jouet, 4 : l'etagere est pleine, il faut jeter
 * celui tout a gauche (2, le plus oublie). Etagere = [3, 1, 4].
 *
 * -- Ce qu'on remarque --
 *
 * On a besoin de DEUX comportements en meme temps : 1) se souvenir de
 * l'ORDRE dans lequel les jouets ont ete touches (pas juste lesquels
 * on a), et 2) jeter automatiquement le plus ancien quand ca deborde.
 * Ecrire ça a la main avec une simple Map serait tres penible (il
 * faudrait gerer l'ordre soi-meme). Bonne nouvelle : LinkedHashMap
 * sait deja faire le point 1) tout seul, si on le lui demande
 * gentiment (un petit interrupteur special a activer). Il ne reste
 * plus qu'a lui dire QUAND jeter (point 2).
 *
 * -- Le plan --
 *
 *   1. Demander a LinkedHashMap de se souvenir de l'ordre "dernier
 *      touche" plutot que l'ordre "premier arrive" (c'est un reglage
 *      a activer a la construction, pas quelque chose a coder
 *      toi-meme).
 *   2. Dire a LinkedHashMap la regle "jette le plus ancien si on
 *      depasse la capacite" - LinkedHashMap te laisse la place pour
 *      ecrire cette regle toi-meme (une petite methode a completer,
 *      appelee automatiquement apres chaque ajout).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Ici, exceptionnellement, ce n'est pas TOI qui decoupes en boites :
 * LinkedHashMap a DEJA ete decoupee pour toi par les auteurs du JDK.
 * Ton travail se limite a "brancher" les 2 reglages au bon endroit
 * (le constructeur, et une methode que LinkedHashMap appelle toute
 * seule au bon moment). C'est un exemple important : parfois, la
 * bonne boite magique existe deja, et le vrai travail d'analyse est
 * de la RECONNAITRE plutot que d'en fabriquer une nouvelle.
 *
 * Exemple a verifier : capacite 3, on pose 1,2,3, on relit 1
 * (get(1)), on pose 4 -> la cle 2 doit disparaitre, l'ordre final
 * doit etre [3, 1, 4].
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - LinkedHashMap a une surcharge de constructeur a 3 parametres :
 *     LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
 *     Passe accessOrder=true pour activer l'ordre "dernier touche".
 *   - Surcharge removeEldestEntry(Map.Entry<K,V> eldest) : elle est
 *     appelee automatiquement APRES chaque put(). Retourner true dit
 *     a la map de supprimer l'entree la plus ancienne. Le test se
 *     fait avec size() > capacity.
 *   - get(key) n'a besoin d'AUCUNE logique manuelle pour deplacer
 *     l'entree : accessOrder=true s'en occupe tout seul des que tu
 *     appelles super.get(key) (qui existe deja, tu n'as rien a
 *     ecrire pour get() lui-meme).
 */
public class Exercise03_LRUCache {

    static class LRUCache<K, V> extends LinkedHashMap<K, V> {

        private final int capacity;

        LRUCache(int capacity) {
            throw new UnsupportedOperationException("TODO : appeler super(...) avec accessOrder=true");
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            throw new UnsupportedOperationException("TODO : implementer removeEldestEntry()");
        }
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        ExerciseChecker.check("Cache plein contient 1,2,3", cache.keySet().toString().equals("[1, 2, 3]"));

        cache.get(1); // 1 redevient le plus recemment utilise
        cache.put(4, "d"); // doit evincer 2 (le moins recemment utilise), pas 1

        ExerciseChecker.check("Apres get(1) puis put(4,'d'), la cle 2 a ete evincee", !cache.containsKey(2));
        ExerciseChecker.check("La cle 1 est toujours presente (consultee juste avant)", cache.containsKey(1));
        ExerciseChecker.check("La taille du cache reste bornee a 3", cache.size() == 3);
        ExerciseChecker.check("L'ordre d'iteration reflete l'usage : [3, 1, 4]",
                cache.keySet().toString().equals("[3, 1, 4]"));

        ExerciseChecker.summary();
    }
}