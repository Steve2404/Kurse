package ch18_design.projects.p03_storage.solution;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** La memoire passe tout le contrat (herite), plus son message propre. */
class MemoryStorageTest extends WritableContractTest {

    @Override
    protected WritableStorage emptyStorage() {
        return new MemoryStorage();
    }

    @Test
    void nullValueMessage() {
        assertEquals("valeur absente pour a",
                assertThrows(IllegalArgumentException.class, () -> new MemoryStorage().write("a", null)).getMessage());
    }
}
