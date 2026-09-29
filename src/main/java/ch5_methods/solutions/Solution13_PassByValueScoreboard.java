package ch5_methods.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise13_PassByValueScoreboard.
 */
public class Solution13_PassByValueScoreboard {

    public static class Player {
        public final String name;
        public int level = 1;

        public Player(String name) {
            this.name = name;
        }
    }

    public static int withBonus(int score, int bonus) {
        // Un primitif ne peut pas etre change dehors : on RENVOIE la nouvelle valeur.
        return score + bonus;
    }

    public static void addBonusToAll(int[] scores, int bonus) {
        // On modifie les cases du tableau partage ; un for-each ne modifierait que des copies.
        for (int i = 0; i < scores.length; i++) {
            scores[i] += bonus;
        }
    }

    public static void resetBroken(int[] scores) {
        // Reassigner le parametre ne touche que la copie de l'adresse : l'appelant ne voit rien.
        scores = new int[scores.length];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = 0;
        }
    }

    public static void resetFixed(int[] scores) {
        // On modifie le tableau recu (le meme que celui de l'appelant).
        Arrays.fill(scores, 0);
    }

    public static void swapFirstTwo(int[] scores) {
        // Echanger des CASES d'un tableau partage marche ; echanger deux int parametres, non.
        int tmp = scores[0];
        scores[0] = scores[1];
        scores[1] = tmp;
    }

    public static void promote(Player player) {
        // Modification de l'objet : visible par tous ceux qui ont son adresse.
        player.level++;
    }

    public static void replaceBroken(Player player) {
        // La copie pointe vers un NOUVEAU joueur ; celui de l'appelant n'est pas touche.
        player = new Player("Nouveau");
        player.level = 99;
    }

    public static void renameInPlace(StringBuilder name, String newName) {
        // StringBuilder est mutable : on le vide puis on le remplit.
        name.setLength(0);
        name.append(newName);
    }

    public static void shoutBroken(String text) {
        // String immuable + variable copiee : l'appelant garde son texte.
        text = text.toUpperCase();
    }
}
