package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EXERCICE 8 - Generiques et wildcards, principe PECS (niveau : difficile)
 * ============================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- PECS en une phrase, avant les enfants et les jouets --
 *
 * PECS = "Producer Extends, Consumer Super" : si une structure PRODUIT
 * des valeurs que vous LISEZ, utilisez <? extends T>. Si elle CONSOMME
 * des valeurs que vous lui DONNEZ, utilisez <? super T>. Tout cet
 * exercice n'est qu'une longue mise en pratique de cette seule phrase.
 *
 *
 * ==================================================================
 * TODO 1 : Box<T>.copyContentTo(Box<? super T> destination)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une boite qui ne contient QUE des petites voitures rouges
 * (une Box<VoitureRouge>). Tu veux vider son contenu dans une autre
 * boite. Dans quelles boites as-tu le droit de verser tes voitures
 * rouges ? Dans une boite "voitures rouges" evidemment, mais aussi
 * dans une boite plus large "voitures" (n'importe quelle couleur), ou
 * meme dans une boite encore plus large "jouets" - parce qu'une
 * voiture rouge EST une voiture, et EST un jouet. Par contre, tu ne
 * peux PAS verser tes voitures rouges dans une boite qui n'accepte que
 * des "voitures bleues" : ca n'aurait aucun sens.
 *
 * -- Essayons a la main --
 *
 * Box<Integer> avec 42 dedans (comme la boite de voitures rouges).
 * Destination : Box<Number> (comme la boite "voitures", plus large).
 * On verse le contenu : la Box<Number> contient maintenant 42.
 * Essaie maintenant d'imaginer l'inverse (verser une Box<Number> dans
 * une Box<Integer>) : est-ce que ca marche toujours ? Non - un Number
 * quelconque n'est pas forcement un Integer (ca pourrait etre un
 * Double), exactement comme un "jouet" quelconque n'est pas forcement
 * une "voiture rouge".
 *
 * -- Ce qu'on remarque --
 *
 * La boite destination n'a pas besoin d'etre EXACTEMENT une Box<T> :
 * elle a juste besoin d'etre capable d'ACCUEILLIR un T (ou n'importe
 * quoi de plus general que T). C'est exactement ce que dit <? super T>
 * : "une Box de T, ou d'un parent de T". Destination CONSOMME ce
 * qu'on lui donne -> c'est le "Consumer Super" du PECS.
 *
 * -- Le plan --
 *
 *   1. Prendre le contenu actuel de cette boite (son get()).
 *   2. Le donner a la boite destination (son set(...)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : deux appels, une seule ligne possible. Ca va directement dans
 * copyContentTo().
 *
 *
 * ==================================================================
 * TODO 2 : copy(src, dest)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Meme idee que TODO 1, mais avec deux CAISSES entieres de jouets au
 * lieu d'une seule boite. Tu prends chaque jouet de la caisse source,
 * un par un, et tu le poses dans la caisse destination, jusqu'a ce que
 * la caisse source soit vide.
 *
 * -- Essayons a la main --
 *
 * src = [1, 2, 3] (une caisse d'Integer). dest = une caisse vide de
 * Number. Tu prends 1, tu le poses dans dest. Tu prends 2, tu le poses
 * dans dest. Tu prends 3, tu le poses dans dest. dest = [1, 2, 3].
 *
 * -- Ce qu'on remarque --
 *
 * src ne fait que PRODUIRE des jouets qu'on regarde et qu'on lit ->
 * <? extends T> (le "Producer Extends"). dest ne fait que CONSOMMER
 * les jouets qu'on lui donne -> <? super T> (le "Consumer Super").
 * On ne lit JAMAIS dans dest, et on n'ecrit JAMAIS dans src : chacune
 * des deux caisses ne joue qu'un seul role a la fois.
 *
 * -- Le plan --
 *
 *   1. Parcourir src, un jouet a la fois.
 *   2. Ajouter chaque jouet a dest.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une simple boucle de 2 lignes, directement dans copy().
 *
 *
 * ==================================================================
 * TODO 3 : sumNumbers(list)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une balance de cuisine. Tu y poses, l'un apres l'autre, des
 * fruits de types differents : une pomme, une poire, un raisin. Peu
 * importe le type exact du fruit, la balance sait toujours lire "son
 * poids" et l'ajouter au total affiche. A la fin, tu obtiens le poids
 * total de tous les fruits, quel que soit le melange de types que tu
 * as pese.
 *
 * -- Essayons a la main --
 *
 * list = [1, 2, 3] (des Integer, vus comme des "fruits qui pesent 1,
 * 2 et 3"). Total = 0. On pese 1 -> total = 1. On pese 2 -> total = 3.
 * On pese 3 -> total = 6. Resultat : 6.0.
 *
 * -- Ce qu'on remarque --
 *
 * La methode ne modifie JAMAIS la liste recue, elle ne fait que la
 * LIRE -> c'est encore et toujours le "Producer Extends" du PECS :
 * List<? extends Number>. Ca doit marcher pour une List<Integer>, une
 * List<Double>, une List<Number>... tant que chaque element sait se
 * transformer en nombre a virgule (double).
 *
 * -- Le plan --
 *
 *   1. Preparer un total a zero.
 *   2. Pour chaque element de la liste (vu comme un Number), ajouter
 *      sa valeur au total.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une boucle de 2 lignes, directement dans sumNumbers().
 *
 *
 * ==================================================================
 * TODO 4 : compileQuiz() - question d'examen, pas de code a ecrire
 * ==================================================================
 *
 * Pour chacune des lignes decrites dans les indices ci-dessous, essaie
 * d'abord de repondre TOI-MEME, a voix haute ou sur une feuille :
 * "ca compile, ou pas, et pourquoi ?" - en te servant uniquement de la
 * phrase PECS du debut de ce fichier. Ne regarde les reponses
 * qu'ensuite, pour verifier ton raisonnement.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 * Indice TODO 1 :
 *   - void copyContentTo(Box<? super T> destination) { destination.set(this.content); }
 *
 * Indice TODO 2 :
 *   - for (T item : src) { dest.add(item); }
 *   - Le type parametre <T> de la METHODE (pas de la classe) doit etre
 *     declare avant le type de retour : public static <T> void copy(...)
 *
 * Indice TODO 3 :
 *   - double total = 0; for (Number n : list) { total += n.doubleValue(); }
 *   - Notez que list.add(...) serait INTERDIT ici (liste en lecture
 *     seule conceptuellement avec "? extends"), c'est le "producer" du PECS.
 *
 * Indice TODO 4 (reponses) :
 *   a) List<? extends Number> l = new ArrayList<Integer>(); l.add(5);
 *      -> NE COMPILE PAS. Avec un wildcard "extends", le compilateur ne
 *         sait pas si 'l' est vraiment une List<Integer> ou une
 *         List<Double> etc, donc il interdit tout add() (sauf null).
 *   b) List<? super Integer> l = new ArrayList<Number>(); l.add(5);
 *      -> COMPILE. Avec "super", on sait qu'on peut au moins y mettre
 *         un Integer (ou tout ce qui EST un Integer), c'est autorise.
 *   c) List<?> l = new ArrayList<String>(); l.add("x");
 *      -> NE COMPILE PAS (meme raison que (a), <?> est encore plus
 *         restrictif : aucun add() sauf null autorise).
 */
