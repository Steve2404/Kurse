package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise16_LocalAndAnonymousAccessingPrivateMembers.
 */
public class Solution16_LocalAndAnonymousAccessingPrivateMembers {

    private final String secret;

    public Solution16_LocalAndAnonymousAccessingPrivateMembers(String secret) {
        this.secret = secret;
    }

    interface Revealable {
        String reveal();
    }

    public String revealViaLocalClass() {
        class Revealer {
            String reveal() {
                return "Local class voit : " + secret;
            }
        }
        return new Revealer().reveal();
    }

    public String revealViaAnonymousClass() {
        Revealable revealable = new Revealable() {
            @Override
            public String reveal() {
                return "Classe anonyme voit : " + secret;
            }
        };
        return revealable.reveal();
    }
}
