package ch18_design.projects.p03_storage.solution;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** L'archive ne passe QUE le contrat de lecture : c'est tout ce qu'elle promet. */
class ArchiveTest extends ReadableContractTest {

    @Override
    protected ReadableStorage storageWith(Map<String, String> content) {
        return new Archive(content);
    }

    @Test
    void archiveIsASnapshot() {
        Map<String, String> source = new HashMap<>(Map.of("a", "un"));
        Archive archive = new Archive(source);
        source.put("b", "deux");
        source.put("a", "change");
        assertEquals(List.of("a"), archive.keys());
        assertEquals(Optional.of("un"), archive.read("a"));
    }

    @Test
    void invalidKeyInTheContentIsRefused() {
        assertEquals("cle invalide : Bilan",
                assertThrows(IllegalArgumentException.class, () -> new Archive(Map.of("Bilan", "x"))).getMessage());
    }
}
