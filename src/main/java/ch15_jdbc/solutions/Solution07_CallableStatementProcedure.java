package ch15_jdbc.solutions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch15_jdbc.exercises.Exercise07_CallableStatementProcedure.
 */
public class Solution07_CallableStatementProcedure {

    public static int applyBonus(int baseScore, int bonusPercent) {
        // Donnee de l'exercice : la methode Java que l'ALIAS SQL APPLY_BONUS appelle.
        return baseScore + (baseScore * bonusPercent / 100);
    }

    public static int callApplyBonus(Connection conn, int baseScore, int bonusPercent) throws SQLException {
        // Syntaxe d'appel {? = call ...} : le 1er ? est le RESULTAT, enregistre avec registerOutParameter avant execute.
        try (CallableStatement cs = conn.prepareCall("{? = call APPLY_BONUS(?, ?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, baseScore);
            cs.setInt(3, bonusPercent);
            cs.execute();
            return cs.getInt(1);
        }
    }
}
