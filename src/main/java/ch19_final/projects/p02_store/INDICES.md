# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — L'ancien code

<details><summary>Indice 1</summary>

Pour écrire la requête reçue par la base, remplace `text` à la main dans `"SELECT title FROM task WHERE title LIKE '%" + text + "%'"`, caractère par caractère, apostrophes comprises. En SQL, `--` commence un commentaire jusqu'à la fin de la ligne.

</details>

<details><summary>Indice 2</summary>

Pour la liste des erreurs, regarde chaque ressource (qui la ferme ?), chaque `catch` (que sait l'appelant après ?), le champ `static`, les valeurs collées dans le SQL même quand ce sont des nombres, et ce que devient une apostrophe dans un titre comme `L'atelier`.

</details>

---

## Étape 2 — Les migrations

<details><summary>Indice 1</summary>

Trois temps dans `apply` : vérifier l'ordre de la liste ; lire ce qui est déjà appliqué (une `Map<Integer, String>` version → description) et vérifier les descriptions ; appliquer ce qui manque. Une petite méthode privée par temps.

</details>

<details><summary>Indice 2</summary>

Les instructions d'une migration n'ont pas de paramètres : un `Statement` suffit, dans `Migrations` seulement. La ligne de `schema_version`, elle, s'écrit avec un `PreparedStatement`, dans le même `tx.write` que les instructions.

</details>

---

## Étape 3 — Le domaine, les erreurs, exécuter autour

<details><summary>Indice 1</summary>

Mets les vérifications dans deux méthodes `static` de `Task` (`checkedTitle`, `checkedPoints`), appelées par les constructeurs compacts de `Task` **et** de `NewTask` : une règle, un seul endroit.

</details>

<details><summary>Indice 2</summary>

Dans `write`, deux `try` imbriqués : l'extérieur (try-with-resources) ouvre et ferme la connexion et traduit la `SQLException` ; l'intérieur fait `run`, `commit`, et dans un `catch (SQLException | RuntimeException e)`, `rollback()` puis `throw e;`.

</details>

---

## Étape 4 — Créer, relire, paginer

<details><summary>Indice 1</summary>

Une seule méthode d'aide `list(Connection c, String sql, Object... parameters)` qui remplit les `?` avec `setObject(i + 1, …)` et transforme chaque ligne en `Task` sert à `find`, `page` et `search`.

</details>

<details><summary>Indice 2</summary>

`page` passe **quatre** paramètres : `column`, `column` (encore, pour `? IS NULL` puis `col = ?`), `size`, et `page * size`. Les vérifications de `page` et `size` se font **avant** d'ouvrir une connexion.

</details>

---

## Étape 5 — Chercher sans injection

<details><summary>Indice 1</summary>

`escapeLike` : un `StringBuilder`, et pour chaque caractère, un `!` devant s'il vaut `!`, `%` ou `_`. Pense à échapper `!` lui-même, sinon `t!` deviendrait le début d'une séquence d'échappement.

</details>

<details><summary>Indice 2</summary>

Le motif complet : `"%" + escapeLike(text.toLowerCase()) + "%"`. Les `%` du début et de la fin sont les **seuls** jokers voulus.

</details>

---

## Étape 6 — Le verrou optimiste

<details><summary>Indice 1</summary>

Écris une méthode privée `updateChecked(Connection c, Task task)` : `move` en aura besoin dans **sa** transaction. Elle rend `new Task(…, task.version() + 1)`.

</details>

<details><summary>Indice 2</summary>

Quand `executeUpdate()` rend 0, une seule question décide : la tâche existe-t-elle ? Un `find` sur **la même connexion** répond (conflit ou introuvable).

</details>

---

## Étape 7 — La transaction

<details><summary>Indice 1</summary>

Dans le lambda de `move` : `find(c, id)`, puis `updateChecked(c, …)` avec la colonne d'arrivée et `expectedVersion`, puis l'`INSERT` dans `task_event`. Tout sur la connexion `c`, qui porte la transaction.

</details>

<details><summary>Indice 2</summary>

Deux `PreparedStatement` dans le même try-with-resources pour `delete` : d'abord `DELETE FROM task_event WHERE task_id = ?`, puis `DELETE FROM task WHERE id = ?`, dont `executeUpdate()` dit si la tâche existait.

</details>

---

## Étape 8 — Les mutants

<details><summary>Indice 1</summary>

Un mutant qui survit touche une règle que tes tests ne regardent pas. Relis le tableau de l'indice 2 et cherche le test qui manque.

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Ce qui change | Le test qui le tue |
|---|---|---|
| 1 | chaque migration est réappliquée | le 2e lancement rend `[]` |
| 2 | une description modifiée est acceptée | le refus de la migration modifiée |
| 3 | deux versions égales sont acceptées | `1 apres 1` |
| 4 | `apply` rend toujours `[]` | le 1er lancement rend `[1, 2, 3]` |
| 5 | pas de transaction (auto-commit) | l'exception au milieu d'un `write` |
| 6 | pas de `commit` : rien n'est enregistré | n'importe quelle écriture relue |
| 7 | `%` et `_` restent des jokers | chercher `50%` |
| 8 | `!` n'est plus échappé | chercher `t!` |
| 9 | la recherche tient compte des majuscules | chercher `remise` |
| 10 | `OFFSET page` au lieu de `page × size` | la 2e page |
| 11 | une taille de 101 est acceptée | la taille 101 refusée |
| 12 | `col <> ?` : les autres colonnes | les pages d'une colonne |
| 13 | l'`UPDATE` ignore la version | Ada et Bob |
| 14 | la version rendue n'augmente pas | la version après `update` |
| 15 | une tâche inconnue donne un conflit | `tache 42 introuvable` |
| 16 | départ et arrivée inversés dans l'historique | le texte de l'historique |
| 17 | l'historique du plus récent au plus ancien | deux déplacements |
| 18 | l'historique n'est pas effacé : la clé étrangère refuse | l'effacement d'une tâche déplacée |
| 19 | le titre garde ses espaces | `"  Selle  "` |
| 20 | 201 caractères acceptés | le titre de 201 caractères |
| 21 | `sqlState()` ne garde que la classe (`22`) | `22001` exact |

</details>
