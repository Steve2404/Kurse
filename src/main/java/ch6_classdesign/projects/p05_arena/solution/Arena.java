package ch6_classdesign.projects.p05_arena.solution;

import ch6_classdesign.projects.p05_arena.Data;

/**
 * SOLUTION du projet 5 - l'arene.
 */
public class Arena {

    static Fighter create(String line) {
        String[] p = line.split(" ");
        int hp = Integer.parseInt(p[2]);
        int atk = Integer.parseInt(p[3]);
        int spd = Integer.parseInt(p[4]);
        return switch (p[0]) {
            case "W" -> new Warrior(p[1], hp, atk, spd);
            case "M" -> new Mage(p[1], hp, atk, spd);
            case "T" -> new Tank(p[1], hp, atk, spd);
            default -> new Healer(p[1], hp, atk, spd);
        };
    }

    static Fighter[] team(String[] lines) {
        Fighter[] t = new Fighter[lines.length];
        for (int i = 0; i < lines.length; i++) {
            t[i] = create(lines[i]);
        }
        return t;
    }

    static boolean alive(Fighter[] team) {
        for (Fighter f : team) {
            if (f.isAlive()) {
                return true;
            }
        }
        return false;
    }

    // Un combat complet ; rend le nom de l'equipe gagnante.
    static String battle(Fighter[] a, Fighter[] b, boolean verbose) {
        Fighter[] all = new Fighter[a.length + b.length];
        System.arraycopy(a, 0, all, 0, a.length);
        System.arraycopy(b, 0, all, a.length, b.length);
        // Ordre d'initiative : vitesse decroissante ; tri par insertion STABLE (A avant B a egalite).
        for (int i = 1; i < all.length; i++) {
            Fighter key = all[i];
            int j = i - 1;
            while (j >= 0 && all[j].getSpeed() < key.getSpeed()) {
                all[j + 1] = all[j];
                j--;
            }
            all[j + 1] = key;
        }
        int round = 1;
        while (alive(a) && alive(b) && round <= Data.MAX_ROUNDS) {
            for (Fighter f : all) {
                if (!f.isAlive() || !alive(a) || !alive(b)) {
                    continue;
                }
                boolean inA = false;
                for (Fighter x : a) {
                    inA |= x == f;
                }
                String log = inA ? f.act(a, b) : f.act(b, a);
                if (verbose) {
                    System.out.println("T" + round + " " + log);
                }
            }
            round++;
        }
        return !alive(b) ? "A" : !alive(a) ? "B" : "nul";
    }

    public static void main(String[] args) {
        Fighter[] a = team(Data.TEAM_A);
        Fighter[] b = team(Data.TEAM_B);
        StringBuilder order = new StringBuilder("equipes :");
        for (Fighter f : a) {
            order.append(' ').append(f);
        }
        order.append(" VS");
        for (Fighter f : b) {
            order.append(' ').append(f);
        }
        System.out.println(order);
        // Copies AVANT le combat : grace a la covariance, chaque copy() rend deja le bon type.
        Fighter[] a2 = new Fighter[a.length];
        Fighter[] b2 = new Fighter[b.length];
        for (int i = 0; i < a.length; i++) {
            a2[i] = a[i].copy();
        }
        for (int i = 0; i < b.length; i++) {
            b2[i] = b[i].copy();
        }
        Warrior conanCopy = new Warrior("Conan", 120, 18, 6).copy();   // pas de cast : copy() rend un Warrior
        System.out.println("vainqueur : equipe " + battle(a, b, true));
        StringBuilder after = new StringBuilder("apres :");
        for (Fighter f : a) {
            after.append(' ').append(f.status());
        }
        System.out.println(after);
        System.out.println("revanche avec les copies, B en premier : equipe " + battle(b2, a2, false) + " ; les copies partaient a "
                + a2[0].getMaxHp() + " pv ; " + conanCopy);
    }
}
