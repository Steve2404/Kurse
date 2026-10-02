package ch5_methods.drills.r03_access.solution.shop;

/**
 * SOLUTION - un article, avec les quatre niveaux d'acces.
 */
public class Item {

    public String name = "stylo";      // partout
    protected int stock = 5;           // meme paquet + sous-classes
    int code = 42;                     // meme paquet seulement (package-private)
    private int secret = 7;            // cette classe seulement

    public int secret() {
        return secret;                 // le prive se lit par une methode publique
    }

    protected static String label() {
        return "article";
    }

    void restock(int n) {
        stock += n;
    }
}
