package ch4_coreapis.projects.p02_editor.solution;

import ch4_coreapis.projects.p02_editor.Data;

/**
 * SOLUTION du projet 2 - une conception possible.
 */
public class Editor {

    // StringBuilder : UN objet modifie sur place (une String serait recreee a chaque modification).
    static final StringBuilder buffer = new StringBuilder();
    static String clipboard = "";

    // Pile d'annulation dans un tableau : history[0..size-1], le sommet est history[size-1].
    static final String[] history = new String[Data.HISTORY];
    static int size;

    static void save() {
        if (size == history.length) {
            // Pile pleine : on oublie le plus ancien en decalant tout d'une case vers la gauche.
            System.arraycopy(history, 1, history, 0, history.length - 1);
            size--;
        }
        history[size++] = buffer.toString();
    }

    static String undo() {
        if (size == 0) {
            return "rien a annuler";
        }
        // setLength(0) vide le buffer sans recreer d'objet ; append recharge l'etat sauve.
        buffer.setLength(0);
        buffer.append(history[--size]);
        return "annule";
    }

    static String run(String command) {
        String[] parts = command.split(" ", 2);
        String rest = parts.length > 1 ? parts[1] : "";
        switch (parts[0]) {
            case "APPEND" -> {
                save();
                buffer.append(rest);
            }
            case "INSERT" -> {
                String[] p = rest.split(" ", 2);
                save();
                buffer.insert(Integer.parseInt(p[0]), p[1]);
            }
            case "REPLACE" -> {
                String[] p = rest.split(" ", 3);
                save();
                buffer.replace(Integer.parseInt(p[0]), Integer.parseInt(p[1]), p[2]);
            }
            case "DELETE" -> {
                String[] p = rest.split(" ");
                save();
                buffer.delete(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
            }
            case "DELCHAR" -> {
                save();
                buffer.deleteCharAt(Integer.parseInt(rest));
            }
            case "CUT" -> {
                String[] p = rest.split(" ");
                int start = Integer.parseInt(p[0]);
                int end = Integer.parseInt(p[1]);
                save();
                clipboard = buffer.substring(start, end);
                buffer.delete(start, end);
                return "CUT -> [" + buffer + "] (" + buffer.length() + "), presse-papiers [" + clipboard + "]";
            }
            case "PASTE" -> {
                String[] p = rest.split(" ", 2);
                int position = Integer.parseInt(p[0]);
                if (position > buffer.length()) {
                    return "PASTE -> refuse : position " + position + " hors limites (longueur " + buffer.length() + ")";
                }
                save();
                buffer.insert(position, clipboard);
            }
            case "REVERSE" -> {
                save();
                buffer.reverse();
            }
            case "FIND" -> {
                return "FIND -> \"" + rest + "\" en position " + buffer.indexOf(rest);
            }
            case "UNDO" -> {
                return "UNDO -> " + undo() + " [" + buffer + "] (historique " + size + ")";
            }
            case "LENGTH" -> {
                return "LENGTH -> " + buffer.length() + ", caractere 0 : " + (buffer.length() > 0 ? buffer.charAt(0) : '-');
            }
            default -> {
                return "commande inconnue : " + parts[0];
            }
        }
        return parts[0] + " -> [" + buffer + "] (" + buffer.length() + ")";
    }

    public static void main(String[] args) {
        for (String command : Data.COMMANDS) {
            System.out.println(run(command));
        }

        System.out.println("--- POOL ET EGALITE ---");
        String a = "java";
        String b = "java";
        String c = new String("java");
        String d = c.intern();
        final String half = "ja";
        String e = half + "va";       // constante de compilation : calculee par javac, donc dans le pool
        String part = "ja";
        String f = part + "va";       // calculee a l'execution : nouvel objet
        System.out.println("a == b " + (a == b) + ", a == c " + (a == c) + ", a.equals(c) " + a.equals(c) + ", a == c.intern() " + (a == d));
        System.out.println("constante " + (a == e) + ", variable " + (a == f) + ", variable.intern() " + (a == f.intern()));
        StringBuilder s1 = new StringBuilder("x");
        StringBuilder s2 = new StringBuilder("x");
        // StringBuilder n'a PAS redefini equals : il compare les references, comme ==.
        String equality = "sb.equals " + s1.equals(s2) + ", contenus " + s1.toString().equals(s2.toString());
        StringBuilder s3 = s1.append("y");     // append modifie s1 ET le rend : s3 est le meme objet
        System.out.println(equality + ", s1 == s3 " + (s1 == s3) + ", s1 " + s1 + ", capacite vide " + new StringBuilder(50).length());
        String immutable = "abc";
        immutable.toUpperCase();
        System.out.println("immuable : " + immutable + ", " + immutable.toUpperCase() + ", concat \"" + immutable.concat("d") + "\" et " + immutable);
    }
}
