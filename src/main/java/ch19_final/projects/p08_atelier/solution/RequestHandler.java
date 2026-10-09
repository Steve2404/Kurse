package ch19_final.projects.p08_atelier.solution;

/**
 * Tout ce qui sait repondre a une requete : le Router, et les FILTRES qui l'enveloppent (limiteur, mesures).
 * Un filtre est un decorateur (chapitre 18) : il recoit le suivant, fait son travail avant ou apres, et lui passe la main.
 */
@FunctionalInterface
public interface RequestHandler {

    Response handle(Request request);
}
