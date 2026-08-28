package ch5_methods.exercises;

/**
 * EXERCICE 2 - Quiz "ca compile ou pas ?" sur les modificateurs d'acces (niveau : examen OCP)
 * =====================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main. Chaque
 * bloc represente PLUSIEURS fichiers .java reels (marques par des
 * commentaires "--- Fichier X.java ---") : pour verifier, recree
 * VRAIMENT ces fichiers dans un petit dossier a part (avec les bons
 * sous-dossiers pkgA/ et pkgB/ pour les paquets), puis compile-les
 * ensemble avec javac. Pour chaque bloc, essaie d'abord de repondre
 * sur une feuille (compile / ne compile pas + pourquoi).
 *
 * -- Les 4 niveaux d'acces, du plus etroit au plus large --
 *
 *   1. private : accessible UNIQUEMENT depuis l'interieur de la
 *      classe (top-level) qui le declare - meme pas depuis une AUTRE
 *      classe du meme fichier.
 *   2. package (aucun mot-cle ecrit) : accessible depuis N'IMPORTE
 *      QUELLE classe du MEME paquet, meme dans un fichier different.
 *   3. protected : accessible comme "package", PLUS depuis les
 *      SOUS-CLASSES situees dans un AUTRE paquet - mais UNIQUEMENT
 *      via un acces "herite" (this, ou une variable du VRAI type de
 *      la sous-classe), JAMAIS via une reference du type PARENT
 *      recue en parametre.
 *   4. public : accessible depuis absolument n'importe ou.
 */
public class Exercise02_AccessModifiersQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : Holder garde un secret PRIVATE. A, une AUTRE classe
    // TOP-LEVEL du MEME fichier, essaie quand meme d'y jeter un
    // oeil - en pensant que "meme fichier" suffit, comme pour des
    // classes imbriquees.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // --- Fichier A.java (un seul fichier, 2 classes top-level) ---
    // class Holder {
    //     private int secret = 42;
    // }
    // public class A {
    //     static void m() {
    //         Holder h = new Holder();
    //         int x = h.secret;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : packageField n'a AUCUN modificateur ecrit (acces
    // "package" par defaut). UserSamePackage, dans un fichier
    // DIFFERENT mais du MEME paquet pkgA, essaie d'y acceder.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // --- Fichier pkgA/Holder.java ---
    // package pkgA;
    // class Holder {
    //     int packageField = 42;
    // }
    // --- Fichier pkgA/UserSamePackage.java ---
    // package pkgA;
    // public class UserSamePackage {
    //     static void m() {
    //         Holder h = new Holder();
    //         int x = h.packageField;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : le meme champ "package" (packageField), mais cette
    // fois sur une classe PUBLIC (PublicHolder), et l'appelant
    // (UserOtherPackage) habite un paquet DIFFERENT (pkgB) - "package"
    // ne "voyage" JAMAIS d'un paquet a l'autre, meme si la classe qui
    // le porte, elle, est publique.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // --- Fichier pkgA/PublicHolder.java ---
    // package pkgA;
    // public class PublicHolder {
    //     int packageField = 42;
    // }
    // --- Fichier pkgB/UserOtherPackage.java ---
    // package pkgB;
    // import pkgA.PublicHolder;
    // public class UserOtherPackage {
    //     static void m() {
    //         PublicHolder h = new PublicHolder();
    //         int x = h.packageField;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : Parent.protectedField est protected. ChildD, dans un
    // AUTRE paquet (pkgB), en herite (extends Parent) et y accede via
    // this - le VRAI type de l'objet est bien ChildD, donc "herite".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // --- Fichier pkgA/Parent.java ---
    // package pkgA;
    // public class Parent {
    //     protected int protectedField = 42;
    // }
    // --- Fichier pkgB/ChildD.java ---
    // package pkgB;
    // import pkgA.Parent;
    // public class ChildD extends Parent {
    //     void m() {
    //         int x = this.protectedField;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : ChildE, elle AUSSI dans pkgB et heritant de Parent,
    // recoit cette fois un AUTRE objet Parent en PARAMETRE (other) -
    // et essaie de lire protectedField DESSUS, plutot que sur
    // elle-meme.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // --- Fichier pkgA/Parent.java ---
    // package pkgA;
    // public class Parent {
    //     protected int protectedField = 42;
    // }
    // --- Fichier pkgB/ChildE.java ---
    // package pkgB;
    // import pkgA.Parent;
    // public class ChildE extends Parent {
    //     void m(Parent other) {
    //         int x = other.protectedField;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : publicField est public. UnrelatedUser, dans pkgB, SANS
    // AUCUN lien d'heritage avec PublicParent (pkgA), y accede quand
    // meme directement.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // --- Fichier pkgA/PublicParent.java ---
    // package pkgA;
    // public class PublicParent {
    //     public int publicField = 42;
    // }
    // --- Fichier pkgB/UnrelatedUser.java ---
    // package pkgB;
    // import pkgA.PublicParent;
    // public class UnrelatedUser {
    //     void m() {
    //         PublicParent p = new PublicParent();
    //         int x = p.publicField;
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. private signifie "uniquement depuis
     *   L'INTERIEUR de la classe qui le declare" - meme une AUTRE
     *   classe TOP-LEVEL du meme fichier n'y a pas acces (contrairement
     *   aux classes IMBRIQUEES d'une meme classe englobante, qui,
     *   elles, PEUVENT s'echanger leurs membres private - voir le
     *   chapitre "Beyond Classes"). Erreur : "secret has private
     *   access in Holder".
     *
     * Bloc B : COMPILE. "package" (aucun mot-cle) est accessible
     *   depuis TOUT le meme paquet, meme d'un fichier a l'autre.
     *
     * Bloc C : NE COMPILE PAS. "package" ne depend QUE du paquet du
     *   MEMBRE (packageField), jamais de la visibilite de la classe
     *   qui le porte - PublicHolder a beau etre public, son champ
     *   packageField, lui, reste bloque a pkgA. Erreur : "packageField
     *   is not public in PublicHolder; cannot be accessed from
     *   outside package".
     *
     * Bloc D : COMPILE. ChildD accede a protectedField via this,
     *   c'est-a-dire via son PROPRE type (ChildD, une sous-classe
     *   reelle de Parent) - exactement le cas d'usage que protected
     *   autorise entre paquets differents.
     *
     * Bloc E : NE COMPILE PAS. Meme heritage, mais cette fois l'acces
     *   se fait via "other", une variable de type PARENT (pas
     *   ChildE) - protected, depuis un AUTRE paquet, n'autorise QUE
     *   l'acces "en tant que sous-classe" (via this ou une variable
     *   du type de la sous-classe), jamais via une reference generique
     *   du type parent, meme recue par une sous-classe legitime.
     *   Erreur : "protectedField has protected access in Parent".
     *
     * Bloc F : COMPILE. public est accessible depuis absolument
     *   n'importe ou, sans aucune condition de paquet ni d'heritage.
     */
}
