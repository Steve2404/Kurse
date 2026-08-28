package classdesign.exercises;

/**
 * EXERCICE 11 - Quiz "ca compile ou pas ?" sur les regles des classes abstraites (niveau : examen OCP)
 * ==============================================================================================================
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
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   1. Une classe abstract ne peut JAMAIS etre instanciee
 *      directement.
 *   2. La 1ere classe CONCRETE d'une chaine d'heritage DOIT
 *      completer TOUTES les methodes abstraites herites - qu'elles
 *      viennent d'une classe abstract OU d'une interface.
 *   3. Une classe abstract, elle, N'A PAS cette obligation : elle
 *      peut laisser des methodes abstract non completees, et
 *      reporter le probleme a SA PROPRE premiere sous-classe
 *      concrete.
 *   4. abstract et final sont des mots-cles CONTRADICTOIRES :
 *      impossible de les combiner, ni sur une classe, ni sur une
 *      methode.
 */
public class Exercise11_AbstractClassRulesQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un essaie de creer directement un "formulaire
    // a moitie rempli" (voir Exercise10), en esperant s'en servir
    // TEL QUEL, sans jamais completer les cases manquantes.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // abstract static class Shape {
    //     abstract double area();
    // }
    // static void m() {
    //     Shape s = new Shape();
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : Circle est presentee comme une classe bien
    // CONCRETE (aucun mot-cle abstract devant), mais elle "oublie"
    // de completer area(), la seule case encore vide heritee de
    // Shape.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // abstract static class Shape {
    //     abstract double area();
    // }
    // static class Circle extends Shape {
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : le meme oubli, mais cette fois la methode abstraite
    // vient d'une INTERFACE (Flyer), pas d'une classe abstract - la
    // regle s'applique EXACTEMENT pareil dans les 2 cas.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // interface Flyer {
    //     void fly();
    // }
    // static class Bird implements Flyer {
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : RoundShape herite d'area() (toujours pas
    // completee), mais elle reste ELLE-MEME abstract - elle ne fait
    // que "faire passer le probleme" a SA PROPRE future sous-classe
    // concrete, sans jamais s'en occuper directement.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // abstract static class Shape {
    //     abstract double area();
    // }
    // abstract static class RoundShape extends Shape {
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : quelqu'un veut a la fois dire "cette classe est un
    // formulaire a moitie rempli" (abstract) ET "personne n'a le
    // droit d'en heriter pour la completer" (final) - 2 intentions
    // qui se contredisent frontalement.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // abstract final static class Shape {
    //     abstract double area();
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : meme contradiction, mais cette fois directement sur
    // LA METHODE abstraite elle-meme plutot que sur la classe
    // entiere.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // abstract static class Shape {
    //     abstract final double area();
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Une classe abstract ne peut JAMAIS
     *   etre instanciee directement, meme si elle ne contient QUE des
     *   methodes deja completes par ailleurs. Erreur : "Shape is
     *   abstract; cannot be instantiated".
     *
     * Bloc B : NE COMPILE PAS. Circle, PRESENTEE comme concrete,
     *   n'implemente pas area() : le compilateur exige alors soit de
     *   completer area(), soit de marquer Circle elle-meme abstract.
     *   Erreur : "Circle is not abstract and does not override
     *   abstract method area() in Shape".
     *
     * Bloc C : NE COMPILE PAS, pour EXACTEMENT la meme raison que le
     *   Bloc B - peu importe que la methode abstraite vienne d'une
     *   classe abstract ou d'une interface, la regle du "1er type
     *   concret doit tout completer" est identique dans les 2 cas.
     *
     * Bloc D : COMPILE. RoundShape reste elle-meme abstract : elle
     *   n'a AUCUNE obligation de completer area() - seule la 1ere
     *   classe VRAIMENT concrete de la chaine y sera forcee.
     *
     * Bloc E : NE COMPILE PAS. abstract et final sont
     *   INCOMPATIBLES sur une classe : l'un dit "obligatoirement
     *   incomplete, a etendre", l'autre dit "personne n'a le droit
     *   d'etendre". Erreur : "illegal combination of modifiers:
     *   abstract and final".
     *
     * Bloc F : NE COMPILE PAS, pour la MEME raison que le Bloc E,
     *   mais applique cette fois a une methode plutot qu'a une
     *   classe entiere - meme message d'erreur exact : "illegal
     *   combination of modifiers: abstract and final".
     */
}
