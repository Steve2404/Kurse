package ch2_operators.projects.p02_permissions.solution;

/**
 * SOLUTION du projet 2 - une conception possible.
 * Un mode Unix tient sur 9 bits : rwx (proprietaire) rwx (groupe) rwx (autres).
 */
public class Permissions {

    static final int OWNER_READ = 1 << 8;   // 0400 : les masques se construisent par decalage
    static final int OWNER_WRITE = 1 << 7;  // 0200
    static final int OTHERS_EXEC = 1;       // 0001
    static final int OTHERS_READ = 1 << 2;  // 0004

    // Un bit, une lettre : & isole le bit, != 0 le transforme en booleen, le ternaire choisit la lettre.
    static String triplet(int bits) {
        return "" + ((bits & 4) != 0 ? 'r' : '-') + ((bits & 2) != 0 ? 'w' : '-') + ((bits & 1) != 0 ? 'x' : '-');
    }

    // >> 6 ramene les bits du proprietaire en bas ; & 7 (0b111) ne garde que 3 bits.
    static String symbolic(int mode) {
        return triplet(mode >> 6 & 7) + triplet(mode >> 3 & 7) + triplet(mode & 7);
    }

    static String show(String label, int mode) {
        return label + " : " + Integer.toOctalString(mode) + " " + symbolic(mode);
    }

    public static void main(String[] args) {
        // decode comprend le 0 initial : "0754" est lu en OCTAL.
        int mode = Integer.decode(args[0]);
        int umask = Integer.decode(args[1]);
        int groups = Integer.decode(args[2]);
        int fileGroup = Integer.parseInt(args[3]);
        boolean owner = Boolean.parseBoolean(args[4]);

        System.out.println(show("mode", mode) + " (decimal " + mode + ")");
        // Nouveau fichier : 0666 prive des bits du umask (& ~ : "et pas").
        System.out.println(show("nouveau fichier", 0666 & ~umask));
        System.out.println(show("nouveau dossier", 0777 & ~umask));
        System.out.println(show("+x autres", mode | OTHERS_EXEC));
        System.out.println(show("-w proprietaire", mode & ~OWNER_WRITE));
        System.out.println(show("bascule lecture autres", mode ^ OTHERS_READ));
        System.out.println(show("bascule deux fois", mode ^ OTHERS_READ ^ OTHERS_READ));
        System.out.println("proprietaire peut lire : " + ((mode & OWNER_READ) != 0) + ", ecrire : " + ((mode & OWNER_WRITE) != 0));

        // Appartenance a un groupe : le bit numero fileGroup de l'ensemble des groupes.
        boolean inGroup = (groups & 1 << fileGroup) != 0;
        int rights = owner ? mode >>> 6 & 7 : inGroup ? mode >> 3 & 7 : mode & 7;
        String who = owner ? "proprietaire" : inGroup ? "groupe" : "autres";
        System.out.println("groupes " + Integer.toBinaryString(groups) + ", groupe du fichier " + fileGroup
                + " -> membre : " + inGroup);
        System.out.println("acces accorde en tant que " + who + " : " + triplet(rights)
                + ", ecriture " + ((rights & 2) != 0 ? "autorisee" : "refusee"));
        // ~ sur un int retourne TOUS les 32 bits : -mode - 1.
        System.out.println("~mode = " + ~mode + ", ~mode & 0777 = " + Integer.toOctalString(~mode & 0777));
        // >> garde le signe, >>> remplit de zeros.
        System.out.println("-16 >> 2 = " + (-16 >> 2) + ", -16 >>> 28 = " + (-16 >>> 28));
    }
}
