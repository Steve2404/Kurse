package ch4_coreapis.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise02_StringPoolIdentity.
 */
public class Solution02_StringPoolIdentity {

    public static List<Boolean> buildExpectedComparisons() {
        return List.of(
                true,  // "hello" == "hello" : 2 litteraux identiques -> meme casier du pool
                false, // new String("hello") == "hello" : objet FORCE hors du pool
                true,  // .equals() : compare le CONTENU, peu importe le pool
                true,  // .intern() : va rechercher le casier du pool et en rend l'adresse
                true,  // "hel" + "lo" : 2 litteraux -> fusionnes A LA COMPILATION -> pool
                false  // a + "lo" (a est une variable) : calcule A L'EXECUTION -> hors pool
        );
    }
}
