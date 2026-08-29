package ch1_buildingblocks.exercises;

/**
 * EXERCICE 13 (CAPSTONE) - Quiz "ca compile ou pas ?" : recapitulatif des pieges du chapitre (niveau : examen OCP)
 * ========================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les autres quiz du chapitre. Pour chaque bloc, c'est TOI la boite
 * magique : lis d'abord l'histoire imagee, essaie de repondre sur une
 * feuille (compile / ne compile pas + pourquoi), PUIS decommente le
 * bloc dans un fichier .java NOMME EXACTEMENT comme indique (le nom
 * du fichier fait partie du piege pour les blocs A/B/C !) et essaie
 * de COMPILER (javac) pour verifier.
 *
 * Ce quiz couvre volontairement des pieges DIFFERENTS de ceux deja
 * testes en profondeur dans les exercices precedents - il recapitule
 * les autres phrases-cles du Summary/Exam Essentials du chapitre.
 */
public class Exercise13_BuildingBlocksCapstoneQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : un fichier nomme "Wrong.java" contient une classe
    // public, mais nommee "RightName" - un nom DIFFERENT du fichier
    // qui la contient.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // Fichier a creer : Wrong.java
    // ------------------------------------------------------------------
    // public class RightName {
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : un fichier contient 2 classes top-level, TOUTES LES
    // DEUX marquees public.
    // Fichier a creer : TwoPublic.java
    // ------------------------------------------------------------------
    // public class TwoPublic {
    // }
    // public class AlsoPublic {
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : le meme genre de fichier, mais cette fois UNE SEULE
    // des 2 classes est public (l'autre n'a AUCUN modificateur
    // d'acces du tout).
    // Fichier a creer : MultiClass.java
    // ------------------------------------------------------------------
    // public class MultiClass {
    // }
    // class Helper {
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : une methode qui UTILISE un champ declare PLUS LOIN
    // dans le MEME fichier, apres elle - en pensant que "l'ordre de
    // lecture" du fichier a une importance, comme dans un script
    // execute ligne par ligne.
    // ------------------------------------------------------------------
    // static class OrderTest {
    //     void useField() {
    //         System.out.println(value);
    //     }
    //     int value = 42;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Une classe PUBLIC doit TOUJOURS porter
     *   EXACTEMENT le meme nom que le fichier .java qui la contient
     *   (a la casse pres). Erreur : "class RightName is public,
     *   should be declared in a file named RightName.java".
     *
     * Bloc B : NE COMPILE PAS. Un fichier .java ne peut contenir
     *   QU'UNE SEULE classe top-level public - la 2eme, elle, viole
     *   automatiquement la meme regle que le Bloc A (elle ne peut
     *   PAS, elle non plus, avoir le nom du fichier, deja pris par la
     *   1ere).
     *
     * Bloc C : COMPILE. Un fichier peut contenir PLUSIEURS classes
     *   top-level, TANT QU'UNE SEULE est public (et qu'elle porte le
     *   nom du fichier) - les autres, sans modificateur (acces
     *   "package"), n'ont AUCUNE contrainte de nommage face au
     *   fichier.
     *
     * Bloc D : COMPILE. Les champs et les methodes d'une classe
     *   peuvent apparaitre dans N'IMPORTE QUEL ORDRE dans le fichier
     *   - Java ne lit jamais un fichier "ligne par ligne comme un
     *   script" pour les MEMBRES d'une classe, il connait DEJA toute
     *   la classe avant d'executer quoi que ce soit.
     */
}
