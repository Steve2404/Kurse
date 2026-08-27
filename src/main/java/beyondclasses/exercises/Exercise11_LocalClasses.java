package beyondclasses.exercises;

import beyondclasses.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * EXERCICE 11 - Classes locales : definies DANS une methode, et le piege du "effectively final" (niveau : difficile)
 * =========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une classe LOCALE est declaree A L'INTERIEUR d'une methode (ou d'un
 * bloc), un peu comme un mot que tu inventes juste pour UNE
 * conversation, et qui n'existe plus une fois la conversation finie.
 * Elle peut lire les variables locales de la methode qui l'entoure -
 * MAIS SEULEMENT celles qui sont final ou "effectively final" (jamais
 * reassignees apres leur premiere valeur). Pourquoi cette regle ?
 * Parce que la classe locale peut continuer a exister APRES que la
 * methode se soit terminee (par exemple, un Supplier qu'on renvoie et
 * qu'on appelle plus tard) - si la variable capturee pouvait encore
 * changer, on ne saurait plus QUELLE valeur elle devrait garder en
 * memoire pour toujours.
 *
 *
 * ==================================================================
 * TODO 1 : buildGreeting(prefix, name)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * buildGreeting("Bonjour", "Ada") doit rendre "Bonjour, Ada !".
 *
 * -- Le plan --
 *
 *   1. Declarer une classe locale Greeting, avec une methode render()
 *      qui renvoie prefix + ", " + name + " !" (prefix et name sont
 *      les 2 parametres de buildGreeting(), effectively final : ils
 *      ne sont jamais reassignes).
 *   2. Renvoyer new Greeting().render().
 *
 *
 * ==================================================================
 * TODO 2 : buildCounters(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut n "compteurs-souvenirs" independants : le 1er se souvient
 * toujours de 0, le 2e toujours de 1, etc. - meme APRES la fin de la
 * boucle qui les a crees. Le piege classique : dans "for (int i = 0;
 * i < n; i++)", la variable i EST REASSIGNEE a chaque tour (i++) -
 * elle n'est donc PAS effectively final, et une classe locale definie
 * DANS le corps de la boucle ne peut PAS l'utiliser directement. La
 * solution : creer, A CHAQUE tour, une NOUVELLE variable locale
 * (final int captured = i;) qui, elle, ne sera JAMAIS reassignee
 * ensuite - une "photo" figee de la valeur de i a CET instant precis.
 *
 * -- Le plan --
 *
 *   1. Creer une liste vide (List<Supplier<Integer>>).
 *   2. Pour i de 0 a n-1 : creer "final int captured = i;", PUIS
 *      declarer (ou reutiliser) une classe locale Counter
 *      implementant Supplier<Integer>, dont get() renvoie captured,
 *      PUIS ajouter "new Counter()" a la liste.
 *   3. Renvoyer la liste.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : les classes locales SONT elles-memes deja les "boites" du
 * plan - inutile d'en extraire encore une de plus.
 *
 * Exemple a verifier : buildCounters(3) rend une liste de 3
 * Supplier<Integer> dont les .get() valent respectivement 0, 1, 2 -
 * chacun se souvient de SA PROPRE valeur, meme appeles bien apres la
 * fin de la boucle.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Une classe locale ne peut PAS etre declaree avec un modificateur
 *     d'acces (ni public, ni private, ni protected) - "class Greeting"
 *     tout court, jamais "public class Greeting".
 *   - class Counter implements Supplier<Integer> { public Integer
 *     get() { return captured; } } - Counter peut etre declaree UNE
 *     FOIS avant la boucle (elle capture "captured", qui, LUI, est
 *     redeclare frais a CHAQUE tour de boucle).
 */
public class Exercise11_LocalClasses {

    public static String buildGreeting(String prefix, String name) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildGreeting()");
    }

    public static List<Supplier<Integer>> buildCounters(int n) {
        throw new UnsupportedOperationException("TODO 2 : implementer buildCounters()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("buildGreeting() capture prefix et name (effectively final)",
                buildGreeting("Bonjour", "Ada").equals("Bonjour, Ada !"));

        List<Supplier<Integer>> counters = buildCounters(3);
        ExerciseChecker.check("buildCounters(3) rend 3 Suppliers", counters.size() == 3);
        ExerciseChecker.check("chaque Supplier se souvient de SA PROPRE valeur capturee",
                counters.get(0).get() == 0 && counters.get(1).get() == 1 && counters.get(2).get() == 2);

        ExerciseChecker.summary();
    }
}
