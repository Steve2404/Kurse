# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le magasin

<details><summary>Indice 1</summary>

Un `PreparedStatement` est préparé (vérifié par la base) **dès sa création** : la table doit déjà exister.

</details>

<details><summary>Indice 2</summary>

`column` construit sa requête par concaténation : c'est sans danger ici, car le nom de colonne vient de ton code, pas d'un utilisateur.

</details>

---

## Étape 2 — Passer les commandes

<details><summary>Indice 1</summary>

`conn.setSavepoint("nom")` pose un point de retour ; `conn.rollback(sp)` défait seulement ce qui suit ce point.

</details>

<details><summary>Indice 2</summary>

Trois `PreparedStatement` dans le même try : le `SELECT price` se fait **après** l'`UPDATE`, mais le prix ne change pas.

</details>

---

## Étape 3 — Le bilan et le contrôle

<details><summary>Indice 1</summary>

`LEFT JOIN` garde les produits **sans** ligne de commande.

</details>

<details><summary>Indice 2</summary>

`COALESCE(x, 0)` remplace un `NULL` par 0 : la somme d'aucune ligne vaut `NULL`.

</details>

---

## Étape 4 — L'API des `Savepoint` (`static void savepoints(Connection)`)

<details><summary>Indice 1</summary>

`setSavepoint()` sans nom crée un point **anonyme**, qui a un numéro (`getSavepointId`) mais pas de nom.

</details>

<details><summary>Indice 2</summary>

`releaseSavepoint` supprime le point : on ne peut plus y revenir.

</details>
