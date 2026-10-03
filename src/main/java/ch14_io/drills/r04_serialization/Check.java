package ch14_io.drills.r04_serialization;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : ana 100 null Banque B false suite 7",
            "D02 : EOFException",
            "D03 : 1 2 1 9 false",
            "D04 : NotSerializableException",
            "D05 : Point[x=1, y=2] [Point[x=3, y=4]] 4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements Serializable", "serialVersionUID", "transient", "static String bank",
            "new ObjectOutputStream(", "new ObjectInputStream(", ".writeObject(", ".readObject()",
            "catch (EOFException", "catch (NotSerializableException", "record Point(",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
