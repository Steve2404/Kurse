package ch4_coreapis.exercises;

/**
 * EXERCICE 15 (CAPSTONE) - Quiz "ca compile ou pas ?" : recapitulatif des pieges du chapitre (niveau : examen OCP)
 * ========================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les quiz des autres chapitres. Pour chaque bloc, c'est TOI la boite
 * magique : lis d'abord l'histoire imagee, essaie de repondre sur une
 * feuille (compile / ne compile pas + pourquoi, ou "quel resultat ?"),
 * PUIS decommente le bloc et essaie de COMPILER/EXECUTER (mvn compile
 * ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * Ce quiz couvre volontairement des pieges DIFFERENTS de ceux deja
 * testes en profondeur dans les exercices precedents - il recapitule
 * les autres phrases-cles du Summary/Exam Essentials du chapitre.
 */
public class Exercise15_CoreApisCapstoneQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un demande l'HEURE (getHour()) directement a
    // une LocalDate, en oubliant qu'une LocalDate ne contient QUE
    // jour/mois/annee - JAMAIS la moindre notion d'heure.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA() {
    //     int h = java.time.LocalDate.now().getHour();
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le probleme INVERSE - quelqu'un demande l'ANNEE
    // (getYear()) a une LocalTime, qui, elle, ne contient JAMAIS la
    // moindre notion de date.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB() {
    //     int y = java.time.LocalTime.now().getYear();
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un essaie d'ajouter des HEURES (plusHours())
    // a une LocalDate - meme erreur de fond que les Blocs A/B, mais
    // cette fois sur une methode de MODIFICATION plutot que de
    // LECTURE.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC() {
    //     java.time.LocalDate d = java.time.LocalDate.now().plusHours(1);
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : quelqu'un essaie de creer une LocalDate avec "new",
    // exactement comme pour n'importe quelle classe normale - en
    // oubliant que LocalDate n'expose JAMAIS de constructeur public
    // (voir Exercise12).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useD() {
    //     java.time.LocalDate d = new java.time.LocalDate(2024, 1, 1);
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : 'a' + 1 + "" - un char, PUIS un int, PUIS un String
    // vide. Beaucoup pensent "un char, c'est presque du texte", et
    // s'attendent a un resultat du genre "a1".
    //
    // Reponse : (celui-ci n'est pas un "compile ou pas" mais un "quel
    // resultat ?" - ecris ta prediction avant de decommenter)
    // ------------------------------------------------------------------
    // static String useE() {
    //     return 'a' + 1 + "";
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. LocalDate n'a AUCUNE methode getHour()
     *   (ni aucun champ d'heure du tout) : elle ne contient QUE une
     *   date. Erreur : "cannot find symbol : method getHour()".
     *
     * Bloc B : NE COMPILE PAS, symetriquement : LocalTime n'a AUCUNE
     *   methode getYear() (ni aucun champ de date du tout).
     *
     * Bloc C : NE COMPILE PAS. LocalDate ne propose QUE des methodes
     *   plusXXX/minusXXX en jours/semaines/mois/annees - JAMAIS en
     *   heures/minutes/secondes, puisqu'elle n'a aucune notion
     *   d'heure a faire avancer.
     *
     * Bloc D : NE COMPILE PAS. Comme dit dans l'Exercise12, LocalDate
     *   n'a AUCUN constructeur public - seules LocalDate.now() et
     *   LocalDate.of(...) permettent d'en creer une. Erreur :
     *   "LocalDate(int,int,int) has private access in LocalDate".
     *
     * Bloc E : Renvoie "98" (une String contenant le TEXTE "98", pas
     *   "a1"). Explication, en lisant de GAUCHE A DROITE (voir
     *   Exercise01) : 'a' est en realite un NOMBRE (son code Unicode,
     *   97) - 'a' + 1 additionne donc NUMERIQUEMENT (97 + 1 = 98, un
     *   int). PUIS 98 (int) + "" (String) : le cote DROIT est un
     *   String, donc la concatenation transforme le NOMBRE 98 en
     *   texte "98". Un char, malgre les apparences, N'EST PAS un
     *   String : il ne "declenche" la concatenation QUE lorsqu'il se
     *   retrouve du meme cote qu'un VRAI String.
     */
}
