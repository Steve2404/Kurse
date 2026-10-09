package ch18_design.projects.p07_editor;

import java.util.ArrayList;
import java.util.List;

/**
 * FOURNI (ne pas modifier) : l'ancien editeur de texte. Chaque action passe par un gros switch, l'annulation
 * garde une COPIE COMPLETE du texte apres chaque action, il n'y a pas de "refaire", et l'editeur met a jour
 * lui-meme le compteur de mots de l'ecran. Lance main pour voir ce que coute son annulation.
 */
public final class Data {

    private Data() {
    }

    public static void main(String[] args) {
        LegacyEditor editor = new LegacyEditor();
        editor.apply("insert", 0, "Bonjour le monde");
        editor.apply("delete", 7, "3");
        editor.apply("replace", 0, "o>0");
        System.out.println("texte : " + editor.text + " | mots a l'ecran : " + editor.wordCountLabel);
        editor.undo();
        System.out.println("apres annuler : " + editor.text + " | mots a l'ecran : " + editor.wordCountLabel);
        editor.undo();
        System.out.println("apres annuler encore : " + editor.text + " | mots a l'ecran : " + editor.wordCountLabel);

        LegacyEditor big = new LegacyEditor();
        for (int i = 0; i < 10_000; i++) {
            big.apply("insert", big.text.length(), "x");
        }
        System.out.println("10 000 frappes : " + big.copiesKeptCharacters() + " caracteres gardes pour annuler");
    }

    public static final class LegacyEditor {
        String text = "";
        String wordCountLabel = "0";
        private final List<String> copies = new ArrayList<>();

        /** action : "insert" (arg = texte), "delete" (arg = longueur), "replace" (arg = "cible>remplacement"). */
        public void apply(String action, int position, String arg) {
            copies.add(text);
            switch (action) {
                case "insert":
                    text = text.substring(0, position) + arg + text.substring(position);
                    break;
                case "delete":
                    text = text.substring(0, position) + text.substring(position + Integer.parseInt(arg));
                    break;
                case "replace":
                    String[] parts = arg.split(">");
                    text = text.replace(parts[0], parts[1]);
                    break;
                default:
                    throw new IllegalArgumentException("action inconnue : " + action);
            }
            wordCountLabel = String.valueOf(text.isBlank() ? 0 : text.strip().split("\\s+").length);
        }

        public void undo() {
            if (!copies.isEmpty()) {
                text = copies.remove(copies.size() - 1);
                // oubli : le compteur de mots n'est pas mis a jour ici
            }
        }

        long copiesKeptCharacters() {
            return copies.stream().mapToLong(String::length).sum();
        }
    }
}
