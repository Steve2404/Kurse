package ch19_final.projects.p08_atelier.solution;

/**
 * Une valeur JSON : exactement l'un des six cas. sealed : le compilateur connait la liste complete,
 * et personne ne peut ajouter un septieme cas ailleurs (le parseur et l'ecrivain peuvent donc tout traiter).
 */
public sealed interface Json permits JsonNull, JsonBool, JsonNumber, JsonString, JsonArray, JsonObject {
}
