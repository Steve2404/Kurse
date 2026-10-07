# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Book.java`](Book.java), [`Catalog.java`](Catalog.java) et [`LibraryApp.java`](LibraryApp.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **H2 2.3.232**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Se connecter et créer la table

**Question — pourquoi `execute` rend-il `false` ?** `execute` rend `true` quand l'ordre produit un **`ResultSet`** (un `SELECT`), et `false` sinon. Un `CREATE TABLE` ne produit pas de lignes : il rend `false`. `executeUpdate` rend un **nombre de lignes** touchées : 0 pour un ordre de structure comme `CREATE INDEX`.

---

## Étape 2 — Insérer et relire

**Question — `getInt("pages")` sans `wasNull()` ?** Il rend **0** pour un `NULL` (vérifié) : un `int` ne peut pas valoir `null`. On ne pourrait pas distinguer « 0 page » de « nombre inconnu ». `wasNull()`, appelé juste après, rend `true` si la dernière valeur lue était `NULL`. (`getObject` rendrait directement `null`.)

---

## Étape 3 — Paginer, regrouper, modifier

**Question — pourquoi `pages = pages + 10` laisse-t-il `NULL` ?** En SQL, **tout calcul avec `NULL` donne `NULL`** : `NULL` veut dire « inconnu », et « inconnu + 10 » reste inconnu. Vérifié : après `UPDATE b SET pages = pages + 10`, une ligne à `NULL` reste `NULL`, une ligne à 100 passe à 110.

---

## Étape 4 — Les pièges

**Les codes obtenus** (vérifiés) :

| Piège | SQLState |
|---|---|
| `getString` avant `next()` | `02000` (pas de donnée) |
| colonne 0 | `90008` (index invalide : les colonnes commencent à 1) |
| colonne inconnue | `42S22` |
| `executeUpdate` d'un `SELECT` | `90001` |
| `executeQuery` d'un `DELETE` | `90002` |
| table inconnue | `42S02` |
| `next` sur un `ResultSet` fermé | `90007` |

Une 2e requête sur le **même** `Statement` ferme le `ResultSet` précédent (`isClosed()` vaut `true`).

**Question — lesquels changeraient avec PostgreSQL ?** Les codes de la forme `9xxxx` sont **propres à H2** : ils changeraient. `02000`, `42S22` et `42S02` viennent de la norme SQL (classes `02` « pas de donnée » et `42` « erreur de syntaxe ou d'accès ») : d'autres bases utilisent les mêmes, ou des codes voisins de la même classe. Un programme portable teste plutôt les **deux premiers** caractères (la classe).

---

## Étape 5 — L'injection SQL, et la fermeture

**Question — la requête complète avec `Data.INJECTION` :**

```sql
SELECT COUNT(*) FROM books WHERE title = 'x' OR '1'='1'
```

Le texte de l'utilisateur a **fermé** l'apostrophe, puis ajouté une condition toujours vraie (`'1'='1'`) : la requête compte **tous** les livres (9). Avec un `PreparedStatement`, le texte entier est **une seule valeur** : la base cherche un titre égal à `x' OR '1'='1`, et n'en trouve aucun. De même, `L'Etranger` casse la requête collée (`42000`, erreur de syntaxe), mais pas la requête préparée.

**Question — qui ferme quoi ?** Fermer la `Connection` ferme tous ses `Statement`, et fermer un `Statement` ferme son `ResultSet`. C'est pourquoi `kept`, utilisé après le try, est inutilisable : `90007`.

**Expérience 1 — les URL fautives** (vérifiées) :
- `jdbc:h2:men:p01` : le pilote H2 **reconnaît** `jdbc:h2:`, mais ne comprend pas `men:p01` comme un nom de base : `HY000`, `General error … Illegal char <:> … men:p01` ;
- `jdbc:hh2:mem:p01` : **aucun** pilote ne reconnaît `jdbc:hh2:` : `08001`, `No suitable driver found`.

**Expérience 2 — une 2e connexion après la fermeture :** la table **n'existe plus** (vérifié : `42S04`, `Table "B" not found (this database is empty)`). Une base `mem:` vit en mémoire, et disparaît quand sa **dernière** connexion se ferme.

**Expérience 3 — `setString(6, …)` sur 5 paramètres :** `90008`, `Invalid value "6" for parameter "parameterIndex"` (vérifié).
