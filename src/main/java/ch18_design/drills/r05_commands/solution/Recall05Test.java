package ch18_design.drills.r05_commands.solution;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 5. */
class Recall05Test {

    private final Counter counter = new Counter();
    private final Undo undo = new Undo();

    @Test
    void d01() {
        List<String> heard = new ArrayList<>();
        CounterListener listener = (oldValue, newValue) -> heard.add(oldValue + "->" + newValue);
        counter.addListener(listener);
        counter.set(5);
        counter.set(2);
        counter.removeListener(listener);
        counter.set(9);
        assertEquals(List.of("0->5", "5->2"), heard);
        assertEquals(9, counter.value());
    }

    @Test
    void d02() {
        List<String> heard = new ArrayList<>();
        counter.addListener(new CounterListener() {
            @Override
            public void changed(int oldValue, int newValue) {
                heard.add("une fois " + newValue);
                counter.removeListener(this);
            }
        });
        counter.addListener((oldValue, newValue) -> heard.add("toujours " + newValue));
        counter.set(1);
        counter.set(2);
        assertEquals(List.of("une fois 1", "toujours 1", "toujours 2"), heard);
    }

    @Test
    void d03() {
        undo.perform(new Add(counter, 5));
        undo.perform(new Add(counter, -2));
        assertEquals(3, counter.value());
        assertTrue(undo.undo());
        assertEquals(5, counter.value());
        assertTrue(undo.undo());
        assertFalse(undo.undo());
        assertTrue(undo.redo());
        assertTrue(undo.redo());
        assertFalse(undo.redo());
        assertEquals(3, counter.value());
    }

    @Test
    void d04() {
        undo.perform(new Add(counter, 7));
        undo.perform(new Reset(counter));
        assertEquals(0, counter.value());
        undo.undo();
        assertEquals(7, counter.value());
        undo.redo();
        assertEquals(0, counter.value());
    }

    @Test
    void d05() {
        undo.perform(new Add(counter, 1));
        undo.undo();
        undo.perform(new Add(counter, 10));
        assertFalse(undo.redo());
        assertEquals(10, counter.value());
    }

    // Reset puis Add : defaire dans l'ordre inverse retrouve 4 ; dans l'ordre normal, on trouverait 1.
    @Test
    void d06() {
        counter.set(4);
        undo.perform(new Batch(List.of(new Reset(counter), new Add(counter, 3))));
        assertEquals(3, counter.value());
        undo.undo();
        assertEquals(4, counter.value());
    }
}
