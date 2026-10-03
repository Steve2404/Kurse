package ch14_io.projects.p07_vcs.solution;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * SOLUTION - le depot : un dossier de travail, et un dossier cache ".minigit" qui contient
 * objects/ (un fichier par CONTENU, nomme par son empreinte), commits/ (un commit serialise par fichier) et HEAD.
 */
public class Repository {

    private final Path work;
    private final Path meta;

    public Repository(Path work) throws IOException {
        this.work = work;
        this.meta = work.resolve(".minigit");
        Files.createDirectories(meta.resolve("objects"));
        Files.createDirectories(meta.resolve("commits"));
        if (!Files.exists(meta.resolve("HEAD"))) {
            Files.writeString(meta.resolve("HEAD"), "0");
        }
    }

    // FNV-1a sur 64 bits : une empreinte du contenu (deux contenus egaux -> meme empreinte).
    static String hash(byte[] bytes) {
        long h = 0xcbf29ce484222325L;
        for (byte b : bytes) {
            h ^= b & 0xFF;
            h *= 0x100000001b3L;
        }
        return Long.toHexString(h);
    }

    int head() throws IOException {
        return Integer.parseInt(Files.readString(meta.resolve("HEAD")).strip());
    }

    // Les fichiers suivis : tout le dossier de travail, sauf .minigit, en chemins relatifs "/".
    TreeMap<String, String> scan() throws IOException {
        TreeMap<String, String> files = new TreeMap<>();
        try (Stream<Path> all = Files.walk(work)) {
            for (Path p : all.filter(Files::isRegularFile).filter(p -> !p.startsWith(meta)).toList()) {
                files.put(work.relativize(p).toString().replace('\\', '/'), hash(Files.readAllBytes(p)));
            }
        }
        return files;
    }

    public Commit load(int id) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(meta.resolve("commits/" + id + ".ser")))) {
            return (Commit) in.readObject();
        }
    }

    public Commit commit(String message) throws IOException {
        TreeMap<String, String> snapshot = scan();
        for (Map.Entry<String, String> e : snapshot.entrySet()) {
            Path object = meta.resolve("objects").resolve(e.getValue());
            if (!Files.exists(object)) {                                  // un contenu deja connu n'est stocke qu'UNE fois
                Files.copy(work.resolve(e.getKey()), object);
            }
        }
        int id = head() + 1;
        Commit c = new Commit(id, message, head(), snapshot);
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(meta.resolve("commits/" + id + ".ser")))) {
            out.writeObject(c);
        }
        Files.writeString(meta.resolve("HEAD"), String.valueOf(id));
        return c;
    }

    // Etat du dossier de travail par rapport au commit courant.
    public Map<String, String> status() throws IOException, ClassNotFoundException {
        Map<String, String> base = head() == 0 ? Map.of() : load(head()).snapshot();
        Map<String, String> now = scan();
        Map<String, String> status = new TreeMap<>();
        now.forEach((f, h) -> {
            if (!base.containsKey(f)) {
                status.put(f, "ajoute");
            } else if (!base.get(f).equals(h)) {
                status.put(f, "modifie");
            }
        });
        base.keySet().stream().filter(f -> !now.containsKey(f)).forEach(f -> status.put(f, "supprime"));
        return status;
    }

    // Remettre le dossier de travail dans l'etat d'un commit (les fichiers en trop sont supprimes).
    public void checkout(int id) throws IOException, ClassNotFoundException {
        Commit c = load(id);
        for (String f : scan().keySet()) {
            if (!c.snapshot().containsKey(f)) {
                Files.delete(work.resolve(f));
            }
        }
        for (Map.Entry<String, String> e : c.snapshot().entrySet()) {
            Path target = work.resolve(e.getKey());
            Files.createDirectories(target.getParent());
            Files.copy(meta.resolve("objects").resolve(e.getValue()), target, StandardCopyOption.REPLACE_EXISTING);
        }
        Files.writeString(meta.resolve("HEAD"), String.valueOf(id));
    }

    public List<String> contentAt(int id, String file) throws IOException, ClassNotFoundException {
        String h = load(id).snapshot().get(file);
        return h == null ? List.of() : Files.readAllLines(meta.resolve("objects").resolve(h));
    }

    public int objectCount() throws IOException {
        try (Stream<Path> objects = Files.list(meta.resolve("objects"))) {
            return (int) objects.count();
        }
    }

    // Diff de lignes par plus longue sous-suite commune (LCS) : "  " commun, "- " retire, "+ " ajoute.
    public static List<String> diff(List<String> a, List<String> b) {
        int[][] lcs = new int[a.size() + 1][b.size() + 1];
        for (int i = a.size() - 1; i >= 0; i--) {
            for (int j = b.size() - 1; j >= 0; j--) {
                lcs[i][j] = a.get(i).equals(b.get(j)) ? lcs[i + 1][j + 1] + 1 : Math.max(lcs[i + 1][j], lcs[i][j + 1]);
            }
        }
        List<String> out = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < a.size() && j < b.size()) {
            if (a.get(i).equals(b.get(j))) {
                out.add("  " + a.get(i++));
                j++;
            } else if (lcs[i + 1][j] >= lcs[i][j + 1]) {
                out.add("- " + a.get(i++));
            } else {
                out.add("+ " + b.get(j++));
            }
        }
        while (i < a.size()) {
            out.add("- " + a.get(i++));
        }
        while (j < b.size()) {
            out.add("+ " + b.get(j++));
        }
        return out;
    }
}
