package io.exercises;

import io.ExerciseChecker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * EXERCICE 9 - Serialiser un objet avec ObjectOutputStream/ObjectInputStream (niveau : difficile)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "Serialiser" un objet, c'est transformer un objet Java VIVANT (en
 * memoire) en une SUITE D'OCTETS qu'on peut sauvegarder (dans un
 * fichier) ou envoyer ailleurs, PUIS "deserialiser" ces octets plus
 * tard pour RECONSTRUIRE un objet EQUIVALENT. Une classe doit
 * implementer Serializable (une interface "marqueur", SANS aucune
 * methode a ecrire) pour avoir le droit d'etre serialisee.
 *
 * -- Le piege du champ transient --
 *
 * Certains champs ne DOIVENT jamais etre sauvegardes (un mot de passe
 * temporaire, une connexion reseau ouverte...) - marquer un champ
 * "transient" dit a Java : "ignore-le completement pendant la
 * serialisation". A la DEserialisation, ce champ ne redevient PAS
 * "comme avant" : il repart a sa valeur PAR DEFAUT (null pour un
 * objet, 0 pour un nombre...), comme s'il n'avait jamais ete
 * initialise.
 *
 *
 * ==================================================================
 * TODO 1 : serialize(player)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Fabriquer un ByteArrayOutputStream (un tampon en memoire, pas
 *      un vrai fichier - pratique pour cet exercice).
 *   2. L'envelopper dans un ObjectOutputStream (try-with-resources).
 *   3. Appeler objectOut.writeObject(player) DANS le try.
 *   4. Renvoyer byteArrayOut.toByteArray() APRES la fermeture du
 *      ObjectOutputStream (il faut qu'il ait fini d'ecrire).
 *
 *
 * ==================================================================
 * TODO 2 : deserialize(data)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Fabriquer un ByteArrayInputStream a partir de 'data'.
 *   2. L'envelopper dans un ObjectInputStream (try-with-resources).
 *   3. Renvoyer (Player) objectIn.readObject() (readObject() lance 2
 *      exceptions checked : IOException et ClassNotFoundException).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul try-with-resources.
 *
 * Exemple a verifier : un Player("Steve", 42, "secret123") serialise
 * puis deserialise redonne un Player avec le MEME nom et le MEME
 * score, MAIS avec sessionToken == null (transient, jamais
 * sauvegarde) - meme si l'objet d'origine avait bien "secret123"
 * dedans.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - writeObject()/readObject() serialisent RECURSIVEMENT tous les
 *     champs non-transient - si un champ objet n'est PAS lui-meme
 *     Serializable, la serialisation echoue avec
 *     NotSerializableException.
 *   - Un serialVersionUID explicite (deja pose sur Player) evite des
 *     soucis de compatibilite si la classe change plus tard - bonne
 *     pratique, meme si l'examen ne l'exige pas forcement.
 */
public class Exercise09_Serialization {

    static class Player implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int score;
        private transient String sessionToken;

        Player(String name, int score, String sessionToken) {
            this.name = name;
            this.score = score;
            this.sessionToken = sessionToken;
        }

        String getName() {
            return name;
        }

        int getScore() {
            return score;
        }

        String getSessionToken() {
            return sessionToken;
        }
    }

    public static byte[] serialize(Player player) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer serialize()");
    }

    public static Player deserialize(byte[] data) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("TODO 2 : implementer deserialize()");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Player original = new Player("Steve", 42, "secret123");

        byte[] data = serialize(original);
        Player restored = deserialize(data);

        ExerciseChecker.check("le nom est bien conserve apres serialisation/deserialisation",
                restored.getName().equals("Steve"));
        ExerciseChecker.check("le score est bien conserve", restored.getScore() == 42);
        ExerciseChecker.check("le champ transient sessionToken est reparti a null (jamais sauvegarde)",
                restored.getSessionToken() == null);
        ExerciseChecker.check("l'objet d'origine, lui, avait bien encore son sessionToken intact",
                original.getSessionToken().equals("secret123"));

        ExerciseChecker.summary();
    }
}
