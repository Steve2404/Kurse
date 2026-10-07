# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Media`, `Book`, `Movie`, `AudioMedia`, `Album`, `Podcast`, `Isbn` et `MediaApp`.
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — La hiérarchie et le catalogue sans doublons

**Le code :** les classes du modèle, et `create`, `seconds` et le début du `main` de [`MediaApp.java`](MediaApp.java).

**`AudioMedia`, une classe abstraite intermédiaire :** elle factorise ce qu'`Album` et `Podcast` ont en commun (l'artiste, les pistes, le calcul des minutes, la recherche par artiste). Elle **implémente** `minutes()`, mais laisse `kind()` abstraite. Ses sous-classes n'ont presque rien à écrire.

**`split("\\|")` :** `split` prend une **expression régulière**, où `|` signifie « ou ». `split("|")` couperait entre chaque caractère. `\\|` (dans le code Java) donne la regex `\|`, c'est-à-dire un `|` littéral.

**`equals` sans l'id :** deux objets `Inception` créés à partir de la même ligne sont deux objets **différents** (ids 2 et 7), mais ils représentent le **même** film. L'égalité « métier » ne regarde que ce qui définit le média.

**Question — pourquoi Fondation a l'id 8 ?** Le 2e `Inception` a été **créé** (ligne 7 des données) avant d'être reconnu comme doublon. Son constructeur a donc incrémenté le compteur et pris l'id **7**. Être écarté du catalogue n'efface pas la création. D'où `objets crees 9, catalogue 8`.

---

## Étape 2 — Trier et chercher

**Le code :** la suite du `main`.

**`matches` polymorphe :**
- « Nolan » ne figure ni dans un titre ni dans un tag. Seule la version de `Movie` (`director.equalsIgnoreCase`) le trouve, donc Inception et Interstellar.
- « jazz » est un **tag** de Kind of Blue : c'est la version de base (`Media`), appelée par `super.matches` dans `AudioMedia`, qui répond.

**Étendre plutôt que remplacer :** si `Book.matches` ne contenait que `author.equalsIgnoreCase(q)`, on perdrait la recherche par titre et par tag pour les livres. `super.matches(q) || …` garde le comportement du parent et **ajoute** le sien.

---

## Étape 3 — ISBN et minutes par type

**Le code :** [`Isbn.java`](Isbn.java), et la boucle `isbn` et `minutes` du `main`.

**Le calcul de la clé, pour `978207061275-?` :**
- avec les poids 1 et 3 en alternance, la somme pondérée des 12 premiers chiffres vaut S ;
- la clé est le chiffre qui complète S à un multiple de 10 : `(10 - S % 10) % 10`. Le second `% 10` traite le cas S % 10 = 0 : la clé vaut alors 0, et non 10.

Pour Dune, la clé calculée est **1**, mais le 13e chiffre vaut 2 : `FAUX (cle attendue 1)`.

**`Isbn` est immuable et `final`** (projet 4) : un `Book` peut le partager sans copie défensive.

**`if (m instanceof Book b)`** : seul un `Book` a un ISBN. Le pattern matching donne une variable `b` de type `Book`, sans cast.

---

## Étape 4 — Recommandations et playlists

**Le code :** `similarity` dans `Media`, `bestPlaylist` dans `AudioMedia`, et la fin du `main`.

**Jaccard pour Dune** (`science-fiction, desert, politique`) :
- Fondation (`science-fiction, espace, politique`) a 2 tags communs, pour une union de 4 : **50 %** ;
- Inception a 1 tag commun, pour une union de 5 : **20 %**.

**`similarity` et `bestPlaylist` sont `final`** : ce sont des **algorithmes**, dont le résultat ne doit pas dépendre de la sous-classe.

**Le sac à dos 0/1 :** `reachable[s]` dit si une combinaison de pistes dure **exactement** s secondes. Ajouter la piste t rend atteignable toute durée `s` telle que `s - t` l'était déjà. `lastTrack` et `previous` permettent de **reconstruire** la combinaison en remontant depuis le meilleur s.

**Question — pourquoi parcourir s à l'envers ?** En descendant, quand on teste `reachable[s - tracks[t]]`, cette case n'a **pas encore** été modifiée pendant le traitement de la piste t (elle est plus petite que s). Chaque piste n'est donc utilisée qu'**une fois**. En montant, `reachable[s - t]` peut avoir été rendu vrai **par cette même piste** un instant plus tôt, et la piste serait réutilisée plusieurs fois. Vérifié sur Kind of Blue, limite 30 min :
- **à l'envers** : `pistes 1 2 5 = 28 min 24 s` (juste) ;
- **à l'endroit** : `pistes 2 3 3 5 = 29 min 54 s`. La piste 3 est prise **deux fois**, ce qui est impossible dans une vraie playlist. C'est la version « sac à dos non borné ».
