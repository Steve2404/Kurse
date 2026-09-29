package ch5_methods.drills.solutions;

import ch5_methods.drills.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.drills.exercises.Drill03_PassByValueAndBoxing.
 */
public class SolutionDrill03_PassByValueAndBoxing {

    public static int doubled(int x) {
        // Un primitif : on RENVOIE, l'appelant range le resultat.
        return x * 2;
    }

    public static void doubleAll(int[] values) {
        // On modifie les cases du tableau partage.
        for (int i = 0; i < values.length; i++) {
            values[i] *= 2;
        }
    }

    public static void appendBang(StringBuilder sb) {
        // Modifier l'objet recu : l'appelant le voit.
        sb.append('!');
    }

    public static void reassignBroken(StringBuilder sb) {
        // Reassigner la copie de l'adresse : l'appelant ne voit rien.
        sb = new StringBuilder("X");
    }

    public static void swapCells(int[] values, int i, int j) {
        // Echanger deux cases d'un tableau partage.
        int tmp = values[i];
        values[i] = values[j];
        values[j] = tmp;
    }

    public static int sumBonuses() {
        // Filtrer null avant de deballer.
        int sum = 0;
        for (Integer b : Team.BONUS) {
            if (b != null) {
                sum += b;
            }
        }
        return sum;
    }

    public static int bonusOrZero(int index) {
        // Le deballage n'a lieu que dans la branche choisie, jamais sur null.
        Integer bonus = Team.BONUS[index];
        return bonus == null ? 0 : bonus;
    }

    public static boolean bigEqualsByValue() {
        // equals compare les valeurs.
        Integer a = 1000;
        Integer b = 1000;
        return a.equals(b);
    }

    public static boolean bigEqualsByIdentity() {
        // == compare les adresses : deux objets differents hors du cache -128..127.
        Integer a = 1000;
        Integer b = 1000;
        return a == b;
    }

    public static List<Integer> removeSeven() {
        // Integer.valueOf force remove(Object) : on enleve la VALEUR 7.
        List<Integer> list = new ArrayList<>(List.of(12, 7, 15));
        list.remove(Integer.valueOf(7));
        return list;
    }

    public static List<Integer> removeFirst() {
        // Un int choisit remove(int index).
        List<Integer> list = new ArrayList<>(List.of(12, 7, 15));
        list.remove(0);
        return list;
    }

    public static boolean longEqualsInt() {
        // 7 est boxe en Integer : un Long n'est jamais egal a un Integer.
        return Long.valueOf(7).equals(7);
    }

    public static String unboxNull() {
        // Deballer null -> NullPointerException.
        try {
            Integer n = null;
            int x = n;
            return "valeur " + x;
        } catch (NullPointerException e) {
            return "NullPointerException";
        }
    }

    public static int parsed(String text) {
        // parseInt rend un int (valueOf rendrait un Integer).
        return Integer.parseInt(text);
    }
}
