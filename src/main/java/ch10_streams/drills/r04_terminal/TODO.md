# Drill de rappel 4 — Opérations terminales

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`** et ton record pour `Data.BOOKS`.
- Écris une ligne `Dxx : ` par défi.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 9) et projet 2 (étapes 5, 7 et 9). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_terminal` → **New** → **Java Class** → `Recall04`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall04`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Affiche trois résultats :
  - le nombre de livres ;
  - le titre le plus petit dans l'ordre alphabétique (`min`) ;
  - le titre du plus épais (`max`).
  → `D01 : 8 Dune Germinal`
- ☐ **D02.** Le premier livre Fantasy (son titre), puis le fait qu'**un** livre Fantasy quelconque existe.
  → `D02 : Le Hobbit true`
- ☐ **D03.** Affiche six réponses :
  - un livre de plus de 500 pages existe ;
  - tous ont plus de 500 pages ;
  - aucun n'a plus de 1000 pages ;
  - puis ces trois mêmes questions sur un stream **vide**.
  → `D03 : true false true | vide : false true true`
- ☐ **D04.** L'initiale de chaque auteur distinct, ajoutée à un `StringBuilder` par l'opération terminale « pour chaque ».
  → `D04 : HASTGZ`
- ☐ **D05.** Transforme les titres en tableau de deux façons : sans argument, puis typé `String[]`. Affiche le nom simple de la classe de chaque tableau et sa taille.
  → `D05 : Object[] 8 / String[] 8`
- ☐ **D06.** Collecte les titres de deux façons : `toList()` du stream, et le collecteur classique. Les deux listes sont-elles égales ? Puis ajoute un élément à celle du collecteur et affiche sa taille.
  → `D06 : meme contenu true, Collectors.toList() apres ajout -> 9 elements`

**Expérience** (hors sortie attendue) : ajoute un élément à la liste de `toList()`, lance, et note l'exception. Que garantit la Javadoc de chacune des deux listes ?
- ☐ **D07.** Affiche trois réductions :
  - le total des pages par `reduce` avec identité ;
  - l'année max par `reduce` **sans** identité ;
  - les genres distincts joints par `/`.
  → `D07 : 3199 1989 SF/Fantasy/Classique`
- ☐ **D08.** Le `min` d'un stream vide de `String`, puis le nombre d'éléments d'un stream **infini** borné à 5.
  → `D08 : Optional.empty 5`
- ☐ **D09.** Sur les entiers 1, 2, 3… à l'infini :
  - existe-t-il un entier supérieur à 1000 ?
  - sont-ils tous inférieurs à 10 ?
  
  Les deux appels doivent terminer.
  → `D09 : true false`
- ☐ **D10.** Le plus grand mot dans l'ordre naturel. Puis le plus court ; à égalité de longueur, prends le plus grand dans l'ordre alphabétique.
  → `D10 : stream map`

## Sortie attendue complète

```
D01 : 8 Dune Germinal
D02 : Le Hobbit true
D03 : true false true | vide : false true true
D04 : HASTGZ
D05 : Object[] 8 / String[] 8
D06 : meme contenu true, Collectors.toList() apres ajout -> 9 elements
D07 : 3199 1989 SF/Fantasy/Classique
D08 : Optional.empty 5
D09 : true false
D10 : stream map
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Terminale | Retour | Court-circuite | Flux infini |
|---|---|---|---|
| `count()` | `long` | non | ne termine pas |
| `min(cmp)` / `max(cmp)` | `Optional<T>` | non | ne termine pas |
| `findFirst()` / `findAny()` | `Optional<T>` | oui | termine |
| `anyMatch` / `allMatch` / `noneMatch` | `boolean` | oui | peut terminer |
| `forEach(Consumer)` | `void` | non | ne termine pas |
| `reduce(…)` | `T`, `Optional<T>` ou `U` | non | ne termine pas |
| `collect(…)` | `R` | non | ne termine pas |
| `toArray()` / `toArray(IntFunction)` | `Object[]` / `A[]` | non | ne termine pas |
| `toList()` (Java 16) | `List<T>` **non modifiable** | non | ne termine pas |
| `iterator()` | `Iterator<T>` | — | — |

**Sur un flux vide :** `allMatch` et `noneMatch` rendent `true`, `anyMatch` rend `false`.

`Stream.min` et `Stream.max` exigent un `Comparator`. Les versions primitives (`IntStream.max()`) n'en prennent pas.

</details>
