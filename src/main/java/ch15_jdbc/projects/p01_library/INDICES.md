# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Se connecter et créer la table

<details><summary>Indice 1</summary>

`DriverManager.getConnection(url, utilisateur, mot de passe)` ouvre la connexion. Tout ce qui l'utilise va **dans** son try-with-resources.

</details>

<details><summary>Indice 2</summary>

`execute` rend `true` seulement si l'ordre produit un `ResultSet` (un `SELECT`). Un `CREATE TABLE` n'en produit pas.

</details>

---

## Étape 2 — Insérer et relire

<details><summary>Indice 1</summary>

Les `?` d'un `PreparedStatement` sont numérotés à partir de **1**. `setString(1, …)`, `setInt(4, …)`…

</details>

<details><summary>Indice 2</summary>

`getInt` rend 0 pour un `NULL` : juste après, `wasNull()` dit si la valeur lue était `NULL`.

</details>

---

## Étape 3 — Paginer, regrouper, modifier

<details><summary>Indice 1</summary>

`LIMIT` et `OFFSET` sont aussi des `?` : `setInt` pour chacun. La page p commence à `(p - 1) * taille`.

</details>

<details><summary>Indice 2</summary>

Une colonne nommée par `AS century` se lit avec `getInt("century")`.

</details>

---

## Étape 4 — Les pièges (`static void pitfalls(Connection)`)

<details><summary>Indice 1</summary>

Chaque piège dans **son** `try`/`catch (SQLException e)`, sinon la 1re erreur empêche les suivantes.

</details>

<details><summary>Indice 2</summary>

`getSQLState()` rend un code de 5 caractères : c'est lui qu'on affiche, jamais le message (qui change d'un pilote à l'autre).

</details>

---

## Étape 5 — L'injection SQL, et la fermeture

<details><summary>Indice 1</summary>

Écris sur papier la requête obtenue en collant `x' OR '1'='1` à la place du titre, apostrophes comprises.

</details>

<details><summary>Indice 2</summary>

Fermer une `Connection` ferme aussi ses `Statement`, qui ferment leurs `ResultSet`.

</details>
