package ch15_jdbc.projects.p07_bikes;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BikeApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "installation : 3 stations, 6 velos",
            "tarifs : [0 0 50 50 100 350]",
            "stations : S1 2/3, S2 1/2, S3 3/4",
            "> membre Ana 300 : ok, membre #1",
            "> membre Ben 100 : ok, membre #2",
            "> membre Ana 50 : refuse : deja inscrit",
            "> louer Ana B1 : ok, location #1 depuis S1",
            "> louer Ben B1 : refuse : velo B1 indisponible",
            "> louer Ben B3 : ok, location #2 depuis S2",
            "> louer Ana B2 : refuse : location deja en cours",
            "> louer Zoe B2 : refuse : membre inconnu Zoe",
            "> rendre Ana S2 45 12 : ok, B1 a S2, 45 min, 50 cts",
            "> rendre Ben S2 130 30 : ok, B3 a S2, 130 min, 350 cts, dont 250 en dette",
            "> louer Ana B4 : ok, location #3 depuis S3",
            "> rendre Ana S2 20 3 : refuse : station S2 pleine",
            "> rendre Ana S1 20 3 : ok, B4 a S1, 20 min, 0 cts",
            "> recharger Ben 400 : ok, credit 150, dette remboursee 250",
            "> maintenance : ok, 2 velos en revision",
            "> louer Ben B5 : refuse : velo B5 indisponible",
            "> louer Ana B6 : ok, location #4 depuis S3",
            "> rendre Ana S1 10 2 : ok, B6 a S1, 10 min, 0 cts",
            "> rendre Ben S1 5 1 : refuse : aucune location en cours",
            "membres : [Ana 250 0, Ben 150 0]",
            "  trajet 1 Ana B1 S1>S2 45 50",
            "  trajet 2 Ben B3 S2>S2 130 350",
            "  trajet 3 Ana B4 S3>S1 20 0",
            "  trajet 4 Ana B6 S3>S1 10 0",
            "recette : [400 4 4] ; en revision : [B3, B5]",
            "reequilibrage : S1 3/3, S2 1/2, S3 0/4 ; mouvements [B4 S1>S3, B6 S1>S3] ; lot [1, 1]",
            "apres : S1 1/3, S2 1/2, S3 2/4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.URL", "Data.SCHEMA", "Data.STATIONS", "Data.BIKES",
            "Data.FREE", "Data.STEP", "Data.STEP_PRICE", "Data.SERVICE_KM",
            "Data.SCRIPT", "public final class Tariff", "Tariff.class.getName()", "CREATE ALIAS TARIF",
            "{? = call TARIF(?)}", ".registerOutParameter(", "CallableStatement", ".addBatch()",
            ".executeBatch()", "Statement.RETURN_GENERATED_KEYS", ".getGeneratedKeys()", ".setObject(",
            "Object... params", ".setAutoCommit(false)", ".commit()", ".rollback()",
            ".setSavepoint(\"paiement\")", ".rollback(payment)", "new SQLException(", "\"45000\"",
            "\"23513\"", "String run() throws SQLException", ".getMetaData().getColumnCount()", ".getObject(",
            "LEFT JOIN", "LIMIT 1 OFFSET ?", "Integer::sum",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "BikeApp", args, EXPECTED, API);
    }
}
