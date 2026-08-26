package io.solutions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise09_Serialization.
 */
public class Solution09_Serialization {

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
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut)) {
            objectOut.writeObject(player);
        }
        return byteArrayOut.toByteArray();
    }

    public static Player deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Player) objectIn.readObject();
        }
    }
}
