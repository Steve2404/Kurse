package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

import java.util.EnumMap;
import java.util.Map;

/**
 * EXERCICE 8 - Un feu tricolore en enum : une machine a etats, values(), valueOf, EnumMap, et les regles des enums (niveau : avance)
 * =============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un feu passe de ROUGE a VERT, de VERT a ORANGE, d'ORANGE a ROUGE.
 * Chaque couleur a une duree (un champ, fixe par le constructeur de
 * l'enum) et SA propre regle "qui vient apres moi ?" (une methode
 * abstract redefinie dans chaque constante). Autour, des methodes
 * static parcourent values(), cherchent par lettre, simulent le temps
 * et comptent dans un EnumMap.
 *
 * -- Verdicts reels de javac 17 sur les enums --
 *
 *   public E() {} ou protected E() {}     -> error: modifier public/protected not allowed here (constructeur toujours private)
 *   new E()                               -> error: enum types may not be instantiated
 *   enum E extends K                      -> error: '{' expected (un enum n'etend rien : il herite deja d'Enum)
 *   enum E { A; abstract int v(); } (A sans corps) -> error: E is not abstract and does not override abstract method v() in E
 *   enum E { int x = 1; A }               -> error: enum constant expected here (les constantes d'abord)
 *   case E.A: dans un switch              -> error: an enum switch case label must be the unqualified name of an enumeration constant
 *   switch expression sans toutes les constantes et sans default -> error: the switch expression does not cover all possible input values
 *   E.A < E.B                             -> error: bad operand types for binary operator '<' (utiliser compareTo ou ordinal())
 *   e == E.A                              -> compile (une seule instance par constante)
 *
 *
 * ==================================================================
 * TODO 1 : RED.next()    TODO 2 : GREEN.next()    TODO 3 : YELLOW.next()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Chaque constante rend la suivante : RED -> GREEN -> YELLOW -> RED.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : c'est la methode abstract next(), redefinie dans le corps de chaque constante.
 *
 *
 * ==================================================================
 * TODO 4 : fromCode(code)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   'R' -> RED ; 'y' -> YELLOW (majuscule ou minuscule) ; 'X' -> IllegalArgumentException("code inconnu : X")
 *
 * -- Le plan --
 *
 *   1. Pour chaque light de values() : si light.code == Character.toUpperCase(code), le rendre.
 *   2. Sinon lancer l'exception.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : cycleSeconds()    et    TODO 6 : afterSteps(start, steps)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   cycleSeconds() -> 30 + 25 + 5 = 60
 *   afterSteps(RED, 4) -> GREEN, YELLOW, RED, GREEN -> GREEN
 *
 * -- Le plan --
 *
 *   1. cycleSeconds : somme des seconds de values().
 *   2. afterSteps : appeler next() steps fois.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : colorAt(start, t)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le feu demarre sur start au temps 0. Quelle couleur montre-t-il a
 * la seconde t ? On avance de couleur en couleur tant que t depasse la
 * duree de la couleur courante.
 *
 * -- Essayons a la main --
 *
 *   colorAt(RED, 0) -> RED ; colorAt(RED, 29) -> RED ; colorAt(RED, 30) -> GREEN ; colorAt(RED, 58) -> YELLOW ; colorAt(RED, 60) -> RED
 *
 * -- Le plan --
 *
 *   1. current = start ; remaining = t % cycleSeconds().
 *   2. Tant que remaining >= current.seconds : remaining -= current.seconds ; current = current.next().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : cycleSeconds (TODO 5).
 *
 *
 * ==================================================================
 * TODO 8 : countDuring(start, seconds)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pendant les secondes 0 a seconds - 1, combien de secondes le feu
 * passe-t-il sur chaque couleur ? Un EnumMap range les compteurs dans
 * l'ordre des constantes.
 *
 * -- Essayons a la main --
 *
 *   countDuring(RED, 90) -> {RED=60, GREEN=25, YELLOW=5}   (un cycle complet, puis 30 s de rouge)
 *
 * -- Le plan --
 *
 *   1. map = new EnumMap<>(Light.class) ; pour chaque seconde t : map.merge(colorAt(start, t), 1, Integer::sum).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : colorAt (TODO 7).
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Light.values() rend un tableau neuf dans l'ordre de declaration ; Light.valueOf("RED") cherche par NOM.
 */
public class Exercise08_TrafficLightStateMachine {

    public enum Light {
        RED('R', 30) {
            @Override
            public Light next() {
                throw new UnsupportedOperationException("TODO 1 : implementer RED.next()");
            }
        },
        GREEN('G', 25) {
            @Override
            public Light next() {
                throw new UnsupportedOperationException("TODO 2 : implementer GREEN.next()");
            }
        },
        YELLOW('Y', 5) {
            @Override
            public Light next() {
                throw new UnsupportedOperationException("TODO 3 : implementer YELLOW.next()");
            }
        };

        private final char code;
        private final int seconds;

        Light(char code, int seconds) {
            this.code = code;
            this.seconds = seconds;
        }

        public int seconds() {
            return seconds;
        }

        public abstract Light next();

        public static Light fromCode(char code) {
            throw new UnsupportedOperationException("TODO 4 : implementer fromCode()");
        }

        public static int cycleSeconds() {
            throw new UnsupportedOperationException("TODO 5 : implementer cycleSeconds()");
        }

        public static Light afterSteps(Light start, int steps) {
            throw new UnsupportedOperationException("TODO 6 : implementer afterSteps()");
        }

        public static Light colorAt(Light start, int t) {
            throw new UnsupportedOperationException("TODO 7 : implementer colorAt()");
        }

        public static Map<Light, Integer> countDuring(Light start, int seconds) {
            throw new UnsupportedOperationException("TODO 8 : implementer countDuring()");
        }
    }

    public static void main(String[] args) {
        ExerciseChecker.check("next : RED -> GREEN -> YELLOW -> RED",
                Light.RED.next() == Light.GREEN && Light.GREEN.next() == Light.YELLOW && Light.YELLOW.next() == Light.RED);
        String error = null;
        try {
            Light.fromCode('X');
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        ExerciseChecker.check("fromCode : R, y, et X refuse",
                Light.fromCode('R') == Light.RED && Light.fromCode('y') == Light.YELLOW && "code inconnu : X".equals(error));
        ExerciseChecker.check("(rappel) valueOf cherche par NOM : valueOf(\"GREEN\")", Light.valueOf("GREEN") == Light.GREEN);
        ExerciseChecker.check("cycleSeconds() == 60", Light.cycleSeconds() == 60);
        ExerciseChecker.check("afterSteps(RED, 4) == GREEN, afterSteps(YELLOW, 0) == YELLOW",
                Light.afterSteps(Light.RED, 4) == Light.GREEN && Light.afterSteps(Light.YELLOW, 0) == Light.YELLOW);
        ExerciseChecker.check("colorAt : 0 RED, 29 RED, 30 GREEN, 58 YELLOW, 60 RED",
                Light.colorAt(Light.RED, 0) == Light.RED && Light.colorAt(Light.RED, 29) == Light.RED && Light.colorAt(Light.RED, 30) == Light.GREEN
                        && Light.colorAt(Light.RED, 58) == Light.YELLOW && Light.colorAt(Light.RED, 60) == Light.RED);
        Map<Light, Integer> counts = Light.countDuring(Light.RED, 90);
        ExerciseChecker.check("countDuring(RED, 90) == {RED=60, GREEN=25, YELLOW=5} " + counts,
                counts.toString().equals("{RED=60, GREEN=25, YELLOW=5}") && counts instanceof EnumMap);

        ExerciseChecker.summary();
    }
}
