package ch1_buildingblocks.projects.p03_bootlog.solution;

/**
 * SOLUTION du projet 3 - une conception possible.
 */
public class BootLog {

    // Champ de classe jamais initialise : il vaut 0 au demarrage (valeur par defaut).
    static int step;
    static int created;

    // Chaque etape est numerotee : c'est ce numero qui PROUVE l'ordre d'execution.
    static void note(String message) {
        step = step + 1;
        System.out.println(step + ". " + message);
    }

    // Un initialiseur de champ peut appeler une methode : elle journalise, puis rend la valeur.
    static int logInt(String field, int value) {
        note(field + " = " + value);
        return value;
    }

    static String logText(String field, String value) {
        note(field + " = " + value);
        return value;
    }

    public static void main(String[] args) {
        System.out.println("--- 1er serveur ---");
        // var : le type (Server) est deduit de l'expression a droite ; il est fixe pour toujours.
        var first = new Server(50);
        System.out.println("--- 2e serveur ---");
        var second = new Server(10);
        System.out.println("--- portee ---");
        first.showScope();
        System.out.println("--- bilan ---");
        final int expected = 2;
        System.out.println(created + " serveurs crees (attendu " + expected + "), " + step + " etapes journalisees");
        // A partir d'ici, plus aucune variable ne designe le 1er serveur : il devient eligible au ramasse-miettes.
        first = second;
        System.out.println("first et second designent le serveur de " + first.maxUsers + " utilisateurs");
    }
}

class Server {
    // 1) Les champs et les blocs d'initialisation s'executent DANS L'ORDRE OU ILS SONT ECRITS...
    int port = BootLog.logInt("Server : champ port", 8080);

    {
        // name est declare PLUS BAS : l'ecrire seul serait une "illegal forward reference" ;
        // via this.name, c'est permis, et on lit sa valeur par defaut (null).
        BootLog.note("Server : bloc A (port = " + port + ", name = " + this.name + ")");
    }

    String name = BootLog.logText("Server : champ name", "alpha");

    // Une METHODE qui lit un champ pas encore initialise ne provoque pas d'erreur : elle lit la valeur par defaut.
    int early = readLate();
    int late = BootLog.logInt("Server : champ late", 42);

    {
        BootLog.note("Server : bloc B (early = " + early + ", late = " + late + ")");
    }

    int maxUsers;

    // 2) ... puis, seulement apres, le corps du constructeur.
    Server(int maxUsers) {
        BootLog.note("Server : constructeur debut (this.maxUsers = " + this.maxUsers + ", parametre maxUsers = " + maxUsers + ")");
        this.maxUsers = maxUsers;
        BootLog.created = BootLog.created + 1;
        BootLog.note("Server : constructeur fin (maxUsers = " + this.maxUsers + ", serveur n " + BootLog.created + ")");
    }

    int readLate() {
        BootLog.note("Server : lecture anticipee de late = " + late);
        return late;
    }

    void showScope() {
        // Une variable LOCALE de meme nom MASQUE le champ ; this.port designe toujours le champ.
        int port = 9090;
        BootLog.note("portee : port local = " + port + ", champ this.port = " + this.port);
        {
            // Variable d'un bloc : elle n'existe que jusqu'a l'accolade fermante.
            int backup = port + 1;
            BootLog.note("portee : dans le bloc, backup = " + backup);
        }
        String name = "local";
        BootLog.note("portee : name local = " + name + ", champ this.name = " + this.name);
    }
}
