package ch6_classdesign.projects.p05_arena;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Arena, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "equipes : Warrior Conan 120/120 Mage Merlin 70/70 mana 30 Healer Mira 80/80 VS Tank Golem 100/100 Warrior Brutus 110/110 Mage Sabrina 65/65 mana 30",
            "T1 Merlin -> Sabrina : 30 (Sabrina 35/65 mana 30)",
            "T1 Sabrina -> Merlin : 33 (Merlin 37/70 mana 20)",
            "T1 Conan -> Sabrina : 18 (Sabrina 17/65 mana 20)",
            "T1 Brutus -> Merlin : 16 (Merlin 21/70 mana 20)",
            "T1 Mira soigne Merlin +25 (Merlin 46/70 mana 20)",
            "T1 Golem -> Merlin : 14 (Merlin 32/70 mana 20)",
            "T2 Merlin -> Sabrina : 17 (Sabrina 0/65 mana 20)",
            "T2 Conan -> Golem : 14 (Golem 86/100)",
            "T2 Brutus -> Merlin : 16 (Merlin 16/70 mana 10)",
            "T2 Mira soigne Merlin +25 (Merlin 41/70 mana 10)",
            "T2 Golem -> Merlin : 14 (Merlin 27/70 mana 10)",
            "T3 Merlin -> Golem : 26 (Golem 60/100)",
            "T3 Conan -> Golem : 32 (Golem 28/100)",
            "T3 Brutus -> Merlin : 27 (Merlin 0/70 mana 0)",
            "T3 Mira -> Golem : 2 (Golem 26/100)",
            "T3 Golem -> Mira : 14 (Mira 66/80)",
            "T4 Conan -> Golem : 14 (Golem 12/100)",
            "T4 Brutus -> Mira : 16 (Mira 50/80)",
            "T4 Mira -> Golem : 2 (Golem 10/100)",
            "T4 Golem -> Mira : 14 (Mira 36/80)",
            "T5 Conan -> Golem : 10 (Golem 0/100)",
            "T5 Brutus -> Mira : 16 (Mira 20/80)",
            "T5 Mira soigne Mira +25 (Mira 45/80)",
            "T6 Conan -> Brutus : 36 (Brutus 74/110)",
            "T6 Brutus -> Mira : 32 (Mira 13/80)",
            "T6 Mira soigne Mira +25 (Mira 38/80)",
            "T7 Conan -> Brutus : 18 (Brutus 56/110)",
            "T7 Brutus -> Mira : 16 (Mira 22/80)",
            "T7 Mira soigne Mira +25 (Mira 47/80)",
            "T8 Conan -> Brutus : 18 (Brutus 38/110)",
            "T8 Brutus -> Mira : 16 (Mira 31/80)",
            "T8 Mira soigne Mira +25 (Mira 56/80)",
            "T9 Conan -> Brutus : 36 (Brutus 2/110)",
            "T9 Brutus -> Mira : 32 (Mira 24/80)",
            "T9 Mira soigne Mira +25 (Mira 49/80)",
            "T10 Conan -> Brutus : 2 (Brutus 0/110)",
            "vainqueur : equipe A",
            "apres : Conan 120/120 Merlin 0/70 mana 0 Mira 49/80",
            "revanche avec les copies, B en premier : equipe B ; les copies partaient a 120 pv ; Warrior Conan 120/120");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.TEAM_A", "Data.TEAM_B", "Data.MAX_ROUNDS", "abstract class Fighter",
            "protected abstract int damageTo(", "public abstract Fighter copy()", "public final boolean isAlive()", "public Warrior copy()",
            "public Mage copy()", "public Tank copy()", "public Healer copy()", "super.takeDamage(",
            "super.act(", "super.status()", "protected Fighter(Fighter", "super(other)",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Arena", args, EXPECTED, API);
    }
}
