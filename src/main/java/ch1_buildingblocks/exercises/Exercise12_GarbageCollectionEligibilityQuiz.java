package ch1_buildingblocks.exercises;

/**
 * EXERCICE 12 - Quiz "combien d'objets sont eligibles au ramasse-miettes ?" (niveau : examen OCP)
 * =========================================================================================================
 *
 * Pas de main() a lancer ici : ce quiz se fait a la main, un peu
 * differemment des autres. Le "ramasse-miettes" (garbage collector)
 * ne se declenche JAMAIS a un moment PRECIS et PREVISIBLE - on ne
 * peut donc pas "tester" son passage avec un simple assert. Le livre
 * recommande cette methode : pour chaque ligne marquee "// ICI ?",
 * DESSINE sur une feuille des boites (les objets) et des fleches
 * (les references qui pointent vers eux) - des qu'AUCUNE fleche ne
 * pointe VERS une boite (meme si CETTE boite pointe encore vers
 * d'autres), elle est eligible.
 *
 * -- La regle a garder en tete pour tout le quiz --
 *
 *   Un objet devient eligible des l'instant ou PLUS AUCUNE variable
 *   accessible ne pointe (directement OU indirectement) vers lui -
 *   PAS forcement quand une variable est explicitement mise a null :
 *   sortir de portee (fin de methode, fin de bloc) ou etre
 *   REMPLACEE par une autre valeur suffit tout autant.
 */
public class Exercise12_GarbageCollectionEligibilityQuiz {

    static class Widget {
        String name;

        Widget(String name) {
            this.name = name;
        }
    }

    static class Node {
        Node next;
    }

    // ------------------------------------------------------------------
    // Scenario A - reassignation simple
    //
    // Reponse : (a completer : combien d'objets eligibles a la ligne
    // "// ICI ?", et pourquoi)
    // ------------------------------------------------------------------
    static void scenarioA() {
        Widget a = new Widget("A");
        Widget b = new Widget("B");
        a = b; // ICI ?
    }

    // ------------------------------------------------------------------
    // Scenario B - un "ilot" de 2 objets qui se pointent l'un l'autre
    //
    // Reponse :
    // ------------------------------------------------------------------
    static void scenarioB() {
        Node n1 = new Node();
        Node n2 = new Node();
        n1.next = n2;
        n2.next = n1;
        n1 = null;
        n2 = null; // ICI ?
    }

    // ------------------------------------------------------------------
    // Scenario C - une variable locale qui sort de portee
    //
    // Reponse :
    // ------------------------------------------------------------------
    static void createTemporaryWidget() {
        Widget temp = new Widget("Temp");
    } // ICI (juste apres la fin de la methode) ?

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Scenario A : 1 objet eligible (le Widget "A"). a pointait
     *   vers "A", puis a ete REAFFECTEE vers "B" - plus AUCUNE
     *   variable ne pointe encore vers "A" (b, elle, pointe vers "B",
     *   pas vers "A") : "A" devient eligible A CET INSTANT PRECIS, pas
     *   besoin d'un null explicite.
     *
     * Scenario B : 2 objets eligibles (n1 ET n2, ENSEMBLE). Meme si
     *   n1.next pointe encore vers n2, et n2.next encore vers n1 (un
     *   "ilot" ferme sur lui-meme), PLUS AUCUNE variable ACCESSIBLE
     *   DEPUIS L'EXTERIEUR ne pointe vers L'UN OU L'AUTRE, une fois n1
     *   ET n2 mises a null - le ramasse-miettes sait detecter ces
     *   "ilots" de references circulaires et les nettoyer QUAND MEME,
     *   ce n'est PAS parce que 2 objets se pointent mutuellement
     *   qu'ils restent "vivants" pour toujours.
     *
     * Scenario C : 1 objet eligible (le Widget "Temp"). temp est une
     *   variable LOCALE (voir Exercise10) : elle sort de portee des
     *   la fin de createTemporaryWidget() - "Temp" perd alors sa SEULE
     *   reference, meme si aucun null n'a jamais ete ecrit nulle
     *   part dans le code.
     */
}
