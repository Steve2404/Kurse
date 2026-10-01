package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * EXERCICE 18 - Serialiser un graphe d'objets : references partagees, transient, static, constructeurs (niveau : avance)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ObjectOutputStream ne sauve pas UN objet : il sauve tout ce qu'on peut
 * atteindre depuis lui (un GRAPHE). Les regles de l'examen, verifiees
 * en direct par main() :
 *
 *   - un objet partage (deux livres, le MEME auteur) reste UN seul objet apres la relecture ;
 *   - un champ transient n'est pas sauve : il revient a sa valeur PAR DEFAUT (0, null, false),
 *     PAS a la valeur de son initialiseur ;
 *   - un champ static n'appartient pas a l'objet : il n'est pas sauve ;
 *   - si un objet atteint n'est pas Serializable : NotSerializableException (une IOException) ;
 *   - a la relecture, AUCUN constructeur de la classe Serializable n'est appele... mais le
 *     constructeur sans argument du 1er parent NON Serializable, lui, est appele.
 *
 *
 * ==================================================================
 * TODO 1 : roundTrip(object)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ecrire l'objet dans un ByteArrayOutputStream avec un ObjectOutputStream.
 *   2. Le relire depuis un ByteArrayInputStream avec un ObjectInputStream ; caster en T.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais roundTrip est la boite magique des TODO 2 a 4.
 *
 *
 * ==================================================================
 * TODO 2 : describeCopy(library)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Faire un roundTrip de library, puis decrire la copie :
 * "livres=N memeAuteur=B visites=V" ou B dit si les deux premiers livres
 * pointent vers le MEME objet Author (==), et V vaut le champ transient visits.
 *
 * -- Essayons a la main --
 *
 *   2 livres du meme auteur, visits = 7 avant -> "livres=2 memeAuteur=true visites=0"
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : roundTrip.
 *
 *
 * ==================================================================
 * TODO 3 : failureOf(object)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. roundTrip(object) dans un try ; rendre "OK" ou le nom simple de l'exception attrapee.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : roundTrip.
 *
 *
 * ==================================================================
 * TODO 4 : originAfterRoundTrip(member)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Member (Serializable) herite de Person (PAS Serializable), dont le
 * constructeur sans argument met origin = "constructeur Person". On change
 * origin avant d'ecrire. Rendre "origin|id" de la copie.
 *
 * -- Essayons a la main --
 *
 *   origin = "modifie", id = "M42" avant -> "constructeur Person|M42"
 *   (le champ du parent non Serializable n'est pas sauve : son constructeur le refabrique)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : roundTrip.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - try (ObjectOutputStream out = new ObjectOutputStream(bytes)) { out.writeObject(object); }
 *   - @SuppressWarnings("unchecked") T copy = (T) in.readObject(); (readObject lance aussi ClassNotFoundException)
 */
public class Exercise18_SerializationGraph {

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
        throw new UnsupportedOperationException("TODO 1 : implementer roundTrip()");
    }

    public static String describeCopy(Library library) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("TODO 2 : implementer describeCopy()");
    }

    public static String failureOf(Serializable object) {
        throw new UnsupportedOperationException("TODO 3 : implementer failureOf()");
    }

    public static String originAfterRoundTrip(Member member) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("TODO 4 : implementer originAfterRoundTrip()");
    }

    public static void main(String[] args) throws Exception {
        Author herbert = new Author("Herbert");
        Book dune = new Book("Dune", herbert);
        Book messiah = new Book("Le Messie de Dune", herbert);
        Book copy = roundTrip(dune);
        ExerciseChecker.check("roundTrip : une copie (pas le meme objet) avec les memes valeurs",
                copy != dune && copy.title.equals("Dune") && copy.author.name.equals("Herbert"));

        Library library = new Library(List.of(dune, messiah));
        library.visits = 42;
        ExerciseChecker.check("describeCopy == livres=2 memeAuteur=true visites=0 (transient -> valeur par defaut)",
                "livres=2 memeAuteur=true visites=0".equals(describeCopy(library)));

        Library.motto = "ecrire";
        Library again = roundTrip(library);
        Library.motto = "relire";
        ExerciseChecker.check("static n'est pas sauve : la copie lit la valeur ACTUELLE de la classe (relire)",
                "relire".equals(Library.motto) && again.books.size() == 2);

        ExerciseChecker.check("failureOf : Box(new Object()) -> NotSerializableException ; Box(\"texte\") -> OK",
                "NotSerializableException".equals(failureOf(new Box(new Object()))) && "OK".equals(failureOf(new Box("texte"))));

        Member member = new Member("M42");
        member.origin = "modifie";
        ExerciseChecker.check("originAfterRoundTrip == constructeur Person|M42",
                "constructeur Person|M42".equals(originAfterRoundTrip(member)));

        ExerciseChecker.summary();
    }
}
