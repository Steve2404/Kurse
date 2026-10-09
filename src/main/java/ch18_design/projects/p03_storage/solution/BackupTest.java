package ch18_design.projects.p03_storage.solution;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Les clients : ils ne demandent que l'interface dont ils ont besoin. */
class BackupTest {

    @Test
    void copyFromAnArchiveOverwritesTheTarget() {
        MemoryStorage target = new MemoryStorage();
        target.write("a", "ancien");
        target.write("z", "garde");
        int copied = Backup.copy(new Archive(Map.of("a", "un", "b", "deux")), target);
        assertEquals(2, copied);
        assertEquals(List.of("a", "b", "z"), target.keys());
        assertEquals(Optional.of("un"), target.read("a"));
    }

    @Test
    void copyIntoAPrefixedView() {
        MemoryStorage all = new MemoryStorage();
        assertEquals(1, Backup.copy(new Archive(Map.of("bilan", "ok")), new PrefixedStorage(all, "2023/")));
        assertEquals(List.of("2023/bilan"), all.keys());
    }

    @Test
    void statsReadAnyStorage() {
        assertEquals("2 cle(s), 7 caractere(s)", StorageStats.describe(new Archive(Map.of("a", "un", "bb", "deux!"))));
        assertEquals("0 cle(s), 0 caractere(s)", StorageStats.describe(new MemoryStorage()));
    }
}
