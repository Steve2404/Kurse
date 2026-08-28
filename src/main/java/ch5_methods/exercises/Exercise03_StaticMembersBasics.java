package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

/**
 * EXERCICE 3 - Membres static : partages par TOUTES les instances, accessibles par le NOM de la classe (niveau : moyen)
 * ============================================================================================================================
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
 * Un champ static, c'est comme un COMPTEUR AFFICHE A L'ENTREE D'UN
 * PARKING : il n'y en a QU'UN SEUL pour TOUT le parking, jamais un
 * par voiture garee - chaque nouvelle voiture (chaque "new Counter()")
 * le fait avancer, et TOUTES les voitures deja garees VOIENT le MEME
 * chiffre a jour. Depuis L'EXTERIEUR de la classe, on y accede par le
 * NOM DE LA CLASSE (Counter.totalCreated), jamais par une instance
 * particuliere - meme si Java tolere aussi la syntaxe "instance.champ",
 * ce n'est PAS la convention preferee. A L'INTERIEUR de la classe, une
 * methode D'INSTANCE peut appeler un membre static SANS AUCUN
 * probleme (elle "voit" tout) - mais l'inverse, lui, est INTERDIT
 * (voir Exercise04).
 *
 *
 * ==================================================================
 * TODO 1 : Counter.describeWithTotal()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec id = 2 (le 2e Counter jamais cree) et totalCreated = 3 (3
 * Counter au total pour l'instant), describeWithTotal() doit rendre
 * "Counter #2 sur 3 au total".
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Counter #" + id + " sur " + totalCreated + " au total"
 *      - id (instance) et totalCreated (static) sont TOUS LES DEUX
 *      directement lisibles ici, sans aucun prefixe.
 *
 *
 * ==================================================================
 * TODO 2 : Counter.describeDoubled()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * doubleTotal() (deja fournie plus bas, static) double simplement
 * totalCreated. describeDoubled(), elle, est une methode D'INSTANCE :
 * elle a parfaitement le droit d'appeler doubleTotal() (un membre
 * static) sans aucun probleme.
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Double du total : " + doubleTotal().
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : creer 3 Counter d'affilee ; Counter.totalCreated
 * (acces par le NOM de la classe, depuis main(), en dehors de la
 * classe) vaut alors 3. Le 2e Counter cree : describeWithTotal() ==
 * "Counter #2 sur 3 au total". describeDoubled() == "Double du total : 6".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "Counter.totalCreated" (depuis main(), EN DEHORS de Counter) :
 *     c'est la convention preferee pour lire un champ static depuis
 *     l'exterieur - jamais via une instance particuliere.
 */
public class Exercise03_StaticMembersBasics {

    static class Counter {
        static int totalCreated = 0;
        int id;

        Counter() {
            totalCreated++;
            id = totalCreated;
        }

        static int doubleTotal() {
            return totalCreated * 2;
        }

        String describeWithTotal() {
            throw new UnsupportedOperationException("TODO 1 : implementer describeWithTotal()");
        }

        String describeDoubled() {
            throw new UnsupportedOperationException("TODO 2 : implementer describeDoubled()");
        }
    }

    public static void main(String[] args) {
        Counter.totalCreated = 0;

        new Counter();
        Counter second = new Counter();
        new Counter();

        ExerciseChecker.check("Counter.totalCreated (acces par le NOM de la classe) == 3",
                Counter.totalCreated == 3);

        ExerciseChecker.check("describeWithTotal() lit id (instance) ET totalCreated (static)",
                second.describeWithTotal().equals("Counter #2 sur 3 au total"));

        ExerciseChecker.check("describeDoubled() (instance) appelle doubleTotal() (static) sans probleme",
                second.describeDoubled().equals("Double du total : 6"));

        ExerciseChecker.summary();
    }
}
