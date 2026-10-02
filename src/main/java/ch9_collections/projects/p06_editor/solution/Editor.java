package ch9_collections.projects.p06_editor.solution;

import ch9_collections.projects.p06_editor.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SOLUTION du projet 6 - l'editeur : List pour le texte, deux Deque pour annuler/retablir, TreeSet pour completer.
 */
public class Editor {

    private final Map<String, List<String>> files = new TreeMap<>();
    private List<String> lines;
    private final Deque<Edit> undo = new ArrayDeque<>();
    private final Deque<Edit> redo = new ArrayDeque<>();
    private final Deque<String> recent = new ArrayDeque<>();
    private final NavigableSet<String> dictionary = new TreeSet<>(Arrays.asList(Data.DICTIONARY));

    // Appliquer une modification dans un sens ou dans l'autre.
    private void apply(Edit e, boolean forward) {
        switch (e.type()) {
            case "ADD", "INSERT" -> {
                if (forward) {
                    lines.add(e.index(), e.after());
                } else {
                    lines.remove(e.index());
                }
            }
            case "DELETE" -> {
                if (forward) {
                    lines.remove(e.index());
                } else {
                    lines.add(e.index(), e.before());
                }
            }
            default -> lines.set(e.index(), forward ? e.after() : e.before());
        }
    }

    private void record(Edit e) {
        apply(e, true);
        undo.push(e);
        redo.clear();                                         // une nouvelle modification efface ce qu'on pouvait retablir
    }

    public String run(String command) {
        String[] p = command.split(" ", 3);
        switch (p[0]) {
            case "OPEN" -> {
                lines = files.computeIfAbsent(p[1], k -> new ArrayList<>());
                undo.clear();
                redo.clear();
                recent.removeFirstOccurrence(p[1]);              // deja present : on le remonte en tete
                recent.addFirst(p[1]);
                if (recent.size() > Data.RECENT) {
                    recent.removeLast();
                }
                return "ouvert " + p[1] + " (" + lines.size() + " lignes), recents " + recent;
            }
            case "ADD" -> record(new Edit("ADD", lines.size(), null, command.substring(4)));
            case "INSERT" -> record(new Edit("INSERT", Integer.parseInt(p[1]), null, p[2]));
            case "SET" -> record(new Edit("SET", Integer.parseInt(p[1]), lines.get(Integer.parseInt(p[1])), p[2]));
            case "DELETE" -> record(new Edit("DELETE", Integer.parseInt(p[1]), lines.get(Integer.parseInt(p[1])), null));
            case "UNDO" -> {
                Edit e = undo.poll();                            // poll : null si vide (pas d'exception)
                if (e == null) {
                    return "rien a annuler";
                }
                apply(e, false);
                redo.push(e);
                return "annule " + e.type() + " -> " + lines;
            }
            case "REDO" -> {
                Edit e = redo.poll();
                if (e == null) {
                    return "rien a retablir";
                }
                apply(e, true);
                undo.push(e);
                return "retabli " + e.type() + " -> " + lines;
            }
            case "COMPLETE" -> {
                // subSet(debut inclus, fin exclue) : tous les mots qui commencent par le prefixe.
                NavigableSet<String> hits = dictionary.subSet(p[1], true, p[1] + Character.MAX_VALUE, false);
                String next = dictionary.ceiling(p[1]);
                return "completer " + p[1] + " : " + hits + " (ceiling " + next + ")";
            }
            default -> {
                return "inconnu";
            }
        }
        return command.split(" ")[0].toLowerCase() + " -> " + lines + " (annulables " + undo.size() + ")";
    }

    // Parentheses equilibrees : une pile de caracteres ouvrants.
    static String brackets(String line) {
        Deque<Character> stack = new ArrayDeque<>();
        String open = "([{";
        String close = ")]}";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (open.indexOf(c) >= 0) {
                stack.push(c);
            } else if (close.indexOf(c) >= 0) {
                if (stack.isEmpty() || open.indexOf(stack.pop()) != close.indexOf(c)) {
                    return "erreur colonne " + i;
                }
            }
        }
        return stack.isEmpty() ? "ok" : "non ferme " + stack.peek();
    }

    // Les mots du dictionnaire a UNE modification pres : suppression, echange, remplacement, insertion.
    static Set<String> suggestions(String word, Set<String> dict) {
        Set<String> out = new TreeSet<>();
        if (dict.contains(word)) {
            out.add(word);
            return out;
        }
        Set<String> candidates = new TreeSet<>();
        for (int i = 0; i < word.length(); i++) {
            candidates.add(word.substring(0, i) + word.substring(i + 1));
            if (i + 1 < word.length()) {
                candidates.add(word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2));
            }
        }
        for (int i = 0; i <= word.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                candidates.add(word.substring(0, i) + c + word.substring(i));
                if (i < word.length()) {
                    candidates.add(word.substring(0, i) + c + word.substring(i + 1));
                }
            }
        }
        candidates.retainAll(dict);                               // l'intersection : seulement les mots connus
        out.addAll(candidates);
        return out;
    }

    public static void main(String[] args) {
        Editor ed = new Editor();
        for (String c : Data.COMMANDS) {
            System.out.println(c + "  =>  " + ed.run(c));
        }
        for (Map.Entry<String, List<String>> f : ed.files.entrySet()) {
            StringBuilder check = new StringBuilder();
            for (String line : f.getValue()) {
                check.append(" [").append(brackets(line)).append(']');
            }
            System.out.println(f.getKey() + " : " + f.getValue().size() + " lignes" + check);
        }
        StringBuilder spell = new StringBuilder("orthographe :");
        for (String t : Data.TYPOS) {
            spell.append(' ').append(t).append("->").append(suggestions(t, ed.dictionary));
        }
        System.out.println(spell);
        System.out.println("dictionnaire : premier " + ed.dictionary.first() + ", dernier " + ed.dictionary.last() + ", avant list " + ed.dictionary.lower("list")
                + ", apres list " + ed.dictionary.higher("list") + ", descendant " + ed.dictionary.descendingSet().headSet("long"));
    }
}
