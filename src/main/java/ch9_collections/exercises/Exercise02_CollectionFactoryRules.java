package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * EXERCICE 2 - Qui a le droit de changer ? Les 5 facons de creer une List, ta regle comparee a la JVM (niveau : difficile)
 * ========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Il existe 5 facons courantes d'obtenir une List a partir de "a", "b", "c" :
 *
 *   "ArrayList"         new ArrayList<>(source)               une vraie liste a toi, tout est permis
 *   "Arrays.asList"     Arrays.asList(tableau)                une liste COLLEE au tableau : taille fixe
 *   "List.of"           List.of("a", "b", "c")                immuable, refuse null (meme pour chercher !)
 *   "List.copyOf"       List.copyOf(source)                   immuable, copie, refuse null
 *   "unmodifiableList"  Collections.unmodifiableList(source)  une VITRINE sur source : lecture seule
 *
 * Trois questions d'examen tombent sans cesse :
 *   - cette operation leve-t-elle une exception (et laquelle) ?
 *   - que se passe-t-il avec null ?
 *   - si je change la source APRES, est-ce que la liste le voit ?
 *
 * Ici, le juge n'est pas javac (tout compile !) mais la JVM : main()
 * execute VRAIMENT chaque operation et compare avec ta regle.
 *
 *
 * ==================================================================
 * TODO 1 : modifyBehavior(factory, operation)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * operation vaut "set", "add", "remove", "sort" ou "clear". Rendre "OK"
 * si l'operation marche, sinon "UnsupportedOperationException".
 *
 * -- Essayons a la main --
 *
 *   ("ArrayList", "add")      -> OK
 *   ("Arrays.asList", "set")  -> OK   (on remplace une case : la taille ne change pas)
 *   ("Arrays.asList", "add")  -> UnsupportedOperationException (la taille changerait)
 *   ("List.of", "sort")       -> UnsupportedOperationException
 *
 * -- Le plan --
 *
 *   1. "ArrayList" : tout est OK.
 *   2. "Arrays.asList" : OK pour ce qui garde la taille (set, sort), sinon exception.
 *   3. Les trois autres : toujours l'exception.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : nullBehavior(factory, operation)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * operation vaut "createWithNull" (creer la liste avec un null dedans)
 * ou "containsNull" (demander list.contains(null) sur une liste sans
 * null). Rendre "OK" ou "NullPointerException".
 *
 * -- Essayons a la main --
 *
 *   ("Arrays.asList", "createWithNull") -> OK
 *   ("List.of", "containsNull")         -> NullPointerException (piege : meme une simple question !)
 *   ("unmodifiableList", "containsNull") -> OK
 *
 * -- Le plan --
 *
 *   1. Les deux immuables modernes (List.of, List.copyOf) : NullPointerException.
 *   2. Les autres : OK.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : seesLaterChanges(factory)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cree la liste a partir d'une source (un tableau pour Arrays.asList,
 * une ArrayList pour les autres), PUIS on change la source. La liste
 * voit-elle le changement ? Une COPIE est une photo : elle ne bouge
 * plus. Une VUE est une fenetre : elle montre toujours la source.
 *
 * -- Essayons a la main --
 *
 *   "List.copyOf"      -> false (photo)
 *   "unmodifiableList" -> true  (fenetre : lecture seule, mais pas figee !)
 *
 * -- Le plan --
 *
 *   1. Les vues : Arrays.asList et unmodifiableList -> true.
 *   2. Les copies : false.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : freeze(list)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut une liste en lecture seule, qui ne bouge plus quand l'original
 * change, ET qui accepte null. Aucune des 5 facons seule ne fait les
 * trois : List.copyOf refuse null, unmodifiableList est une fenetre.
 *
 * -- Essayons a la main --
 *
 *   freeze([a, null]) -> [a, null] ; l'original recoit "z" -> le gel ne change pas ; gel.add -> exception
 *
 * -- Le plan --
 *
 *   1. Faire une photo privee (une nouvelle ArrayList).
 *   2. Mettre une vitrine devant cette photo.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : les deux etapes existent deja dans le JDK.
 *
 * Exemple a verifier : voir les tests de main() (40 comparaisons avec la JVM).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - switch sur un String avec "case "a", "b" ->" (Java 14+).
 *   - Collections.unmodifiableList(new ArrayList<>(list)).
 */
