# Drill de rappel 1 — `Optional` et ses cousins primitifs

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 20 min la 1re fois, puis 10 min aux répétitions.

**Règles :**
- Tout se fait **de mémoire** : ni solution, ni Javadoc, ni carte mémoire avant d'avoir fini.
- Crée toi-même, dans ce paquet, la classe **`Recall01`** et son `main`.
- Les données viennent de `ch10_streams.drills.Data`. Transforme `Data.BOOKS` en **ton propre record** (titre, auteur, genre, année, pages, prix).
- Écris **une ligne par défi**, préfixée `Dxx : `. `Check` compare au caractère près.
- `Optional.get()` est **interdit**. `Check` refuse **tout** appel `.get()` : pour lire un `AtomicInteger`, utilise `intValue()`.

## Défis

- ☐ **D01.** Affiche un `Optional` contenant `"Dune"`, puis un `Optional` vide, tels que leur `toString()` les écrit.
  → `D01 : Optional[Dune] Optional.empty`
- ☐ **D02.** Teste si un `Optional` créé à partir de `null` est vide. Puis crée un `Optional` à partir de `null` avec **l'autre** fabrique, et affiche le nom simple de l'exception.
  → `D02 : true NullPointerException`
- ☐ **D03.** Affiche le titre du premier livre de Zola, puis celui du premier livre de Proust. S'il n'y en a pas, affiche `inconnu`.
  → `D03 : Germinal | inconnu`
- ☐ **D04.** Sur un `Optional` **présent**, appelle les deux méthodes « valeur par défaut ». Leur défaut vient d'une méthode qui compte ses appels. Combien de fois chacune l'a-t-elle appelée ?
  → `D04 : orElse appelle 1 fois, orElseGet 0 fois`
- ☐ **D05.** Écris une méthode « suite d'un livre » qui rend un `Optional<String>` à partir d'une `Map` (`Dune` → `Le Messie de Dune`, `Le Hobbit` → `Le Seigneur des Anneaux`). Affiche la suite de Dune, puis celle de Germinal (`aucune suite`), sans `Optional<Optional<…>>`.
  → `D05 : Le Messie de Dune | aucune suite`
- ☐ **D06.** Cherche le titre exact `Hobbit`. Si la recherche est vide, cherche **de façon paresseuse** un titre qui **contient** `Hobbit`. Le résultat reste un `Optional`, transformé en titre.
  → `D06 : Optional[Le Hobbit]`
- ☐ **D07.** En **un appel par livre**, ajoute `trouve <année>` si le livre existe, sinon `absent`. Fais-le pour Dune puis pour Ulysse.
  → `D07 : trouve 1965 absent`
- ☐ **D08.** Deux recherches vides. La première lève l'exception par défaut, la seconde lève `IllegalArgumentException` via une référence de constructeur. Affiche les deux noms simples.
  → `D08 : NoSuchElementException IllegalArgumentException`
- ☐ **D09.** Liste triée des suites de **tous** les livres. Les livres sans suite disparaissent, sans `filter`.
  → `D09 : [Le Messie de Dune, Le Seigneur des Anneaux]`
- ☐ **D10.** Affiche :
  - le max des pages en `OptionalInt` ;
  - l'année max en `OptionalLong` ;
  - la moyenne des prix en `OptionalDouble`, avec 2 décimales (`Locale.US`) ;
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
D02 : true NullPointerException
D03 : Germinal | inconnu
D04 : orElse appelle 1 fois, orElseGet 0 fois
D05 : Le Messie de Dune | aucune suite
D06 : Optional[Le Hobbit]
D07 : trouve 1965 absent
D08 : NoSuchElementException IllegalArgumentException
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
