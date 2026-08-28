package classdesign.exercises;

import classdesign.ExerciseChecker;

/**
 * EXERCICE 7 - Surcharge (overload) vs redefinition (override) : 2 mecanismes qu'on confond souvent (niveau : difficile)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * classdesign.exercises.Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * 2 methodes qui portent le MEME nom, ce n'est PAS toujours la meme
 * chose :
 *   - REDEFINIR (override), c'est REMPLACER completement une methode
 *     HERITEE, avec EXACTEMENT la meme signature (memes types de
 *     parametres, dans le meme ordre) - decide a L'EXECUTION, selon
 *     le VRAI type de l'objet en memoire (le polymorphisme).
 *   - SURCHARGER (overload), c'est ajouter une AUTRE version de la
 *     methode, avec une signature DIFFERENTE (nombre ou types de
 *     parametres differents) - decide a la COMPILATION, selon les
 *     types des arguments FOURNIS a l'appel.
 * compute(int, int) redefini et compute(double, double) - une
 * signature COMPLETEMENT differente - ne sont PAS le "meme"
 * mecanisme, meme s'ils portent le meme nom compute.
 *
 *
 * ==================================================================
 * TODO 1 : SmartCalculator.compute(a, b) - REDEFINITION
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Calculator.compute(2, 3) (la version originale) vaut 5.
 * SmartCalculator.compute(2, 3) doit DOUBLER ce resultat : 10.
 *
 * -- Le plan --
 *
 *   1. Renvoyer super.compute(a, b) * 2 - reutilise le calcul
 *      original, plutot que de le recopier.
 *
 *
 * ==================================================================
 * TODO 2 : SmartCalculator.compute(a, b) avec des double - SURCHARGE
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Cette version-ci ne REDEFINIT RIEN du tout : Calculator ne declare
 * AUCUNE version avec 2 double, donc ce n'est pas une "redefinition"
 * mais une TOUTE NOUVELLE methode, qui existe UNIQUEMENT sur
 * SmartCalculator.
 *
 * -- Le plan --
 *
 *   1. Renvoyer (int) (a + b) - un exemple simple de conversion.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : avec Calculator ref = new SmartCalculator()
 * (une reference de type PARENT, pointant vers un objet ENFANT),
 * ref.compute(2, 3) == 10 (le VRAI type de l'objet, SmartCalculator,
 * decide - polymorphisme). ref.compute(2, 3, 4) == 9 (compute(int,
 * int, int), lui, n'a JAMAIS ete redefini : la version HERITEE de
 * Calculator s'applique telle quelle). new SmartCalculator().compute(2.0, 3.0)
 * == 5 (la surcharge existe UNIQUEMENT sur SmartCalculator).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - @Override (deja ecrit sur compute(int, int)) fait VERIFIER par
 *     le compilateur que ca redefinit VRAIMENT une methode heritee -
 *     s'il n'y avait, par erreur, AUCUNE methode compute(int, int)
 *     dans Calculator, @Override ferait ECHOUER LA COMPILATION (voir
 *     Exercise08 pour toutes les regles precises de redefinition).
 */
public class Exercise07_OverloadingVsOverriding {

    static class Calculator {
        int compute(int a, int b) {
            return a + b;
        }

        int compute(int a, int b, int c) {
            return a + b + c;
        }
    }

    static class SmartCalculator extends Calculator {
        @Override
        int compute(int a, int b) {
            throw new UnsupportedOperationException("TODO 1 : implementer la redefinition de compute(int, int)");
        }

        int compute(double a, double b) {
            throw new UnsupportedOperationException("TODO 2 : implementer la surcharge compute(double, double)");
        }
    }

    public static void main(String[] args) {
        Calculator ref = new SmartCalculator();

        ExerciseChecker.check("compute(int, int) REDEFINI : le VRAI type (SmartCalculator) decide",
                ref.compute(2, 3) == 10);
        ExerciseChecker.check("compute(int, int, int) JAMAIS redefini : version heritee telle quelle",
                ref.compute(2, 3, 4) == 9);

        ExerciseChecker.check("compute(double, double) SURCHARGE : une methode toute nouvelle",
                new SmartCalculator().compute(2.0, 3.0) == 5);

        ExerciseChecker.summary();
    }
}
