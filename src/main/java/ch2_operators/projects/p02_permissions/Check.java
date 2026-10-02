package ch2_operators.projects.p02_permissions;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Permissions, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"0754", "022", "0xB", "3", "false"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "mode : 754 rwxr-xr-- (decimal 492)",
            "nouveau fichier : 644 rw-r--r--",
            "nouveau dossier : 755 rwxr-xr-x",
            "+x autres : 755 rwxr-xr-x",
            "-w proprietaire : 554 r-xr-xr--",
            "bascule lecture autres : 750 rwxr-x---",
            "bascule deux fois : 754 rwxr-xr--",
            "proprietaire peut lire : true, ecrire : true",
            "groupes 1011, groupe du fichier 3 -> membre : true",
            "acces accorde en tant que groupe : r-x, ecriture refusee",
            "~mode = -493, ~mode & 0777 = 23",
            "-16 >> 2 = -4, -16 >>> 28 = 15");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Integer.decode(", "<<", ">>>", "re:[^>]>> \\d##decalage signe >>",
            "& ~", " ^ ", " | ", "& 7",
            "Integer.toOctalString(", "Integer.toBinaryString(", "re:\\(\\w+ & [\\w <]+\\) != 0##test d'un bit (x & masque) != 0", "re:\\b0[0-7]{3}\\b##litteral octal (0666...)",
            "2xre:\\?[^;:]*:[^;]*\\?##ternaires imbriques (a ? b : c ? d : e)",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Permissions", args, ARGS, EXPECTED, API);
    }
}
