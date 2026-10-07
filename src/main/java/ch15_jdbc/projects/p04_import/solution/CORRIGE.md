# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Customer.java`](Customer.java), [`Importer.java`](Importer.java) et [`ImportApp.java`](ImportApp.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **H2 2.3.232**, sur la solution et sur des copies modifiées.

---

## Étape 1 — Le nettoyage (pur Java)

**Question — pourquoi une `LinkedHashMap` ?** Elle garde l'**ordre d'insertion** (chapitre 9) : les clients sont importés dans l'ordre du fichier, ce qui rend les clés et la sortie prévisibles. Une `HashMap` les rendrait dans un ordre quelconque.

---

## Étape 2 — L'import par lots

**Question — combien d'allers-retours ?** Avec des lots de 4, les 9 clients partent en **3** envois (`lots [4, 4, 1]`). Sans lot, il faudrait **9** envois, un par `INSERT`. Sur un vrai réseau, chaque aller-retour coûte du temps : c'est tout l'intérêt des lots.

---

## Étape 3 — Le fichier qui gêne

**Question — la norme oblige-t-elle à continuer après l'échec ?** Non : un pilote peut aussi s'arrêter à la 1re erreur, et le tableau de `getUpdateCounts()` est alors plus court. H2 continue, et marque chaque ligne ratée `EXECUTE_FAILED`.

**Question — pourquoi les clés sautent-elles de 9 à 14, puis de 2 en 2 ?** Chaque `INSERT` **tenté** consomme un numéro, même s'il échoue ou s'il est annulé, et la base ne les rend pas :
- l'import strict a consommé des numéros, puis tout a été annulé (`rollback`) ;
- dans l'import tolérant, les lignes en doublon consomment aussi un numéro en échouant, d'où `14, 16, 18`.

**Expérience 1 — la taille des lots** (vérifié) :
- avec `CHUNK = 1` : `lots [1, 1, 1, 1, 1, 1, 1, 1, 1]`. En mode strict, l'import s'arrête dès le **1er** lot en échec, donc avec un seul rejet (`ben@mail.fr`). Les clés tolérantes deviennent `[12, 14, 16]` ;
- avec `CHUNK = 100` : un seul lot, `lots [9]`, puis `lots [5]` pour `delta.csv`. Les clés deviennent `[15, 17, 19]`.

Le nombre de numéros consommés dépend donc de la façon dont les lots sont découpés.

**Expérience 2 — sans le `commit()` final :** avec **H2**, ce qui n'est pas validé est **annulé** à la fermeture de la connexion (vérifié au projet 2, étape 4) : les clients importés disparaîtraient. D'autres pilotes valident à la fermeture : ne compte jamais dessus.

**Expérience 3 — `executeBatch()` sans `addBatch()` :** un tableau **vide** (longueur 0), sans erreur : c'est ce que montre la ligne `lot vide apres clearBatch : 0 ordre`.

---

## Étape 4 — Le lot de `Statement`, et la clé par son nom

**Question — pourquoi pas de `SELECT` dans un lot ?** `executeBatch()` rend seulement un **nombre de lignes** par ordre (`int[]`) : il n'a aucun moyen de rendre des `ResultSet`. Vérifié avec H2 : un `SELECT` ajouté au lot fait échouer `executeBatch()` avec une `BatchUpdateException` (la classe de H2 s'appelle `JdbcBatchUpdateException`), SQLState `90001` : `Method is not allowed for a query`.

**Le lot mixte `[12, 3, 1]` :** l'`UPDATE` touche 12 lignes, le `DELETE` en supprime 3, l'`INSERT` en ajoute 1.
