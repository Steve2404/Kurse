# Drill de rappel 4 — Transactions et `Savepoint`

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p03.

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall04`** dans le paquet `ch15_jdbc.drills.r04_transactions`. Le `main` déclare `throws SQLException`.
- Ajoute `static int stock(Connection c)` : `SELECT qty FROM stock WHERE item = 'pomme'`, lu avec `getInt(1)`.
- Un seul try-with-resources pour **deux** connexions vers `jdbc:h2:mem:r04` (`conn` et `other`), et `st = conn.createStatement()`. Puis :
  - `CREATE TABLE stock (item VARCHAR(10) PRIMARY KEY, qty INT NOT NULL CHECK (qty >= 0))` ;
  - `INSERT INTO stock VALUES ('pomme', 10)`.
- Tous les ordres SQL des défis passent par `st`.

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 2 à 4) et projet 3 (étapes 2 et 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_transactions` → **New** → **Java Class** → `Recall04`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall04`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `UPDATE stock SET qty = 11`. Affiche `conn.getAutoCommit()`, puis `stock(other)`.
  → `D01 : autoCommit true, l'autre voit 11`
- ☐ **D02.** Dans cet ordre :
  1. `conn.setAutoCommit(false)`, puis `UPDATE stock SET qty = 20` ;
  2. garde `stock(other)` ;
  3. `conn.commit()`.
  
  Affiche `stock(conn)`, la valeur gardée, puis `stock(other)`.
  → `D02 : moi 20, l'autre avant commit 11, apres 20`
- ☐ **D03.** `UPDATE stock SET qty = 0`, puis `conn.rollback()`.
  → `D03 : apres rollback 20`
- ☐ **D04.** Dans cet ordre :
  1. `UPDATE stock SET qty = qty - 5` ;
  2. `half = conn.setSavepoint("moitie")` ;
  3. de nouveau `qty - 5` ;
  4. `rollback(half)`, puis `commit()`.
  
  Affiche le stock et `half.getSavepointName()`.
  → `D04 : 15 (nom moitie)`
- ☐ **D05.** Dans cet ordre :
  1. `a = conn.setSavepoint()` (anonyme), puis `UPDATE stock SET qty = 1` ;
  2. un 2e `setSavepoint()` (sans variable), puis `UPDATE stock SET qty = 2` ;
  3. `rollback(a)`.
  
  → `D05 : 15`
- ☐ **D06.**
  - `b = conn.setSavepoint("b")`, `releaseSavepoint(b)`, puis `rollback(b)` : `ok`, ou le SQLState ;
  - puis `a.getSavepointName()` : son résultat, ou `SQLException`.
  
  → `D06 : rollback(libere) 90063, getSavepointName(anonyme) SQLException`
- ☐ **D07.** `UPDATE stock SET qty = 7`, puis `UPDATE stock SET qty = -1` (attrapée). Dans le `catch`, affiche le SQLState et `stock(conn)`.
  → `D07 : 23513, toujours 7 dans la transaction`
- ☐ **D08.** `conn.setAutoCommit(true)`, puis `stock(other)`.
  → `D08 : l'autre voit 7`
- ☐ **D09.** Dans cet ordre :
  1. `conn.setAutoCommit(false)`, puis `old = conn.setSavepoint("ancien")` ;
  2. `UPDATE stock SET qty = 8` ;
  3. `conn.commit()` ;
  4. `conn.rollback(old)`, attrapée : affiche le SQLState et `stock(conn)`.
  
  Remets enfin `setAutoCommit(true)`.
  → `D09 : rollback apres commit 90063, stock 8`

## Expériences (hors sortie attendue)

1. Remplace D08 par un simple `conn.close()` : que voit une nouvelle connexion ? (Cela dépend du fournisseur : ne compte jamais dessus.)
2. Appelle `conn.rollback()` en auto-commit : que dit la Javadoc ? Que fait H2 ?
3. Dans D05, essaie `rollback` vers le 2e point **après** `rollback(a)`. La Javadoc dit qu'il n'est plus valide ; H2 est-il strict ?

## Sortie attendue complète

```
D01 : autoCommit true, l'autre voit 11
D02 : moi 20, l'autre avant commit 11, apres 20
D03 : apres rollback 20
D04 : 15 (nom moitie)
D05 : 15
D06 : rollback(libere) 90063, getSavepointName(anonyme) SQLException
D07 : 23513, toujours 7 dans la transaction
D08 : l'autre voit 7
D09 : rollback apres commit 90063, stock 8
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **L'auto-commit :**
  - actif par **défaut** : chaque ordre est une transaction à lui seul ;
  - `setAutoCommit(false)` : les ordres s'accumulent jusqu'à `commit()` ou `rollback()` ;
  - `setAutoCommit(true)` alors qu'une transaction est ouverte : elle est **validée** ;
  - `commit()` ou `rollback()` en auto-commit : la Javadoc annonce une `SQLException` (H2 tolère).
- **La fermeture** d'une connexion avec une transaction ouverte : le résultat **dépend du fournisseur**. Toujours `commit()` ou `rollback()` soi-même.
- **L'isolation :** les autres connexions ne voient que ce qui est **validé** (le niveau par défaut de la plupart des bases : `READ_COMMITTED`).
- **Un ordre qui échoue** (contrainte, doublon…) est annulé seul. La transaction reste ouverte : à toi de choisir `commit()` ou `rollback()`.
- **`Savepoint`** :
  - `setSavepoint()` (anonyme : `getSavepointId()`) ou `setSavepoint("nom")` (`getSavepointName()`). L'autre accesseur lève une `SQLException` ;
  - `rollback(sp)` annule ce qui suit `sp` (les points posés après `sp` deviennent invalides), et garde la transaction **ouverte** ;
  - `releaseSavepoint(sp)` : `sp` n'existe plus ;
  - `commit()` ou `rollback()` libèrent tous les points ;
  - en auto-commit, `setSavepoint` n'a pas de sens : la Javadoc annonce une `SQLException`.

</details>
