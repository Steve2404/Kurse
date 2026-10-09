package ch17_algorithms.projects.p05_stacks.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/** Les tests de reference de MinStack et QueueFromStacks. */
class StructuresTest {

    @Test
    void minStackRemembersTheMinimumAtEachHeight() {
        MinStack s = new MinStack();
        s.push(5);
        s.push(3);
        s.push(7);
        s.push(3);
        assertEquals(3, s.min());
        assertEquals(3, s.pop());
        assertEquals(3, s.min());
        assertEquals(7, s.pop());
        assertEquals(3, s.min());
        assertEquals(3, s.pop());
        assertAll(
                () -> assertEquals(5, s.min()),
                () -> assertEquals(5, s.peek()),
                () -> assertEquals(1, s.size()));
    }

    @Test
    void emptyMinStackComplains() {
        MinStack s = new MinStack();
        NoSuchElementException e = assertThrows(NoSuchElementException.class, s::min);
        assertAll(
                () -> assertEquals("pile vide", e.getMessage()),
                () -> assertThrows(NoSuchElementException.class, s::pop),
                () -> assertThrows(NoSuchElementException.class, s::peek));
    }

    @Test
    void queueFromStacksIsFirstInFirstOut() {
        QueueFromStacks<String> q = new QueueFromStacks<>();
        q.offer("a");
        q.offer("b");
        assertEquals("a", q.poll());
        q.offer("c");
        assertAll(
                () -> assertEquals("b", q.peek()),
                () -> assertEquals("b", q.poll()),
                () -> assertEquals("c", q.poll()),
                () -> assertNull(q.poll()),
                () -> assertNull(q.peek()),
                () -> assertEquals(0, q.size()));
    }

    @Test
    void queueSizeCountsBothStacks() {
        QueueFromStacks<Integer> q = new QueueFromStacks<>();
        q.offer(1);
        q.offer(2);
        q.poll();
        q.offer(3);
        assertEquals(2, q.size());
    }

    @Test
    void aMillionQueueOperationsAreAmortizedConstant() {
        QueueFromStacks<Integer> q = new QueueFromStacks<>();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 1_000_000; i++) {
                q.offer(i);
                if (i % 2 == 0) {
                    assertEquals(i / 2, q.poll());
                }
            }
        });
    }
}
