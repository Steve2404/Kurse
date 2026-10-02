# Drill de rappel 9 — `groupingBy`, `partitioningBy`, collecteurs en aval, `teeing`

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 30 min, puis 15 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall09`** et ton record pour `Data.BOOKS`.
- Les `Map` affichées doivent être triées par clé.
- Écris une ligne `Dxx : ` par défi.

## Défis

- ☐ **D01.** Un `groupingBy` par genre **avec un seul argument**. Affiche :
  - le nom simple de la classe de la `Map` obtenue ;
  - la taille du groupe SF ;
  - les clés triées.
  → `D01 : HashMap 4 [Classique, Fantasy, SF]`
- ☐ **D02.** Le nombre de livres par genre.
  → `D02 : {Classique=2, Fantasy=2, SF=4}`
- ☐ **D03.** Les auteurs **triés et sans doublon**, par genre.
  → `D03 : {Classique=[Zola], Fantasy=[Tolkien], SF=[Asimov, Gibson, Herbert, Simmons]}`
- ☐ **D04.** Trois résultats :
  - le nombre de livres parus avant 1950, partitionné (vrai/faux) ;
  - les clés d'une partition « plus de 10 000 pages », qui est toujours vide côté `true` ;
  - la taille de ce côté `true`.
  → `D04 : {false=5, true=3} [false, true] 0`
- ☐ **D05.** Le nombre de livres à plus de 9 €, par genre. Un genre sans aucun livre de ce type doit **rester** dans la `Map`, avec 0.
  → `D05 : {Classique=0, Fantasy=1, SF=2}`
- ☐ **D06.** Pour chaque auteur, l'ensemble trié des **mots** de ses titres. Affiche celui de Tolkien.
  → `D06 : [Hobbit, Le, Silmarillion]`
- ☐ **D07.** Le titre du plus épais livre de chaque genre. La `Map` ne contient **aucun** `Optional`.
  → `D07 : {Classique=Germinal, Fantasy=Le Silmarillion, SF=Hyperion}`
- ☐ **D08.** Deux `reducing` :
  - le total des pages par genre, avec `reducing` à 3 arguments ;
  - le livre le plus ancien par genre, avec `reducing` à 1 argument : affiche celui de SF.
  → `D08 : {Classique=1104, Fantasy=675, SF=1420} Fondation`
- ☐ **D09.** Genre → (siècle → nombre de livres). C'est un groupement à deux niveaux, trié aux deux niveaux.
  → `D09 : {Classique={1800=2}, Fantasy={1900=2}, SF={1900=4}}`
- ☐ **D10.** En **un seul** passage, la moyenne des pages (division entière) et le nombre de livres, mis en phrase.
  → `D10 : 399 pages en moyenne sur 8`
- ☐ **D11.** Le nombre de livres par auteur, trié par auteur. Puis le nombre de genres distincts, collectés en ensemble.
  → `D11 : {Asimov=1, Gibson=1, Herbert=1, Simmons=1, Tolkien=2, Zola=2} 3`
- ☐ **D12.** Les titres **non** SF joints par `/`, en passant par une partition.
  → `D12 : Le Hobbit/Le Silmarillion/Germinal/L'Assommoir`

## Sortie attendue complète

```
D01 : HashMap 4 [Classique, Fantasy, SF]
D02 : {Classique=2, Fantasy=2, SF=4}
D03 : {Classique=[Zola], Fantasy=[Tolkien], SF=[Asimov, Gibson, Herbert, Simmons]}
D04 : {false=5, true=3} [false, true] 0
D05 : {Classique=0, Fantasy=1, SF=2}
D06 : [Hobbit, Le, Silmarillion]
D07 : {Classique=Germinal, Fantasy=Le Silmarillion, SF=Hyperion}
D08 : {Classique=1104, Fantasy=675, SF=1420} Fondation
D09 : {Classique={1800=2}, Fantasy={1900=2}, SF={1900=4}}
D10 : 399 pages en moyenne sur 8
D11 : {Asimov=1, Gibson=1, Herbert=1, Simmons=1, Tolkien=2, Zola=2} 3
D12 : Le Hobbit/Le Silmarillion/Germinal/L'Assommoir
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Forme | Résultat |
|---|---|
| `groupingBy(f)` | `Map<K, List<T>>` (un `HashMap` en pratique, sans garantie) |
| `groupingBy(f, downstream)` | `Map<K, D>` |
| `groupingBy(f, mapFactory, downstream)` | la `Map` fournie, par exemple `TreeMap::new` |
| `partitioningBy(pred)` | `Map<Boolean, List<T>>` ; contient **toujours** `false` et `true` |
| `partitioningBy(pred, downstream)` | `Map<Boolean, D>` ; pas de fabrique de `Map` |

**Les collecteurs en aval :**
- `counting`, `summingX`, `averagingX`, `minBy` / `maxBy` (rendent un `Optional`) ;
- `mapping(f, d)`, `filtering(pred, d)` (Java 9 ; garde le groupe **vide**), `flatMapping(f, d)` (Java 9) ;
- `reducing(identity, mapper, op)`, `reducing(op)` (rend un `Optional`) ;
- `collectingAndThen(d, finisher)` ;
- `teeing(d1, d2, merger)` (Java 12).

**Différence clé :** `filter` **avant** `groupingBy` supprime les groupes vides ; `filtering` **en aval** les garde.

</details>
