# Drill de rappel 2 — Créer des streams, paresse, usage unique

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall02`**.
- Utilise `ch10_streams.drills.Data` (`WORDS`, `NUMBERS`).
- Écris une ligne `Dxx : ` par défi.

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 1, 4 et 10) et projet 3 (étapes 1 et 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r02_sources` → **New** → **Java Class** → `Recall02`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall02`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall02`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Compte trois cas :
  - un stream de `"a"`, `"b"`, `"c"` ;
  - un stream vide ;
  - le stream d'**une valeur `null`** (Java 9).
  → `D01 : 3 0 0`
- ☐ **D02.** Deux listes :
  - les puissances de 2 à partir de 1, limitées à 6, avec le `iterate` **infini** ;
  - les puissances de 3 strictement inférieures à 100, avec le `iterate` **qui porte sa condition**.
  → `D02 : [1, 2, 4, 8, 16, 32] [1, 3, 9, 27, 81]`
- ☐ **D03.** `"ab"` répété 3 fois grâce à une source **infinie**, puis collé.
  → `D03 : ababab`
- ☐ **D04.** Colle un stream de 1 et 2 avec un stream de 3.
  → `D04 : [1, 2, 3]`
- ☐ **D05.** Les éléments d'indices 2 à 4 de `NUMBERS`, pris directement sur le tableau sans copie. Affiche avec `Arrays.toString`.
  → `D05 : [8, 1, 9]`
- ☐ **D06.** Les lettres de `"java"` séparées par des tirets, en partant de la `String`.
  → `D06 : j-a-v-a`
- ☐ **D07.** Le premier mot de plus de 6 lettres, et le nombre d'éléments **réellement examinés**. Note-les dans une liste avec une opération de débogage (un `int` ne peut pas être modifié dans une lambda).
  → `D07 : optional apres 3 elements examines`
- ☐ **D08.** Construis un pipeline dont l'opération de débogage note chaque élément dans une liste, mais **sans** opération terminale. Affiche la taille de la liste.
  → `D08 : sans operation terminale : 0 appel`
- ☐ **D09.** Un `Supplier` de streams de `WORDS` : compte les mots avec un premier flux, puis les mots **distincts** avec un second flux venant du même `Supplier`.
  → `D09 : 9 7`

**Expérience** (hors sortie attendue) : consomme **deux fois** le même stream (deux `count()`), lance, et note l'exception. Pourquoi le `Supplier` l'évite-t-il ?
- ☐ **D10.** Les 3 premiers mots, lus avec l'**opération terminale qui rend un itérateur** classique.
  → `D10 : stream lambda optional`
- ☐ **D11.** Compte un `Stream.of(NUMBERS)`, puis le stream du tableau d'`int` construit correctement. Explique la différence en commentaire.
  → `D11 : 1 8`
- ☐ **D12.** Les entiers à partir de 1 dont le carré est inférieur à 50, sur une source infinie, **sans** `limit` ni `filter`.
  → `D12 : [1, 2, 3, 4, 5, 6, 7]`
- ☐ **D13.** Sur `WORDS`, compare les deux ordres :
  - `limit(4)` puis les mots de plus de 4 lettres ;
  - les mots de plus de 4 lettres puis `limit(4)`.
  → `D13 : [stream, lambda, optional] [stream, lambda, optional, stream]`

## Sortie attendue complète

```
D01 : 3 0 0
D02 : [1, 2, 4, 8, 16, 32] [1, 3, 9, 27, 81]
D03 : ababab
D04 : [1, 2, 3]
D05 : [8, 1, 9]
D06 : j-a-v-a
D07 : optional apres 3 elements examines
D08 : sans operation terminale : 0 appel
D09 : 9 7
D10 : stream lambda optional
D11 : 1 8
D12 : [1, 2, 3, 4, 5, 6, 7]
D13 : [stream, lambda, optional] [stream, lambda, optional, stream]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Source | Remarque |
|---|---|
| `Stream.of(T...)`, `Stream.empty()` | `Stream.of(intArray)` donne un `Stream<int[]>` d'**un** élément |
| `Stream.ofNullable(t)` | Java 9 ; 0 ou 1 élément |
| `Stream.iterate(seed, UnaryOperator)` | **infini** |
| `Stream.iterate(seed, Predicate, UnaryOperator)` | Java 9 ; fini |
| `Stream.generate(Supplier)` | infini, sans ordre de dépendance entre les éléments |
| `Stream.concat(a, b)` | deux `Stream<T>` exactement |
| `Arrays.stream(arr, from, toExclusive)` | `IntStream` pour un `int[]` |
| `"txt".chars()` | `IntStream` de codes de caractères |
| `collection.stream()` | |

**Les règles du pipeline :**
- Les opérations intermédiaires sont **paresseuses**. Rien ne s'exécute sans opération terminale.
- Le traitement est **vertical** : chaque élément traverse tout le pipeline avant le suivant.
- Un stream ne se consomme qu'**une fois** : sinon `IllegalStateException`.
- Les opérations court-circuitantes (`findFirst`, `anyMatch`, `limit`, `takeWhile`…) terminent même sur un flux infini.
- `sorted()` ou `count()` sur un flux infini ne terminent **jamais**.
- `iterator()` est une opération **terminale**.

</details>
