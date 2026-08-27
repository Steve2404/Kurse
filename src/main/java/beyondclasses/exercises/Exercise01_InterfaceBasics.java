package beyondclasses.exercises;

import beyondclasses.ExerciseChecker;

/**
 * EXERCICE 1 - Interfaces : methodes abstraites, champs implicites, heritage multiple (niveau : moyen)
 * ==========================================================================================================
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
 * Une interface, c'est un CONTRAT : "toute classe qui signe ce papier
 * s'engage a savoir faire CECI" (une methode abstraite, sans corps -
 * juste sa signature). Un canard, lui, doit signer DEUX contrats en
 * meme temps : celui du club de voltige aerienne (Flyable : "tu dois
 * savoir voler") ET celui du club de natation (Swimmable : "tu dois
 * savoir nager"). Une classe Java, contrairement a l'heritage de
 * classe (UN SEUL parent possible), peut implementer AUTANT
 * d'interfaces qu'elle veut : c'est ca, "l'heritage multiple" que les
 * interfaces autorisent (et que l'heritage de classe interdit).
 *
 * ATTENTION aux modificateurs INVISIBLES que le compilateur ajoute
 * TOUT SEUL, sans qu'on les ecrive :
 *   - une methode d'interface SANS corps est TOUJOURS "public abstract",
 *     meme si on n'ecrit ni "public" ni "abstract".
 *   - un champ d'interface est TOUJOURS "public static final" (une
 *     CONSTANTE partagee, jamais une variable d'instance), meme si on
 *     n'ecrit ni "public" ni "static" ni "final".
 *
 *
 * ==================================================================
 * TODO 1 : Duck.fly()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * MAX_ALTITUDE_M vaut 12000.0 (un double). fly() doit rendre "Vole
 * jusqu'a 12000.0m".
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Vole jusqu'a " + MAX_ALTITUDE_M + "m" - remarque
 *      qu'on n'ecrit PAS "Flyable.MAX_ALTITUDE_M" : comme Duck
 *      IMPLEMENTE Flyable, la constante devient directement
 *      accessible SANS prefixe, un peu comme un champ herite.
 *
 *
 * ==================================================================
 * TODO 2 : Duck.swim()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Meme principe : renvoyer "Nage jusqu'a " + MAX_DEPTH_M + "m".
 *
 *
 * ==================================================================
 * TODO 3 : describeViaBothInterfaces(duck)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le MEME canard peut etre "regarde" de 2 facons differentes : par
 * les yeux du club de voltige (qui ne voit QUE fly(), rien d'autre),
 * ou par les yeux du club de natation (qui ne voit QUE swim()). C'est
 * le meme objet en memoire, mais le TYPE de la variable qui le
 * regarde decide ce qu'on a le droit d'appeler dessus.
 *
 * -- Le plan --
 *
 *   1. Declarer une variable de type Flyable qui pointe vers duck.
 *   2. Declarer une variable de type Swimmable qui pointe AUSSI vers
 *      le MEME duck.
 *   3. Renvoyer flyableRef.fly() + " | " + swimmableRef.swim().
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : describeViaBothInterfaces(new Duck()) ==
 * "Vole jusqu'a 12000.0m | Nage jusqu'a 300.0m".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "class Duck implements Flyable, Swimmable" (virgule pour
 *     separer plusieurs interfaces - contrairement a "extends" qui
 *     n'accepte qu'UN SEUL nom pour une classe).
 *   - Flyable flyableRef = duck; ne fait AUCUNE copie : c'est
 *     TOUJOURS le meme objet Duck en memoire, juste vu a travers une
 *     "fenetre" plus etroite (celle du contrat Flyable).
 */
public class Exercise01_InterfaceBasics {

    interface Flyable {
        double MAX_ALTITUDE_M = 12000; // implicitement public static final
        String fly(); // implicitement public abstract
    }

    interface Swimmable {
        double MAX_DEPTH_M = 300; // implicitement public static final
        String swim(); // implicitement public abstract
    }

    static class Duck implements Flyable, Swimmable {
        @Override
        public String fly() {
            throw new UnsupportedOperationException("TODO 1 : implementer fly()");
        }

        @Override
        public String swim() {
            throw new UnsupportedOperationException("TODO 2 : implementer swim()");
        }
    }

    public static String describeViaBothInterfaces(Duck duck) {
        throw new UnsupportedOperationException("TODO 3 : implementer describeViaBothInterfaces()");
    }

    public static void main(String[] args) {
        Duck duck = new Duck();

        ExerciseChecker.check("fly() utilise la constante MAX_ALTITUDE_M sans prefixe",
                duck.fly().equals("Vole jusqu'a 12000.0m"));
        ExerciseChecker.check("swim() utilise la constante MAX_DEPTH_M sans prefixe",
                duck.swim().equals("Nage jusqu'a 300.0m"));

        ExerciseChecker.check("describeViaBothInterfaces() combine les 2 contrats sur le MEME objet",
                describeViaBothInterfaces(duck).equals("Vole jusqu'a 12000.0m | Nage jusqu'a 300.0m"));

        ExerciseChecker.summary();
    }
}
