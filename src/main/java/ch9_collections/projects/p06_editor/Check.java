package ch9_collections.projects.p06_editor;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Editor, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "OPEN main.java  =>  ouvert main.java (0 lignes), recents [main.java]",
            "ADD int x = (a + b;  =>  add -> [int x = (a + b;] (annulables 1)",
            "ADD list.add(map.get(k));  =>  add -> [int x = (a + b;, list.add(map.get(k));] (annulables 2)",
            "INSERT 0 // debut  =>  insert -> [// debut, int x = (a + b;, list.add(map.get(k));] (annulables 3)",
            "SET 1 int x = (a + b);  =>  set -> [// debut, int x = (a + b);, list.add(map.get(k));] (annulables 4)",
            "DELETE 0  =>  delete -> [int x = (a + b);, list.add(map.get(k));] (annulables 5)",
            "UNDO  =>  annule DELETE -> [// debut, int x = (a + b);, list.add(map.get(k));]",
            "UNDO  =>  annule SET -> [// debut, int x = (a + b;, list.add(map.get(k));]",
            "REDO  =>  retabli SET -> [// debut, int x = (a + b);, list.add(map.get(k));]",
            "OPEN util.java  =>  ouvert util.java (0 lignes), recents [util.java, main.java]",
            "ADD if (x) { y[0] = 1; }  =>  add -> [if (x) { y[0] = 1; }] (annulables 1)",
            "OPEN main.java  =>  ouvert main.java (3 lignes), recents [main.java, util.java]",
            "ADD retrun [x);  =>  add -> [// debut, int x = (a + b);, list.add(map.get(k));, retrun [x);] (annulables 1)",
            "COMPLETE ma  =>  completer ma : [main, map, math] (ceiling main)",
            "COMPLETE li  =>  completer li : [list, listiterator] (ceiling list)",
            "COMPLETE zz  =>  completer zz : [] (ceiling null)",
            "OPEN notes.txt  =>  ouvert notes.txt (0 lignes), recents [notes.txt, main.java, util.java]",
            "UNDO  =>  rien a annuler",
            "REDO  =>  rien a retablir",
            "OPEN test.java  =>  ouvert test.java (0 lignes), recents [test.java, notes.txt, main.java]",
            "main.java : 4 lignes [ok] [ok] [ok] [erreur colonne 9]",
            "notes.txt : 0 lignes",
            "test.java : 0 lignes",
            "util.java : 1 lignes [ok]",
            "orthographe : retrun->[return] mapp->[map] lsit->[list] deqeu->[deque] set->[set] xyz->[]",
            "dictionnaire : premier add, dernier set, avant list int, apres list listiterator, descendant [set, return, retain, queue, math, map, main]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.COMMANDS", "Data.DICTIONARY", "record Edit(", "Deque<Edit> undo = new ArrayDeque<>()",
            ".push(", ".poll()", "redo.clear()", ".removeFirstOccurrence(",
            ".addFirst(", ".removeLast()", "NavigableSet<String>", ".subSet(",
            ".ceiling(", ".lower(", ".higher(", ".descendingSet()",
            "Deque<Character>", ".retainAll(", "computeIfAbsent(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Editor", args, EXPECTED, API);
    }
}
