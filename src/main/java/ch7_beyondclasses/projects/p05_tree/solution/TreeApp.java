package ch7_beyondclasses.projects.p05_tree.solution;

import ch7_beyondclasses.projects.p05_tree.Data;

/**
 * SOLUTION du projet 5 - l'arbre et les classes imbriquees.
 */
public class TreeApp {

    public static void main(String[] args) {
        SortedTree tree = new SortedTree.Builder().add(Data.VALUES).build();   // classe static imbriquee
        StringBuilder inOrder = new StringBuilder();
        SortedTree.Cursor cursor = tree.cursor();                             // classe interne
        SortedTree.Cursor second = tree.new Cursor();                         // syntaxe explicite : objet.new Interne()
        int third = 0;
        for (int i = 0; i < 3; i++) {
            third = second.next();
        }
        while (cursor.hasNext()) {
            inOrder.append(cursor.next()).append(' ');
        }
        System.out.println("infixe : " + inOrder.toString().strip() + " (" + tree.size() + " valeurs, hauteur " + tree.height() + ", 3e plus petite " + third + ")");

        // Classe ANONYME : elle implemente Visitor sur place ; elle lit sideways (effectively final).
        final StringBuilder sideways = new StringBuilder();
        tree.visitSideways(new SortedTree.Visitor() {
            @Override
            public void visit(int value, int depth) {
                sideways.append("    ".repeat(depth)).append(value).append('\n');
            }
        });
        System.out.print(sideways);

        System.out.println("par niveaux : " + tree.levels());
        System.out.println("entre " + Data.LOW + " et " + Data.HIGH + " : " + tree.countBetween(Data.LOW, Data.HIGH) + " ; plancher(42) " + tree.floor(42)
                + ", plafond(42) " + tree.ceiling(42) + ", plancher(4) " + tree.floor(4) + " ; ancetre(35, 45) " + tree.commonAncestor(35, 45)
                + ", ancetre(5, 65) " + tree.commonAncestor(5, 65));

        // Une deuxieme classe anonyme, avec un etat : elle cumule somme et profondeur maximale.
        int[] stats = new int[2];
        tree.visitSideways(new SortedTree.Visitor() {
            @Override
            public void visit(int value, int depth) {
                stats[0] += value;
                stats[1] = Math.max(stats[1], depth);
            }
        });
        System.out.println("somme " + stats[0] + ", profondeur max " + stats[1]);

        StringBuilder removed = new StringBuilder("suppressions :");
        for (int v : Data.DELETE) {
            removed.append(' ').append(v).append('=').append(tree.remove(v));
        }
        System.out.println(removed + " -> " + tree.levels());

        int[] sorted = new int[tree.size()];
        int k = 0;
        for (SortedTree.Cursor c = tree.cursor(); c.hasNext(); ) {
            sorted[k++] = c.next();
        }
        SortedTree balanced = SortedTree.balanced(sorted);
        System.out.println("equilibre : hauteur " + tree.height() + " -> " + balanced.height() + " ; " + balanced.levels());
    }
}
