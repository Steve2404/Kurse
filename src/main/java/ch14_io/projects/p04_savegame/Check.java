package ch14_io.projects.p04_savegame;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON SaveGame, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "sauve  : Ayla niv 1 [baton(2)] session 45 min, origine cree par le constructeur de Hero",
            "relu   : Ayla niv 1 [baton(2)] session 0 min, origine neuf ; suite fin de sauvegarde 42 puis EOFException",
            "constructeurs : Entity 1 (sans argument, classe mere non serialisable), Item 1 (record), Hero.heroes reste 99 (static), meme objet false",
            "ecriture refusee : NotSerializableException (java.lang.Object)",
            "loot epee 7     -> Bram niv 1 [epee(7)] (pile 1)",
            "level           -> Bram niv 2 [epee(7)] (pile 2)",
            "loot bouclier 4 -> Bram niv 2 [epee(7), bouclier(4)] (pile 3)",
            "undo            -> Bram niv 2 [epee(7)] (pile 2)",
            "loot arc 5      -> Bram niv 2 [epee(7), arc(5)] (pile 3)",
            "level           -> Bram niv 3 [epee(7), arc(5)] (pile 4)",
            "drop epee       -> Bram niv 3 [arc(5)] (pile 5)",
            "undo            -> Bram niv 3 [epee(7), arc(5)] (pile 4)",
            "undo            -> Bram niv 2 [epee(7), arc(5)] (pile 3)",
            "level           -> Bram niv 3 [epee(7), arc(5)] (pile 4)",
            "copie profonde independante : original 2 objets, copie 3");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SANDBOX", "Data.ACTIONS", "implements Serializable", "serialVersionUID",
            "transient", "record Item(", "new ObjectOutputStream(", "new ObjectInputStream(",
            ".writeObject(", ".readObject()", ".writeInt(", ".readInt()",
            "catch (EOFException", "catch (NotSerializableException", "ByteArrayOutputStream", "ByteArrayInputStream",
            "Deque<Hero>",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "SaveGame", args, EXPECTED, API);
    }
}
