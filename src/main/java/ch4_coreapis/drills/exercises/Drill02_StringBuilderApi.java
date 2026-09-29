package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * DRILL 02 - L'API StringBuilder : une methode par TODO
 * =====================================================
 *
 * Mode d'emploi : voir Drill01_StringApi. Chaque TODO part d'un
 * StringBuilder donne en parametre (ou d'un texte) et rend le resultat
 * en String.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : greet(name)          [append chaine] "Bonjour, " + name + " !" -> "Bonjour, Ada !".
 * TODO 2  : prefix(sb)           [insert(0, ...)] ajoute "> " devant -> "> journal".
 * TODO 3  : cut(sb)              [delete(debut, fin)] "journal de bord" : enleve "journal " -> "de bord".
 * TODO 4  : dropFirst(sb)        [deleteCharAt] enleve le 1er caractere -> "ournal".
 * TODO 5  : swapWord(sb)         [replace(debut, fin, texte)] "pigeon dirty" : index 3 a 6 par "sty" -> "pigsty dirty".
 * TODO 6  : mirror(text)         [reverse] "stressed" -> "desserts".
 * TODO 7  : capitalize(sb)       [setCharAt] 1re lettre en majuscule -> "Journal".
 * TODO 8  : keep(sb, n)          [setLength] garde n caracteres -> "jour".
 * TODO 9  : findSecond(sb)       [indexOf(texte, depart)] 2e "an" dans "banana" (depuis 2) -> 3.
 * TODO 10 : middle(sb)           [substring, SANS modifier sb] index 1 a 3 de "hello" -> "el".
 * TODO 11 : sameContent(a, b)    [contentEquals ou toString().equals] ("ab", sb "ab") -> true.
 *                                  (sb1.equals(sb2) compare les ADRESSES : false !)
 * TODO 12 : chain()              [chainage] new StringBuilder("0123456789") puis delete(2, 4),
 *                                  insert(2, "X"), reverse(), deleteCharAt(0) -> "87654X10".
 * TODO 13 : mixed()              [append de plusieurs types] 1, 'c', true, 2.5 -> "1ctrue2.5".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   append(x) insert(i, x) delete(d, f) deleteCharAt(i) replace(d, f, s) reverse()
 *     -> modifient sb ET rendent le MEME sb (chainage possible)
 *   setCharAt(i, c) setLength(n) -> void
 *   charAt(i) length() indexOf(s) indexOf(s, d) lastIndexOf(s) substring(d, f) toString()
 *     -> lisent sans modifier
 *   delete(1, 100) sur "abc" : accepte (fin trop grande = fin du texte)
 *   insert(5, "x") sur "abc" : StringIndexOutOfBoundsException
 *   StringBuilder n'a pas equals() par contenu : utiliser toString().equals ou contentEquals
 * ---------------------------------------------------------------------
 */
public class Drill02_StringBuilderApi {

    public static String greet(String name) {
        throw new UnsupportedOperationException("TODO 1 : implementer greet()");
    }

    public static String prefix(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 2 : implementer prefix()");
    }

    public static String cut(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 3 : implementer cut()");
    }

    public static String dropFirst(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 4 : implementer dropFirst()");
    }

    public static String swapWord(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 5 : implementer swapWord()");
    }

    public static String mirror(String text) {
        throw new UnsupportedOperationException("TODO 6 : implementer mirror()");
    }

    public static String capitalize(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 7 : implementer capitalize()");
    }

    public static String keep(StringBuilder sb, int n) {
        throw new UnsupportedOperationException("TODO 8 : implementer keep()");
    }

    public static int findSecond(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 9 : implementer findSecond()");
    }

    public static String middle(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 10 : implementer middle()");
    }

    public static boolean sameContent(String a, StringBuilder b) {
        throw new UnsupportedOperationException("TODO 11 : implementer sameContent()");
    }

    public static String chain() {
        throw new UnsupportedOperationException("TODO 12 : implementer chain()");
    }

    public static String mixed() {
        throw new UnsupportedOperationException("TODO 13 : implementer mixed()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  greet(\"Ada\")", greet("Ada").equals("Bonjour, Ada !"));
        ExerciseChecker.check("2  prefix -> \"> journal\"", prefix(new StringBuilder("journal")).equals("> journal"));
        ExerciseChecker.check("3  cut -> \"de bord\"", cut(new StringBuilder("journal de bord")).equals("de bord"));
        ExerciseChecker.check("4  dropFirst -> \"ournal\"", dropFirst(new StringBuilder("journal")).equals("ournal"));
        ExerciseChecker.check("5  swapWord -> \"pigsty dirty\"", swapWord(new StringBuilder("pigeon dirty")).equals("pigsty dirty"));
        ExerciseChecker.check("6  mirror -> \"desserts\"", mirror("stressed").equals("desserts"));
        StringBuilder word = new StringBuilder("journal");
        ExerciseChecker.check("7  capitalize -> \"Journal\" (sb modifie)", capitalize(word).equals("Journal") && word.toString().equals("Journal"));
        ExerciseChecker.check("8  keep(4) -> \"jour\"", keep(new StringBuilder("journal"), 4).equals("jour"));
        ExerciseChecker.check("9  findSecond -> 3", findSecond(new StringBuilder("banana")) == 3);
        StringBuilder hello = new StringBuilder("hello");
        ExerciseChecker.check("10 middle -> \"el\" et sb intact", middle(hello).equals("el") && hello.toString().equals("hello"));
        ExerciseChecker.check("11 sameContent -> true", sameContent("ab", new StringBuilder("ab")) && !sameContent("ab", new StringBuilder("ba")));
        ExerciseChecker.check("12 chain() == \"87654X10\"", chain().equals("87654X10"));
        ExerciseChecker.check("13 mixed() == \"1ctrue2.5\"", mixed().equals("1ctrue2.5"));

        ExerciseChecker.summary();
    }
}
