package ch2_operators.projects.p05_reportcard.solution;

/**
 * SOLUTION du projet 5 (capstone) - une conception possible.
 */
public class ReportCard {

    static final int DELEGATE = 1;      // bit 0
    static final int SPORT = 1 << 1;    // bit 1
    static final int LATIN = 1 << 2;    // bit 2
    static final double CLASS_AVERAGE = 11.5;

    // Arrondi au dixieme "au plus proche" : decaler, ajouter 0.5, tronquer par le cast, redecaler.
    static double tenth(double value) {
        return (int) (value * 10 + 0.5) / 10.0;
    }

    static String mention(double avg) {
        return avg >= 16 ? "Tres bien" : avg >= 14 ? "Bien" : avg >= 12 ? "Assez bien" : avg >= 10 ? "Passable" : "Ajourne";
    }

    public static void main(String[] args) {
        double maths = Double.parseDouble(args[0]);
        double physics = Double.parseDouble(args[1]);
        double french = Double.parseDouble(args[2]);
        int cMaths = Integer.parseInt(args[3]);
        int cPhysics = Integer.parseInt(args[4]);
        int cFrench = Integer.parseInt(args[5]);
        int absences = Integer.parseInt(args[6]);
        // Les options arrivent ecrites en binaire ("101") : parseInt avec la base 2.
        int options = Integer.parseInt(args[7], 2);

        int coefficients = cMaths + cPhysics + cFrench;
        // Promotion : double * int -> double, la somme reste double.
        double points = maths * cMaths + physics * cPhysics + french * cFrench;
        double average = points / coefficients;
        System.out.println("points " + points + " / coefficients " + coefficients + " = " + average);
        // Le piege de la division entiere : le cast ne s'applique qu'a ce qui le SUIT immediatement.
        int whole = (int) points;
        System.out.println("piege : (int) points = " + whole + " ; " + whole + " / " + coefficients + " = " + whole / coefficients
                + " ; (double) (" + whole + " / " + coefficients + ") = " + (double) (whole / coefficients)
                + " ; (double) " + whole + " / " + coefficients + " = " + (double) whole / coefficients);

        double penalty = absences > 3 ? (absences - 3) * 0.25 : 0;
        average -= penalty;
        // Bonus par bits : & isole chaque option ; += accumule.
        average += (options & LATIN) != 0 ? 0.5 : 0;
        average += (options & DELEGATE) != 0 ? 0.2 : 0;
        average = average > 20 ? 20 : average;
        double rounded = tenth(average);
        System.out.println("absences " + absences + " -> penalite " + penalty + " ; options " + Integer.toBinaryString(options)
                + " : " + ((options & DELEGATE) != 0 ? "delegue " : "") + ((options & SPORT) != 0 ? "sport " : "")
                + ((options & LATIN) != 0 ? "latin" : ""));
        System.out.println("moyenne finale : " + rounded + " (" + mention(rounded) + ")");

        double gap = tenth(rounded - CLASS_AVERAGE);
        System.out.println("ecart a la classe : " + (gap >= 0 ? "+" : "") + gap);
        // Lettre A (20) a E (0) : arithmetique de char, puis cast.
        char letter = (char) ('A' + (int) ((20 - rounded) / 4));
        System.out.println("lettre : " + letter + ", admis : " + (rounded >= 10 && absences < 10) + ", felicitations : "
                + (rounded >= 16 || rounded >= 14 && (options & DELEGATE) != 0));
    }
}
