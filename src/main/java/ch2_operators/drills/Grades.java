package ch2_operators.drills;

/**
 * Les donnees du "projet bulletin" partagees par TOUS les drills du chapitre 2.
 * ============================================================================
 *
 * Toujours les memes donnees : ton cerveau se concentre sur les
 * operateurs, pas sur la decouverte des donnees. Lis ce fichier une
 * fois et garde-le ouvert a cote pendant les drills.
 *
 *   SCORES    : les notes de la classe          {15, 16, 8, 9, 10, 12}  (somme 70, 6 notes)
 *   SMALL     : une note stockee en byte         120 (proche de la limite 127 !)
 *   GRADE     : une lettre                       'B' (code 66)
 *   Options d'un eleve, un bit chacune : ABSENT = 1, LATE = 2, EXEMPT = 4
 *   OPTIONS   : les options d'un eleve           ABSENT | EXEMPT  (= 5, binaire 101)
 */
public final class Grades {

    public static final int[] SCORES = {15, 16, 8, 9, 10, 12};

    public static final byte SMALL = 120;

    public static final char GRADE = 'B';

    public static final int ABSENT = 1;
    public static final int LATE = 2;
    public static final int EXEMPT = 4;

    public static final int OPTIONS = ABSENT | EXEMPT;

    private Grades() {
    }
}
