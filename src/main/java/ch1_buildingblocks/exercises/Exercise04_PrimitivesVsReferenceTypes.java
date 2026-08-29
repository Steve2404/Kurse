package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 4 - Primitifs vs types de reference : seuls les references peuvent etre null (niveau : moyen)
 * =================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MainMethodArgs.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un type PRIMITIF (int, boolean, double...) est une VALEUR NUE,
 * directement rangee en memoire - elle ne peut JAMAIS etre "vide"
 * (null n'existe pas pour un int). Un type de REFERENCE (String,
 * Integer, ou n'importe quelle classe) est, LUI, une ADRESSE qui
 * POINTE vers un objet ailleurs - cette adresse PEUT parfaitement
 * "ne pointer nulle part" (null). Chaque type primitif a d'ailleurs
 * sa "boite wrapper" equivalente (int -> Integer, boolean ->
 * Boolean...) - UN type de reference PAR primitif, justement pour
 * pouvoir, entre autres, representer "aucune valeur du tout".
 *
 * AUTRE REGLE IMPORTANTE : un CHAMP D'INSTANCE (pas une variable
 * LOCALE) non initialise explicitement recoit AUTOMATIQUEMENT sa
 * valeur "zero" par defaut des la creation de l'objet - 0 pour les
 * nombres, false pour boolean, et null pour TOUT type de reference
 * (String, Integer...) - AUCUNE exception a lancer, AUCUNE erreur.
 *
 *
 * ==================================================================
 * TODO 1 : describeDefaults()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Creer un new Defaults() (voir plus bas - AUCUN champ n'y est
 *      explicitement initialise).
 *   2. Renvoyer number + "/" + flag + "/" + text + "/" + wrapped
 *      (leurs valeurs par DEFAUT, automatiques).
 *
 *
 * ==================================================================
 * TODO 2 : nullableWrapper()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer null - un Integer (type de reference) PEUT
 *      parfaitement etre null, contrairement a un int primitif (qui,
 *      lui, n'accepterait meme pas de compiler avec null).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : describeDefaults() == "0/false/null/null" (4
 * champs JAMAIS initialises explicitement, tous a leur valeur par
 * defaut). nullableWrapper() == null.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "int number = null;" (essayez, PUIS remettez en commentaire)
 *     NE COMPILE PAS DU TOUT - la preuve concrete qu'un primitif ne
 *     peut jamais accueillir null, contrairement a Integer.
 */
public class Exercise04_PrimitivesVsReferenceTypes {

    static class Defaults {
        int number;
        boolean flag;
        String text;
        Integer wrapped;
    }

    public static String describeDefaults() {
        throw new UnsupportedOperationException("TODO 1 : implementer describeDefaults()");
    }

    public static Integer nullableWrapper() {
        throw new UnsupportedOperationException("TODO 2 : implementer nullableWrapper()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("describeDefaults() == \"0/false/null/null\" (valeurs par defaut automatiques)",
                describeDefaults().equals("0/false/null/null"));

        ExerciseChecker.check("nullableWrapper() == null (un type de reference PEUT etre null)",
                nullableWrapper() == null);

        ExerciseChecker.summary();
    }
}
