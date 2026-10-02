package ch6_classdesign.projects.p07_media;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON MediaApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "#1 [livre] Le Petit Prince (1943) 192 min de Saint-Exupery",
            "#2 [film] Inception (2010) 148 min de Nolan",
            "#3 [album] Kind of Blue (1959) 46 min",
            "#4 [livre] Dune (1965) 1200 min de Herbert",
            "#5 [film] Interstellar (2014) 169 min de Nolan",
            "#6 [podcast] Code Story (2021) 90 min, 3 episodes",
            "#8 [livre] Fondation (1951) 510 min de Asimov",
            "#9 [album] Random Access Memories (2013) 26 min",
            "doublons ignores : Inception #7 ; objets crees 9, catalogue 8",
            "du plus recent : 2021 2014 2013 2010 1965 1959 1951 1943",
            "recherche science : [Inception] [Dune] [Interstellar] [Fondation]",
            "recherche Nolan : [Inception] [Interstellar]",
            "recherche Asimov : [Fondation]",
            "recherche jazz : [Kind of Blue]",
            "isbn : 978-207061275-8 ok 978-226632048-2 FAUX (cle attendue 1) 978-207036053-6 ok",
            "minutes : livre=1902 film=317 album=72 podcast=90",
            "si vous aimez Dune : Fondation (50%) Inception (20%) Interstellar (20%)",
            "playlist Kind of Blue <= 30 min : pistes 1 2 5 = 28 min 24 s",
            "playlist Code Story <= 30 min : pistes 1 = 30 min 0 s",
            "playlist Random Access Memories <= 30 min : pistes 1 2 3 4 5 = 25 min 47 s");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.MEDIA", "Data.QUERIES", "Data.LIKED", "Data.PLAYLIST_LIMIT",
            "abstract class Media", "abstract class AudioMedia extends Media", "2xextends AudioMedia", "final class Isbn",
            "2xpublic boolean equals(Object", "2xpublic int hashCode()", "super.matches(", "super.toString()",
            ".clone()", "instanceof AudioMedia", "public final int similarity(",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "MediaApp", args, EXPECTED, API);
    }
}
