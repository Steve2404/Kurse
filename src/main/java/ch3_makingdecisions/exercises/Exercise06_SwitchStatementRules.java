package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 6 - Les regles du switch statement : types acceptes, fall-through, constantes, null, default au milieu (niveau : difficile)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- Verdicts REELS de javac 17 --
 *
 *   switch sur byte, short, char, int, Byte, Short, Character, Integer,
 *   String, un enum, ou un var qui vaut un int  -> COMPILE
 *   switch sur long, float, double, boolean, Long, Boolean
 *   -> error: patterns in switch statements are a preview feature and are disabled by default.
 *      (message trompeur : le compilateur croit qu'on tente une nouveaute de Java 21)
 *   case avec une variable NON constante (meme final si elle vient d'un parametre) :
 *      final int z = y;  case z:  -> error: constant expression required
 *   case 1: ... case 1:  -> error: duplicate case label
 *   "case 1, 2:" (plusieurs valeurs par case) : permis depuis Java 14.
 *   A l'execution : switch sur un String null -> NullPointerException.
 *
 *
 * ==================================================================
 * TODO 1 : acceptsSwitch(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le switch est une armoire a tiroirs etiquetes : il faut des
 * etiquettes qu'on peut comparer exactement et facilement. Les petits
 * entiers (jusqu'a int), les caracteres, les textes et les enums
 * conviennent. Pas les long (trop grands pour ses tiroirs), ni les
 * nombres a virgule (1.0 et 0.99999 ?), ni les boolean (un if suffit).
 *
 * -- Essayons a la main --
 *
 *   "int" -> true ; "Character" -> true ; "enum" -> true ; "long" -> false ; "Boolean" -> false
 *
 * -- Le plan --
 *
 *   1. Vrai pour : byte, short, char, int, Byte, Short, Character,
 *      Integer, String, enum. Faux pour tout le reste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : fallThroughCount(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un switch statement SAUTE au bon case, puis COULE vers le bas (il
 * execute les case suivants) jusqu'au premier break. Ecris exactement
 * ce switch :
 *
 *   int count = 0;
 *   switch (x) { case 1: count++;  case 2: count++;  case 3: count++; break;  case 4: count += 10; }
 *
 * -- Essayons a la main (valeurs reelles) --
 *
 *   1 -> 3 ; 2 -> 2 ; 3 -> 1 ; 4 -> 10 ; 9 -> 0 (aucun case, pas de default : rien)
 *
 * -- Le plan --
 *
 *   1. Recopier ce switch, rendre count.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : dayType(day)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un case exige une CONSTANTE DE COMPILATION : une valeur que le
 * compilateur connait deja. Une variable locale final initialisee
 * avec un litteral en est une.
 *
 * -- Essayons a la main --
 *
 *   6 et 7 -> "week-end" ; 1 a 5 -> "semaine" ; 9 -> "invalide"
 *
 * -- Le plan --
 *
 *   1. Declarer final int SATURDAY = 6 et final int SUNDAY = 7.
 *   2. switch (day) : case SATURDAY, SUNDAY -> "week-end" ; case 1, 2, 3, 4, 5 -> "semaine" ;
 *      default -> "invalide" (forme avec ":" et return, ou avec des fleches).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : commandLabel(command)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un switch sur un String compare avec equals. Mais si le String est
 * null, il n'y a rien a comparer : NullPointerException. On garde la
 * porte : on teste null AVANT.
 *
 * -- Essayons a la main --
 *
 *   "start" -> "demarrage" ; "stop" -> "arret" ; "quit" et "exit" -> "sortie" ;
 *   "abc" -> "inconnue" ; null -> "aucune"
 *
 * -- Le plan --
 *
 *   1. null -> "aucune".
 *   2. switch (command) : case "start" ; case "stop" ; case "quit", "exit" ; default.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : defaultInTheMiddle(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * default n'est pas oblige d'etre en dernier. Il est choisi seulement
 * si aucun case ne correspond, mais une fois entre, on coule vers le
 * bas comme ailleurs. Ecris exactement :
 *
 *   String s = "";
 *   switch (x) { case 1: s += "1";  default: s += "D";  case 2: s += "2"; }
 *
 * -- Essayons a la main (valeurs reelles) --
 *
 *   1 -> "1D2" ; 2 -> "2" ; 7 -> "D2"
 *
 * -- Le plan --
 *
 *   1. Recopier ce switch, rendre s.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 */
public class Exercise06_SwitchStatementRules {

    public static boolean acceptsSwitch(String type) {
        throw new UnsupportedOperationException("TODO 1 : implementer acceptsSwitch()");
    }

    public static int fallThroughCount(int x) {
        throw new UnsupportedOperationException("TODO 2 : implementer fallThroughCount()");
    }

    public static String dayType(int day) {
        throw new UnsupportedOperationException("TODO 3 : implementer dayType()");
    }

    public static String commandLabel(String command) {
        throw new UnsupportedOperationException("TODO 4 : implementer commandLabel()");
    }

    public static String defaultInTheMiddle(int x) {
        throw new UnsupportedOperationException("TODO 5 : implementer defaultInTheMiddle()");
    }

    public static void main(String[] args) {
        String[] accepted = {"byte", "short", "char", "int", "Byte", "Short", "Character", "Integer", "String", "enum"};
        String[] refused = {"long", "float", "double", "boolean", "Long", "Boolean"};
        boolean ok = true;
        for (String t : accepted) {
            ok &= acceptsSwitch(t);
        }
        for (String t : refused) {
            ok &= !acceptsSwitch(t);
        }
        ExerciseChecker.check("1 acceptsSwitch reproduit les 16 verdicts de javac", ok);
        ExerciseChecker.check("2 fallThroughCount : 1 -> 3, 2 -> 2, 3 -> 1, 4 -> 10, 9 -> 0",
                fallThroughCount(1) == 3 && fallThroughCount(2) == 2 && fallThroughCount(3) == 1
                        && fallThroughCount(4) == 10 && fallThroughCount(9) == 0);
        ExerciseChecker.check("3 dayType : 6 et 7 week-end, 3 semaine, 9 invalide",
                dayType(6).equals("week-end") && dayType(7).equals("week-end") && dayType(3).equals("semaine")
                        && dayType(9).equals("invalide"));
        ExerciseChecker.check("4 commandLabel : start, stop, quit, exit, abc, null",
                commandLabel("start").equals("demarrage") && commandLabel("stop").equals("arret")
                        && commandLabel("quit").equals("sortie") && commandLabel("exit").equals("sortie")
                        && commandLabel("abc").equals("inconnue") && commandLabel(null).equals("aucune"));
        ExerciseChecker.check("5 defaultInTheMiddle : 1 -> 1D2, 2 -> 2, 7 -> D2",
                defaultInTheMiddle(1).equals("1D2") && defaultInTheMiddle(2).equals("2") && defaultInTheMiddle(7).equals("D2"));

        ExerciseChecker.summary();
    }
}
