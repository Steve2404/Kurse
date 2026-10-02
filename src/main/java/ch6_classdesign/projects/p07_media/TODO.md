# Projet 7 (CAPSTONE) — La médiathèque

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Notions visées :** tout le chapitre 6 :
- une hiérarchie sur **3 niveaux** avec une abstraite intermédiaire : `Media` → `AudioMedia` → `Album` et `Podcast` ;
- les constructeurs chaînés, avec un **identifiant `final`** tiré d'un compteur `static` ;
- les **copies défensives** de tableaux ;
- **`equals(Object)` et `hashCode()`** pour détecter les doublons ;
- `toString()` enrichi par `super.toString()` ;
- `matches()` étendu par `super.matches()` ;
- des méthodes `final` ;
- une petite classe **immuable** `Isbn`.

Côté algorithmes :
- dédoublonnage ;
- tri à deux critères ;
- **similarité de Jaccard** et recommandations ;
- **clé de contrôle ISBN-13** ;
- **sac à dos 0/1** (programmation dynamique) pour la meilleure playlist.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch6_classdesign.projects.p07_media` :
- `Isbn` ;
- `Media` (abstraite), `Book`, `Movie`, `AudioMedia` (abstraite), `Album`, `Podcast` ;
- **`MediaApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 6. Pas de collection : le catalogue est un tableau. Pas de cast d'objet.

---

## Tableau de bord

### ☐ Étape 1 — La hiérarchie et le catalogue sans doublons

```
#1 [livre] Le Petit Prince (1943) 192 min de Saint-Exupery
#2 [film] Inception (2010) 148 min de Nolan
...
#6 [podcast] Code Story (2021) 90 min, 3 episodes
#8 [livre] Fondation (1951) 510 min de Asimov
#9 [album] Random Access Memories (2013) 26 min
doublons ignores : Inception #7 ; objets crees 9, catalogue 8
```
- **`Media`** (`abstract`) :
  - `private static int created` ;
  - `private final int id`, `title`, `year`, `tags` (`String[]`, **copié** avec `clone()`) ;
  - le constructeur `protected Media(String title, int year, String[] tags)`, qui fait `id = ++created` ;
  - les méthodes abstraites `kind()` et `minutes()` ;
  - `matches(String query)` : le titre (sans casse) ou un tag contient la requête ;
  - `public final int similarity(Media other)` ;
  - `public final int getId()` ;
  - `equals` : même `kind()`, même titre, même année. L'id ne compte **pas** ;
  - `hashCode` cohérent ;
  - `toString()` = `[kind] titre (annee) N min`.
- **`Book`** :
  - `MINUTES_PER_PAGE = 2`, l'auteur, les pages, un `Isbn` ;
  - `matches` = `super.matches(q) || author.equalsIgnoreCase(q)` ;
  - `toString` = `super.toString() + " de " + author`.
- **`Movie`** : réalisateur et minutes, avec la même logique pour `matches` et `toString`.
- **`AudioMedia`** (`abstract`, `extends Media`) :
  - l'artiste et `protected final int[] tracks` (copié), en secondes ;
  - elle **implémente** `minutes()` (le total, arrondi à la minute supérieure : `(total + 59) / 60`) ;
  - elle étend `matches` avec l'artiste ;
  - elle laisse `kind()` à ses sous-classes.
- **`Album`** : `kind()` = `album`.
- **`Podcast`** : `kind()` = `podcast`, et `toString` ajoute `, N episodes`.
- **Dans `MediaApp`** :
  - `create(String line)` découpe sur `"\\|"` (un `|` échappé, car `split` prend une regex) ;
  - les tags se découpent sur `,` ;
  - les durées, avec `seconds(String csv)`.
- **Le catalogue :** pour chaque ligne, crée l'objet. S'il est `equals` à un média déjà présent, note-le dans les doublons (`titre #id`) ; sinon, ajoute-le.
- Affiche chaque média sous la forme `#id ` + `toString()`, puis la ligne des doublons avec `Media.created()`.
- **Question :** pourquoi Fondation a-t-il l'id 8, et pas 7 ?

### ☐ Étape 2 — Trier et chercher

