package ch14_io.projects.p04_savegame.solution;

import ch14_io.projects.p04_savegame.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * SOLUTION du projet 4 - sauvegarder et relire des objets ; une pile d'annulation par copies profondes.
 */
public class SaveGame {

    // Une copie PROFONDE : on serialise en memoire, puis on relit.
    @SuppressWarnings("unchecked")
    static <T> T deepCopy(T object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(object);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (T) in.readObject();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path sandbox = Path.of(Data.SANDBOX);
        Files.createDirectories(sandbox);
        String file = Data.SANDBOX + "/partie.ser";

        Hero hero = new Hero("Ayla");
        hero.loot(new Item("baton", 2));
        hero.play(45);
        int entityBefore = Entity.constructions;
        int itemsBefore = Item.validations;
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(hero);
            out.writeObject("fin de sauvegarde");
            out.writeInt(42);
        }
        Hero.heroes = 99;                                    // modifie la valeur STATIQUE apres la sauvegarde
        Hero loaded;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            loaded = (Hero) in.readObject();                  // readObject rend un Object : on caste
            String marker = (String) in.readObject();
            int number = in.readInt();
            String end;
            try {
                in.readObject();
                end = "encore";
            } catch (EOFException e) {
                end = e.getClass().getSimpleName();
            }
            System.out.println("sauve  : " + hero);
            System.out.println("relu   : " + loaded + " ; suite " + marker + " " + number + " puis " + end);
        }
        System.out.println("constructeurs : Entity " + (Entity.constructions - entityBefore) + " (sans argument, classe mere non serialisable), Item "
                + (Item.validations - itemsBefore) + " (record), Hero.heroes reste " + Hero.heroes + " (static), meme objet " + (loaded == hero));

        // Un champ d'un type NON serialisable fait echouer toute l'ecriture.
        List<Object> withPlainObject = new ArrayList<>(List.of("ok", new Object()));
        try (ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream())) {
            out.writeObject(withPlainObject);
        } catch (NotSerializableException e) {
            System.out.println("ecriture refusee : " + e.getClass().getSimpleName() + " (" + e.getMessage() + ")");
        }

        // La pile d'annulation : avant chaque action, on empile une COPIE PROFONDE de l'etat.
        Hero game = new Hero("Bram");
        Deque<Hero> undo = new ArrayDeque<>();
        for (String action : Data.ACTIONS) {
            String[] p = action.split(" ");
            if (p[0].equals("undo")) {
                game = undo.isEmpty() ? game : undo.pop();
            } else {
                undo.push(deepCopy(game));
                switch (p[0]) {
                    case "loot" -> game.loot(new Item(p[1], Integer.parseInt(p[2])));
                    case "level" -> game.levelUp();
                    default -> game.drop(p[1]);
                }
            }
            System.out.println(String.format("%-16s", action) + "-> " + game.toString().replaceAll(" session.*", "") + " (pile " + undo.size() + ")");
        }
        Hero copy = deepCopy(game);
        copy.loot(new Item("potion", 1));
        System.out.println("copie profonde independante : original " + game.inventory().size() + " objets, copie " + copy.inventory().size());
    }
}
