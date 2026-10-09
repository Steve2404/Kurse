package ch18_design.projects.p03_storage.solution;

/** La regle des cles, ecrite UNE fois et partagee par tous les stockages : le contrat est le meme pour tous. */
public final class Keys {

    private Keys() {
    }

    // Minuscules, chiffres et / . _ - ; au moins un caractere. Rend la cle pour s'ecrire en une ligne.
    public static String check(String key) {
        if (key == null || !key.matches("[a-z0-9/._-]+")) {
            throw new IllegalArgumentException("cle invalide : " + key);
        }
        return key;
    }
}
