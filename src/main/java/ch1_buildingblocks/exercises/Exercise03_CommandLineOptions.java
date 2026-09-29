package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 3 - Un vrai lanceur en ligne de commande : options, valeurs par defaut, arguments positionnels (niveau : difficile)
 * ==========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Le contexte --
 *
 * Les vrais programmes en ligne de commande (git, mvn...) recoivent
 * des arguments de 2 sortes :
 *   - des OPTIONS, qui commencent par "--" : "--client=Lea" (avec une
 *     valeur), "--vip" (un simple interrupteur, sans valeur) ;
 *   - des arguments POSITIONNELS, tout le reste : "pomme", "pain".
 *
 *   java Caisse --client=Lea --qty=3 pomme --vip pain --prix=abc
 *   -> args = {"--client=Lea", "--qty=3", "pomme", "--vip", "pain", "--prix=abc"}
 *
 * Rappel de l'Exercise01 : args est un tableau NORMAL, Java ne verifie
 * rien. C'est a TOI de lire ce qui est fourni, et de ne jamais planter
 * si un argument manque ou est mal ecrit. Et chaque valeur arrive
 * sous forme de TEXTE : "3" n'est pas encore le nombre 3.
 *
 *
 * ==================================================================
 * TODO 1 : isOption(arg)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une option porte un badge "--" devant son nom. Mais "--" tout seul
 * n'a pas de nom : ce n'est pas une option.
 *
 * -- Essayons a la main --
 *
 *   "--vip" -> true ; "--client=Lea" -> true ; "pomme" -> false ;
 *   "--" -> false ; "-v" -> false (un seul tiret)
 *
 * -- Le plan --
 *
 *   1. Vrai si le texte commence par "--" ET fait plus de 2 caracteres.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique utilisee par les TODO 4 et 6.
 *
 *
 * ==================================================================
 * TODO 2 : optionName(arg)  et  TODO 3 : optionValue(arg)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "--client=Lea" : le NOM est entre "--" et "=", la VALEUR est apres
 * "=". Un interrupteur "--vip" n'a pas de "=" : son nom va jusqu'au
 * bout, et sa valeur est "true" (il est allume).
 *
 * -- Essayons a la main --
 *
 *   "--client=Lea" -> nom "client", valeur "Lea"
 *   "--vip"        -> nom "vip",    valeur "true"
 *   "--note="      -> nom "note",   valeur "" (vide mais presente)
 *
 * -- Le plan --
 *
 *   optionName  : 1. chercher la position du "=" ; 2. s'il n'y en a pas,
 *                 prendre de la position 2 jusqu'a la fin ; sinon de 2
 *                 jusqu'au "=" (exclu).
 *   optionValue : 1. chercher le "=" ; 2. pas de "=" -> "true" ;
 *                 sinon tout ce qui suit le "=".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : chacune tient en 2-3 lignes.
 *
 *
 * ==================================================================
 * TODO 4 : findOption(args, name, defaultValue)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cherche une option precise dans tous les arguments. Si elle est
 * absente, on rend une valeur par defaut au lieu de planter. Si elle
 * apparait 2 fois, c'est la DERNIERE qui gagne (comme dans la plupart
 * des vrais outils : on peut "ecraser" un reglage plus loin).
 *
 * -- Essayons a la main --
 *
 *   findOption(args, "client", "anonyme") -> "Lea"
 *   findOption(args, "vip", "false")      -> "true"
 *   findOption(args, "remise", "0")       -> "0" (absente)
 *   {"--qty=1", "--qty=5"}, "qty"         -> "5" (la derniere gagne)
 *
 * -- Le plan --
 *
 *   1. Resultat = defaultValue.
 *   2. Pour chaque argument : si c'est une option ET que son nom est
 *      name, resultat = sa valeur.
 *   3. Rendre le resultat.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isOption, optionName, optionValue (TODO 1 a 3).
 *
 *
 * ==================================================================
 * TODO 5 : intOption(args, name, defaultValue)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "3" est du TEXTE. Integer.parseInt("3") le transforme en nombre 3.
 * Mais Integer.parseInt("abc") EXPLOSE (NumberFormatException). Un bon
 * lanceur ne plante pas pour une faute de frappe : il reprend la
 * valeur par defaut.
 *
 * -- Essayons a la main --
 *
 *   intOption(args, "qty", 1)  -> "3"   -> 3
 *   intOption(args, "prix", 0) -> "abc" -> NumberFormatException -> 0
 *   intOption(args, "age", 18) -> absente -> 18
 *
 * -- Le plan --
 *
 *   1. Lire la valeur texte avec findOption (null si absente).
 *   2. Absente -> defaultValue.
 *   3. Essayer de la convertir ; en cas de NumberFormatException,
 *      rendre defaultValue.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : findOption (TODO 4).
 *
 *
 * ==================================================================
 * TODO 6 : positionalArguments(args)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tout ce qui n'est pas une option est un argument positionnel, dans
 * l'ordre ou il a ete tape.
 *
 * -- Essayons a la main --
 *
 *   args -> ["pomme", "pain"]
 *
 * -- Le plan --
 *
 *   1. Une liste vide.
 *   2. Ajouter chaque argument qui n'est PAS une option.
 *   3. Rendre la liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isOption (TODO 1).
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - arg.startsWith("--"), arg.length(), arg.indexOf('=') rend -1 si absent
 *   - arg.substring(debut) / arg.substring(debut, finExclue)
 *   - for (String arg : args) { ... }
 *   - try { return Integer.parseInt(text); } catch (NumberFormatException e) { return defaultValue; }
 *     (try/catch est detaille au chapitre 11 ; ici, c'est juste "essaie, sinon...")
 *   - List<String> result = new ArrayList<>(); result.add(arg);
 */
public class Exercise03_CommandLineOptions {

    public static boolean isOption(String arg) {
        throw new UnsupportedOperationException("TODO 1 : implementer isOption()");
    }

    public static String optionName(String arg) {
        throw new UnsupportedOperationException("TODO 2 : implementer optionName()");
    }

    public static String optionValue(String arg) {
        throw new UnsupportedOperationException("TODO 3 : implementer optionValue()");
    }

    public static String findOption(String[] args, String name, String defaultValue) {
        throw new UnsupportedOperationException("TODO 4 : implementer findOption()");
    }

    public static int intOption(String[] args, String name, int defaultValue) {
        throw new UnsupportedOperationException("TODO 5 : implementer intOption()");
    }

    public static List<String> positionalArguments(String[] args) {
        throw new UnsupportedOperationException("TODO 6 : implementer positionalArguments()");
    }

    public static void main(String[] args) {
        String[] simulated = {"--client=Lea", "--qty=3", "pomme", "--vip", "pain", "--prix=abc"};

        ExerciseChecker.check("1 isOption : --vip et --client=Lea oui ; pomme, --, -v non",
                isOption("--vip") && isOption("--client=Lea") && !isOption("pomme") && !isOption("--") && !isOption("-v"));
        ExerciseChecker.check("2 optionName(--client=Lea) == client, (--vip) == vip, (--note=) == note",
                optionName("--client=Lea").equals("client") && optionName("--vip").equals("vip")
                        && optionName("--note=").equals("note"));
        ExerciseChecker.check("3 optionValue(--client=Lea) == Lea, (--vip) == true, (--note=) == \"\"",
                optionValue("--client=Lea").equals("Lea") && optionValue("--vip").equals("true")
                        && optionValue("--note=").isEmpty());
        ExerciseChecker.check("4 findOption client == Lea, vip == true, remise absente == 0",
                findOption(simulated, "client", "anonyme").equals("Lea") && findOption(simulated, "vip", "false").equals("true")
                        && findOption(simulated, "remise", "0").equals("0"));
        ExerciseChecker.check("4 findOption : la derniere occurrence gagne",
                findOption(new String[]{"--qty=1", "--qty=5"}, "qty", "0").equals("5"));
        ExerciseChecker.check("4 findOption sur args vide == valeur par defaut",
                findOption(new String[0], "client", "anonyme").equals("anonyme"));
        ExerciseChecker.check("5 intOption qty == 3, prix (abc) == 0, age absente == 18",
                intOption(simulated, "qty", 1) == 3 && intOption(simulated, "prix", 0) == 0 && intOption(simulated, "age", 18) == 18);
        ExerciseChecker.check("6 positionalArguments == [pomme, pain]",
                positionalArguments(simulated).equals(List.of("pomme", "pain")));

        ExerciseChecker.summary();
    }
}
