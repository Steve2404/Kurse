package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 3 - Chainage de constructeurs : this(), super(), et le constructeur "gratuit" (niveau : difficile)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * classdesign.exercises.Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La TOUTE PREMIERE ligne d'un constructeur est TOUJOURS un appel a
 * un AUTRE constructeur : soit this(...) (un autre constructeur de LA
 * MEME classe), soit super(...) (un constructeur du PARENT) - jamais
 * les deux a la fois, et jamais ailleurs qu'en premiere ligne. Si tu
 * n'ecris NI l'un NI l'autre, le compilateur AJOUTE tout seul, en
 * silence, un "super()" (sans argument) en toute premiere ligne -
 * MAIS seulement si le parent possede VRAIMENT un constructeur sans
 * argument. Si le parent n'en a AUCUN (parce qu'il a defini un
 * constructeur AVEC des parametres, et RIEN d'autre), le compilateur
 * ne peut RIEN deviner tout seul : il faut alors ECRIRE
 * explicitement le bon super(...), sinon ca ne compile pas -
 * MEME si le corps du constructeur ne fait rien d'autre qu'un throw
 * provisoire, comme dans cet exercice.
 *
 *
 * ==================================================================
 * TODO 1 : Car(log) - completer APRES le super() (deja ecrit, force par le compilateur)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Vehicle n'a QU'UN SEUL constructeur, et il exige un parametre (log)
 * - donc AUCUN constructeur sans argument n'existe chez Vehicle. Le
 * compilateur ne peut donc PAS inserer de super() automatique ici :
 * il DOIT etre ecrit a la main, en premiere ligne - c'est deja fait
 * plus bas (super(log)), ce n'est pas un choix a faire, juste une
 * contrainte de syntaxe. Le vrai TODO commence APRES.
 *
 * -- Le plan --
 *
 *   1. APRES le super(log) deja present : log.add("Car").
 *
 *
 * ==================================================================
 * TODO 2 : Car() - this() vers l'AUTRE constructeur de la MEME classe
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut un raccourci "sans rien fournir" : Car() cree sa PROPRE
 * liste vide, puis delegue TOUT le travail au VRAI constructeur
 * Car(log) juste au-dessus - plutot que de recopier la meme logique
 * 2 fois. Le corps actuel appelle super(...) directement (un appel
 * BIDON, qui COURT-CIRCUITE Car(log) et son log.add("Car")) : a
 * remplacer ENTIEREMENT par this(...).
 *
 * -- Le plan --
 *
 *   1. Remplacer TOUT le corps par une seule ligne : this(new ArrayList<>()).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre "boite" (un constructeur dedie).
 *
 * Exemple a verifier : new Car(log).log().equals(List.of("Vehicle",
 * "Car")) (Vehicle s'initialise TOUJOURS en premier). new
 * Car().log().equals(List.of("Vehicle", "Car")) AUSSI, via this(new
 * ArrayList<>()) qui delegue au VRAI constructeur plutot que de le
 * court-circuiter.
 *
 * Enfin, remarquez SimpleParent et SimpleChild plus bas : AUCUN des 2
 * ne declare le MOINDRE constructeur - et pourtant "new SimpleChild()"
 * fonctionne quand meme parfaitement : le compilateur a insere, tout
 * seul et en silence, un constructeur SimpleParent() vide, PUIS un
 * constructeur SimpleChild() { super(); } tout aussi vide - c'est LE
 * "constructeur par defaut" dont parle l'Exam Essentials.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - super(log) et this(new ArrayList<>()) doivent TOUJOURS etre la
 *     toute PREMIERE instruction du constructeur - aucune ligne,
 *     meme un simple commentaire de code, ne peut passer avant.
 */
public class Exercise03_ConstructorChaining {

    static class Vehicle {
        protected final List<String> log;

        Vehicle(List<String> log) {
            this.log = log;
            log.add("Vehicle");
        }
    }

    static class Car extends Vehicle {
        Car(List<String> log) {
            super(log);
            throw new UnsupportedOperationException("TODO 1 : completer Car(log) apres le super(log)");
        }

        Car() {
            super(new ArrayList<>()); // TODO 2 : appel BIDON (court-circuite Car(log)), a remplacer par this(...)
            throw new UnsupportedOperationException("TODO 2 : implementer Car() via this(new ArrayList<>())");
        }

        List<String> log() {
            return log;
        }
    }

    static class SimpleParent {
        String whoAmI() {
            return "SimpleParent";
        }
    }

    static class SimpleChild extends SimpleParent {
    }

    public static void main(String[] args) {
        List<String> log = new ArrayList<>();
        Car car = new Car(log);
        ExerciseChecker.check("super(log) explicite : Vehicle s'initialise avant Car",
                car.log().equals(List.of("Vehicle", "Car")));

        ExerciseChecker.check("Car() delegue via this(new ArrayList<>()) au VRAI constructeur",
                new Car().log().equals(List.of("Vehicle", "Car")));

        ExerciseChecker.check("constructeurs par defaut inseres par le compilateur (aucun ecrit ici)",
                new SimpleChild().whoAmI().equals("SimpleParent"));

        ExerciseChecker.summary();
    }
}
