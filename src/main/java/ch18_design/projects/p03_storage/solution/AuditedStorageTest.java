package ch18_design.projects.p03_storage.solution;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Le journal ne doit rien changer au contrat : le contrat herite le prouve. */
class AuditedStorageTest extends WritableContractTest {

    @Override
    protected WritableStorage emptyStorage() {
        return new AuditedStorage(new MemoryStorage());
    }

    @Test
    void logsEachChange() {
        AuditedStorage storage = new AuditedStorage(new MemoryStorage());
        storage.write("a", "un");
        storage.read("a");
        storage.delete("a");
        storage.delete("b");
        assertEquals(List.of("write a", "delete a", "delete b (absente)"), storage.log());
    }

    @Test
    void refusedWriteLeavesNoTrace() {
        AuditedStorage storage = new AuditedStorage(new MemoryStorage());
        assertThrows(IllegalArgumentException.class, () -> storage.write("A", "x"));
        assertEquals(List.of(), storage.log());
        assertThrows(UnsupportedOperationException.class, () -> storage.log().add("triche"));
    }
}
