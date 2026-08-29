package ch1_buildingblocks.exercises;

/**
 * EXERCICE 3 - Quiz "ca compile ou pas ?" sur les packages et les imports (niveau : examen OCP)
 * =======================================================================================================
 *
 * Pas de main() a lancer ici directement (les blocs C/D representent
 * plusieurs fichiers reels, marques par "--- Fichier X.java ---") :
 * pour verifier, recree VRAIMENT ces fichiers dans un petit dossier a
 * part, puis compile-les/lance-les avec javac/java. Pour les blocs A
 * et B, un seul fichier suffit. Pour chaque bloc, essaie d'abord de
 * repondre sur une feuille.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   1. import package.* n'importe QUE les classes DIRECTEMENT dans
 *      ce paquet - JAMAIS celles d'un SOUS-paquet (meme s'il porte
 *      un nom qui semble "inclus").
 *   2. Un fichier .java suit TOUJOURS cet ordre STRICT : d'abord
 *      package (si present), PUIS les import (si presents), PUIS
 *      la declaration de classe - jamais un autre ordre.
 *   3. En cas de conflit entre un import PAR NOM DE CLASSE et un
 *      import PAR WILDCARD (les 2 import une classe portant le meme
 *      nom simple), c'est TOUJOURS l'import PAR NOM qui gagne.
 *   4. java.lang est le SEUL paquet a ne JAMAIS avoir besoin d'etre
 *      importe explicitement.
 */
public class Exercise03_PackagesAndImportsQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un importe "java.util.*" puis utilise
    // AtomicInteger (qui vit en realite dans
    // java.util.concurrent.atomic, un SOUS-paquet de java.util) - en
    // pensant que le wildcard "descend" automatiquement dans les
    // sous-dossiers, comme un explorateur de fichiers le ferait.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // import java.util.*;
    // public class A {
    //     static void m() {
    //         java.util.List<Integer> list = new java.util.ArrayList<>();
    //         AtomicInteger a = new AtomicInteger();
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un ecrit son import AVANT le package,
    // peut-etre en pensant "je precise d'abord ce dont j'ai besoin,
    // puis d'ou je viens".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // import java.util.List;
    // package com.example;
    // public class B {
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : mypkg.Date (une classe MAISON, avec son propre
    // toString()) est importee PAR SON NOM. java.util.* est AUSSI
    // importe, en wildcard - et contient LUI AUSSI une classe Date
    // (bien connue). Que se passe-t-il quand on ecrit juste "Date"
    // sans prefixe ?
    //
    // Reponse : (celui-ci n'est pas un "compile ou pas" mais un
    // "quelle classe Date est VRAIMENT utilisee ?")
    // ------------------------------------------------------------------
    // --- Fichier mypkg/Date.java ---
    // package mypkg;
    // public class Date {
    //     public String toString() { return "MyCustomDate"; }
    // }
    // --- Fichier C.java ---
    // import mypkg.Date;
    // import java.util.*;
    // public class C {
    //     public static void main(String[] args) {
    //         Date d = new Date();
    //         System.out.println(d);
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. "java.util.*" ne couvre QUE les
     *   classes DIRECTEMENT dans java.util (comme List, ArrayList) -
     *   AtomicInteger vit dans java.util.concurrent.atomic, un
     *   SOUS-paquet totalement DIFFERENT, jamais couvert par ce
     *   wildcard. Erreur : "cannot find symbol : class
     *   AtomicInteger".
     *
     * Bloc B : NE COMPILE PAS. L'ordre package -> import -> classe
     *   est STRICT et jamais interchangeable. Erreur : "class,
     *   interface, enum, or record expected" (le compilateur ne
     *   s'attend PAS a un "package" a cet endroit du fichier).
     *
     * Bloc C : affiche "MyCustomDate". L'import PAR NOM (mypkg.Date)
     *   l'EMPORTE TOUJOURS sur l'import wildcard (java.util.*), meme
     *   si ce dernier contient AUSSI une classe nommee Date - "Date"
     *   sans prefixe designe donc mypkg.Date, jamais java.util.Date,
     *   des que l'import par nom existe.
     */
}
