package ch18_design.projects.p03_storage.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Le TEST DE CONTRAT de ReadableStorage, ecrit UNE fois. Chaque implementation a sa sous-classe qui dit
 * seulement comment fabriquer un stockage : JUnit relance alors TOUS ces tests sur elle.
 * Abstraite : JUnit ne la lance pas seule.
 */
abstract class ReadableContractTest {

    /** Un stockage qui contient exactement ces paires (cle, valeur). */
    protected abstract ReadableStorage storageWith(Map<String, String> content);

    @Test
    void readsWhatItContains() {
        ReadableStorage storage = storageWith(Map.of("a", "un", "b/2", "deux"));
        assertEquals(Optional.of("un"), storage.read("a"));
        assertEquals(Optional.of("deux"), storage.read("b/2"));
    }

    @Test
    void missingKeyIsEmpty() {
        assertEquals(Optional.empty(), storageWith(Map.of("a", "un")).read("b"));
    }

    @Test
    void keysAreSorted() {
        ReadableStorage storage = storageWith(Map.of("zeta", "1", "alpha", "2", "m/1", "3", "b", "4"));
        assertEquals(List.of("alpha", "b", "m/1", "zeta"), storage.keys());
    }

    @Test
    void emptyStorageHasNoKeys() {
        assertEquals(List.of(), storageWith(Map.of()).keys());
    }

    @Test
    void keysListIsUnmodifiable() {
        List<String> keys = storageWith(Map.of("a", "un")).keys();
        assertThrows(UnsupportedOperationException.class, () -> keys.add("b"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "A", "a b", "été"})
    void invalidKeyIsRefused(String key) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> storageWith(Map.of()).read(key));
        assertEquals("cle invalide : " + key, e.getMessage());
    }
}
