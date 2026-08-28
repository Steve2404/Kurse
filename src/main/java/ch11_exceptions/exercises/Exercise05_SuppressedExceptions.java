package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

/**
 * EXERCICE 5 - Les exceptions "suppressed" (niveau : difficile)
 * ==========================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que le CORPS de ton try-with-resources jette deja une
 * exception (l'action principale a echoue), ET qu'en plus, au moment
 * de refermer la ressource, close() jette ELLE AUSSI sa propre
 * exception (la fermeture a echoue aussi). Java ne peut en laisser
 * remonter qu'UNE SEULE a l'appelant (l'exception PRIMAIRE - celle du
 * corps du try, la cause "la plus importante"), mais il ne veut PAS
 * pour autant perdre la trace de la seconde : il l'attache
 * silencieusement a la premiere, comme "suppressed" (supprimee, mais
 * PAS oubliee). L'appelant peut la retrouver plus tard avec
 * e.getSuppressed().
 *
 *
 * ==================================================================
 * TODO : runWithSuppressed()
 * ==================================================================
 *
 * -- Le plan --
 *
 * Un try-with-resources tout simple, SANS catch ni finally (rappelle-
 * toi de l'Exercise04 : un try-with-resources peut se suffire a
 * lui-meme) :
 *
 *   1. Declarer une FaultyResource (deja fournie plus bas - son
 *      close() jette TOUJOURS une IllegalArgumentException("Erreur
 *      de fermeture")).
 *   2. A l'INTERIEUR du corps du try, lancer une nouvelle
 *      IllegalStateException("Erreur primaire").
 *   3. NE RIEN attraper : laisser l'exception primaire remonter
 *      naturellement hors de la methode (d'ou le "throws
 *      IllegalStateException" deja present sur la signature).
 *
 * -- Ce qu'on remarque --
 *
 * Tu n'as RIEN de special a ecrire pour que l'exception de close()
 * devienne "suppressed" - c'est Java qui s'en charge automatiquement
 * des qu'une DEUXIEME exception survient pendant que la premiere est
 * deja en train de remonter.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un seul try-with-resources suffit.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - public static void runWithSuppressed() throws IllegalStateException {
 *         try (FaultyResource r = new FaultyResource()) {
 *             throw new IllegalStateException("Erreur primaire");
 *         }
 *     }
 *   - Cote appelant : catch (IllegalStateException e) { ...
 *     e.getSuppressed() renvoie un Throwable[], ici de taille 1,
 *     contenant l'IllegalArgumentException de close(). }
 */
public class Exercise05_SuppressedExceptions {

    static class FaultyResource implements AutoCloseable {
        @Override
        public void close() {
            throw new IllegalArgumentException("Erreur de fermeture");
        }
    }

    public static void runWithSuppressed() {
        throw new UnsupportedOperationException("TODO : implementer runWithSuppressed()");
    }

    public static void main(String[] args) {
        Throwable[] suppressed = new Throwable[0];
        String primaryMessage = null;
        try {
            runWithSuppressed();
        } catch (IllegalStateException e) {
            primaryMessage = e.getMessage();
            suppressed = e.getSuppressed();
        }

        ExerciseChecker.check("l'exception primaire est bien l'IllegalStateException du corps du try",
                "Erreur primaire".equals(primaryMessage));
        ExerciseChecker.check("exactement 1 exception suppressed", suppressed.length == 1);
        ExerciseChecker.check("la suppressed est l'IllegalArgumentException de close()",
                suppressed.length == 1
                        && suppressed[0] instanceof IllegalArgumentException
                        && "Erreur de fermeture".equals(suppressed[0].getMessage()));

        ExerciseChecker.summary();
    }
}
