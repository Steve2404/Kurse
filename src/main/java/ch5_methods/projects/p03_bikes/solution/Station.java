package ch5_methods.projects.p03_bikes.solution;

/**
 * SOLUTION - une station : des membres d'INSTANCE (un par station) et un registre static (un pour toutes).
 */
public class Station {

    // static : le registre et le compteur sont partages par toutes les stations.
    private static final Station[] ALL = new Station[10];
    private static int created;

    // final d'instance : affecte une seule fois, ici dans le bloc d'initialisation d'instance.
    final int id;
    String name;
    int bikes;
    int capacity;

    // Bloc d'instance : il s'execute a CHAQUE new Station(), avant le retour de new.
    {
        id = created++;
        System.out.println("[objet] station #" + id);
    }

    // Fabrique static : le seul moyen de creer une station, et elle s'enregistre au passage.
    static Station create(String name, int bikes, int capacity) {
        Station s = new Station();
        s.name = name;
        s.bikes = bikes;
        s.capacity = capacity;
        ALL[s.id] = s;
        return s;
    }

    static int count() {
        return created;
    }

    static Station get(int id) {
        return ALL[id];
    }

    // La station la plus proche (par la route) qui a un velo (needBike) ou une place libre.
    static Station nearest(Station from, boolean needBike) {
        Station best = null;
        for (int i = 0; i < created; i++) {
            Station s = ALL[i];
            boolean ok = needBike ? s.bikes > 0 : s.bikes < s.capacity;
            if (s != from && ok && (best == null || Network.km(from.id, s.id) < Network.km(from.id, best.id))) {
                best = s;
            }
        }
        return best;
    }

    static String snapshot() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < created; i++) {
            sb.append(i == 0 ? "" : " ").append(ALL[i].name).append('=').append(ALL[i].bikes).append('/').append(ALL[i].capacity);
        }
        return sb.toString();
    }

    // Une methode d'INSTANCE peut lire les membres static ; l'inverse demande une reference.
    String describe() {
        return name + " (#" + id + " sur " + created + ")";
    }
}
