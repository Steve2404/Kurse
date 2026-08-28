package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

/**
 * EXERCICE 9 - Cacher (hiding) n'est PAS redefinir (overriding) : static et les champs ne sont JAMAIS polymorphiques (niveau : difficile)
 * ===================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * classdesign.exercises.Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Avec Parent ref = new Child() (une reference de type PARENT
 * pointant vers un VRAI objet Child), 3 choses se comportent
 * DIFFEREMMENT selon qu'on les redefinit ou qu'on les CACHE :
 *
 *   - instanceGreet() (methode d'INSTANCE, redefinie/override) :
 *     regarde le VRAI type de l'objet en memoire (Child) - c'est LE
 *     polymorphisme, la "vraie magie" de l'heritage.
 *   - staticGreet() (methode STATIC, "cachee" - ce n'est PAS une
 *     redefinition, meme si la syntaxe se ressemble) : regarde
 *     uniquement le TYPE DE LA REFERENCE (Parent), jamais le vrai
 *     objet - les methodes static ne sont JAMAIS polymorphiques, elle
 *     appartiennent a la CLASSE, pas a l'instance.
 *   - field (un CHAMP, "cache" lui aussi) : EXACTEMENT le meme
 *     principe que staticGreet() - les champs ne sont JAMAIS
 *     polymorphiques non plus, meme quand ils sont des champs
 *     d'INSTANCE (contrairement aux methodes d'instance).
 *
 *
 * ==================================================================
 * TODO : describeAll(ref)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer ref.staticGreet() + " | " + ref.instanceGreet() + " | " + ref.field.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 * Exemple a verifier : avec Parent ref = new Child(),
 * describeAll(ref) == "Parent.static | Child.instance | Parent.field"
 * - remarquez que SEUL instanceGreet() (une VRAIE redefinition)
 * "voit" Child ; staticGreet() et field, eux, restent bloques sur ce
 * que Parent (le type de la REFERENCE) declare.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Appeler une methode static via une variable d'instance
 *     (ref.staticGreet()) est LEGAL en Java, meme si la convention
 *     preferee est Parent.staticGreet() ou Child.staticGreet() - mais
 *     dans les 2 cas, c'est TOUJOURS le type ECRIT dans le code
 *     (Parent ici) qui decide, jamais le vrai objet.
 */
public class Exercise09_MethodAndFieldHiding {

    static class Parent {
        static String staticGreet() {
            return "Parent.static";
        }

        String instanceGreet() {
            return "Parent.instance";
        }

        String field = "Parent.field";
    }

    static class Child extends Parent {
        static String staticGreet() {
            return "Child.static";
        }

        @Override
        String instanceGreet() {
            return "Child.instance";
        }

        String field = "Child.field";
    }

    public static String describeAll(Parent ref) {
        throw new UnsupportedOperationException("TODO : implementer describeAll()");
    }

    public static void main(String[] args) {
        Parent ref = new Child();

        ExerciseChecker.check("methode static CACHEE : resolue par le type de la REFERENCE (Parent)",
                describeAll(ref).equals("Parent.static | Child.instance | Parent.field"));

        ExerciseChecker.check("methode d'instance REDEFINIE : resolue par le VRAI type (Child)",
                ref.instanceGreet().equals("Child.instance"));

        ExerciseChecker.summary();
    }
}
