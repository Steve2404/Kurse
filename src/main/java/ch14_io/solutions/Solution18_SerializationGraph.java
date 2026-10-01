package ch14_io.solutions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise18_SerializationGraph.
 */
public class Solution18_SerializationGraph {

    public static class Author implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String name;

        public Author(String name) {
            this.name = name;
        }
    }

    public static class Book implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String title;
        public final Author author;

        public Book(String title, Author author) {
            this.title = title;
            this.author = author;
        }
    }

    public static class Library implements Serializable {
        private static final long serialVersionUID = 1L;
        public static String motto = "lire";
        public final List<Book> books;
        public transient int visits = 7;

        public Library(List<Book> books) {
            this.books = books;
        }
    }

    public static class Person {
        public String origin;

        public Person() {
            origin = "constructeur Person";
        }
    }

    public static class Member extends Person implements Serializable {
        private static final long serialVersionUID = 1L;
        public String id;

        public Member(String id) {
            this.id = id;
        }
    }

    public static class Box implements Serializable {
        private static final long serialVersionUID = 1L;
        public final Object content;

        public Box(Object content) {
            this.content = content;
        }
    }

    public static <T extends Serializable> T roundTrip(T object) throws IOException, ClassNotFoundException {
        // Tout en memoire : ecrire vers un tableau d'octets, puis relire ce tableau ; le cast est inevitable (readObject rend Object).
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(object);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            @SuppressWarnings("unchecked")
            T copy = (T) in.readObject();
            return copy;
        }
    }

    public static String describeCopy(Library library) throws IOException, ClassNotFoundException {
        // Un meme objet ecrit deux fois n'est stocke qu'une fois (une reference) : les deux livres partagent la copie de l'auteur.
        // visits est transient : valeur par defaut 0 (l'initialiseur "= 7" ne tourne pas, aucun constructeur de Library non plus).
        Library copy = roundTrip(library);
        boolean sameAuthor = copy.books.get(0).author == copy.books.get(1).author;
        return "livres=" + copy.books.size() + " memeAuteur=" + sameAuthor + " visites=" + copy.visits;
    }

    public static String failureOf(Serializable object) {
        // Tout objet ATTEINT doit etre Serializable : un simple Object dans un champ fait echouer l'ecriture.
        try {
            roundTrip(object);
            return "OK";
        } catch (IOException | ClassNotFoundException e) {
            return e.getClass().getSimpleName();
        }
    }

    public static String originAfterRoundTrip(Member member) throws IOException, ClassNotFoundException {
        // Person n'est pas Serializable : son champ n'est pas sauve, et son constructeur sans argument tourne a la relecture.
        Member copy = roundTrip(member);
        return copy.origin + "|" + copy.id;
    }
}
