package ch4_coreapis.projects.p02_editor;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Editor, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "APPEND -> [Bonjour monde] (13)",
            "INSERT -> [Bonjour le grand monde] (22)",
            "FIND -> \"monde\" en position 17",
            "REPLACE -> [Bonjour le GRAND monde] (22)",
            "DELETE -> [le GRAND monde] (14)",
            "UNDO -> annule [Bonjour le GRAND monde] (historique 3)",
            "DELCHAR -> [Bonjourle GRAND monde] (21)",
            "CUT -> [e GRAND monde] (13), presse-papiers [Bonjourl]",
            "PASTE -> refuse : position 99 hors limites (longueur 13)",
            "PASTE -> [Bonjourle GRAND monde] (21)",
            "REVERSE -> [ednom DNARG elruojnoB] (21)",
            "REVERSE -> [Bonjourle GRAND monde] (21)",
            "UNDO -> annule [ednom DNARG elruojnoB] (historique 4)",
            "UNDO -> annule [Bonjourle GRAND monde] (historique 3)",
            "UNDO -> annule [e GRAND monde] (historique 2)",
            "commande inconnue : SHOUT",
            "UNDO -> annule [Bonjourle GRAND monde] (historique 1)",
            "UNDO -> annule [Bonjour le GRAND monde] (historique 0)",
            "UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)",
            "UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)",
            "UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)",
            "UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)",
            "LENGTH -> 22, caractere 0 : B",
            "--- POOL ET EGALITE ---",
            "a == b true, a == c false, a.equals(c) true, a == c.intern() true",
            "constante true, variable false, variable.intern() true",
            "sb.equals false, contenus true, s1 == s3 true, s1 xy, capacite vide 0",
            "immuable : abc, ABC, concat \"abcd\" et abc");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new StringBuilder(", ".append(", ".insert(", ".replace(",
            ".delete(", ".deleteCharAt(", ".reverse()", ".substring(",
            ".indexOf(", ".setLength(", ".charAt(", ".toString()",
            ".intern()", "new String(", "re:split\\(\" \", \\d\\)##split avec une limite", "System.arraycopy(",
            "re:new String\\[[\\w.]+\\]##tableau de String (pile)", "re:final String \\w+ = \"##constante de compilation",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Editor", args, EXPECTED, API);
    }
}
