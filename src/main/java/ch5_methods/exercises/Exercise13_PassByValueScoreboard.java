package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 13 - Passage par valeur en situation : un tableau des scores ou chaque methode doit choisir sa technique (niveau : avance)
 * ===============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Java donne TOUJOURS a une methode une COPIE de ce qu'on lui passe :
 *   - un int : une copie du nombre. Changer la copie ne change rien dehors.
 *   - un objet (tableau, StringBuilder, Player) : une copie de l'ADRESSE.
 *     Avec cette adresse, on peut MODIFIER l'objet (tout le monde le voit),
 *     mais faire pointer la copie ailleurs (p = new ...) ne change rien dehors.
 *
 * Pour chaque TODO, il faut donc choisir : RENDRE la nouvelle valeur, ou
 * MODIFIER l'objet recu. main() verifie ce que voit l'APPELANT.
 *
 *
 * ==================================================================
 * TODO 1 : withBonus(score, bonus)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un int ne peut pas etre change par la methode : on RENVOIE le
 * nouveau score, et c'est l'appelant qui le range (score = withBonus(...)).
 *
 * -- Le plan --
 *
 *   1. Rendre score + bonus.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : addBonusToAll(scores, bonus)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le tableau, lui, est un objet : en modifiant ses cases, l'appelant
 * voit le changement. Rien a rendre.
 *
 * -- Le plan --
 *
 *   1. Pour chaque index : scores[i] += bonus. (Un for-each ne marcherait PAS :
 *      sa variable est une copie de la case.)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : resetBroken(scores)    [le piege : doit NE RIEN changer dehors]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ecris exactement : scores = new int[scores.length]; puis mets 0 dans
 * chaque case du NOUVEAU tableau. main() verifie que le tableau de
 * l'appelant n'a PAS change : la copie de l'adresse pointe ailleurs,
 * l'original est intact.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : resetFixed(scores)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Arrays.fill(scores, 0) : on modifie le tableau recu, pas la variable.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : swapFirstTwo(scores)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une methode swap(int a, int b) ne peut RIEN echanger chez l'appelant
 * (des copies). Mais dans un tableau, on echange des CASES : ca marche.
 *
 * -- Le plan --
 *
 *   1. tmp = scores[0] ; scores[0] = scores[1] ; scores[1] = tmp.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : promote(player)    et    TODO 7 : replaceBroken(player)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * promote : player.level++ -> l'appelant voit le nouveau niveau (on
 * modifie l'objet). replaceBroken : ecris player = new Player("Nouveau") ;
 * player.level = 99 ; -> l'appelant ne voit RIEN (on a change la copie
 * de l'adresse, pas son joueur).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : renameInPlace(name, newName)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un StringBuilder se modifie : on le vide (setLength(0)) puis on
 * ajoute le nouveau nom. L'appelant voit le nouveau nom.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 9 : shoutBroken(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ecris text = text.toUpperCase(); : un String est immuable ET la
 * variable est une copie. L'appelant garde son texte en minuscules.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Question a se poser a chaque fois : est-ce que je MODIFIE l'objet, ou est-ce que je
 *     REASSIGNE la variable ? Seule la modification est visible dehors.
 */
public class Exercise13_PassByValueScoreboard {

    public static class Player {
        public final String name;
        public int level = 1;

        public Player(String name) {
            this.name = name;
        }
    }

    public static int withBonus(int score, int bonus) {
        throw new UnsupportedOperationException("TODO 1 : implementer withBonus()");
    }

    public static void addBonusToAll(int[] scores, int bonus) {
        throw new UnsupportedOperationException("TODO 2 : implementer addBonusToAll()");
    }

    public static void resetBroken(int[] scores) {
        throw new UnsupportedOperationException("TODO 3 : implementer resetBroken()");
    }

    public static void resetFixed(int[] scores) {
        throw new UnsupportedOperationException("TODO 4 : implementer resetFixed()");
    }

    public static void swapFirstTwo(int[] scores) {
        throw new UnsupportedOperationException("TODO 5 : implementer swapFirstTwo()");
    }

    public static void promote(Player player) {
        throw new UnsupportedOperationException("TODO 6 : implementer promote()");
    }

    public static void replaceBroken(Player player) {
        throw new UnsupportedOperationException("TODO 7 : implementer replaceBroken()");
    }

    public static void renameInPlace(StringBuilder name, String newName) {
        throw new UnsupportedOperationException("TODO 8 : implementer renameInPlace()");
    }

    public static void shoutBroken(String text) {
        throw new UnsupportedOperationException("TODO 9 : implementer shoutBroken()");
    }

    public static void main(String[] args) {
        int score = 10;
        score = withBonus(score, 5);
        ExerciseChecker.check("withBonus : l'appelant range le resultat -> 15", score == 15);

        int[] scores = {10, 20, 30};
        addBonusToAll(scores, 1);
        ExerciseChecker.check("addBonusToAll : le tableau de l'appelant a change", Arrays.equals(scores, new int[] {11, 21, 31}));

        resetBroken(scores);
        ExerciseChecker.check("resetBroken : RIEN n'a change chez l'appelant", Arrays.equals(scores, new int[] {11, 21, 31}));

        swapFirstTwo(scores);
        ExerciseChecker.check("swapFirstTwo : {21, 11, 31}", Arrays.equals(scores, new int[] {21, 11, 31}));

        resetFixed(scores);
        ExerciseChecker.check("resetFixed : {0, 0, 0}", Arrays.equals(scores, new int[] {0, 0, 0}));

        Player ada = new Player("Ada");
        promote(ada);
        replaceBroken(ada);
        ExerciseChecker.check("promote visible (niveau 2), replaceBroken invisible (toujours Ada)",
                ada.level == 2 && ada.name.equals("Ada"));

        StringBuilder name = new StringBuilder("Linus");
        renameInPlace(name, "Grace");
        ExerciseChecker.check("renameInPlace : \"Grace\"", name.toString().equals("Grace"));

        String text = "bonjour";
        shoutBroken(text);
        ExerciseChecker.check("shoutBroken : l'appelant a toujours \"bonjour\"", text.equals("bonjour"));

        ExerciseChecker.summary();
    }
}
