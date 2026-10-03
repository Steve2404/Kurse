package ch15_jdbc.projects.p02_bank;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BankApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "comptes : {A1=500, B2=200, C3=0, D4=1000} ; autoCommit true",
            "banque ouverte : autoCommit false",
            "  A1>B2:150 -> ok (journal #1)",
            "  B2>C3:400 -> refuse : solde insuffisant (23513), rien n'a change",
            "  C3>A1:0 -> refuse : virement invalide",
            "  D4>Z9:100 -> refuse : compte inconnu Z9 (02000), rien n'a change",
            "  D4>C3:250 -> ok (journal #2)",
            "  Z9>A1:5 -> refuse : compte inconnu Z9 (02000), rien n'a change",
            "  C3>B2:250 -> ok (journal #3)",
            "  A1>A1:10 -> refuse : virement invalide",
            "  B2>D4:350 -> ok (journal #4)",
            "paie de D4 [A1:100, B2:100, C3:100] -> ok, 3 virements",
            "paie de D4 [A1:200, B2:200, C3:500] -> annulee entierement : solde insuffisant (23513) au virement 3, 2 deja faits annules",
            "soldes : {A1=450, B2=350, C3=100, D4=800} ; total 1700 ; conserve true",
            "journal : [1, 2, 3, 4, 5, 6, 7] ; le rejeu retrouve les soldes true",
            "pendant la transaction : moi 1450, observateur 450",
            "apres rollback : moi 450",
            "avant setAutoCommit(true) : observateur voit Cleo",
            "apres setAutoCommit(true) : observateur voit Cleopatre");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.URL", "Data.SCHEMA", "Data.ACCOUNTS", "Data.TRANSFERS",
            "Data.PAYROLLS", "2xDriverManager.getConnection(", ".setAutoCommit(false)", ".setAutoCommit(true)",
            ".getAutoCommit()", ".commit()", ".rollback()", "new SQLException(",
            ".getSQLState()", "case \"23513\"", ".getMessage()", "Statement.RETURN_GENERATED_KEYS",
            ".getGeneratedKeys()", ".prepareStatement(", ".executeUpdate()", ".executeQuery(",
            "ORDER BY seq", ".merge(", "Integer::sum", "TreeMap",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "BankApp", args, EXPECTED, API);
    }
}
