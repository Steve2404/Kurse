package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 1 - args[] de main() : indexe a partir de 0, et JAMAIS "verifie" tout seul (niveau : moyen)
 * ================================================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand on lance "java NomDeLaClasse premier deuxieme" depuis un
 * terminal, "premier" et "deuxieme" arrivent DANS args[], le
 * parametre de main(String[] args) - EXACTEMENT comme un tableau
 * normal (voir le chapitre "Core APIs") : args[0] est le PREMIER
 * argument, args[1] le 2eme, etc. Java ne verifie JAMAIS a l'avance
 * si un argument attendu a bien ete fourni - demander args[5] alors
 * que seulement 2 arguments ont ete donnes lance une
 * ArrayIndexOutOfBoundsException, EXACTEMENT comme pour n'importe
 * quel autre tableau.
 *
 *
 * ==================================================================
 * TODO 1 : getArgumentAt(args, index)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer args[index] - AUCUNE verification a faire ici,
 *      l'exception se declenchera TOUTE SEULE si index n'existe pas.
 *
 *
 * ==================================================================
 * TODO 2 : firstArgumentOrDefault(args, defaultValue)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Cette fois, on veut EVITER l'exception : si AUCUN argument n'a ete
 * fourni, on utilise une valeur de secours plutot que de planter.
 *
 * -- Le plan --
 *
 *   1. Si args.length == 0 : renvoyer defaultValue.
 *   2. Sinon : renvoyer args[0].
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : avec args = {"hello", "world"},
 * getArgumentAt(args, 0) == "hello". getArgumentAt(args, 5) lance
 * ArrayIndexOutOfBoundsException. firstArgumentOrDefault({}, "def")
 * == "def" (aucun argument). firstArgumentOrDefault(args, "def") ==
 * "hello".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "public static void main(String[] args)" est la signature LA
 *     PLUS COURANTE, mais "String... args" et "String args[]" sont
 *     TOUTES LES 2 aussi valides (voir Exercise02).
 */
public class Exercise01_MainMethodArgs {

    public static String getArgumentAt(String[] args, int index) {
        throw new UnsupportedOperationException("TODO 1 : implementer getArgumentAt()");
    }

    public static String firstArgumentOrDefault(String[] args, String defaultValue) {
        throw new UnsupportedOperationException("TODO 2 : implementer firstArgumentOrDefault()");
    }

    public static void main(String[] args) {
        String[] simulatedArgs = {"hello", "world"};

        ExerciseChecker.check("getArgumentAt(args, 0) == \"hello\"",
                getArgumentAt(simulatedArgs, 0).equals("hello"));

        boolean caught = false;
        try {
            getArgumentAt(simulatedArgs, 5);
        } catch (ArrayIndexOutOfBoundsException e) {
            caught = true;
        }
        ExerciseChecker.check("getArgumentAt(args, 5) lance ArrayIndexOutOfBoundsException (index inexistant)",
                caught);

        ExerciseChecker.check("firstArgumentOrDefault({}, \"def\") == \"def\" (aucun argument fourni)",
                firstArgumentOrDefault(new String[0], "def").equals("def"));
        ExerciseChecker.check("firstArgumentOrDefault(args, \"def\") == \"hello\"",
                firstArgumentOrDefault(simulatedArgs, "def").equals("hello"));

        ExerciseChecker.summary();
    }
}
