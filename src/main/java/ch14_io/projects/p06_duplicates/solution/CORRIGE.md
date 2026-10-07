# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`DiskUsage.java`](DiskUsage.java) et [`Dedup.java`](Dedup.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Le visiteur

**`walkFileTree`** appelle les méthodes du visiteur à chaque moment du parcours :
- `preVisitDirectory` avant d'entrer dans un dossier : `SKIP_SUBTREE` évite de le parcourir (ici `cache`) ;
- `visitFile` pour chaque fichier, avec ses attributs (la taille, sans relire le disque) ;
- `postVisitDirectory` après le contenu d'un dossier : pour la racine, c'est la toute fin.

---

## Étape 2 — Lire et modifier les attributs

**Question — que veut dire `null` dans `setTimes` ?** Les trois arguments sont la date de **modification**, celle du **dernier accès** et celle de **création**. `null` veut dire « ne change pas celle-là ». Ici, seule la date de modification change.

---

## Étape 3 — Les doublons

**Question — pourquoi grouper par taille d'abord ?** Comparer deux contenus oblige à **lire** les deux fichiers, ce qui coûte cher. Comparer deux tailles est **gratuit** : elle est dans les attributs. Deux fichiers de tailles différentes ne peuvent pas être identiques. En groupant par taille, on ne compare les contenus qu'entre fichiers de même taille, souvent très peu nombreux.

**Les octets récupérables** : pour un groupe de n fichiers identiques de taille t, on peut en supprimer n − 1, soit t × (n − 1) octets. Ici : 17 + 17 = 34.
