package ch18_design.projects.p03_storage.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Le contrat de WritableStorage. Il HERITE du contrat de lecture : un stockage modifiable doit aussi
 * passer tous les tests de ReadableContractTest. C'est Liskov, verifie par les tests.
 */
abstract class WritableContractTest extends ReadableContractTest {

    /** Un stockage modifiable, NEUF et vide a chaque appel. */
    protected abstract WritableStorage emptyStorage();

    // Le contrat de lecture se remplit par des write : rien a ecrire de plus dans les sous-classes.
    @Override
    protected ReadableStorage storageWith(Map<String, String> content) {
        WritableStorage storage = emptyStorage();
        content.forEach(storage::write);
        return storage;
    }

    @Test
    void overwriteKeepsTheLastValue() {
        WritableStorage storage = emptyStorage();
        storage.write("a", "un");
        storage.write("a", "uno");
        assertEquals(Optional.of("uno"), storage.read("a"));
        assertEquals(List.of("a"), storage.keys());
    }

    @Test
    void deleteExistingKey() {
        WritableStorage storage = emptyStorage();
        storage.write("a", "un");
        storage.write("b", "deux");
        assertTrue(storage.delete("a"));
        assertEquals(Optional.empty(), storage.read("a"));
        assertEquals(List.of("b"), storage.keys());
    }

    @Test
    void deleteMissingKeyReturnsFalse() {
        WritableStorage storage = emptyStorage();
        storage.write("a", "un");
        assertFalse(storage.delete("b"));
        assertEquals(List.of("a"), storage.keys());
    }

    @Test
    void keysStaySortedWhateverTheWriteOrder() {
        WritableStorage storage = emptyStorage();
        for (String key : List.of("zeta", "alpha", "m/1", "b")) {
            storage.write(key, "x");
        }
        assertEquals(List.of("alpha", "b", "m/1", "zeta"), storage.keys());
    }

    @Test
    void nullValueIsRefused() {
        WritableStorage storage = emptyStorage();
        assertThrows(IllegalArgumentException.class, () -> storage.write("a", null));
        assertEquals(List.of(), storage.keys());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "A", "a b"})
    void invalidKeyIsRefusedOnWriteAndDelete(String key) {
        WritableStorage storage = emptyStorage();
        assertEquals("cle invalide : " + key,
                assertThrows(IllegalArgumentException.class, () -> storage.write(key, "x")).getMessage());
        assertEquals("cle invalide : " + key,
                assertThrows(IllegalArgumentException.class, () -> storage.delete(key)).getMessage());
    }
}
