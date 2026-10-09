package ch18_design.projects.p07_editor;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Document.java", "position > text.length()", "position >= text.length()"),
            new Mutant("Document.java", "List.copyOf(listeners).forEach(", "listeners.forEach("),
            new Mutant("Document.java", "        fire(new DocumentEvent(\"insert\", position, inserted));\n", ""),
            new Mutant("Document.java", "new DocumentEvent(\"delete\", position, removed)", "new DocumentEvent(\"delete\", position, \"\")"),
            new Mutant("Document.java", "        listeners.remove(listener);\n", ""),
            new Mutant("DeleteCommand.java", "document.insert(position, removed);", "document.insert(0, removed);"),
            new Mutant("History.java", "        redoStack.clear();\n", ""),
            new Mutant("History.java", "command.undo();\n        redoStack.push(command);", "command.undo();"),
            new Mutant("History.java", "undoStack.size() > capacity", "undoStack.size() >= capacity"),
            new Mutant("History.java", "if (undoStack.isEmpty()) {", "if (undoStack == null) {"),
            new Mutant("History.java", "if (capacity < 1) {", "if (capacity < 0) {"),
            new Mutant("History.java", "command.execute();\n        undoStack.push(command);\n        if", "command.execute();\n        undoStack.addLast(command);\n        if"),
            new Mutant("MacroCommand.java", "for (int i = steps.size() - 1; i >= 0; i--) {\n            steps.get(i).undo();\n        }", "steps.forEach(Command::undo);"),
            new Mutant("ReplaceAllCommand.java", "document.restore(before);", "document.restore(document.snapshot());"),
            new Mutant("WordCounter.java", "split(\"\\\\s+\")", "split(\" \")"),
            new Mutant("WordCounter.java", "        counter.count = words(document.text());\n", ""));

    static final List<String> API_CODE = List.of(
            "record DocumentEvent(", "interface DocumentListener", "final class Document", "static final class Snapshot",
            "interface Command", "final class InsertCommand implements Command", "final class DeleteCommand implements Command",
            "final class ReplaceAllCommand implements Command", "final class MacroCommand implements Command", "final class History",
            "final class WordCounter implements DocumentListener", "final class ChangeLog implements DocumentListener",
            "static WordCounter attachTo(", "Deque<Command>", "!switch", "!instanceof", "!Legacy",
            "max:method=10",
            "in:Document.java=private Snapshot(String##le memento ne se fabrique que dans Document",
            "in:Document.java!WordCounter##le document ne connait pas ses observateurs",
            "in:Document.java!ChangeLog##le document ne connait pas ses observateurs",
            "in:History.java!InsertCommand##l'historique ne connait aucune commande concrete",
            "in:History.java!DeleteCommand##l'historique ne connait aucune commande concrete",
            "in:History.java!ReplaceAllCommand##l'historique ne connait aucune commande concrete",
            "in:ReplaceAllCommand.java=snapshot()##le remplacement s'annule par un memento");

    static final List<String> API_TESTS = List.of(
            "new History(", "new MacroCommand(", "new ReplaceAllCommand(", "WordCounter.attachTo(", "removeListener(",
            "undoLabels()", "@ParameterizedTest", "assertThrows(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 12, MUTANTS, API_CODE, API_TESTS);
    }
}
