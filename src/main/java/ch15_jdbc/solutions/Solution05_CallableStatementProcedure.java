package ch15_jdbc.solutions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans jdbc.exercises.Exercise05_CallableStatementProcedure.
 */
public class Solution05_CallableStatementProcedure {

    public static int applyBonus(int baseScore, int bonusPercent) {
        return baseScore + (baseScore * bonusPercent / 100);
    }

    public static int callApplyBonus(Connection conn, int baseScore, int bonusPercent) throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{? = call APPLY_BONUS(?, ?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, baseScore);
            cs.setInt(3, bonusPercent);
            cs.execute();
            return cs.getInt(1);
        }
    }
}
