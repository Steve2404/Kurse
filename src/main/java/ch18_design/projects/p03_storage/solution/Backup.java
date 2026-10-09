package ch18_design.projects.p03_storage.solution;

import java.util.List;

/** La sauvegarde : elle LIT n'importe quel stockage et ECRIT dans un stockage modifiable. Les types le disent. */
public final class Backup {

    private Backup() {
    }

    // Les cles deja presentes dans la destination sont ecrasees : la destination devient une copie a jour.
    public static int copy(ReadableStorage from, WritableStorage to) {
        List<String> keys = from.keys();
        keys.forEach(key -> to.write(key, from.read(key).orElseThrow()));
        return keys.size();
    }
}
