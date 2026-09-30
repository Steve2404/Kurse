package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 15 - Les regles des records, ecrites par toi et comparees a 17 verdicts reels de javac (niveau : difficile)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un record R(int x) est une boite a donnees IMMUABLE : le compilateur
 * fabrique le champ private final x, le constructeur canonique R(int x),
 * l'accesseur public x(), equals, hashCode et toString. Tout ce qui
 * menacerait cette immuabilite est interdit.
 *
 * -- Verdicts reels de javac 17 (record R(int x)) --
 *
 *   int y;  (champ d'instance)           -> error: field declaration must be static
 *   static int y;                         -> compile
 *   record R(int x) extends K             -> error: '{' expected (un record herite deja de Record)
 *   implements I                          -> compile
 *   abstract record R                     -> error: modifier abstract not allowed here
 *   final record R                        -> compile (deja implicitement final)
 *   void m() { x = 2; }                   -> error: cannot assign a value to final variable x
 *   R { this.x = x; }  (compact)          -> error: cannot assign a value to final variable x
 *   R { x = Math.abs(x); }  (compact)     -> compile (on modifie le PARAMETRE, affecte ensuite au champ)
 *   R { return; }  (compact)              -> error: invalid compact constructor in record <init>
 *   R(int x) { this.x = x; }  (canonique ecrit en entier) -> compile
 *   R(int x) { }  (canonique qui oublie le champ) -> error: variable x might not have been initialized
 *   R(String s) { }  (autre constructeur) -> error: constructor is not canonical, so its first statement must invoke another constructor of class R
 *   R(String s) { this(s.length()); }     -> compile
 *   int x() { return x; }  (pas public)   -> error: invalid accessor method in record R
 *   public long x() { return x; }         -> error: invalid accessor method in record R (mauvais type)
 *   public int x() { return x; }          -> compile
 *   { System.out.println(); }  (bloc d'instance) -> error: instance initializers not allowed in records
 *   R { } ET R(int x) { this.x = x; }     -> error: constructor R(int) is already defined in record R
 *   record R() { }, methodes static, "withers" (R withX(int nx) { return new R(nx); }) -> compile
 *
 *
 * ==================================================================
 * TODO 1 : declarationError(modifiers, extendsClass, hasInstanceField, hasInstanceInitializer)
 * ==================================================================
 *
 * -- Le plan (dans cet ordre) --
 *
 *   1. extendsClass -> "'{' expected".
 *   2. "abstract" ecrit -> "modifier abstract not allowed here".
 *   3. hasInstanceField -> "field declaration must be static".
 *   4. hasInstanceInitializer -> "instance initializers not allowed in records".
 *   5. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : accessorError(isPublic, sameTypeAsComponent)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Public ET meme type -> "OK" ; sinon "invalid accessor method in record R".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : constructorError(kind, assignsField, hasReturn, firstCallsThis)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * kind vaut "compact" (R { ... }), "canonical" (R(int x) { ... }) ou
 * "other" (une autre liste de parametres). assignsField : le corps
 * ecrit-il this.x = ... ?
 *
 * -- Le plan --
 *
 *   1. compact : hasReturn -> "invalid compact constructor in record <init>" ;
 *      assignsField -> "cannot assign a value to final variable x" ; sinon "OK".
 *   2. canonical : assignsField -> "OK" ; sinon "variable x might not have been initialized".
 *   3. other : firstCallsThis -> "OK" ; sinon
 *      "constructor is not canonical, so its first statement must invoke another constructor of class R".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : generatedMembers(components)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La liste de ce que javac fabrique pour un record, a partir de ses
 * composants ("int x", "String name") : un accesseur par composant,
 * puis equals, hashCode, toString.
 *
 * -- Essayons a la main --
 *
 *   ["int x", "String name"] -> ["public int x()", "public String name()", "equals", "hashCode", "toString"]
 *
 * -- Le plan --
 *
 *   1. Pour chaque composant "type nom" : ajouter "public " + type + " " + nom + "()".
 *   2. Ajouter "equals", "hashCode", "toString".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "int x".split(" ") -> ["int", "x"].
 */
public class Exercise15_RecordRules {

    public static String declarationError(List<String> modifiers, boolean extendsClass, boolean hasInstanceField,
                                          boolean hasInstanceInitializer) {
        throw new UnsupportedOperationException("TODO 1 : implementer declarationError()");
    }

    public static String accessorError(boolean isPublic, boolean sameTypeAsComponent) {
        throw new UnsupportedOperationException("TODO 2 : implementer accessorError()");
    }

    public static String constructorError(String kind, boolean assignsField, boolean hasReturn, boolean firstCallsThis) {
        throw new UnsupportedOperationException("TODO 3 : implementer constructorError()");
    }

    public static List<String> generatedMembers(List<String> components) {
        throw new UnsupportedOperationException("TODO 4 : implementer generatedMembers()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("declarationError() == javac sur 7 cas",
                declarationError(List.of(), true, false, false).equals("'{' expected")
                        && declarationError(List.of("abstract"), false, false, false).equals("modifier abstract not allowed here")
                        && declarationError(List.of(), false, true, false).equals("field declaration must be static")
                        && declarationError(List.of(), false, false, true).equals("instance initializers not allowed in records")
                        && declarationError(List.of("final"), false, false, false).equals("OK")
                        && declarationError(List.of(), false, false, false).equals("OK")
                        && declarationError(List.of("public"), false, false, false).equals("OK"));

        ExerciseChecker.check("accessorError() == javac sur 3 cas",
                accessorError(false, true).equals("invalid accessor method in record R")
                        && accessorError(true, false).equals("invalid accessor method in record R")
                        && accessorError(true, true).equals("OK"));

        String notCanonical = "constructor is not canonical, so its first statement must invoke another constructor of class R";
        ExerciseChecker.check("constructorError() == javac sur 7 cas",
                constructorError("compact", true, false, false).equals("cannot assign a value to final variable x")
                        && constructorError("compact", false, false, false).equals("OK")
                        && constructorError("compact", false, true, false).equals("invalid compact constructor in record <init>")
                        && constructorError("canonical", true, false, false).equals("OK")
                        && constructorError("canonical", false, false, false).equals("variable x might not have been initialized")
                        && constructorError("other", false, false, false).equals(notCanonical)
                        && constructorError("other", false, false, true).equals("OK"));

        ExerciseChecker.check("generatedMembers",
                generatedMembers(List.of("int x", "String name"))
                        .equals(List.of("public int x()", "public String name()", "equals", "hashCode", "toString")));

        ExerciseChecker.summary();
    }
}
