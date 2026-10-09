# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les trois surprises

<details><summary>Indice 1</summary>

Pour la surprise 3, regarde ce que fait `setTo` : il **copie** la liste, ou il garde **la même** ?

</details>

<details><summary>Indice 2</summary>

Pour compter les constructeurs : chaque champ facultatif est présent ou absent. Puis regarde les **types** : peut-on avoir à la fois un constructeur `(String from, String to, String subject)` et un `(String from, String to, String body)` ?

</details>

---

## Étape 2 — La fabrique statique

<details><summary>Indice 1</summary>

`private static final Map<String, EmailAddress> CACHE = new ConcurrentHashMap<>();` puis `return CACHE.computeIfAbsent(normalized, EmailAddress::new);` : la référence de constructeur `EmailAddress::new` est appelée seulement si la clé manque.

</details>

<details><summary>Indice 2</summary>

Dans une chaîne Java, le point littéral de l'expression régulière s'écrit `\\.` (le `\` est doublé). `matches` vérifie la chaîne **entière**.

</details>

---

## Étape 3 — L'e-mail immuable et son builder

<details><summary>Indice 1</summary>

La classe imbriquée `Builder` est `static` : elle n'a pas besoin d'un `Email` pour exister. Comme elle est **dans** `Email`, elle a le droit d'appeler le constructeur privé `new Email(this)`, et `Email` a le droit de lire ses champs privés.

</details>

<details><summary>Indice 2</summary>

Une petite méthode privée `require(boolean condition, String message)` qui lance l'`IllegalStateException` rend `build()` court. Pour le doublon : un `HashSet` et `Stream.concat(to.stream(), cc.stream()).filter(a -> !seen.add(a)).findFirst()` (`add` rend `false` si l'élément y était déjà).

</details>

---

## Étape 4 — Le rendu, `toBuilder()` et les fabriques

<details><summary>Indice 1</summary>

Une méthode privée `joined(List<?> items)` : `items.stream().map(Object::toString).collect(Collectors.joining(", "))`, pour les adresses comme pour les pièces jointes.

</details>

<details><summary>Indice 2</summary>

`toBuilder()` : `builder().from(from).subject(subject).body(body).priority(priority)`, puis ajoute les listes une à une dans les listes du builder (`b.to.addAll(to)`, sans oublier `cc` ni `attachments`).

</details>

---

## Étape 5 — Les mutants

<details><summary>Indice 1</summary>

Pour une copie défensive, le test est toujours le même : modifier la source **après** la construction, et vérifier que l'objet construit n'a pas bougé.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Un e-mail sans expéditeur est accepté.
2. Un e-mail sans destinataire est accepté.
3. Un sujet fait d'espaces est accepté.
4. 10 000 Ko pile sont refusés.
5. Une adresse à la fois dans `A` et dans `Cc` n'est plus refusée.
6. L'e-mail garde une **vue** de la liste des destinataires du builder.
7. L'e-mail garde **la même** liste de pièces jointes que le builder.
8. `toBuilder()` oublie les copies (`cc`).
9. La ligne `Cc` s'affiche même vide.
10. La ligne `Priorite` ne s'affiche plus pour `LOW`.
11. La priorité par défaut est `LOW`.
12. `orderReady` n'est plus prioritaire.
13. Les adresses ne sont plus mises en minuscules.
14. Le cache des adresses ne sert plus : chaque appel crée un nouvel objet.
15. Une adresse sans extension (`ada@example`) est acceptée.
16. Une pièce jointe de 0 Ko est acceptée.

</details>
