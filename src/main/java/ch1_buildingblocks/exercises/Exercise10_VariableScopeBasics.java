package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 10 - 3 sortes de variables, 3 portees DIFFERENTES : locale, d'instance, de classe (niveau : moyen)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise04_PrimitivesVsReferenceTypes.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * 3 sortes de variables, 3 "portees" (l'endroit ou elles restent
 * accessibles) :
 *   - LOCALE (dans une methode/constructeur/bloc) : n'existe QUE
 *     pendant l'execution de CE bloc precis, disparait des qu'il se
 *     termine - jamais accessible depuis l'exterieur.
 *   - D'INSTANCE (un champ NON static) : UNE COPIE par OBJET - 2
 *     objets differents ont chacun LEUR PROPRE valeur, independante.
 *   - DE CLASSE (un champ static) : UNE SEULE copie, PARTAGEE par
 *     TOUS les objets de la classe (voir aussi le chapitre
 *     "Methods") - la modifier via UN objet la modifie pour TOUS les
 *     autres en meme temps.
 *
 *
 * ==================================================================
 * TODO 1 : ScopeDemo.computeWithLocal()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Declarer une variable LOCALE : int localVar = 5.
 *   2. Renvoyer instanceVar + classVar + localVar - les 3 sortes de
 *      variables sont TOUTES lisibles ici, sans aucune difference de
 *      syntaxe entre elles.
 *
 *
 * ==================================================================
 * TODO 2 : incrementSharedClassVar()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * classVar est PARTAGEE : L'INCREMENTER, meme depuis une seule
 * methode static (qui n'a AUCUN objet particulier "a elle", voir le
 * chapitre "Methods"), change la valeur pour TOUS LES OBJETS
 * EXISTANTS, meme ceux crees AVANT cet appel.
 *
 * -- Le plan --
 *
 *   1. Incrementer ScopeDemo.classVar de 1 (classVar++).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : avec 2 objets DIFFERENTS ScopeDemo (des
 * instanceVar differents, mais le MEME classVar au depart de 100),
 * chacun voit sa PROPRE valeur pour instanceVar dans
 * computeWithLocal(). Apres incrementSharedClassVar(), classVar vaut
 * 101 pour LES 2 objets a la fois, meme si un seul appel a ete fait.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - localVar, declaree DANS computeWithLocal(), n'existe QUE
 *     pendant cet appel precis - impossible d'y acceder depuis
 *     N'IMPORTE OU ailleurs dans la classe, meme depuis une autre
 *     methode de ScopeDemo.
 */
public class Exercise10_VariableScopeBasics {

    static class ScopeDemo {
        static int classVar = 100;
        int instanceVar;

        ScopeDemo(int instanceVar) {
            this.instanceVar = instanceVar;
        }

        int computeWithLocal() {
            throw new UnsupportedOperationException("TODO 1 : implementer computeWithLocal()");
        }
    }

    public static void incrementSharedClassVar() {
        throw new UnsupportedOperationException("TODO 2 : implementer incrementSharedClassVar()");
    }

    public static void main(String[] args) {
        ScopeDemo.classVar = 100;
        ScopeDemo first = new ScopeDemo(1);
        ScopeDemo second = new ScopeDemo(2);

        ExerciseChecker.check("computeWithLocal() combine local + instance + classe (1er objet)",
                first.computeWithLocal() == 1 + 100 + 5);
        ExerciseChecker.check("computeWithLocal() combine local + instance + classe (2e objet, instanceVar DIFFERENT)",
                second.computeWithLocal() == 2 + 100 + 5);

        incrementSharedClassVar();
        ExerciseChecker.check("incrementSharedClassVar() change classVar pour LES 2 objets a la fois",
                first.computeWithLocal() == 1 + 101 + 5 && second.computeWithLocal() == 2 + 101 + 5);

        ExerciseChecker.summary();
    }
}
