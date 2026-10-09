package ch18_design.projects.p03_storage.solution;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * La vue prefixee passe tout le contrat ALORS QUE le stockage dessous contient deja des voisins :
 * pour elle, "vide" veut dire "rien sous users/".
 */
class PrefixedStorageTest extends WritableContractTest {

    private MemoryStorage inner;

    @Override
    protected WritableStorage emptyStorage() {
        inner = new MemoryStorage();
        inner.write("users2/x", "voisin");
        inner.write("users", "pas un dossier");
        inner.write("admin/a", "root");
        return new PrefixedStorage(inner, "users/");
    }

    @Test
    void writesGoUnderThePrefix() {
        WritableStorage users = emptyStorage();
        users.write("ada", "Lovelace");
        assertEquals(Optional.of("Lovelace"), inner.read("users/ada"));
        assertEquals(List.of("admin/a", "users", "users/ada", "users2/x"), inner.keys());
    }

    @Test
    void neighboursStayInvisibleAndUntouched() {
        WritableStorage users = emptyStorage();
        inner.write("users/x", "moi");
        assertEquals(List.of("x"), users.keys());
        assertEquals(Optional.of("moi"), users.read("x"));
        assertFalse(users.delete("a"));
        users.delete("x");
        assertEquals(List.of("admin/a", "users", "users2/x"), inner.keys());
    }

    @Test
    void prefixMustEndWithASlash() {
        assertEquals("prefixe invalide : users",
                assertThrows(IllegalArgumentException.class, () -> new PrefixedStorage(new MemoryStorage(), "users")).getMessage());
    }
}
