# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`TablePrinter.java`](TablePrinter.java), [`Schema.java`](Schema.java) et [`ReportApp.java`](ReportApp.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **H2 2.3.232**, sur la solution, sur une copie modifiée et sur de petits programmes d'essai.

---

## Étape 1 — La base, et ce qu'elle dit d'elle-même

`DatabaseMetaData` permet d'écrire un programme qui **s'adapte** à la base : son nom, son pilote, et ce qu'elle sait faire (`supportsBatchUpdates`, `supportsSavepoints`…).

---

## Étape 2 — Le moteur de tableau

**Question — pourquoi deux passages ?** La largeur d'une colonne dépend de **sa plus longue cellule**, qu'on ne connaît qu'après avoir lu **toutes** les lignes. Le 1er passage mesure, le 2e affiche.

**Question — `getColumnName` d'une colonne calculée ?** Vérifié : pour `price * 2 AS double_prix`, le label **et** le nom valent `DOUBLE_PRIX`. Pour une vraie colonne renommée (`title AS titre`), le label vaut `TITRE` et le nom `TITLE`. Une colonne calculée n'a pas de colonne d'origine : H2 lui donne son alias comme nom. D'autres bases peuvent rendre autre chose : pour l'affichage, utilise `getColumnLabel`.

---

## Étape 3 — Explorer le schéma

**Question — pourquoi en MAJUSCULES ?** Les noms écrits **sans guillemets** sont mis en majuscules par H2 (comme le veut la norme SQL). `books` est enregistré `BOOKS`. Vérifié : `getColumns(null, "PUBLIC", "b", "%")` ne trouve **aucune** colonne, alors que `"B"` en trouve 2.

---

## Étape 4 — Le dump, la copie, la vérification

**Question — pourquoi la 1re suppression échoue-t-elle ?** `AUTHORS` est **référencée** par `BOOKS` (une clé étrangère). La supprimer laisserait des livres pointer vers des auteurs inexistants : la base refuse, avec `23503` (violation de clé étrangère).

**Question — pourquoi `TAGS` arrive-t-il en dernier ?** Il ne dépend de rien, donc il est « prêt » dès le début. Mais `ready` est un `TreeSet` : `pollFirst()` prend toujours le plus petit **dans l'ordre alphabétique**, et `TAGS` vient après `AUTHORS`, `BOOKS`, `PUBLISHERS` et `REVIEWS`. Il ne sort que quand il reste seul.

**Expérience 1 — une table qui se référence elle-même** (vérifié, avec `employees (id, manager_id REFERENCES employees(id))` ajoutée au schéma) : le tri **tient**. `EMPLOYEES` apparaît avec `references {MANAGER_ID=EMPLOYEES}`, prend sa place dans l'ordre de création, et la copie reste identique (`6/6 tables`). C'est parce que les dépendances d'une table sont calculées **sans elle-même** : sinon, elle s'attendrait elle-même pour toujours.

**Expérience 2 — les colonnes de `getTables`** (vérifiées) : `TABLE_CAT`, `TABLE_SCHEM`, `TABLE_NAME`, `TABLE_TYPE`, `REMARKS`, `TYPE_CAT`, `TYPE_SCHEM`, `TYPE_NAME`, `SELF_REFERENCING_COL_NAME`, `REF_GENERATION`. Ce sont les colonnes **imposées par la norme JDBC** : toutes les bases les fournissent.

**Expérience 3 — `getColumns` en minuscules :** rien (voir l'étape 3).
