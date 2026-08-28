package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 13 - Polymorphisme : casts, et la difference entre erreur de COMPILATION et d'EXECUTION (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un objet Dog existe TOUJOURS en memoire sous UNE SEULE forme reelle
 * (un vrai chien, avec toutes ses capacites de chien) - mais on peut
 * le REGARDER a travers differentes "lunettes" (des types de
 * reference differents). Avec des lunettes "Animal", on ne voit que
 * ce qu'un Animal generique sait faire (sound()) - meme si l'objet
 * EST reellement un chien capable de fetch() (rapporter la balle).
 * Pour retrouver l'acces a fetch(), il faut explicitement "changer de
 * lunettes" : un CAST vers Dog. 2 facons de faire ce cast :
 *   - la façon SURE : "if (a instanceof Dog dog)" - verifie D'ABORD
 *     que l'objet est VRAIMENT un chien, ne caste que si c'est le cas.
 *   - la façon RISQUEE : "(Dog) a" tout court - caste sans verifier,
 *     et lance une ClassCastException A L'EXECUTION (pas a la
 *     compilation !) si l'objet n'etait PAS vraiment un chien.
 *
 * IMPORTANT (juste a comprendre, pas a coder ici) : un cast entre 2
 * types SANS AUCUN LIEN de parente (par exemple caster un Animal en
 * String) est, lui, refuse des la COMPILATION - le compilateur sait
 * DEJA, sans meme executer le programme, qu'aucun objet ne pourra
 * jamais etre les deux a la fois. Le cast (Dog) a, lui, compile
 * TOUJOURS (Dog EST un Animal), le probleme ne peut se voir qu'a
 * L'EXECUTION, quand on regarde le VRAI objet qui se cache derriere.
 *
 *
 * ==================================================================
 * TODO 1 : castAndFetch(a) - la facon SURE
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Si a instanceof Dog dog : renvoyer dog.fetch().
 *   2. Sinon : renvoyer "Ce n'est pas un chien, impossible de rapporter la balle".
 *
 *
 * ==================================================================
 * TODO 2 : forceCastToDog(a) - la facon RISQUEE
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ecrire Dog dog = (Dog) a; (cast direct, SANS verification).
 *   2. Renvoyer dog.fetch().
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : castAndFetch(new Dog()) ==
 * "Rapporte la balle". castAndFetch(new Cat()) ==
 * "Ce n'est pas un chien, impossible de rapporter la balle" (PAS
 * d'exception, juste un message). forceCastToDog(new Dog()) ==
 * "Rapporte la balle". forceCastToDog(new Cat()) lance
 * ClassCastException A L'EXECUTION (le code COMPILE tres bien, il
 * plante seulement quand on l'EXECUTE avec un Cat).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "a instanceof Dog dog" declare ET verifie ET caste EN UNE
 *     SEULE FOIS (voir aussi Exercise06) : si vrai, dog est DEJA
 *     utilisable directement dans le bloc, sans cast manuel
 *     supplementaire.
 */
public class Exercise13_PolymorphismAndCasting {

    static class Animal {
        String sound() {
            return "...";
        }
    }

    static class Dog extends Animal {
        @Override
        String sound() {
            return "Wouf";
        }

        String fetch() {
            return "Rapporte la balle";
        }
    }

    static class Cat extends Animal {
        @Override
        String sound() {
            return "Miaou";
        }
    }

    public static String castAndFetch(Animal a) {
        throw new UnsupportedOperationException("TODO 1 : implementer castAndFetch()");
    }

    public static String forceCastToDog(Animal a) {
        throw new UnsupportedOperationException("TODO 2 : implementer forceCastToDog()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("castAndFetch(Dog) -> fetch() accessible via instanceof",
                castAndFetch(new Dog()).equals("Rapporte la balle"));
        ExerciseChecker.check("castAndFetch(Cat) -> message de repli, PAS d'exception",
                castAndFetch(new Cat()).equals("Ce n'est pas un chien, impossible de rapporter la balle"));

        ExerciseChecker.check("forceCastToDog(Dog) -> cast direct reussi",
                forceCastToDog(new Dog()).equals("Rapporte la balle"));

        boolean caught = false;
        try {
            forceCastToDog(new Cat());
        } catch (ClassCastException e) {
            caught = true;
        }
        ExerciseChecker.check("forceCastToDog(Cat) lance ClassCastException A L'EXECUTION", caught);

        ExerciseChecker.summary();
    }
}
