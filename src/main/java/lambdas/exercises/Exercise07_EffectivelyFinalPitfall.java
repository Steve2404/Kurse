package lambdas.exercises;

import lambdas.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * EXERCICE 7 - Le piege de la variable "effectivement finale" dans une boucle (niveau : difficile)
 * ========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- La regle a connaitre --
 *
 * Un lambda ne peut utiliser une variable locale (ou un parametre de
 * methode) que si elle est FINAL ou EFFECTIVEMENT FINALE - c'est-a-
 * dire que le code compilerait encore si on ecrivait "final" devant
 * elle. En clair : une fois qu'on lui a donne sa valeur, elle ne doit
 * plus JAMAIS etre reassignee ensuite. Une variable de boucle
 * classique "for (int i = 0; ...; i++)" est reassignee a CHAQUE tour
 * (i++) : elle n'est PAS effectivement finale, et un lambda ne peut
 * PAS l'utiliser directement.
 *
 *
 * ==================================================================
 * TODO : buildDeferredReports(tasks)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une maitresse qui prepare, pour CHAQUE eleve de la classe,
 * un petit mot cachete a n'ouvrir que plus tard ("Supplier<String>" :
 * une recette qui rendra un texte quand on l'appellera, pas
 * maintenant). Chaque mot doit dire quelque chose comme "Tache #3 :
 * Bob". Le piege : si la maitresse ecrit CETTE PHRASE en utilisant
 * directement le numero de la boucle qui tourne pendant qu'elle
 * prepare tous les mots, et que ce numero continue de changer apres
 * coup, tous les mots risqueraient de dire la MEME chose au moment ou
 * on les ouvrira (le dernier numero atteint), au lieu du numero
 * propre a chacun. En Java, ce piege ne peut MEME PAS compiler avec
 * une boucle for classique - c'est le compilateur qui te protege de
 * l'erreur AVANT qu'elle ne se produise en silence.
 *
 * -- Essayons a la main (avec du pseudo-code d'abord) --
 *
 * for (int i = 0; i < tasks.size(); i++) {
 *     deferred.add(() -> "Tache #" + i + " : " + tasks.get(i));
 * }
 *
 * Ce code, tel quel, NE COMPILE PAS : 'i' change de valeur a chaque
 * tour de boucle (i++), elle n'est pas effectivement finale, le
 * lambda n'a pas le droit d'y toucher.
 *
 * La solution : a l'INTERIEUR de chaque tour de boucle, fabriquer une
 * COPIE locale de 'i', qui elle, ne sera JAMAIS reassignee (chaque
 * tour de boucle en fabrique une TOUTE NOUVELLE, jamais reutilisee) :
 *
 * for (int i = 0; i < tasks.size(); i++) {
 *     int index = i;   // copie fraiche, jamais reassignee ensuite
 *     deferred.add(() -> "Tache #" + index + " : " + tasks.get(index));
 * }
 *
 * -- Ce qu'on remarque --
 *
 * 'index' est une variable DIFFERENTE a CHAQUE tour de boucle (elle
 * est re-declaree a chaque passage dans les accolades). Chaque lambda
 * cree dans ce tour-la capture SA PROPRE copie de 'index', figee pour
 * toujours a la valeur qu'elle avait CE tour-la. C'est exactement
 * comme donner a chaque eleve un mot cachete avec SON PROPRE numero
 * ecrit dessus a l'encre indelebile, plutot que de tous les faire
 * pointer vers un tableau d'affichage qui continue de changer.
 *
 * -- Le plan --
 *
 *   1. Preparer une liste vide de Supplier<String> (les mots
 *      cachetes, pas encore ouverts).
 *   2. Parcourir les taches avec une boucle for CLASSIQUE (index
 *      numerique, pas de for-each - c'est le piege qu'on veut
 *      pratiquer).
 *   3. A CHAQUE tour, fabriquer une copie locale de l'indice courant.
 *   4. Ajouter a la liste un nouveau Supplier<String> qui, quand on
 *      l'appellera PLUS TARD, utilise cette copie locale (jamais
 *      l'indice de boucle original) pour construire le texte "Tache
 *      #N : nom".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : c'est une seule boucle, avec une seule ligne de "copie
 * defensive" ajoutee dedans. Le decouper en plusieurs methodes
 * casserait le lien visuel entre "pourquoi on copie" et "ou on
 * l'utilise".
 *
 * Exemple a verifier : tasks = ["Ranger", "Balayer", "Arroser"] ->
 * les 3 Supplier, une fois APPELES (get()), rendent respectivement
 * "Tache #0 : Ranger", "Tache #1 : Balayer", "Tache #2 : Arroser" -
 * CHACUN avec son propre numero, meme si on les appelle dans le
 * desordre, ou longtemps apres la fin de la boucle.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - for (int i = 0; i < tasks.size(); i++) {
 *         int index = i;
 *         deferred.add(() -> "Tache #" + index + " : " + tasks.get(index));
 *     }
 *   - Verifie par toi-meme (essaie de le faire compiler) : que se
 *     passe-t-il si tu supprimes la ligne "int index = i;" et que tu
 *     utilises directement 'i' dans le lambda ? Lis attentivement le
 *     message d'erreur du compilateur, il nomme precisement la regle
 *     violee.
 */
public class Exercise07_EffectivelyFinalPitfall {

    public static List<Supplier<String>> buildDeferredReports(List<String> tasks) {
        throw new UnsupportedOperationException("TODO : implementer buildDeferredReports()");
    }

    public static void main(String[] args) {
        List<String> tasks = Arrays.asList("Ranger", "Balayer", "Arroser");
        List<Supplier<String>> deferred = buildDeferredReports(tasks);

        ExerciseChecker.check("3 rapports differes prepares", deferred.size() == 3);

        List<String> executedInOrder = new ArrayList<>();
        for (Supplier<String> report : deferred) {
            executedInOrder.add(report.get());
        }
        ExerciseChecker.check("executes dans l'ordre -> chacun garde SON propre numero",
                executedInOrder.equals(Arrays.asList("Tache #0 : Ranger", "Tache #1 : Balayer", "Tache #2 : Arroser")));

        String lastFirst = deferred.get(2).get();
        String firstAfter = deferred.get(0).get();
        ExerciseChecker.check("executes dans le DESORDRE -> toujours le bon numero pour chacun",
                lastFirst.equals("Tache #2 : Arroser") && firstAfter.equals("Tache #0 : Ranger"));

        ExerciseChecker.summary();
    }
}
