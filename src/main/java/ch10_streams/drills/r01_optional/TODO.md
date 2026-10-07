# Drill de rappel 1 — `Optional` et ses cousins primitifs

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 20 min la 1re fois, puis 10 min aux répétitions.

**Règles :**
- Tout se fait **de mémoire** : ni solution, ni Javadoc, ni carte mémoire avant d'avoir fini.
- Crée toi-même, dans ce paquet, la classe **`Recall01`** et son `main`.
- Les données viennent de `ch10_streams.drills.Data`. Transforme `Data.BOOKS` en **ton propre record** (titre, auteur, genre, année, pages, prix).
- Écris **une ligne par défi**, préfixée `Dxx : `. `Check` compare au caractère près.
- `Optional.get()` est **interdit** (`Check` refuse tout appel `.get()`).

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 à 9) et projet 3 (étape 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_optional` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Affiche un `Optional` contenant `"Dune"`, puis un `Optional` vide, tels que leur `toString()` les écrit.
  → `D01 : Optional[Dune] Optional.empty`
- ☐ **D02.** Teste si un `Optional` créé à partir de `null` (avec la fabrique qui l'accepte) est vide. Puis, avec la même fabrique, teste si un `Optional` de `"Dune"` est présent.
  → `D02 : true true`

**Expérience** (hors sortie attendue) : crée un `Optional` à partir de `null` avec **l'autre** fabrique, lance ton `main` et lis l'exception, puis retire la ligne.
- ☐ **D03.** Affiche le titre du premier livre de Zola, puis celui du premier livre de Proust. S'il n'y en a pas, affiche `inconnu`.
  → `D03 : Germinal | inconnu`
- ☐ **D04.** Sur un `Optional` **présent**, appelle les deux méthodes « valeur par défaut ». Leur défaut vient d'une méthode qui note chaque appel dans une liste. Combien de fois chacune l'a-t-elle appelée ? (Pourquoi une liste et pas un `int` ? Pense à « effectivement final », chapitre 8.)
  → `D04 : orElse appelle 1 fois, orElseGet 0 fois`
- ☐ **D05.** Écris une méthode « suite d'un livre » qui rend un `Optional<String>` à partir d'une `Map` (`Dune` → `Le Messie de Dune`, `Le Hobbit` → `Le Seigneur des Anneaux`). Affiche la suite de Dune, puis celle de Germinal (`aucune suite`), sans `Optional<Optional<…>>`.
  → `D05 : Le Messie de Dune | aucune suite`
- ☐ **D06.** Cherche le titre exact `Hobbit`. Si la recherche est vide, cherche **de façon paresseuse** un titre qui **contient** `Hobbit`. Le résultat reste un `Optional`, transformé en titre.
  → `D06 : Optional[Le Hobbit]`
- ☐ **D07.** En **un appel par livre**, ajoute `trouve <année>` si le livre existe, sinon `absent`. Fais-le pour Dune puis pour Ulysse.
  → `D07 : trouve 1965 absent`
- ☐ **D08.** Sur deux recherches **présentes** : l'auteur de Dune avec `orElseThrow()` sans argument, puis l'année de Germinal avec `orElseThrow` et une **référence de constructeur** vers `IllegalArgumentException`.
  → `D08 : Herbert 1885`

**Expérience** (hors sortie attendue) : fais les mêmes appels sur une recherche **vide** (Ulysse), lance, et note quelle exception chacun lève.
- ☐ **D09.** Liste triée des suites de **tous** les livres. Les livres sans suite disparaissent, sans `filter`.
  → `D09 : [Le Messie de Dune, Le Seigneur des Anneaux]`
- ☐ **D10.** Affiche :
  - le max des pages en `OptionalInt` ;
  - l'année max en `OptionalLong` ;
  - la moyenne des prix en `OptionalDouble`, arrondie à 2 décimales avec `Math.round` (chapitre 4) ;
  - le max d'un `IntStream` vide, avec la valeur par défaut `-1`.
  → `D10 : 592 1989 8.89 -1`
- ☐ **D11.** Compare deux `Optional` de `"a"` avec `equals`. Puis transforme `Optional.of(1)` avec une fonction qui rend `null`, et affiche le résultat.
  → `D11 : true Optional.empty`
- ☐ **D12.** Garde Dune seulement si son prix est supérieur à 10, puis transforme en titre. Même chose pour Hyperion.
  → `D12 : Optional.empty Optional[Hyperion]`
- ☐ **D13.** Seulement si Fondation existe : affiche son auteur, puis le test booléen « présent ».
  → `D13 : Asimov true`

## Sortie attendue complète

```
D01 : Optional[Dune] Optional.empty
D02 : true true
D03 : Germinal | inconnu
D04 : orElse appelle 1 fois, orElseGet 0 fois
D05 : Le Messie de Dune | aucune suite
D06 : Optional[Le Hobbit]
D07 : trouve 1965 absent
D08 : Herbert 1885
D09 : [Le Messie de Dune, Le Seigneur des Anneaux]
D10 : 592 1989 8.89 -1
D11 : true Optional.empty
D12 : Optional.empty Optional[Hyperion]
D13 : Asimov true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Méthode | Signature / retour | Piège |
|---|---|---|
| `Optional.of(v)` | `Optional<T>` | `v == null` → **NPE immédiate** |
| `Optional.ofNullable(v)` | `Optional<T>` | `null` → vide |
| `Optional.empty()` | `Optional<T>` | `toString()` → `Optional.empty` |
| `isPresent()` / `isEmpty()` | `boolean` | `isEmpty` date de Java 11 |
| `ifPresent(Consumer)` | `void` | |
| `ifPresentOrElse(Consumer, Runnable)` | `void` | Java 9 ; le 2e argument est un `Runnable` |
| `map(Function)` | `Optional<U>` | une fonction qui rend `null` → `Optional` vide |
| `flatMap(Function<T, Optional<U>>)` | `Optional<U>` | sinon on obtient `Optional<Optional<U>>` |
| `filter(Predicate)` | `Optional<T>` | |
| `or(Supplier<Optional>)` | `Optional<T>` | Java 9 ; paresseux |
| `orElse(T)` | `T` | l'argument est **toujours évalué** |
| `orElseGet(Supplier)` | `T` | évalué seulement si vide |
| `orElseThrow()` | `T` | `NoSuchElementException` (Java 10) |
| `orElseThrow(Supplier<X>)` | `T` | lève `X` |
| `stream()` | `Stream<T>` de 0 ou 1 élément | Java 9 ; s'utilise avec `flatMap(Optional::stream)` |
| `get()` | `T` | `NoSuchElementException` si vide ; à éviter |
| `OptionalInt` / `OptionalLong` / `OptionalDouble` | `getAsInt()` / `getAsLong()` / `getAsDouble()` | **pas** de `map`, `filter` ni `flatMap` |
| `average()` d'un `IntStream` | `OptionalDouble` | jamais `OptionalInt` |

</details>
