package ch1_buildingblocks.exercises;

/**
 * EXERCICE 8 - Quiz "ca compile ou pas ?" sur les identifiants (niveau : examen OCP)
 * ==========================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main. Pour
 * chaque bloc, c'est TOI la boite magique : lis d'abord l'histoire
 * imagee, essaie de repondre sur une feuille (compile / ne compile
 * pas + pourquoi), PUIS decommente le bloc et essaie de COMPILER (mvn
 * compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   Un identifiant (nom de variable, de methode, de classe...) peut
 *   contenir des lettres, des chiffres, des symboles MONETAIRES ($,
 *   €...) et _ - MAIS il ne peut JAMAIS COMMENCER par un chiffre. Et,
 *   depuis Java 9, _ TOUT SEUL (un unique caractere underscore, sans
 *   rien d'autre) n'est PLUS un identifiant valide du tout - c'est
 *   devenu un mot-cle reserve.
 */
public class Exercise08_IdentifiersQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : 2 variables, l'une nommee avec un symbole $ devant
    // (comme pour marquer "c'est de l'argent"), l'autre commencant
    // par un _ (une convention frequente pour marquer un champ
    // "prive" dans d'autres langages).
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA() {
    //     int $price = 5;
    //     int _value = 10;
    //     System.out.println($price + _value);
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un nomme une variable "1value", en pensant
    // que "c'est juste un nom qui COMMENCE par un chiffre", pas un
    // NOMBRE a proprement parler.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB() {
    //     int 1value = 5;
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un nomme une variable "_" TOUT SEUL (un
    // unique caractere), une habitude prise dans d'autres langages
    // pour dire "cette valeur, je m'en fiche, je ne vais jamais la
    // relire".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC() {
    //     int _ = 5;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : COMPILE. $ et _ sont TOUS LES DEUX des caracteres
     *   valides pour COMMENCER un identifiant (au meme titre qu'une
     *   lettre) - aucun probleme ici.
     *
     * Bloc B : NE COMPILE PAS. Un identifiant ne peut JAMAIS commencer
     *   par un chiffre - Java lit "1" comme un litteral numerique a
     *   part entiere, PUIS ne sait plus quoi faire de "value" juste
     *   apres. Erreur (un peu indirecte) : "not a statement" / "';'
     *   expected" - le compilateur ne "voit" meme pas ca comme une
     *   tentative de nommer une variable.
     *
     * Bloc C : NE COMPILE PAS. Depuis Java 9, _ TOUT SEUL est devenu
     *   un MOT-CLE RESERVE (reserve pour un futur usage du langage,
     *   comme les "unnamed variables" apparues plus tard) - il n'est
     *   PLUS utilisable comme identifiant, meme si _value (Bloc A)
     *   ou __ (double underscore) restent, eux, parfaitement valides.
     *   Erreur : "as of release 9, '_' is a keyword, and may not be
     *   used as an identifier".
     */
}
