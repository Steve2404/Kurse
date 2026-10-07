# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Les agrégats concurrents

**Le code :** `Stats.java`. Chaque structure est faite pour être partagée entre threads :
- `ConcurrentHashMap` : une `Map` sûre, sans verrou global ;
- `ConcurrentSkipListMap` et `ConcurrentSkipListSet` : la même chose, **triée** (comme `TreeMap` et `TreeSet`) ;
- `ConcurrentLinkedQueue` : une file sûre, sans blocage.

**Question — pourquoi `hits.put(page, hits.get(page) + 1)` est-il faux ?** Chaque appel (`get`, puis `put`) est sûr **séparément**, mais pas les deux ensemble. Deux threads peuvent lire la même valeur, ajouter 1 chacun, et écrire la même chose : une visite est perdue. Vérifié avec 8 threads qui font chacun 10 000 incréments (3 lancements) :

```
put(get+1) 52045 ; merge 80000
put(get+1) 63097 ; merge 80000
put(get+1) 75169 ; merge 80000
```

`merge` fait la lecture et l'écriture en **une seule** opération atomique : le compte est toujours juste.

---

## Étape 2 — Le pipeline

**Le principe producteur-consommateur :** les producteurs **déposent** (`put`), les consommateurs **prennent** (`take`). La file à capacité limitée ralentit les producteurs trop rapides.

**Question — pourquoi les producteurs ne peuvent-ils pas envoyer les pilules eux-mêmes ?** Un producteur qui finit **avant** les autres enverrait sa pilule pendant que d'autres déposent encore des lignes. Un consommateur s'arrêterait alors **trop tôt**, en laissant des lignes non traitées, ou une pilule de trop resterait dans la file. Il faut attendre la fin de **tous** les producteurs, puis envoyer exactement une pilule par consommateur.

---

## Étape 3 — Les règles des collections

- **La file de capacité 1 :** le 1er `offer` réussit (`true`), le 2e attend 10 ms puis abandonne (`false`) ; le 1er `poll` rend `1`, le 2e attend puis rend `null` ;
- **`ArrayList` modifiée pendant un for-each :** `ConcurrentModificationException`. L'itérateur détecte la modification ;
- **`ConcurrentHashMap` et `null` :** `put(null, 1)` lance une `NullPointerException`, alors qu'une `HashMap` accepte une clé `null`. En concurrence, `get` qui rend `null` doit vouloir dire « absent », sans ambiguïté ;
- **`synchronizedList` :** chaque méthode est protégée, mais **trier** ou **parcourir** demande plusieurs opérations : il faut le faire dans un `synchronized (liste)`.

**Question — pourquoi la `CopyOnWriteArrayList` ne fait-elle que 3 tours ?** Chaque `add` crée une **nouvelle copie** du tableau. Le for-each parcourt la copie qui existait au **début** de la boucle, avec 3 éléments : il fait 3 tours, sans erreur. Les 3 ajouts se voient seulement après la boucle (taille 6).
