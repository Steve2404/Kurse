package ch15_jdbc.projects.p03_orders;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ShopApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "stock initial : {P1=5, P2=10, P3=2, P4=20}",
            "#1 Ana PARTIEL -> PARTIELLE, livre [P1:2, P4:5], refuse [P3 rupture], total 105",
            "#2 Ben TOUT -> COMPLETE, livre [P1:2, P2:3], refuse [], total 125",
            "#3 Cleo TOUT -> ANNULEE (rupture sur P1)",
            "#4 Dan PARTIEL -> PARTIELLE, livre [P2:4], refuse [P9 inconnu, P2 doublon], total 60",
            "#5 Eve PARTIEL -> COMPLETE, livre [P3:1, P4:2], refuse [], total 190",
            "#6 Fay PARTIEL -> ANNULEE (rien a livrer : [P3 rupture])",
            "stock final : {P1=1, P2=3, P3=1, P4=13}",
            "commandes en base : [1 Ana PARTIELLE 105, 2 Ben COMPLETE 125, 4 Dan PARTIELLE 60, 5 Eve COMPLETE 190]",
            "controle : stock + livre = stock initial true",
            "promos : {P1=40, P2=10, P3=130, P4=5} ; nom promo-ecran, id anonyme >= 0 true",
            "  getSavepointId d'un point nomme : SQLException",
            "rollback(promo-ecran) : {P1=40, P2=15, P3=180, P4=5}",
            "  rollback vers un point libere : 90063",
            "apres commit : {P1=40, P2=15, P3=180, P4=6}");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.URL", "Data.SCHEMA", "Data.PRODUCTS", "Data.ORDERS",
            "Savepoint", ".setSavepoint(\"ligne-\"", ".setSavepoint()", ".rollback(sp)",
            ".rollback()", ".releaseSavepoint(", ".getSavepointName()", ".getSavepointId()",
            ".setAutoCommit(false)", ".commit()", "new SQLException(", "case \"23505\"",
            "case \"23513\"", "Statement.RETURN_GENERATED_KEYS", "LEFT JOIN", "COALESCE",
            "GROUP BY", "5x.prepareStatement(",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ShopApp", args, EXPECTED, API);
    }
}
