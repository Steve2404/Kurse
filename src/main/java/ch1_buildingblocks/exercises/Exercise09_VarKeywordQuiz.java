package ch1_buildingblocks.exercises;

/**
 * EXERCICE 9 - Quiz "ca compile ou pas ?" sur var : le TYPE se fige a la compilation, pas la VALEUR (niveau : examen OCP)
 * ====================================================================================================================================
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
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * var n'est PAS "un type flou qui accepte n'importe quoi" - c'est
 * juste un moyen de ne pas RE-ECRIRE le type, que le compilateur
 * DEVINE UNE SEULE FOIS, a la compilation, a partir de la valeur
 * d'initialisation - et ce type devine reste FIGE pour toujours
 * ensuite, exactement comme si on l'avait ecrit explicitement.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   var EXIGE une initialisation SUR LA MEME LIGNE que sa
 *   declaration (impossible de "deviner" un type a partir de rien).
 *   var ne peut PAS etre initialisee avec null TOUTE SEULE (null ne
 *   donne AUCUN indice sur le VRAI type voulu). var est INTERDITE
 *   dans une declaration groupee (plusieurs variables separees par
 *   une virgule sur la meme ligne).
 */
public class Exercise09_VarKeywordQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : x est declaree var, initialisee avec un int (5) -
    // puis, plus loin, on essaie de lui donner un String, en pensant
    // que "var" veut dire "n'importe quel type, a tout moment".
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA() {
    //     var x = 5;
    //     x = "hello";
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un initialise une var directement avec null,
    // en pensant "je preciserai le type plus tard, ou Java le
    // devinera d'un usage futur".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB() {
    //     var x = null;
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : var utilisee pour declarer 2 variables d'un coup,
    // separees par une virgule - exactement comme au Bloc B de
    // l'Exercise07, mais avec var au lieu d'un type explicite.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC() {
    //     var a = 1, b = 2;
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : var declaree SANS aucune valeur d'initialisation,
    // avec l'idee de l'affecter "juste apres", sur la ligne suivante.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useD() {
    //     var x;
    //     x = 5;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Le type de x a ete FIGE en int des sa
     *   declaration ("var x = 5" equivaut a "int x = 5") - le
     *   reassigner avec un String est aussi illegal que "int x =
     *   5; x = "hello";" l'aurait ete. Erreur : "incompatible types:
     *   String cannot be converted to int".
     *
     * Bloc B : NE COMPILE PAS. null ne donne AUCUN indice sur le
     *   VRAI type voulu (contrairement a "String x = null;", ou LE
     *   TYPE, lui, est deja explicitement ecrit) - le compilateur ne
     *   peut RIEN deviner ici. Erreur : "cannot infer type for local
     *   variable x".
     *
     * Bloc C : NE COMPILE PAS. var est explicitement INTERDITE dans
     *   une declaration groupee, meme si les 2 variables auraient
     *   fini par avoir le meme type devine (int). Erreur : "'var' is
     *   not allowed in a compound declaration".
     *
     * Bloc D : NE COMPILE PAS. Sans AUCUNE valeur sur la ligne de
     *   declaration, le compilateur n'a RIEN a partir de quoi
     *   deviner un type - contrairement a une variable normalement
     *   typee (int x;, valide, voir Exercise07), var EXIGE toujours
     *   une initialisation IMMEDIATE. Erreur : "cannot infer type for
     *   local variable x" (avec la precision "cannot use 'var' on
     *   variable without initializer").
     */
}
