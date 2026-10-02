package inv.app;

import inv.core.Item;
import inv.core.Restock;

import java.lang.module.ModuleDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * SOLUTION du projet 5 - le reassort, puis l'inspection du graphe des modules a l'execution.
 */
public class Main {

    static final List<Item> ITEMS = List.of(new Item("cafe", 12, 30), new Item("the", 7, 14), new Item("sucre", 4, 9), new Item("biscuits", 9, 21),
            new Item("lait", 6, 10), new Item("miel", 11, 25));
    static final int BUDGET = 30;

    // Les noms des modules REQUIS par un module (directives requires), tries.
    static Set<String> requiresOf(String name) {
        ModuleDescriptor d = ModuleLayer.boot().findModule(name).orElseThrow().getDescriptor();
        Set<String> out = new TreeSet<>();
        d.requires().forEach(r -> out.add(r.name()));
        return out;
    }

    // Parcours en profondeur depuis root ; un module est ajoute APRES ses dependances (ordre topologique).
    static void visit(String name, Set<String> seen, List<String> order) {
        if (!seen.add(name)) {
            return;
        }
        for (String dep : requiresOf(name)) {
            visit(dep, seen, order);
        }
        order.add(name);
    }

    public static void main(String[] args) {
        List<Item> chosen = Restock.choose(ITEMS, BUDGET);
        System.out.println("reassort (budget " + BUDGET + ") : " + chosen.stream().map(Item::name).toList() + ", cout "
                + chosen.stream().mapToInt(Item::cost).sum() + ", valeur " + chosen.stream().mapToInt(Item::value).sum());
        System.out.println("journal : " + Restock.loggerName());
        System.out.println("inv.app requiert " + requiresOf("inv.app") + ", inv.core requiert " + requiresOf("inv.core"));
        List<String> order = new ArrayList<>();
        visit("inv.app", new HashSet<>(), order);
        System.out.println("ordre de chargement : " + order);
        ModuleDescriptor app = Main.class.getModule().getDescriptor();
        System.out.println("inv.app : paquets " + app.packages() + ", classe principale " + app.mainClass().orElse("aucune") + ", version "
                + app.version().map(Object::toString).orElse("aucune"));
    }
}
