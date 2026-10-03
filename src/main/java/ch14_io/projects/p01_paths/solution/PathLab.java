package ch14_io.projects.p01_paths.solution;

import ch14_io.projects.p01_paths.Data;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * SOLUTION du projet 1 - Path sans toucher au disque : construire, decomposer, resoudre, normaliser, relativiser.
 */
public class PathLab {

    // Sous Windows, toString() utilise '\' : on affiche toujours avec '/', pour une sortie identique partout.
    static String show(Path p) {
        return p.toString().replace('\\', '/');
    }

    // Le plus long ancetre commun de deux chemins relatifs (en comparant nom par nom).
    static Path commonAncestor(Path a, Path b) {
        Path common = Path.of("");
        for (int i = 0; i < Math.min(a.getNameCount(), b.getNameCount()) && a.getName(i).equals(b.getName(i)); i++) {
            common = common.resolve(a.getName(i));
        }
        return common;
    }

    public static void main(String[] args) {
        Path p = Path.of("docs", "api", "io", "files.html");
        Path same = Paths.get("docs/api/io/files.html");
        System.out.println("decomposition : " + show(p) + " egal " + p.equals(same) + ", nom " + p.getFileName() + ", parent " + show(p.getParent()) + ", racine "
                + p.getRoot() + ", " + p.getNameCount() + " noms, getName(1) " + p.getName(1) + ", subpath(1, 3) " + show(p.subpath(1, 3)));
        System.out.println("comparaisons : startsWith(\"docs\") " + p.startsWith("docs") + ", startsWith(\"doc\") " + p.startsWith("doc") + ", endsWith(\"io/files.html\") "
                + p.endsWith(Path.of("io", "files.html")) + ", absolu " + p.isAbsolute() + ", toAbsolutePath absolu " + p.toAbsolutePath().isAbsolute());

        // Normaliser : retirer "." et resoudre ".." ; deux chemins egaux une fois normalises ne le sont pas forcement avant.
        Set<String> pages = new TreeSet<>();
        StringBuilder fixed = new StringBuilder();
        for (String page : Data.PAGES) {
            Path raw = Path.of(page);
            Path clean = raw.normalize();
            pages.add(show(clean));
            if (!raw.equals(clean)) {
                fixed.append(' ').append(page).append(" -> ").append(show(clean));
            }
        }
        System.out.println("normalisees :" + fixed);

        // Lien relatif = dossier de la page source .relativize( page cible ).
        for (String link : Data.LINKS) {
            String[] ends = link.split(" -> ");
            Path from = Path.of(ends[0]);
            Path to = Path.of(ends[1]).normalize();
            Path href = from.getParent() == null ? to : from.getParent().relativize(to);
            boolean ok = pages.contains(show(to));
            // Verification : partir du dossier source, suivre le lien, normaliser -> on doit retomber sur la cible.
            Path back = (from.getParent() == null ? Path.of("") : from.getParent()).resolve(href).normalize();
            System.out.println("lien " + ends[0] + " -> " + show(href) + (ok ? "" : " (CASSE)") + ", aller-retour " + back.equals(to)
                    + ", ancetre commun '" + show(commonAncestor(from, to)) + "'");
        }

        // Le mini-shell : cd = resolve puis normalize ; refuser de sortir de la racine et les chemins absolus.
        Path cwd = Path.of("");
        StringBuilder trail = new StringBuilder();
        for (String command : Data.COMMANDS) {
            String arg = command.substring(3);
            String result;
            if (arg.startsWith("/")) {
                result = "refuse (absolu)";
            } else {
                Path next = cwd.resolve(arg).normalize();
                if (next.startsWith("..")) {
                    result = "refuse (hors racine)";
                } else {
                    cwd = next;
                    result = "/" + show(cwd);
                }
            }
            trail.append(" | ").append(command).append(" => ").append(result);
        }
        System.out.println("shell" + trail);

        // L'arborescence : on trie les pages, puis on indente selon la profondeur.
        StringBuilder tree = new StringBuilder();
        Set<String> printedDirs = new TreeSet<>();
        for (String page : pages) {
            Path path = Path.of(page);
            for (int depth = 1; depth < path.getNameCount(); depth++) {
                String dir = show(path.subpath(0, depth));
                if (printedDirs.add(dir)) {
                    tree.append("  ".repeat(depth - 1)).append(path.getName(depth - 1)).append("/\n");
                }
            }
            tree.append("  ".repeat(path.getNameCount() - 1)).append(path.getFileName()).append('\n');
        }
        System.out.print("arborescence :\n" + tree);

        // resolve / resolveSibling, File <-> Path, et les erreurs.
        Path guide = Path.of("docs/guide");
        File asFile = guide.resolve("intro.html").toFile();
        List<String> errors = new ArrayList<>();
        try {
            guide.subpath(1, 5);
        } catch (IllegalArgumentException e) {
            errors.add("subpath " + e.getClass().getSimpleName());
        }
        try {
            guide.relativize(guide.toAbsolutePath());
        } catch (IllegalArgumentException e) {
            errors.add("relativize relatif/absolu " + e.getClass().getSimpleName());
        }
        try {
            Path.of("a\u0000b");
        } catch (InvalidPathException e) {
            errors.add("caractere nul " + e.getClass().getSimpleName());
        }
        System.out.println("resolve " + show(guide.resolve("intro.html")) + ", resolveSibling " + show(guide.resolveSibling("api")) + ", resolve(absolu) rend l'argument "
                + guide.resolve(guide.toAbsolutePath()).equals(guide.toAbsolutePath()) + " ; File : nom " + asFile.getName() + ", parent "
                + asFile.getParent().replace('\\', '/') + ", retour toPath " + asFile.toPath().equals(guide.resolve("intro.html")) + " ; erreurs " + errors);
    }
}
