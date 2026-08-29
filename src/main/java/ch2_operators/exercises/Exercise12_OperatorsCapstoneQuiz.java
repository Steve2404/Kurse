package ch2_operators.exercises;

/**
 * EXERCICE 12 (CAPSTONE) - Quiz "ca compile ou pas ?" : chaque operateur EXIGE des types COMPATIBLES (niveau : examen OCP)
 * =================================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les autres quiz du chapitre. Pour chaque bloc, c'est TOI la boite
 * magique : lis d'abord l'histoire imagee, essaie de repondre sur une
 * feuille (compile / ne compile pas + pourquoi), PUIS decommente le
 * bloc et essaie de COMPILER (mvn compile ou votre IDE) pour
 * verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * Ce quiz couvre volontairement des pieges DIFFERENTS de ceux deja
 * testes en profondeur dans les exercices precedents - il recapitule
 * les autres phrases-cles du Summary/Exam Essentials du chapitre :
 * "il est important de remarquer quand un operateur et ses operandes
 * ne correspondent pas".
 */
public class Exercise12_OperatorsCapstoneQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un additionne un boolean et un int, en
    // pensant que true "vaut" 1 et false "vaut" 0 (comme dans
    // d'autres langages) - Java, lui, ne convertit JAMAIS un boolean
    // en nombre.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA() {
    //     boolean b = true;
    //     int x = b + 1;
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un compare directement un int a un boolean
    // avec ==, en esperant que "x == true" veuille dire "x est
    // different de 0".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB() {
    //     int x = 5;
    //     boolean same = x == true;
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un compare un String et un int avec == - 2
    // types de reference/primitif totalement SANS RAPPORT l'un avec
    // l'autre, meme si le String "contient" des chiffres.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC() {
    //     String s = "hi";
    //     boolean same = s == 5;
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : le meme genre de comparaison, mais cette fois entre
    // un String et null - null est un litteral special, valide pour
    // N'IMPORTE QUEL type de reference (jamais pour un primitif).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useD() {
    //     String s = "hi";
    //     boolean isNull = s == null;
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : rappel de l'Exercise05 - un byte auquel on tente
    // d'ajouter un int SANS l'operateur COMPOSE (+=), juste avec un
    // + et un = separes.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useE() {
    //     byte b = 10;
    //     b = b + 5;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. + n'accepte JAMAIS un boolean comme
     *   operande arithmetique - aucune conversion automatique
     *   boolean -> nombre n'existe en Java. Erreur : "bad operand
     *   types for binary operator '+'".
     *
     * Bloc B : NE COMPILE PAS. == exige des types COMPARABLES entre
     *   eux (numeriques entre eux, ou boolean entre eux, ou objets
     *   compatibles entre eux) - un int et un boolean ne le sont
     *   jamais. Erreur : "incomparable types: int and boolean".
     *
     * Bloc C : NE COMPILE PAS, pour la meme raison de fond que le
     *   Bloc B : String (un type de reference) et int (un primitif)
     *   ne sont jamais comparables avec ==. Erreur : "bad operand
     *   types for binary operator '=='".
     *
     * Bloc D : COMPILE. null est valide face a N'IMPORTE QUEL type de
     *   reference (String en fait partie) - c'est meme la facon
     *   STANDARD de tester si une reference est vide.
     *
     * Bloc E : NE COMPILE PAS (voir Exercise05 pour l'explication
     *   complete) : b + 5 promeut en int (voir Exercise03), et un
     *   int ne rentre jamais automatiquement dans un byte via un
     *   simple "=" - seul l'operateur COMPOSE b += 5 cache le cast
     *   necessaire.
     */
}
