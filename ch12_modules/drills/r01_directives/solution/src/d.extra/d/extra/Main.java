package d.extra;

/** SOLUTION - d.open n'est pas resolu a l'execution sauf si on l'ajoute (--add-modules) ou si un autre module le requiert. */
public class Main {
    public static void main(String[] args) {
        System.out.println("d.open present " + ModuleLayer.boot().findModule("d.open").isPresent());
    }
}