public class Exercise08_GenericsPecs {

    static class Box<T> {
        private T content;

        void set(T content) {
            this.content = content;
        }

        T get() {
            return content;
        }

        void copyContentTo(Box<? super T> destination) {
            throw new UnsupportedOperationException("TODO 1 : implementer copyContentTo()");
        }
    }

    public static <T> void copy(List<? extends T> src, List<? super T> dest) {
        throw new UnsupportedOperationException("TODO 2 : implementer copy()");
    }

    public static double sumNumbers(List<? extends Number> list) {
        throw new UnsupportedOperationException("TODO 3 : implementer sumNumbers()");
    }

    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>();
        intBox.set(42);
        Box<Number> numberBox = new Box<>();
        intBox.copyContentTo(numberBox);
        ExerciseChecker.check("copyContentTo copie 42 d'une Box<Integer> vers une Box<Number>",
                numberBox.get().equals(42));

        List<Integer> src = Arrays.asList(1, 2, 3);
        List<Number> dest = new ArrayList<>();
        copy(src, dest);
        ExerciseChecker.check("copy(List<Integer>, List<Number>) transfere bien les elements",
                dest.equals(Arrays.asList(1, 2, 3)));

        ExerciseChecker.check("sumNumbers(List<Integer>) == 6.0", sumNumbers(Arrays.asList(1, 2, 3)) == 6.0);
        ExerciseChecker.check("sumNumbers(List<Double>) == 4.5", sumNumbers(Arrays.asList(1.5, 3.0)) == 4.5);

        ExerciseChecker.summary();
    }
}