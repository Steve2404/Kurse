package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EXERCICE 1 - Algorithmes sur List (niveau : echauffement / moyen)
 * ==================================================================
 *
 *
 * ==================================================================
 * AVANT TOUT : comment je sais qu'il faut couper un probleme en
 * plusieurs methodes ? (a lire une fois, ca servira pour TOUS les
 * exercices suivants, je ne le reexpliquerai plus a chaque fois)
 * ==================================================================
 *
 * -- Une methode, c'est une "boite magique" --
 *
 * Imagine une boite magique. Tu la nourris avec des ingredients (les
 * parametres). A l'interieur, il se passe quelque chose que tu n'as
 * PAS besoin de regarder. Et il ressort un resultat (la valeur de
 * retour). Tant que la boite fait bien son travail, tu peux t'en
 * servir sans jamais savoir comment elle fonctionne dedans.
 *
 * La grande question n'est pas "comment je fabrique une boite
 * magique" (ca, c'est juste ecrire du Java). La vraie question, celle
 * que personne ne t'explique jamais, c'est : "QUAND est-ce que je
 * dois en fabriquer une nouvelle, plutot que de tout ecrire d'un
 * bloc ?"
 *
 * -- Les 3 questions a te poser sur CHAQUE etape de ton plan --
 *
 * Reprends ton plan ecrit en francais (celui de l'etape 4 de la
 * methode generale). Pour CHAQUE etape numerotee, pose-toi ces 3
 * questions :
 *
 *   Q1. "Est-ce que je peux raconter cette etape en UNE phrase, SANS
 *        parler du reste du probleme ?"
 *       Exemple : "transformer un nombre quelconque en un nombre
 *       simple entre 0 et la taille moins 1" se comprend tout seul,
 *       meme si tu ne sais pas que c'est pour une rotation de liste.
 *       -> Si OUI, c'est un bon candidat pour sa propre boite.
 *
 *   Q2. "Est-ce que je refais exactement ce petit travail plusieurs
 *        fois dans mon plan (meme sur des donnees differentes) ?"
 *       -> Si OUI, fabrique une boite : sinon tu vas recopier le
 *       meme bout de recette plusieurs fois, et le jour ou tu dois le
 *       corriger, tu devras te souvenir de le corriger PARTOUT. Une
 *       seule boite, corrigee une seule fois, utilisee partout.
 *
 *   Q3. "Est-ce que cette etape a sa PROPRE petite recette compliquee
 *        a l'interieur (sa propre boucle, sa propre decision), au
 *        lieu d'etre juste UNE ligne evidente ?"
 *       -> Si OUI, c'est un signe fort qu'elle merite sa propre boite,
 *       pour que ta recette principale reste courte et lisible.
 *
 * Si tu reponds OUI a au moins une de ces 3 questions : fabrique une
 * boite magique separee. Donne-lui un nom (un verbe + ce qu'elle
 * produit), decide quels ingredients elle prend en entree, et ce
 * qu'elle rend en sortie. Si tu reponds NON aux 3 : laisse cette
 * etape comme une simple ligne, directement dans la recette
 * principale. Fabriquer une boite pour une seule ligne evidente, c'est
 * comme emballer un seul bonbon dans un carton geant : ca complique
 * pour rien.
 *
 * -- Exemple concret : appliquons ca au plan de rotate() ci-dessous --
 *
 * Rappelle-toi le plan en 3 etapes qu'on a ecrit plus bas pour
 * rotate() :
 *   1. Ramener k a une valeur simple et positive.
 *   2. Se souvenir de la photo de depart (copier la liste).
 *   3. Pour chaque voiture, calculer sa nouvelle position et la
 *      reposer.
 *
 * Etape 1 ("ramener k") : Q1 -> oui, ca se raconte tout seul ("rendre
 * un nombre de rotation simple et positif"), meme sans parler de
 * listes. Q3 -> oui, ca a sa propre petite formule a part (le
 * modulo). Verdict : BONNE candidate pour sa propre boite magique.
 * On pourrait l'appeler normalizeRotation(k, size), qui prend k et la
 * taille en ingredients, et qui rend le nombre simplifie.
 *
 * Etape 2 ("photo de depart") : c'est important, mais ca tient en une
 * seule ligne evidente (copier la liste). Fabriquer toute une boite
 * magique pour une seule ligne evidente n'apporterait rien : Verdict :
 * PAS besoin d'une boite a part, ca reste une ligne dans la methode
 * principale.
 *
 * Etape 3 ("calculer et reposer chaque voiture") : c'est le coeur du
 * probleme, celui qui utilise le resultat des 2 etapes precedentes.
 * C'est lui qui reste dans la methode principale rotate() elle-meme -
 * une methode principale, c'est aussi une boite magique, juste celle
 * qui orchestre les autres.
 *
 * Retiens la phrase courte : une etape devient sa propre boite des
 * qu'elle SE RACONTE TOUTE SEULE, qu'elle REVIENT PLUSIEURS FOIS, ou
 * qu'elle CACHE SA PROPRE PETITE RECETTE. Sinon, elle reste une ligne
 * toute simple dans la grande recette.
 *
 * -- A toi de jouer (TODO 1b, bonus, apres avoir fini TODO 1) --
 *
 * Une fois que rotate() fonctionne (meme ecrit d'un seul bloc, sans
 * boite separee), reviens ici et entraine-toi : extrais toi-meme un
 * petit helper prive nomme normalizeRotation(int k, int size) qui
 * rend uniquement le k simplifie, et fais appeler ce helper par
 * rotate(). Ne me demande pas de le faire a ta place : c'est TOI qui
 * dois sentir, avec tes mains, la difference entre le code "avant
 * decoupage" et "apres decoupage". Relis ton code apres : est-ce que
 * rotate() est plus facile a lire maintenant que la partie "calcul
 * savant" est rangee dans sa propre boite bien nommee ?
 *
 *
 * ==================================================================
 * TODO 1 : rotate(list, k)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine 5 enfants qui font la ronde, en file indienne, en se tenant
 * par la main : Alice, Bob, Chloe, David, Emma (dans cet ordre).
 *
 * La maitresse dit : "Les 2 derniers de la file, venez devant tout le
 * monde, sans lacher la main de vos voisins, et sans changer l'ordre
 * entre vous."
 *
 * David et Emma (les 2 derniers) passent devant. La nouvelle file
 * devient : David, Emma, Alice, Bob, Chloe.
 *
 * C'est exactement ca, "faire tourner une liste de k=2 vers la
 * droite" : les k derniers elements passent devant, et tout le monde
 * garde le meme ordre entre eux.
 *
 * -- Essayons a la main, avec des jouets --
 *
 * Pose 5 petites voitures sur la table, numerotees 1 2 3 4 5, dans
 * cet ordre de gauche a droite. On veut tourner de k=2 vers la
 * droite. Resultat attendu : 4 5 1 2 3.
 *
 * Fais-le vraiment, avec ta main, en deplacant les voitures 4 et 5
 * devant les voitures 1 2 3. Regarde bien : est-ce que l'ordre ENTRE
 * 4 et 5 a change ? Non. Est-ce que l'ordre ENTRE 1, 2 et 3 a change ?
 * Non plus. Seul l'endroit ou ils sont assis a change.
 *
 * -- Ce qu'on remarque --
 *
 * Chaque voiture, en fait, se contente de "glisser" de quelques places
 * vers la droite. La voiture 1 etait a la position 0 (la 1ere), elle
 * finit a la position 2 (la 3eme). La voiture 4 etait a la position 3,
 * elle finit a la position 0. Chaque voiture avance de k=2 places, et
 * quand elle depasse le bout de la table, elle revient au debut (comme
 * les aiguilles d'une horloge qui reviennent a minuit apres minuit
 * moins une).
 *
 * -- Le piege des grands nombres et des nombres negatifs --
 *
 * Que se passe-t-il si la maitresse dit "avancez de 7 places" alors
 * qu'il n'y a que 5 enfants ? Fais un tour complet de la ronde (5
 * places) et il te restera 2 places a faire : avancer de 7, avec 5
 * enfants, revient exactement au meme resultat qu'avancer de 2. Et si
 * elle dit "avancez de -1" (un nombre negatif) ? Avancer de -1 vers la
 * droite, c'est pareil que reculer de 1, ce qui revient a avancer de
 * 4 vers la droite (5 - 1). Un enfant n'a pas besoin de calcul savant
 * pour ca : il compte, tout simplement, en boucle. Le calcul qui fait
 * ca proprement pour du code s'appelle un modulo (le reste d'une
 * division) : c'est indique dans les indices si tu en as besoin.
 *
 * -- Le plan, etape par etape, avant d'ecrire une seule ligne de Java --
 *
 *   1. Ramener k a une valeur simple et positive, entre 0 et
 *      (taille de la liste - 1), en tenant compte du tour complet de
 *      la ronde et des nombres negatifs.
 *   2. Se souvenir de "qui est ou" dans la liste de depart (comme une
 *      photo avant de bouger les voitures), car on va deplacer les
 *      objets EN PLACE et on ne veut pas ecraser une information dont
 *      on a encore besoin.
 *   3. Pour chaque voiture de la photo de depart, calculer sa NOUVELLE
 *      position (position de depart + k, avec le "retour au debut" si
 *      on depasse le bout de la table), et la reposer a cette place
 *      dans la vraie liste.
 *
 * Refais ce plan a la main sur les 5 voitures avec k=2, en ecrivant
 * sur une feuille "position de depart -> position d'arrivee" pour
 * chacune, AVANT de regarder la contrainte technique ci-dessous.
 *
 * Contraintes techniques (une fois que le plan a la main fonctionne) :
 *   - Vous modifiez l'objet 'list' recu (vous ne retournez pas une
 *     nouvelle liste, exactement comme les enfants se deplacent
 *     eux-memes, on ne cree pas de "faux enfants").
 *   - Interdiction d'utiliser java.util.Collections.rotate(...) (ce
 *     serait demander a quelqu'un d'autre de bouger les enfants a
 *     votre place).
 *   - Vous avez le droit d'utiliser au maximum UNE liste/tableau
 *       auxiliaire de la meme taille que 'list' (c'est votre "photo
 *       de depart" de l'etape 2, pas plus).
 *
 * Exemple a verifier : [1,2,3,4,5] avec k=2 -> [4,5,1,2,3]
 *
 *
 * ==================================================================
 * TODO 2 : distinctPreservingOrder(list)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu ranges tes petites voitures dans une boite, une par
 * une, dans l'ordre ou tu les attrapes par terre : voiture rouge,
 * voiture bleue, voiture rouge (encore une !), voiture verte, voiture
 * bleue (encore une !), voiture jaune.
 *
 * Ta maman te dit : "Range-les dans la boite, mais n'y mets jamais
 * deux fois la meme couleur, et garde l'ordre dans lequel tu as
 * decouvert chaque nouvelle couleur pour la premiere fois."
 *
 * -- Essayons a la main --
 *
 * Prends les couleurs une par une, dans l'ordre : rouge, bleue,
 * rouge, verte, bleue, jaune. Pour chacune, avant de la mettre dans
 * la boite, regarde DANS LA BOITE (pas dans le tas de depart) si
 * cette couleur y est deja.
 *
 *   - rouge : la boite est vide -> je la mets. Boite : [rouge]
 *   - bleue : pas dans la boite -> je la mets. Boite : [rouge, bleue]
 *   - rouge : deja dans la boite ! -> je ne la mets pas.
 *   - verte : pas dans la boite -> je la mets. Boite : [rouge, bleue, verte]
 *   - bleue : deja dans la boite ! -> je ne la mets pas.
 *   - jaune : pas dans la boite -> je la mets. Boite : [rouge, bleue, verte, jaune]
 *
 * -- Ce qu'on remarque --
 *
 * A chaque nouvel objet du tas de depart, il n'y a que DEUX questions
 * a se poser : "est-ce que je l'ai deja mis dans ma boite ?" et si
 * non, "je le mets". Rien de plus compique que ca.
 *
 * -- Le plan, etape par etape --
 *
 *   1. Preparer une boite vide (une nouvelle liste resultat).
 *   2. Prendre chaque element du tas de depart, dans l'ordre.
 *   3. Pour chacun, regarder si la boite le contient deja.
 *   4. S'il n'y est pas encore, l'ajouter a la boite. S'il y est deja,
 *      passer au suivant sans rien faire.
 *
 * Refais ce plan a la main avec [3,1,3,2,1,4] (des chiffres a la place
 * des couleurs) sur une feuille, en dessinant ta "boite" qui grandit
 * a chaque etape, avant de regarder la contrainte technique ci-dessous.
 *
 * Contrainte technique (une fois que le plan a la main fonctionne) :
 *   - Interdiction d'utiliser un Set ou une Map (l'exercice porte sur
 *     List ; le but est de sentir avec les mains pourquoi "regarder si
 *     la boite contient deja la couleur" devient lent quand la boite
 *     est tres grande, et pourquoi un Set resoudrait exactement ce
 *     souci plus tard).
 *
 * Exemple a verifier : [3,1,3,2,1,4] -> [3,1,2,4]
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 * Indice TODO 1 :
 *   - k peut etre normalise avec un modulo : k = ((k % size) + size) % size
 *     pour gerer les k negatifs et les k > size.
 *
 * Indice TODO 2 :
 *   - "Regarder si la boite contient deja" se traduit par
 *     result.contains(element) avant de l'ajouter a 'result'.
 *   - Question a te poser toi-meme : combien de fois, au pire, la
 *     methode contains() doit-elle regarder toute la boite ? Et
 *     combien de fois fait-on ce controle en tout ? Qu'est-ce que ca
 *     donne comme complexite totale (indice : ca s'appelle O(n^2)) ?
 */
public class Exercise01_ListAlgorithms {

    public static <T> void rotate(List<T> list, int k) {
        throw new UnsupportedOperationException("TODO 1 : implementer rotate()");
    }

    public static <T> List<T> distinctPreservingOrder(List<T> list) {
        throw new UnsupportedOperationException("TODO 2 : implementer distinctPreservingOrder()");
    }

    public static void main(String[] args) {
        List<Integer> l1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        rotate(l1, 2);
        ExerciseChecker.check("rotate([1,2,3,4,5], 2) == [4,5,1,2,3]", l1.equals(Arrays.asList(4, 5, 1, 2, 3)));

        List<Integer> l2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        rotate(l2, -1);
        ExerciseChecker.check("rotate([1,2,3,4,5], -1) == [2,3,4,5,1]", l2.equals(Arrays.asList(2, 3, 4, 5, 1)));

        List<Integer> l3 = new ArrayList<>(Arrays.asList(1, 2, 3));
        rotate(l3, 7);
        ExerciseChecker.check("rotate([1,2,3], 7) == [3,1,2]  (7 % 3 == 1)", l3.equals(Arrays.asList(3, 1, 2)));

        List<Integer> source = Arrays.asList(3, 1, 3, 2, 1, 4);
        List<Integer> distinct = distinctPreservingOrder(source);
        ExerciseChecker.check("distinctPreservingOrder([3,1,3,2,1,4]) == [3,1,2,4]",
                distinct.equals(Arrays.asList(3, 1, 2, 4)));

        ExerciseChecker.summary();
    }
}