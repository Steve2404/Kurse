package ch14_io.projects.p01_paths;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON PathLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "decomposition : docs/api/io/files.html egal true, nom files.html, parent docs/api/io, racine null, 4 noms, getName(1) api, subpath(1, 3) api/io",
            "comparaisons : startsWith(\"docs\") true, startsWith(\"doc\") false, endsWith(\"io/files.html\") true, absolu false, toAbsolutePath absolu true",
            "normalisees : docs/guide/./install.html -> docs/guide/install.html blog/2026/../2025/bilan.html -> blog/2025/bilan.html",
            "lien docs/guide/intro.html -> ../api/index.html, aller-retour true, ancetre commun 'docs'",
            "lien docs/api/io/files.html -> ../../../index.html, aller-retour true, ancetre commun ''",
            "lien blog/2026/janvier.html -> ../2025/bilan.html, aller-retour true, ancetre commun 'blog'",
            "lien index.html -> docs/guide/install.html, aller-retour true, ancetre commun ''",
            "lien docs/api/index.html -> io/paths.html (CASSE), aller-retour true, ancetre commun 'docs/api'",
            "shell | cd docs => /docs | cd guide => /docs/guide | cd ../api/./io => /docs/api/io | cd ../../.. => / | cd .. => refuse (hors racine) | cd blog/2026/../2025 => /blog/2025 | cd /tmp => refuse (absolu) | cd docs//api => /blog/2025/docs/api",
            "arborescence :",
            "blog/",
            "  2025/",
            "    bilan.html",
            "  2026/",
            "    janvier.html",
            "docs/",
            "  api/",
            "    index.html",
            "    io/",
            "      files.html",
            "  guide/",
            "    install.html",
            "    intro.html",
            "index.html",
            "resolve docs/guide/intro.html, resolveSibling docs/api, resolve(absolu) rend l'argument true ; File : nom intro.html, parent docs/guide, retour toPath true ; erreurs [subpath IllegalArgumentException, relativize relatif/absolu IllegalArgumentException, caractere nul InvalidPathException]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.PAGES", "Data.LINKS", "Data.COMMANDS", "Path.of(",
            "Paths.get(", ".getFileName()", ".getParent()", ".getRoot()",
            ".getNameCount()", ".getName(", ".subpath(", ".startsWith(",
            ".endsWith(", ".isAbsolute()", ".toAbsolutePath()", ".normalize()",
            ".relativize(", ".resolve(", ".resolveSibling(", ".toFile()",
            ".toPath()", "catch (IllegalArgumentException", "catch (InvalidPathException", "replace('\\\\', '/')",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "PathLab", args, EXPECTED, API);
    }
}
