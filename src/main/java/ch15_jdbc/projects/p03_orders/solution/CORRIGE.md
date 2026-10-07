# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Shop.java`](Shop.java) et [`ShopApp.java`](ShopApp.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **H2 2.3.232**, sur la solution et sur des copies modifiées.

---

## Étape 1 — Le magasin

**Question — le `PreparedStatement` avant le schéma ?** La création échoue **tout de suite** : un `PreparedStatement` est envoyé à la base dès `prepareStatement(…)`, qui vérifie que la table existe. Vérifié : `42S02`, `Table "PAS_ENCORE" not found`. Dans le même try, les ressources sont créées **avant** le corps : le schéma ne serait pas encore exécuté.

---

## Étape 2 — Passer les commandes

**Question — pour Dan, qui remet le stock de P2 ?** Le **`rollback(sp)`** : il défait tout ce qui a été fait **depuis** le point de sauvegarde de la ligne, donc la baisse du stock **et** l'insertion ratée.

**Expérience 1 — sans `rollback(sp)`** (vérifié) : la baisse du stock de la ligne refusée **reste**, et elle est validée avec la commande. Le stock final de P2 vaut 2 au lieu de 3, et le contrôle dit `false` : un article a disparu sans être livré.

**Question — pourquoi la commande de Cleo n'existe plus, et pourquoi Dan a le numéro 4 ?** En politique `TOUT`, l'erreur déclenche `rollback()` : **toute** la transaction est défaite, y compris l'`INSERT INTO orders` de Cleo. Mais le numéro 3 a déjà été **consommé** par la base, qui ne le rend pas (voir le projet 2, étape 3) : Dan reçoit le 4.

**Expérience 3 — `rollback()` au lieu de `rollback(sp)` en `PARTIEL`** (vérifié) : à la 1re ligne refusée, **toute** la commande est défaite, y compris la ligne `orders` et les lignes déjà livrées. Mais le code continue comme si de rien n'était : il valide plus loin une commande qui n'existe plus. Résultat : la commande d'Ana disparaît de la base, et, comme son stock n'a pas été consommé, la commande de Cleo passe ensuite (`COMPLETE`). Les commandes en base deviennent `[2 Ben …, 3 Cleo …]`.

---

## Étape 3 — Le bilan et le contrôle

**Question — `LEFT JOIN` et `COALESCE` :** un `JOIN` ordinaire ne garderait que les produits **qui ont** des lignes de commande. `LEFT JOIN` garde **tous** les produits, avec des colonnes `NULL` pour ceux qui n'en ont pas. Leur `SUM` vaut alors `NULL`, et `p.stock + NULL` donnerait `NULL` (projet 1, étape 3). `COALESCE(…, 0)` remplace ce `NULL` par 0.

---

## Étape 4 — L'API des `Savepoint`

**Question — pourquoi `rollback(screen)` a-t-il annulé les deux promotions ?** Revenir à un point de sauvegarde défait **tout** ce qui a été fait **après** lui, y compris les points de sauvegarde posés plus tard (ici `mouse`) et ce qui les suit.

**Le point libéré :** après `releaseSavepoint(cable)`, on ne peut plus y revenir : `90063`.

**Expérience 2 — `setSavepoint()` en auto-commit :** vérifié avec H2 : accepté sans erreur. La Javadoc prévoit pourtant une `SQLException` si la connexion est en auto-commit : ne compte pas sur la tolérance de H2.
