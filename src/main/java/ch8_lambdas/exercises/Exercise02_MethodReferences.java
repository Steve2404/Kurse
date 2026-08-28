package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.function.Function;

/**
 * EXERCICE 2 - Pipeline de normalisation avec les 4 types de method references (niveau : difficile)
 * ========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Les 4 types de method reference, en une phrase chacun --
 *
 *   1. Methode static :                ClasseX::maMethodeStatique
 *   2. Methode d'instance sur un OBJET PRECIS (deja connu) :
 *                                       monObjet::maMethode
 *   3. Methode d'instance sur un PARAMETRE (l'objet arrivera plus
 *      tard, c'est LUI qui sera "this" a l'interieur) :
 *                                       ClasseX::maMethode
 *   4. Constructeur :                  ClasseX::new
 *
 *
 * ==================================================================
 * TODO : buildNormalizationPipeline()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une chaine de fabrication, comme dans une usine de jouets.
 * Un jouet brut, mal fini, passe d'un poste de travail a l'autre, et
 * a CHAQUE poste, un ouvrier fait UNE SEULE petite amelioration, sans
 * se soucier de ce que les autres postes font. A la toute fin de la
 * chaine, le jouet brut est devenu un jouet fini, propre, emballe.
 *
 * Ici, la matiere premiere est une chaine de caracteres brute tapee
 * par un utilisateur (avec des espaces en trop, des majuscules mal
 * placees...), et le produit fini est un objet Username propre.
 *
 * -- Essayons a la main --
 *
 * Entree : "  Steve   Jean  "
 *
 *   Poste 1 (trim)           : "Steve   Jean"      (enleve les espaces
 *                               aux 2 bouts)
 *   Poste 2 (collapseSpaces) : "Steve Jean"        (remplace les
 *                               espaces multiples DEDANS par un seul)
 *   Poste 3 (toLowerCase)    : "steve jean"        (tout en minuscule)
 *   Poste 4 (prefix.concat)  : "user:steve jean"   (ajoute "user:" au
 *                               debut, une etiquette fixe)
 *   Poste 5 (new Username)   : Username("user:steve jean")  (emballe
 *                               dans l'objet final)
 *
 * -- Ce qu'on remarque --
 *
 * Chaque poste de la chaine est, en fait, une Function<String,String>
 * (sauf le tout dernier, qui transforme un String en Username). Voici
 * a quel TYPE de method reference correspond chaque poste :
 *
 *   - trim() : s.trim() - la chaine 's' elle-meme est "this". On ne
 *     connait PAS a l'avance quelle chaine on va trim - elle arrivera
 *     en parametre. C'est donc une reference d'instance SUR UN
 *     PARAMETRE (String::trim).
 *   - collapseSpaces(s) : une methode que VOUS ecrivez vous-meme,
 *     static, dans cette classe. C'est une reference de methode
 *     STATIC (Exercise02_MethodReferences::collapseSpaces).
 *   - toLowerCase() : meme raisonnement que trim() - reference
 *     d'instance sur un PARAMETRE (String::toLowerCase).
 *   - prefix.concat(s) : ici, "prefix" est un objet PRECIS, DEJA
 *     CONNU a l'avance (la chaine fixe "user:"), pas le parametre qui
 *     arrivera plus tard. C'est une reference d'instance sur un OBJET
 *     PARTICULIER (prefix::concat).
 *   - new Username(s) : fabrique un nouvel objet a partir de la
 *     chaine finale. C'est une reference de CONSTRUCTEUR
 *     (Username::new).
 *
 * -- Le plan --
 *
 *   1. Ecrire la petite methode static collapseSpaces(s), qui
 *      remplace toute suite d'espaces par un seul espace (regarde
 *      l'indice technique si la regex te bloque).
 *   2. Fabriquer une chaine fixe prefix = "user:" (une variable
 *      locale, effectivement finale).
 *   3. Assembler les 5 postes, DANS L'ORDRE, avec andThen() : chaque
 *      maillon transforme le resultat du precedent.
 *   4. Le TYPE de retour final de la chaine assemblee doit etre
 *      Function<String, Username> (le tout dernier maillon change de
 *      type, de String vers Username).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * collapseSpaces() en a besoin : elle cache sa propre petite regle
 * (une regex), et son nom ("aplatir les espaces") se raconte tout
 * seul. Les 4 autres postes sont deja des methodes toutes faites du
 * JDK (ou du constructeur de Username) : pas besoin de refabriquer
 * quoi que ce soit pour elles.
 *
 * Exemple a verifier : buildNormalizationPipeline().apply("  Steve   Jean  ")
 * -> Username dont la valeur est "user:steve jean"
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - collapseSpaces : return s.replaceAll("\\s+", " ");
 *   - Assemblage (les types s'enchainent, chaque andThen() peut
 *     changer le type de sortie) :
 *       Function<String, String> trimStep = String::trim;
 *       Function<String, String> collapseStep = Exercise02_MethodReferences::collapseSpaces;
 *       Function<String, String> lowerStep = String::toLowerCase;
 *       String prefix = "user:";
 *       Function<String, String> prefixStep = prefix::concat;
 *       Function<String, Username> wrapStep = Username::new;
 *       return trimStep.andThen(collapseStep).andThen(lowerStep).andThen(prefixStep).andThen(wrapStep);
 */
public class Exercise02_MethodReferences {

    static final class Username {
        private final String value;

        Username(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

    static String collapseSpaces(String s) {
        throw new UnsupportedOperationException("TODO : implementer collapseSpaces()");
    }

    public static Function<String, Username> buildNormalizationPipeline() {
        throw new UnsupportedOperationException("TODO : implementer buildNormalizationPipeline()");
    }

    public static void main(String[] args) {
        Function<String, Username> pipeline = buildNormalizationPipeline();

        Username result = pipeline.apply("  Steve   Jean  ");
        ExerciseChecker.check("pipeline sur '  Steve   Jean  ' == 'user:steve jean'",
                result.getValue().equals("user:steve jean"));

        Username result2 = pipeline.apply("ALICE");
        ExerciseChecker.check("pipeline sur 'ALICE' == 'user:alice'",
                result2.getValue().equals("user:alice"));

        ExerciseChecker.check("collapseSpaces('a    b   c') == 'a b c'",
                collapseSpaces("a    b   c").equals("a b c"));

        ExerciseChecker.summary();
    }
}
