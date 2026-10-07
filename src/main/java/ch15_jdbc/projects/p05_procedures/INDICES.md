# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Écrire et enregistrer les procédures

<details><summary>Indice 1</summary>

`CREATE ALIAS NOM FOR "paquet.Classe.methode"` : H2 retrouve ta méthode par son nom complet, d'où `Classe.class.getName()`.

</details>

<details><summary>Indice 2</summary>

Luhn : parcours la chaîne sans espaces de la fin vers le début ; le rang `i` (0 pour le dernier chiffre) dit s'il faut doubler.

</details>

---

## Étape 2 — OUT et IN OUT

<details><summary>Indice 1</summary>

Dans `{? = call F(?, ?)}`, le `?` de gauche est le paramètre **1** : c'est lui qu'on déclare avec `registerOutParameter`.

</details>

<details><summary>Indice 2</summary>

En IN OUT, le **même** paramètre reçoit une valeur (`setString`) et en rend une (`registerOutParameter`, puis `getString`).

</details>

---

## Étape 3 — Les procédures qui travaillent sur la base

<details><summary>Indice 1</summary>

Une méthode dont le 1er paramètre est une `Connection` reçoit la connexion de l'appelant : H2 la passe tout seul.

</details>

<details><summary>Indice 2</summary>

Une procédure qui rend un `ResultSet` ne doit pas fermer ce qui le produit.

</details>

---

## Étape 4 — Les erreurs

<details><summary>Indice 1</summary>

Chaque appel fautif dans son `try`/`catch (SQLException e)`, et affiche `getSQLState()`.

</details>

<details><summary>Indice 2</summary>

Pour l'oubli, prépare l'appel avec un paramètre IN non rempli, puis `execute()`.

</details>
