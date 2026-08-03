package collections.exercises;

import collections.ExerciseChecker;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * EXERCICE 2 - Le contrat equals()/hashCode() et Set (niveau : moyen)
 * =====================================================================
 *
 * Rappel express de la technique de decoupage en "boites magiques" :
 * regarde Exercise01_ListAlgorithms.java si tu ne l'as pas encore lu.
 * Les 3 questions a te poser sur chaque etape d'un plan : est-ce
 * qu'elle se raconte toute seule (Q1) ? est-ce qu'elle revient
 * plusieurs fois (Q2) ? est-ce qu'elle cache sa propre petite recette
 * (Q3) ? On va s'en resservir plus bas.
 *
 *
 * ==================================================================
 * TODO 1 : corriger hashCode() dans Point
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une garderie avec plein de casiers pour ranger les
 * manteaux. Pour aller plus vite, la maitresse a invente un systeme :
 * au lieu de regarder TOUS les casiers un par un pour trouver un
 * manteau, elle calcule d'abord un "numero de casier probable" a
 * partir du manteau (par exemple, sa couleur), et elle va DIRECTEMENT
 * a ce casier-la. C'est ca, un hashCode : un calcul rapide qui te dit
 * "va voir par la, il y a des chances que ce soit range ici".
 *
 * Mais il y a UNE regle d'or, absolue, qu'on n'a pas le droit de
 * casser : si deux manteaux sont EXACTEMENT le meme manteau (equals
 * dit "oui, ce sont les memes"), alors ils DOIVENT avoir le meme
 * numero de casier probable. Sinon, la maitresse va chercher le
 * deuxieme manteau au mauvais endroit, ne le trouve pas, et pense a
 * tort qu'il n'existe pas encore dans la garderie -> elle le range
 * une deuxieme fois, en double, alors qu'il y etait deja !
 *
 * Dans cet exercice, un Point est considere "le meme" qu'un autre
 * point (equals) uniquement s'ils sont sur la MEME COLONNE (meme x),
 * peu importe la ligne (y) - un peu comme deux enfants consideres
 * "dans la meme equipe" s'ils portent juste le meme numero de dossard,
 * meme s'ils ne se tiennent pas exactement au meme endroit sur le
 * terrain.
 *
 * -- Essayons a la main --
 *
 * Point(1, 2) et Point(1, 3) : meme colonne (x=1) -> equals() dit
 * "ce sont les memes". Donc, d'apres la regle d'or, ils DOIVENT avoir
 * le meme numero de casier (le meme hashCode).
 *
 * Regarde le code de Point plus bas : equals() ne regarde QUE x. Mais
 * hashCode() (le calcul du numero de casier), lui, regarde x ET y.
 * Deux points sur la meme colonne mais des lignes differentes vont
 * donc recevoir des numeros de casier DIFFERENTS, alors qu'equals()
 * les considere identiques. C'est exactement la regle d'or qui est
 * cassee.
 *
 * -- Ce qu'on remarque --
 *
 * La regle est simple a memoriser une fois qu'on l'a vue une fois :
 * le calcul du "numero de casier" (hashCode) ne doit JAMAIS utiliser
 * plus d'informations que ce que regarde equals() pour dire "pareil"
 * ou "pas pareil". Ici, equals() ne regarde que x -> hashCode() doit,
 * lui aussi, ne regarder QUE x.
 *
 * -- Le plan --
 *
 *   1. Regarder ce que equals() compare vraiment (relis le code : ici,
 *      seulement x).
 *   2. Faire en sorte que hashCode() se base EXACTEMENT sur les memes
 *      informations, ni plus, ni moins.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Applique les 3 questions : est-ce que "calculer un numero a partir
 * de x" se raconte tout seul (Q1) ? Oui, mais c'est litteralement une
 * seule ligne (un simple appel a un outil du JDK). Est-ce que ca
 * revient plusieurs fois dans ce fichier (Q2) ? Non. Est-ce que ca
 * cache sa propre petite recette compliquee (Q3) ? Non plus, le JDK
 * fait deja tout le travail compique pour toi. Verdict : PAS besoin
 * d'une boite a part, hashCode() reste une seule ligne.
 *
 * Exemple a verifier : Point(1,2) et Point(1,3) doivent finir dans le
 * MEME "casier" (meme hashCode), Point(2,2) doit finir ailleurs.
 *
 *
 * ==================================================================
 * TODO 2 : symmetricDifference(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pose deux cerceaux de hula-hoop sur le sol, qui se touchent un peu
 * au milieu (comme un diagramme de Venn). Dans le cerceau de gauche,
 * mets tes jouets a toi. Dans celui de droite, les jouets de ton
 * ami. Certains jouets, vous les avez TOUS LES DEUX (la zone ou les
 * deux cerceaux se chevauchent).
 *
 * La difference symetrique, c'est : "tous les jouets qui sont dans UN
 * SEUL des deux cerceaux, jamais dans les deux en meme temps". Les
 * jouets du chevauchement (vous les avez tous les deux) ne comptent
 * PAS.
 *
 * -- Essayons a la main --
 *
 * a = {1, 2, 3} (tes jouets), b = {2, 3, 4} (ceux de ton ami). Le
 * jouet 2 et le jouet 3 : vous les avez tous les deux -> exclus. Le
 * jouet 1 : toi seulement -> garde. Le jouet 4 : ton ami seulement ->
 * garde. Resultat : {1, 4}.
 *
 * -- Le plan --
 *
 *   1. Regarder chaque jouet de a : s'il n'est PAS dans b, le garder.
 *   2. Regarder chaque jouet de b : s'il n'est PAS dans a, le garder.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Les 2 etapes font exactement le meme genre de travail ("regarder
 * une collection, garder ce qui n'est pas dans l'autre"), juste avec
 * les roles de a et b inverses. Q2 (ca revient plusieurs fois) est
 * clairement OUI. Tu pourrais fabriquer une boite du genre
 * ajouterSiAbsentDeLAutre(source, autre, resultat) et l'appeler deux
 * fois. Essaie-le si tu veux t'entrainer au decoupage - ce n'est pas
 * obligatoire ici car les 2 etapes restent courtes, mais c'est un bon
 * exercice de sentir la difference.
 *
 *
 * ==================================================================
 * TODO 3 : caseInsensitiveTreeSet(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un dictionnaire ou tu ranges des mots dans l'ordre
 * alphabetique. Pour toi, "Java" et "java" c'est le MEME mot (juste
 * ecrit avec ou sans majuscule au debut) - tu ne veux pas les deux
 * fois dans le dictionnaire. Mais tu veux garder l'ecriture du PREMIER
 * que tu as rencontre.
 *
 * -- Ce qu'on remarque --
 *
 * Un TreeSet range toujours ses elements tries, et decide que deux
 * elements sont "les memes" des que sa regle de comparaison dit
 * "egaux" (zero difference) - meme si equals() penserait autrement.
 * Il faut donc donner au TreeSet une regle de comparaison qui ignore
 * les majuscules/minuscules.
 *
 * -- Le plan --
 *
 *   1. Creer le TreeSet en lui donnant une regle de tri qui compare
 *      les mots SANS tenir compte de la casse.
 *   2. Ajouter les mots un par un, dans l'ordre recu.
 *
 * Exemple a verifier : ["banane","Java","ananas","java","Ananas"] ->
 * 3 mots gardes (banane, Java, ananas), "Java" garde sa majuscule
 * d'origine.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 * Indice TODO 1 :
 *   - java.util.Objects.hash(x) (un seul argument, pas x et y).
 *
 * Indice TODO 2 :
 *   - for (T item : a) { if (!b.contains(item)) result.add(item); }
 *     puis la meme chose en inversant a et b.
 *
 * Indice TODO 3 :
 *   - new TreeSet<>(Comparator.comparing(String::toLowerCase))
 *   - Piege a retenir pour l'examen OCP : TreeSet considere deux
 *     elements comme des doublons des que le Comparator renvoie 0,
 *     meme si equals() dirait le contraire.
 */
public class Exercise02_SetContract {

    static final class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point)) {
                return false;
            }
            Point other = (Point) o;
            return this.x == other.x;
        }

        @Override
        public int hashCode() {
            // BUG VOLONTAIRE : regarde x ET y, alors qu'equals() ne
            // regarde que x. A corriger (TODO 1).
            return java.util.Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    public static <T> Set<T> symmetricDifference(Set<T> a, Set<T> b) {
        throw new UnsupportedOperationException("TODO 2 : implementer symmetricDifference()");
    }

    public static TreeSet<String> caseInsensitiveTreeSet(Iterable<String> words) {
        throw new UnsupportedOperationException("TODO 3 : implementer caseInsensitiveTreeSet()");
    }

    public static void main(String[] args) {
        Set<Point> points = new HashSet<>();
        points.add(new Point(1, 2));
        points.add(new Point(1, 3)); // meme colonne (x=1) : equals() dit que c'est "le meme" point
        ExerciseChecker.check("HashSet<Point> fusionne (1,2) et (1,3) car equals() ne regarde que x",
                points.size() == 1);
        points.add(new Point(2, 2)); // colonne differente : nouveau point
        ExerciseChecker.check("HashSet<Point> garde (2,2) comme point distinct", points.size() == 2);

        Set<Integer> a = new HashSet<>(java.util.Arrays.asList(1, 2, 3));
        Set<Integer> b = new HashSet<>(java.util.Arrays.asList(2, 3, 4));
        Set<Integer> diff = symmetricDifference(a, b);
        ExerciseChecker.check("symmetricDifference({1,2,3},{2,3,4}) == {1,4}",
                diff.equals(new HashSet<>(java.util.Arrays.asList(1, 4))));

        TreeSet<String> words = caseInsensitiveTreeSet(java.util.Arrays.asList("banane", "Java", "ananas", "java", "Ananas"));
        ExerciseChecker.check("caseInsensitiveTreeSet garde 3 mots distincts (banane, Java, ananas)",
                words.size() == 3);
        ExerciseChecker.check("caseInsensitiveTreeSet conserve la casse d'origine (\"Java\" pas \"java\")",
                words.contains("Java"));

        ExerciseChecker.summary();
    }
}