```
du plus recent : 2021 2014 2013 2010 1965 1959 1951 1943
recherche science : [Inception] [Dune] [Interstellar] [Fondation]
recherche Nolan : [Inception] [Interstellar]
recherche Asimov : [Fondation]
recherche jazz : [Kind of Blue]
```
- Trie une **copie** du catalogue par année décroissante, puis par titre (insertion). Affiche les années.
- Pour chaque requête de `Data.QUERIES`, affiche les titres qui correspondent, chacun entre crochets. Chaque objet applique **sa** version de `matches`.

### ☐ Étape 3 — ISBN et minutes par type

```
isbn : 978-207061275-8 ok 978-226632048-2 FAUX (cle attendue 1) 978-207036053-6 ok
minutes : livre=1902 film=317 album=72 podcast=90
```
- **`Isbn`** (`final`) :
  - un champ `private final String digits` ;
  - `expectedCheck()` : la somme des 12 premiers chiffres avec les poids 1, 3, 1, 3…, puis `(10 - somme % 10) % 10` ;
  - `isValid()` ;
  - `equals` et `hashCode` ;
  - `toString()` = `978-xxxxxxxxx-c`.
- Parcours le catalogue : avec `if (m instanceof Book b)`, vérifie l'ISBN. Cumule aussi les minutes par `kind()`, dans l'ordre livre, film, album, podcast.

### ☐ Étape 4 — Recommandations et playlists

```
si vous aimez Dune : Fondation (50%) Inception (20%) Interstellar (20%)
playlist Kind of Blue <= 30 min : pistes 1 2 5 = 28 min 24 s
playlist Code Story <= 30 min : pistes 1 = 30 min 0 s
playlist Random Access Memories <= 30 min : pistes 1 2 3 4 5 = 25 min 47 s
```
- **`similarity`** (Jaccard, en pourcentage entier) : `100 * |communs| / |union|`.
- **Les recommandations :** trie les autres médias par similarité décroissante, puis par titre. Affiche les 3 premiers.
- **`public final String bestPlaylist(int limit)`**, dans `AudioMedia`, en **sac à dos 0/1** sur les secondes :
  1. `reachable[s]` = une combinaison de pistes dure exactement s ;
  2. pour chaque piste, parcours s **de la limite vers le bas**, pour que chaque piste serve au plus une fois ;
  3. mémorise la dernière piste et la somme précédente, puis remonte pour lister les pistes (numérotées à partir de 1) ;
  4. le meilleur total est le plus grand s atteignable.
- Appelle-la pour chaque `AudioMedia`, avec `Data.PLAYLIST_LIMIT`.
- **Question :** pourquoi parcourir s à l'envers ? Que se passerait-il à l'endroit ?

---

## Checklist (vérifiée par `Check`)

- `Data.MEDIA`, `Data.QUERIES`, `Data.LIKED`, `Data.PLAYLIST_LIMIT` ;
- `abstract class Media`, `abstract class AudioMedia extends Media`, 2 `extends AudioMedia` ;
- `final class Isbn` ;
- 2 `equals(Object` et 2 `hashCode()` ;
- `super.matches(` et `super.toString()` ;
- `clone()` et `instanceof AudioMedia` ;
- `public final int similarity(`.

---

## Sortie attendue complète

```
#1 [livre] Le Petit Prince (1943) 192 min de Saint-Exupery
#2 [film] Inception (2010) 148 min de Nolan
#3 [album] Kind of Blue (1959) 46 min
#4 [livre] Dune (1965) 1200 min de Herbert
#5 [film] Interstellar (2014) 169 min de Nolan
#6 [podcast] Code Story (2021) 90 min, 3 episodes
#8 [livre] Fondation (1951) 510 min de Asimov
#9 [album] Random Access Memories (2013) 26 min
doublons ignores : Inception #7 ; objets crees 9, catalogue 8
du plus recent : 2021 2014 2013 2010 1965 1959 1951 1943
recherche science : [Inception] [Dune] [Interstellar] [Fondation]
recherche Nolan : [Inception] [Interstellar]
recherche Asimov : [Fondation]
recherche jazz : [Kind of Blue]
isbn : 978-207061275-8 ok 978-226632048-2 FAUX (cle attendue 1) 978-207036053-6 ok
minutes : livre=1902 film=317 album=72 podcast=90
si vous aimez Dune : Fondation (50%) Inception (20%) Interstellar (20%)
playlist Kind of Blue <= 30 min : pistes 1 2 5 = 28 min 24 s
playlist Code Story <= 30 min : pistes 1 = 30 min 0 s
playlist Random Access Memories <= 30 min : pistes 1 2 3 4 5 = 25 min 47 s
```
