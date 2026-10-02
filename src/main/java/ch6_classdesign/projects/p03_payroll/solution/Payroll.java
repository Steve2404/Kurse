package ch6_classdesign.projects.p03_payroll.solution;

import ch6_classdesign.projects.p03_payroll.Data;

/**
 * SOLUTION du projet 3 - la paie et l'organigramme.
 */
public class Payroll {

    // byId[i] = la personne d'identifiant i (case 0 inutilisee).
    private static Employee[] byId;

    public static void main(String[] args) {
        byId = new Employee[Data.STAFF.length + 1];
        for (String line : Data.STAFF) {
            String[] p = line.split(" ");
            int id = Integer.parseInt(p[0]);
            long base = Long.parseLong(p[3]);
            Employee e;
            if (p[1].equals("M")) {
                e = new Manager(id, p[2], base);
            } else if (p[1].equals("E")) {
                e = new Engineer(id, p[2], base, Integer.parseInt(p[5]));
            } else {
                e = new Intern(id, p[2], base);
            }
            e.setManagerId(Integer.parseInt(p[4]));
            byId[id] = e;
        }
        // Les primes des managers dependent du nombre de personnes encadrees directement.
        for (int i = 1; i < byId.length; i++) {
            if (byId[i] instanceof Manager m) {
                m.setReports(children(i).length);
            }
        }
        print(1, 0);
        System.out.println("masse salariale " + Employee.money(cost(1)) + " ; equipe Bruno " + Employee.money(cost(2)) + " ; equipe Chloe "
                + Employee.money(cost(3)));
        StringBuilder common = new StringBuilder("manager commun :");
        for (String pair : Data.PAIRS) {
            String[] names = pair.split(" ");
            common.append(' ').append(names[0]).append('+').append(names[1]).append('=').append(byId[lca(find(names[0]), find(names[1]))].getName());
        }
        System.out.println(common);
        int deepest = 1;
        for (int i = 1; i < byId.length; i++) {
            if (depth(i) > depth(deepest)) {
                deepest = i;
            }
        }
        StringBuilder chain = new StringBuilder();
        for (int i = deepest; i != 0; i = byId[i].getManagerId()) {
            chain.insert(0, (chain.length() == 0 ? "" : " > ")).insert(0, byId[i].getName());
        }
        System.out.println("plus longue chaine : " + chain + " (" + (depth(deepest) + 1) + " niveaux)");

        // Pas de cast (chapitre 7) : le pattern matching du chapitre 3 donne une reference de type Manager.
        if (byId[8] instanceof Manager hugo) {
            Employee asEmployee = hugo;
            System.out.println("champ masque : " + hugo.type + " / " + asEmployee.type + " / " + hugo.typeSeenFromInside());
            System.out.println("static masquee : " + Manager.category() + " / " + Employee.category() + " ; redefinie : " + Employee.money(asEmployee.pay())
                    + " = " + Employee.money(hugo.pay()));
            System.out.println("prive redeclare : hausse de Hugo " + Employee.money(hugo.raise()) + " (3 %, pas 10 %) ; badge final " + asEmployee.badge());
        }
    }

    static int find(String name) {
        for (int i = 1; i < byId.length; i++) {
            if (byId[i].getName().equals(name)) {
                return i;
            }
        }
        return 0;
    }

    // Les subordonnes directs de id, dans l'ordre des identifiants.
    static int[] children(int id) {
        int[] tmp = new int[byId.length];
        int n = 0;
        for (int i = 1; i < byId.length; i++) {
            if (byId[i].getManagerId() == id) {
                tmp[n++] = i;
            }
        }
        int[] result = new int[n];
        System.arraycopy(tmp, 0, result, 0, n);
        return result;
    }

    // Parcours en profondeur : le manager, puis chacun de ses subordonnes, un cran plus a droite.
    static void print(int id, int level) {
        Employee e = byId[id];
        System.out.println("  ".repeat(level) + e.badge() + " [" + e.role() + "] " + Employee.money(e.pay()));
        for (int child : children(id)) {
            print(child, level + 1);
        }
    }

    // Cout d'un sous-arbre : son salaire + le cout de chaque sous-arbre enfant.
    static long cost(int id) {
        long total = byId[id].pay();
        for (int child : children(id)) {
            total += cost(child);
        }
        return total;
    }

    static int depth(int id) {
        int manager = byId[id].getManagerId();
        return manager == 0 ? 0 : 1 + depth(manager);
    }

    // Plus proche ancetre commun : on remonte le plus profond jusqu'au meme niveau, puis les deux ensemble.
    static int lca(int a, int b) {
        while (depth(a) > depth(b)) {
            a = byId[a].getManagerId();
        }
        while (depth(b) > depth(a)) {
            b = byId[b].getManagerId();
        }
        while (a != b) {
            a = byId[a].getManagerId();
            b = byId[b].getManagerId();
        }
        return a;
    }
}
