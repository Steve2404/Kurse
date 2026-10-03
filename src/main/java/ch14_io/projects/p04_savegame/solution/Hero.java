package ch14_io.projects.p04_savegame.solution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION - le heros sauvegarde. Seuls les champs d'INSTANCE non transient de la partie serialisable sont ecrits.
 */
public class Hero extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;       // la "version" de la classe : a changer si le format change
    static int heroes;                                     // static : appartient a la classe, jamais serialise

    private final String name;
    private int level = 1;
    private final List<Item> inventory = new ArrayList<>();  // ArrayList et Item sont serialisables
    private transient int sessionMinutes;                    // transient : non ecrit ; relu a 0

    public Hero(String name) {
        this.name = name;
        heroes++;
        origin = "cree par le constructeur de Hero";
    }

    public void play(int minutes) {
        sessionMinutes += minutes;
    }

    public void levelUp() {
        level++;
    }

    public void loot(Item item) {
        inventory.add(item);
    }

    public void drop(String itemName) {
        inventory.removeIf(i -> i.name().equals(itemName));
    }

    public List<Item> inventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return name + " niv " + level + " " + inventory.stream().map(i -> i.name() + "(" + i.power() + ")").toList() + " session " + sessionMinutes + " min, origine "
                + origin;
    }
}
