package ch18_design.projects.p07_editor.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EditorTest {

    private final Document doc = new Document();
    private final History history = new History(100);

    @Test
    void documentInsertsDeletesAndChecksPositions() {
        doc.insert(0, "Bonjour");
        doc.insert(7, " monde");
        assertEquals("Bonjour monde", doc.text());
        assertEquals(" monde", doc.delete(7, 6));
        assertEquals("position invalide : 8", assertThrows(IllegalArgumentException.class, () -> doc.insert(8, "x")).getMessage());
        assertEquals("position invalide : -1", assertThrows(IllegalArgumentException.class, () -> doc.delete(-1, 1)).getMessage());
        assertEquals("longueur invalide : 8", assertThrows(IllegalArgumentException.class, () -> doc.delete(0, 8)).getMessage());
        assertEquals("Bonjour", doc.text());
    }

    @Test
    void undoAndRedoWalkThroughTheHistory() {
        history.run(new InsertCommand(doc, 0, "Bonjour le monde"));
        history.run(new DeleteCommand(doc, 7, 3));
        assertEquals("Bonjour monde", doc.text());
        assertTrue(history.undo());
        assertEquals("Bonjour le monde", doc.text());
        assertTrue(history.undo());
        assertEquals("", doc.text());
        assertFalse(history.undo());
        assertTrue(history.redo());
        assertTrue(history.redo());
        assertEquals("Bonjour monde", doc.text());
        assertFalse(history.redo());
    }

    @Test
    void aNewActionErasesTheRedoStack() {
        history.run(new InsertCommand(doc, 0, "abc"));
        history.undo();
        history.run(new InsertCommand(doc, 0, "xyz"));
        assertFalse(history.redo());
        assertEquals("xyz", doc.text());
    }

    @Test
    void historyForgetsTheOldestBeyondItsCapacity() {
        History small = new History(2);
        small.run(new InsertCommand(doc, 0, "a"));
        small.run(new InsertCommand(doc, 1, "b"));
        small.run(new InsertCommand(doc, 2, "c"));
        assertEquals(List.of("inserer 'c'", "inserer 'b'"), small.undoLabels());
        assertTrue(small.undo());
        assertTrue(small.undo());
        assertFalse(small.undo());
        assertEquals("a", doc.text());
        assertEquals("capacite invalide : 0", assertThrows(IllegalArgumentException.class, () -> new History(0)).getMessage());
    }

    @Test
    void replaceAllUndoesWithASnapshot() {
        history.run(new InsertCommand(doc, 0, "pain, pain au chocolat"));
        history.run(new ReplaceAllCommand(doc, "pain", "pain bio"));
        assertEquals("pain bio, pain bio au chocolat", doc.text());
        history.undo();
        assertEquals("pain, pain au chocolat", doc.text());
        history.redo();
        assertEquals("pain bio, pain bio au chocolat", doc.text());
        assertEquals(List.of("remplacer 'pain' par 'pain bio'", "inserer 'pain, pain au chocolat'"), history.undoLabels());
    }

    // Les etapes dependent l'une de l'autre : les defaire dans le mauvais ordre casserait tout.
    @Test
    void macroUndoesInReverseOrder() {
        doc.insert(0, "tarte");
        history.run(new MacroCommand("titre", List.of(new InsertCommand(doc, 0, "La "), new DeleteCommand(doc, 0, 3),
                new InsertCommand(doc, 5, " aux pommes"))));
        assertEquals("tarte aux pommes", doc.text());
        history.undo();
        assertEquals("tarte", doc.text());
        assertEquals(List.of(), history.undoLabels());
    }

    @Test
    void observersAreNotifiedOfEveryChange() {
        ChangeLog log = new ChangeLog();
        doc.addListener(log);
        WordCounter counter = WordCounter.attachTo(doc);
        history.run(new InsertCommand(doc, 0, "Bonjour le monde"));
        assertEquals(3, counter.count());
        history.run(new DeleteCommand(doc, 7, 3));
        assertEquals(2, counter.count());
        history.undo();
        assertEquals(3, counter.count());
        Document.Snapshot empty = new Document().snapshot();
        doc.restore(empty);
        assertEquals(0, counter.count());
        assertEquals(List.of("insert 0 'Bonjour le monde'", "delete 7 ' le'", "insert 7 ' le'", "restore 0 ''"), log.entries());
    }

    @Test
    void anUnsubscribedListenerHearsNothing() {
        ChangeLog log = new ChangeLog();
        doc.addListener(log);
        doc.insert(0, "a");
        doc.removeListener(log);
        doc.insert(1, "b");
        assertEquals(List.of("insert 0 'a'"), log.entries());
    }

    // Un abonne qui se desabonne pendant qu'on le previent : pas de ConcurrentModificationException.
    @Test
    void aListenerMayUnsubscribeWhileBeingNotified() {
        List<String> heard = new ArrayList<>();
        DocumentListener once = new DocumentListener() {
            @Override
            public void changed(DocumentEvent event) {
                heard.add(event.text());
                doc.removeListener(this);
            }
        };
        doc.addListener(once);
        doc.addListener(event -> heard.add("toujours " + event.text()));
        doc.insert(0, "a");
        doc.insert(1, "b");
        assertEquals(List.of("a", "toujours a", "toujours b"), heard);
    }

    @ParameterizedTest
    @CsvSource(value = {"'' ; 0", "'   ' ; 0", "un ; 1", "'  deux   mots ' ; 2", "'a\tb c' ; 3"}, delimiter = ';')
    void wordCount(String text, int words) {
        doc.insert(0, text);
        assertEquals(words, WordCounter.attachTo(doc).count());
    }
}
