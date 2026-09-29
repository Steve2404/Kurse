package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * EXERCICE 5 - Les sources de stream : iterate, generate, concat, takeWhile/dropWhile, et un stream ne sert qu'UNE fois (niveau : difficile)
 * =========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Stream fini, stream infini --
 *
 * List.stream() ou Stream.of(...) produisent un stream FINI. Mais
 * Stream.iterate(graine, suivant) et Stream.generate(fournisseur)
 * produisent un stream INFINI : un robinet qui ne se ferme jamais tout
 * seul. Ce n'est pas un probleme GRACE a la paresse (Exercise04) : tant
 * qu'une operation "coupe le robinet" (limit, takeWhile, findFirst,
 * anyMatch...), le pipeline se termine. Sans elle, il tourne pour
 * toujours.
 *
 *
 * ==================================================================
 * TODO 1 : powersOfTwo(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une legende raconte qu'on pose 1 grain de riz sur la 1re case d'un
 * echiquier, 2 sur la 2e, 4 sur la 3e... chaque case DOUBLE la
 * precedente. Stream.iterate, c'est exactement ca : une graine (1) et
 * une regle "comment passer d'une case a la suivante" (x2). La suite
 * est infinie : c'est a toi de dire combien de cases tu veux.
 *
 * -- Essayons a la main --
 *
 *   graine 1, regle x2 : 1, 2, 4, 8, 16, 32, ...
 *   n = 5 -> [1, 2, 4, 8, 16]
 *   n = 0 -> []
 *
 * -- Le plan --
 *
 *   1. Partir de la graine 1 (en long : les puissances de 2 grandissent
 *      tres vite).
 *   2. Regle : multiplier par 2.
 *   3. Ne garder que les n premiers.
 *   4. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un pipeline d'une ligne.
 *
 *
 * ==================================================================
 * TODO 2 : collatz(start)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Jeu de Collatz : si le nombre est pair, divise-le par 2 ; sinon,
 * multiplie par 3 et ajoute 1. On s'arrete quand on arrive a 1. On ne
 * sait PAS a l'avance combien d'etapes il faut, donc limit(n) ne sert
 * a rien. Il faut la version a 3 arguments de Stream.iterate (Java 9) :
 * graine, CONDITION pour continuer, regle - comme une boucle for.
 *
 * Piege : comme une boucle for (i = 6; i != 1; ...), la condition est
 * testee AVANT de laisser passer un element, donc le 1 final n'est
 * JAMAIS emis. Il faut le rajouter a la fin avec Stream.concat (qui
 * colle 2 streams bout a bout).
 *
 * -- Essayons a la main --
 *
 *   6 (pair) -> 3 (impair) -> 10 -> 5 -> 16 -> 8 -> 4 -> 2 -> 1 (stop)
 *   iterate(6, x != 1, ...) emet : 6, 3, 10, 5, 16, 8, 4, 2
 *   + concat avec [1]         -> [6, 3, 10, 5, 16, 8, 4, 2, 1]
 *
 *   collatz(1) : la condition 1 != 1 est fausse DES LA GRAINE ->
 *   iterate n'emet rien, concat ajoute 1 -> [1]
 *
 * -- Le plan --
 *
 *   1. Construire la suite : graine start, continuer tant que != 1,
 *      etape suivante selon pair/impair.
 *   2. Coller un stream contenant juste 1 a la fin.
 *   3. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Eventuellement pour "l'etape suivante" (pair -> /2, impair -> 3x+1) :
 * elle se raconte seule. Mais un operateur ternaire dans la lambda
 * suffit aussi. A toi de voir.
 *
 *
 * ==================================================================
 * TODO 3 : generateIds(prefix, count)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Stream.generate, c'est une machine a tickets : a chaque fois qu'on
 * tire, elle donne un ticket. Elle n'a PAS de graine, donc pour
 * numeroter les tickets, elle a besoin d'un compteur qui se souvient.
 * Piege : une lambda n'a PAS le droit de modifier une variable locale
 * int (elle doit etre "effectivement finale") - "compteur++" dans la
 * lambda ne compile pas. On utilise un AtomicInteger : la VARIABLE ne
 * change pas (c'est toujours le meme objet), c'est son CONTENU qui
 * change.
 *
 * -- Essayons a la main --
 *
 *   generateIds("ID-", 3) -> tirage 1 : "ID-1", tirage 2 : "ID-2",
 *                            tirage 3 : "ID-3" -> [ID-1, ID-2, ID-3]
 *
 * -- Le plan --
 *
 *   1. Creer un compteur (AtomicInteger) a 0.
 *   2. Machine a tickets : prefix + (compteur incremente puis lu).
 *   3. Ne tirer que count tickets.
 *   4. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : headerLines(lines) et bodyLines(lines)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un fichier commence par des lignes d'en-tete qui commencent par "#",
 * puis vient le contenu (qui peut lui-meme contenir un "#" plus loin).
 *
 *   takeWhile(condition) : "je prends TANT QUE c'est vrai, et au
 *     premier 'faux' je m'arrete DEFINITIVEMENT".
 *   dropWhile(condition) : "je jette TANT QUE c'est vrai, et au
 *     premier 'faux' je garde TOUT le reste, sans plus regarder".
 *   filter(condition)    : regarde CHAQUE element, un par un, sans
 *     jamais s'arreter.
 *
 * -- Essayons a la main --
 *
 *   lignes = ["#titre", "#auteur", "texte 1", "#pas un en-tete", "texte 2"]
 *   headerLines -> ["#titre", "#auteur"]           (s'arrete a "texte 1")
 *   bodyLines   -> ["texte 1", "#pas un en-tete", "texte 2"]
 *   (un filter(l -> !l.startsWith("#")) aurait perdu "#pas un en-tete")
 *
 * -- Le plan --
 *
 *   headerLines : prendre tant que la ligne commence par "#".
 *   bodyLines   : jeter tant que la ligne commence par "#".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : chacune tient en une ligne.
 *
 *
 * ==================================================================
 * TODO 5 : shortestAndLongest(source)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un stream, c'est un tube de dentifrice : une fois presse (operation
 * terminale), il est VIDE, on ne peut pas le repasser. Appeler une 2e
 * operation terminale sur le MEME stream lance
 * IllegalStateException: "stream has already been operated upon or
 * closed". Si on a besoin de parcourir 2 fois, on garde la RECETTE du
 * tube (un Supplier<Stream<String>>) et on en fabrique un NEUF a
 * chaque fois avec source.get().
 *
 * -- Essayons a la main --
 *
 *   source -> ["pomme", "kiwi", "pamplemousse"]
 *   plus court : "kiwi" ; plus long : "pamplemousse"
 *   -> "court=kiwi;long=pamplemousse"
 *   source vide -> "court=?;long=?"
 *
 * -- Le plan --
 *
 *   1. Fabriquer un 1er stream, y chercher le mot de longueur minimale
 *      ("?" si aucun).
 *   2. Fabriquer un 2e stream NEUF, y chercher le mot de longueur
 *      maximale ("?" si aucun).
 *   3. Assembler "court=" + min + ";long=" + max.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais le main() verifie que source.get() est appele exactement
 * 2 fois : un parcours par question.
 *
 *
 * Exemple a verifier :
 *
 *   powersOfTwo(5) == [1, 2, 4, 8, 16] ; powersOfTwo(0) == []
 *   collatz(6) == [6, 3, 10, 5, 16, 8, 4, 2, 1] ; collatz(1) == [1]
 *   collatz(7) == [7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1]
 *   generateIds("ID-", 3) == [ID-1, ID-2, ID-3] ; generateIds("X", 0) == []
 *   headerLines == [#titre, #auteur]
 *   bodyLines == [texte 1, #pas un en-tete, texte 2]
 *   shortestAndLongest(pomme, kiwi, pamplemousse) == "court=kiwi;long=pamplemousse",
 *     avec source.get() appele exactement 2 fois
 *   shortestAndLongest(vide) == "court=?;long=?"
 *   (et main() montre que reutiliser un stream deja consomme lance
 *   IllegalStateException)
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Stream.iterate(1L, x -> x * 2).limit(n).toList()
 *   - Stream.iterate(graine, x -> x != 1, x -> x % 2 == 0 ? x / 2 : 3 * x + 1)
 *   - Stream.concat(streamA, Stream.of(1))
 *   - AtomicInteger c = new AtomicInteger(); c.incrementAndGet() rend
 *     la NOUVELLE valeur (1 au premier appel).
 *   - Stream.generate(() -> ...).limit(count)
 *   - lines.stream().takeWhile(l -> l.startsWith("#")) / dropWhile(...)
 *   - source.get().min(Comparator.comparingInt(String::length)) rend un
 *     Optional<String> ; .orElse("?").
 */
public class Exercise05_StreamSourcesInfinite {

    public static List<Long> powersOfTwo(int n) {
        throw new UnsupportedOperationException("TODO 1 : implementer powersOfTwo()");
    }

    public static List<Integer> collatz(int start) {
        throw new UnsupportedOperationException("TODO 2 : implementer collatz()");
    }

    public static List<String> generateIds(String prefix, int count) {
        throw new UnsupportedOperationException("TODO 3 : implementer generateIds()");
    }

    public static List<String> headerLines(List<String> lines) {
        throw new UnsupportedOperationException("TODO 4 : implementer headerLines()");
    }

    public static List<String> bodyLines(List<String> lines) {
        throw new UnsupportedOperationException("TODO 4 : implementer bodyLines()");
    }

    public static String shortestAndLongest(Supplier<Stream<String>> source) {
        throw new UnsupportedOperationException("TODO 5 : implementer shortestAndLongest()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("powersOfTwo(5) == [1, 2, 4, 8, 16]", powersOfTwo(5).equals(List.of(1L, 2L, 4L, 8L, 16L)));
        ExerciseChecker.check("powersOfTwo(0) == []", powersOfTwo(0).isEmpty());

        ExerciseChecker.check("collatz(6) == [6, 3, 10, 5, 16, 8, 4, 2, 1]",
                collatz(6).equals(List.of(6, 3, 10, 5, 16, 8, 4, 2, 1)));
        ExerciseChecker.check("collatz(1) == [1]", collatz(1).equals(List.of(1)));
        ExerciseChecker.check("collatz(7) a 17 elements et finit par 1",
                collatz(7).equals(List.of(7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1)));

        ExerciseChecker.check("generateIds(\"ID-\", 3) == [ID-1, ID-2, ID-3]",
                generateIds("ID-", 3).equals(List.of("ID-1", "ID-2", "ID-3")));
        ExerciseChecker.check("generateIds(\"X\", 0) == []", generateIds("X", 0).isEmpty());

        List<String> lines = List.of("#titre", "#auteur", "texte 1", "#pas un en-tete", "texte 2");
        ExerciseChecker.check("headerLines == [#titre, #auteur]",
                headerLines(lines).equals(List.of("#titre", "#auteur")));
        ExerciseChecker.check("bodyLines == [texte 1, #pas un en-tete, texte 2]",
                bodyLines(lines).equals(List.of("texte 1", "#pas un en-tete", "texte 2")));

        int[] calls = {0};
        Supplier<Stream<String>> fruits = () -> {
            calls[0]++;
            return Stream.of("pomme", "kiwi", "pamplemousse");
        };
        ExerciseChecker.check("shortestAndLongest == court=kiwi;long=pamplemousse",
                shortestAndLongest(fruits).equals("court=kiwi;long=pamplemousse"));
        ExerciseChecker.check("source.get() appele exactement 2 fois", calls[0] == 2);
        ExerciseChecker.check("shortestAndLongest(vide) == court=?;long=?",
                shortestAndLongest(Stream::empty).equals("court=?;long=?"));

        Stream<String> once = Stream.of("a", "b");
        once.count();
        boolean threw = false;
        try {
            once.count();
        } catch (IllegalStateException e) {
            threw = e.getMessage().equals("stream has already been operated upon or closed");
        }
        ExerciseChecker.check("(demo) reutiliser un stream consomme lance IllegalStateException", threw);

        ExerciseChecker.summary();
    }
}
