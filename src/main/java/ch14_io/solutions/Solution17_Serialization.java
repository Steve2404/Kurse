package ch14_io.solutions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise17_Serialization.
 */
public class Solution17_Serialization {

    public static class Player implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int score;
        private transient String sessionToken;

        public Player(String name, int score, String sessionToken) {
            this.name = name;
            this.score = score;
            this.sessionToken = sessionToken;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        public String getSessionToken() {
            return sessionToken;
        }
    }

    public static byte[] serialize(Player player) throws IOException {
        // La classe doit implementer Serializable ; le champ transient n'est pas ecrit.
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut)) {
            objectOut.writeObject(player);
        }
        return byteArrayOut.toByteArray();
    }

    public static Player deserialize(byte[] data) throws IOException, ClassNotFoundException {
        // readObject rend Object (cast) et peut lancer ClassNotFoundException ; aucun constructeur de Player n'est appele.
        try (ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Player) objectIn.readObject();
        }
    }
}
