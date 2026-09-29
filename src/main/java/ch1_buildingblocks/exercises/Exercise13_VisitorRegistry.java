package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 13 - Registre de visiteurs : portee de classe, d'instance, locale, et le piege du parametre qui cache un champ (niveau : difficile)
 * ==========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Le contexte --
 *
 * Un musee donne un numero a chaque visiteur : 1, 2, 3... Le compteur
 * doit etre PARTAGE par tous les visiteurs (un champ static), alors
 * que le numero et le nom sont PROPRES a chaque visiteur (des champs
 * d'instance). C'est la suite directe de l'Exercise12, avec un piege
 * en plus : le "masquage" (shadowing).
 *
 * -- Le piege du masquage --
 *
 * Dans un constructeur Visitor(String name), le PARAMETRE s'appelle
 * name, comme le CHAMP name. A l'interieur, "name" tout court designe
 * le PARAMETRE (le plus proche gagne). Ecrire "name = name;" recopie
 * le parametre dans... lui-meme : le champ reste null, sans aucune
 * erreur de compilation ! Il faut "this.name = name;" (this.name = le
 * champ de CET objet).
 *
 *
 * ==================================================================
 * TODO 1 : Visitor(String name) - le constructeur
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * A l'entree, on tourne la manivelle du compteur (qui est au guichet,
 * partage), et on donne au visiteur le numero qui sort. Puis on ecrit
 * son nom sur SON badge (pas sur le papier qu'il tient dans la main).
 *
 * -- Essayons a la main --
 *
 *   created = 0 ; new Visitor("Lea")  -> created = 1, id 1, name "Lea"
 *                 new Visitor("Hugo") -> created = 2, id 2
 *
 * -- Le plan --
 *
 *   1. Augmenter le compteur partage created de 1.
 *   2. Donner a id la nouvelle valeur du compteur.
 *   3. Recopier le parametre name dans le CHAMP name (attention au masquage).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : rename(String name)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Meme piege qu'au TODO 1 : le parametre cache le champ.
 *
 * -- Le plan --
 *
 *   1. Remplacer le champ name de cet objet par le parametre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : resetCounter()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le lendemain matin, on remet la manivelle a zero. Une methode static
 * n'a PAS d'objet a elle ("this" n'existe pas ici) : elle ne peut
 * toucher que les champs static, comme created.
 *
 * -- Le plan --
 *
 *   1. Remettre created a 0.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : badgeLine(visitors...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On fabrique une ligne "1:Lea 2:Hugo 3:Ines". La variable qui
 * construit la ligne est LOCALE : elle nait a chaque appel et meurt a
 * la fin. La variable de la boucle for n'existe QUE dans la boucle.
 *
 * -- Essayons a la main --
 *
 *   (Lea#1, Hugo#2, Ines#3) -> "1:Lea 2:Hugo 3:Ines" ; () -> ""
 *
 * -- Le plan --
 *
 *   1. Une ligne vide.
 *   2. Pour chaque visiteur : ajouter un espace si la ligne n'est pas
 *      vide, puis id + ":" + name.
 *   3. Rendre la ligne.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : 3 visiteurs -> ids 1, 2, 3 et created == 3 ;
 * rename("Leo") change le nom de Lea seulement ; badgeLine ; apres
 * resetCounter(), un nouveau visiteur a l'id 1.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - created++; id = created; this.name = name;
 *   - Visitor.created = 0;  (ou juste created = 0; depuis une methode static de Visitor)
 *   - String line = ""; for (Visitor v : visitors) { ... } ; line.isEmpty()
 */
public class Exercise13_VisitorRegistry {

    static class Visitor {
        static int created = 0;
        final int id;
        String name;

        Visitor(String name) {
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur Visitor(String)");
        }

        void rename(String name) {
            throw new UnsupportedOperationException("TODO 2 : implementer rename()");
        }

        static void resetCounter() {
            throw new UnsupportedOperationException("TODO 3 : implementer resetCounter()");
        }
    }

    public static String badgeLine(Visitor... visitors) {
        throw new UnsupportedOperationException("TODO 4 : implementer badgeLine()");
    }

    public static void main(String[] args) {
        Visitor.created = 0;
        Visitor lea = new Visitor("Lea");
        Visitor hugo = new Visitor("Hugo");
        Visitor ines = new Visitor("Ines");
        ExerciseChecker.check("1 ids 1, 2, 3 dans l'ordre de creation", lea.id == 1 && hugo.id == 2 && ines.id == 3);
        ExerciseChecker.check("1 created == 3 (partage par tous)", Visitor.created == 3);
        ExerciseChecker.check("1 le champ name est rempli (pas masque par le parametre)",
                "Lea".equals(lea.name) && "Ines".equals(ines.name));

        lea.rename("Leo");
        ExerciseChecker.check("2 rename(Leo) change Lea seulement", "Leo".equals(lea.name) && "Hugo".equals(hugo.name));

        ExerciseChecker.check("4 badgeLine == \"1:Leo 2:Hugo 3:Ines\"", badgeLine(lea, hugo, ines).equals("1:Leo 2:Hugo 3:Ines"));
        ExerciseChecker.check("4 badgeLine() == \"\"", badgeLine().isEmpty());

        Visitor.resetCounter();
        ExerciseChecker.check("3 apres resetCounter(), created == 0 et le visiteur suivant a l'id 1",
                Visitor.created == 0 && new Visitor("Tom").id == 1);

        ExerciseChecker.summary();
    }
}
