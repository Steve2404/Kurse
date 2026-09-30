package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 20. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise20_LocalAndAnonymousAccessingPrivateMembers.
 */
public class Solution20_LocalAndAnonymousAccessingPrivateMembers {

    private final String secret;

    public Solution20_LocalAndAnonymousAccessingPrivateMembers(String secret) {
        this.secret = secret;
    }

    interface Revealable {
        String reveal();
    }

    public String revealViaLocalClass() {
        // Methode d'INSTANCE : la classe locale a un objet englobant et lit son champ private.
        class Revealer {
            String reveal() {
                return "Local class voit : " + secret;
            }
        }
        return new Revealer().reveal();
    }

    public String revealViaAnonymousClass() {
        // Meme chose pour une anonyme creee dans une methode d'instance.
        Revealable revealable = new Revealable() {
            @Override
            public String reveal() {
                return "Classe anonyme voit : " + secret;
            }
        };
        return revealable.reveal();
    }
}
