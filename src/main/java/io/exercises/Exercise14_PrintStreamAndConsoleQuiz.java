package io.exercises;

import io.ExerciseChecker;

import java.io.IOException;

/**
 * EXERCICE 14 - PrintStream/PrintWriter (streams HAUT niveau de mise en forme) + quiz Console (niveau : difficile)
 * ==================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * PrintStream (pour les octets, System.out EN EST UN) et PrintWriter
 * (son equivalent pour les caracteres) sont des streams HAUT NIVEAU
 * (Exercise07) specialises dans la MISE EN FORME : ils ajoutent des
 * methodes tres pratiques comme printf()/format() (des gabarits de
 * texte avec %s, %d, %.2f...) et println(), qu'aucun stream bas
 * niveau n'offre. Contrairement a presque tous les autres streams du
 * chapitre, leurs methodes d'ecriture NE LANCENT PAS d'IOException
 * checked - les erreurs sont juste enregistrees en interne
 * (consultables via checkError()), pour ne jamais casser
 * l'affichage a l'utilisateur avec une exception intempestive.
 *
 * -- Piege a eviter : le formatage depend de la Locale --
 *
 * "%.2f" utilise le separateur decimal de la Locale PAR DEFAUT si on
 * ne precise rien (une virgule en France/Allemagne, un point aux
 * Etats-Unis) - exactement le meme piege que Locale par defaut vu au
 * chapitre exceptions. TOUJOURS preciser Locale.US explicitement dans
 * du code dont le resultat doit etre PREVISIBLE.
 *
 *
 * ==================================================================
 * TODO 1 : formatReport(name, score)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * formatReport("Steve", 42.5) -> "Joueur : Steve, Score : 42.50" (le
 * score a TOUJOURS 2 decimales, meme si la valeur d'origine n'en avait
 * qu'une), suivi du separateur de ligne du systeme.
 *
 * -- Le plan --
 *
 *   1. Fabriquer un ByteArrayOutputStream (un tampon en memoire).
 *   2. L'envelopper dans un PrintStream (avec Locale.US et
 *      StandardCharsets.UTF_8 explicites).
 *   3. Appeler printStream.printf(Locale.US, "Joueur : %s, Score : %.2f%n", name, score).
 *   4. Renvoyer buffer.toString(StandardCharsets.UTF_8).
 *
 *
 * ==================================================================
 * TODO 2 : formatSummary(itemCount)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Fabriquer un StringWriter (l'equivalent "texte" du
 *      ByteArrayOutputStream).
 *   2. L'envelopper dans un PrintWriter.
 *   3. Appeler printWriter.printf(Locale.US, "Total : %d items%n", itemCount),
 *      PUIS printWriter.flush() (un PrintWriter garde parfois des
 *      donnees en attente tant qu'on ne le vide pas explicitement ou
 *      qu'on ne le ferme pas).
 *   4. Renvoyer stringWriter.toString().
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 *
 * ------------------------------------------------------------------
 * QUIZ (pas de code a executer) - la classe Console
 * ------------------------------------------------------------------
 *
 * Rappel de l'Exercise10 : System.console() renvoie null des qu'aucun
 * VRAI terminal interactif n'est attache (comme ici) - impossible donc
 * de tester REELLEMENT le code ci-dessous dans ce projet. Lis-le, et
 * reponds : pourquoi Console.readPassword() est-elle PLUS SURE qu'un
 * simple Scanner ou BufferedReader pour demander un mot de passe ?
 *
 * // Console console = System.console();
 * // if (console != null) {
 * //     char[] password = console.readPassword("Mot de passe : ");
 * //     console.format("Bonjour %s !%n", console.readLine("Nom : "));
 * // }
 *
 * Reponse officielle : readPassword() n'AFFICHE JAMAIS a l'ecran ce
 * que l'utilisateur tape (contrairement a readLine(), qui echo les
 * caracteres tapes) - personne ne peut lire le mot de passe
 * par-dessus l'epaule de l'utilisateur. Elle renvoie en plus un
 * char[] plutot qu'un String, expres : un tableau de char peut etre
 * EFFACE explicitement de la memoire (Arrays.fill(password, ' '))
 * juste apres usage, alors qu'un String, lui, reste potentiellement
 * en memoire (dans le pool de String) bien apres qu'on ait fini de
 * s'en servir, sans aucun moyen fiable de le forcer a disparaitre.
 */
public class Exercise14_PrintStreamAndConsoleQuiz {

    public static String formatReport(String name, double score) {
        throw new UnsupportedOperationException("TODO 1 : implementer formatReport()");
    }

    public static String formatSummary(int itemCount) {
        throw new UnsupportedOperationException("TODO 2 : implementer formatSummary()");
    }

    public static void main(String[] args) throws IOException {
        String report = formatReport("Steve", 42.5);
        ExerciseChecker.check("formatReport() formate bien avec 2 decimales, Locale.US",
                report.equals("Joueur : Steve, Score : 42.50" + System.lineSeparator()));

        String summary = formatSummary(5);
        ExerciseChecker.check("formatSummary() formate bien l'entier",
                summary.equals("Total : 5 items" + System.lineSeparator()));

        ExerciseChecker.summary();
    }
}