public class Exercise02_CollectionFactoryRules {

    static final List<String> FACTORIES = List.of("ArrayList", "Arrays.asList", "List.of", "List.copyOf", "unmodifiableList");

    public static String modifyBehavior(String factory, String operation) {
        throw new UnsupportedOperationException("TODO 1 : implementer modifyBehavior()");
    }

    public static String nullBehavior(String factory, String operation) {
        throw new UnsupportedOperationException("TODO 2 : implementer nullBehavior()");
    }

    public static boolean seesLaterChanges(String factory) {
        throw new UnsupportedOperationException("TODO 3 : implementer seesLaterChanges()");
    }

    public static List<String> freeze(List<String> list) {
        throw new UnsupportedOperationException("TODO 4 : implementer freeze()");
    }

    public static void main(String[] args) {
        int agree = 0;
        int total = 0;
        for (String factory : FACTORIES) {
            for (String op : List.of("set", "add", "remove", "sort", "clear")) {
                total++;
                if (modifyBehavior(factory, op).equals(jvm(factory, op))) {
                    agree++;
                }
            }
        }
        ExerciseChecker.check("modifyBehavior == JVM sur " + total + " cas (" + agree + " d'accord)", agree == total);

        agree = 0;
        total = 0;
        for (String factory : FACTORIES) {
            for (String op : List.of("createWithNull", "containsNull")) {
                total++;
                if (nullBehavior(factory, op).equals(jvm(factory, op))) {
                    agree++;
                }
            }
        }
        ExerciseChecker.check("nullBehavior == JVM sur " + total + " cas (" + agree + " d'accord)", agree == total);

        agree = 0;
        for (String factory : FACTORIES) {
            if (seesLaterChanges(factory) == jvmSeesChanges(factory)) {
                agree++;
            }
        }
        ExerciseChecker.check("seesLaterChanges == JVM sur 5 fabriques (" + agree + " d'accord)", agree == 5);

        List<String> original = new ArrayList<>(Arrays.asList("a", null));
        List<String> frozen = freeze(original);
        original.add("z");
        ExerciseChecker.check("freeze([a, null]) garde null et ne voit pas l'ajout de z", frozen.equals(Arrays.asList("a", null)));
        ExerciseChecker.check("freeze(...) est en lecture seule", "UnsupportedOperationException".equals(attempt(() -> frozen.add("b"))));

        ExerciseChecker.summary();
    }

    // ---- Le juge : execute vraiment l'operation (ne pas modifier) ----

    static List<String> create(String factory, String[] array, List<String> source) {
        switch (factory) {
            case "ArrayList":
                return new ArrayList<>(source);
            case "Arrays.asList":
                return Arrays.asList(array);
            case "List.of":
                return List.of(array);
            case "List.copyOf":
                return List.copyOf(source);
            default:
                return Collections.unmodifiableList(source);
        }
    }

    static String jvm(String factory, String op) {
        if (op.equals("createWithNull")) {
            return attempt(() -> create(factory, new String[]{"a", null}, new ArrayList<>(Arrays.asList("a", null))));
        }
        List<String> list = create(factory, new String[]{"c", "a", "b"}, new ArrayList<>(List.of("c", "a", "b")));
        switch (op) {
            case "set":
                return attempt(() -> list.set(0, "x"));
            case "add":
                return attempt(() -> list.add("x"));
            case "remove":
                return attempt(() -> list.remove(0));
            case "sort":
                return attempt(() -> list.sort(null));
            case "clear":
                return attempt(list::clear);
            default:
                return attempt(() -> list.contains(null));
        }
    }

    static boolean jvmSeesChanges(String factory) {
        String[] array = {"a", "b"};
        List<String> source = new ArrayList<>(List.of("a", "b"));
        List<String> list = create(factory, array, source);
        array[0] = "z";
        source.set(0, "z");
        return list.get(0).equals("z");
    }

    static String attempt(Runnable action) {
        try {
            action.run();
            return "OK";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
