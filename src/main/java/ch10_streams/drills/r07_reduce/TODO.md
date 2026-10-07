# Drill de rappel 7 — `reduce` et `collect`

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`**.
- Utilise `Data.WORDS`.
- Écris une ligne `Dxx : ` par défi.

**Les notions de ce drill ont été apprises dans :** projet 4 (étapes 1 à 6). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_reduce` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall07`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Deux `reduce` **avec identité** :
  - le nombre total de lettres (après un `map` des longueurs) ;
  - les initiales concaténées.
  → `D01 : 52 slojscmjf`
- ☐ **D02.** Le mot le plus long avec un `reduce` **sans identité** (à égalité, garde le premier). Puis concatène un stream **vide** de la même façon et affiche le résultat brut.
  → `D02 : collector Optional.empty`
- ☐ **D03.** Le total des lettres en **un seul** `reduce`, directement sur les mots, sans `map`.
  → `D03 : 52`
- ☐ **D04.** Réduis 1, 2, 3, 4 avec l'identité **10** et `Integer::sum` en un seul passage. Puis réduis **séparément** 1, 2 et 3, 4 (même identité) et additionne les deux résultats, comme le ferait un découpage.
  → `D04 : un passage 20, deux moities 30`
- ☐ **D05.** Deux `collect` à 3 arguments (sans `Collectors`) :
  - une `ArrayList` des mots en majuscules : affiche le 1er et la taille ;
  - un `TreeSet` des mots : affiche le 1er et la taille.
  → `D05 : STREAM 9 collector 7`
- ☐ **D06.** Les initiales, avec un `collect` à 3 arguments dans un `StringBuilder`.
  → `D06 : slojscmjf`
- ☐ **D07.** Écris avec `Collector.of` un collecteur « mot le plus court ». Il a un conteneur mutable, un combiner correct, et un finisher qui rend `-` si le flux est vide. Applique-le avec `collect`. Puis applique-le **à la main** sur deux moitiés de `WORDS` (indices 0 à 3, puis 4 à 8) : `supplier()` et `accumulator()` pour chaque moitié, puis `combiner()` et `finisher()`. Enfin, applique-le sur un flux vide.
  → `D07 : map map -`
- ☐ **D08.** Concatène `a`, `b`, `c` par `reduce` (`String::concat`) puis par `collect` (`StringBuilder`). Les deux résultats sont-ils égaux ?
  → `D08 : true`

## Sortie attendue complète

```
D01 : 52 slojscmjf
D02 : collector Optional.empty
D03 : 52
D04 : un passage 20, deux moities 30
D05 : STREAM 9 collector 7
D06 : slojscmjf
D07 : map map -
D08 : true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Forme | Retour | Quand |
|---|---|---|
| `reduce(T identity, BinaryOperator<T>)` | `T` | même type ; l'identité doit être **neutre** |
| `reduce(BinaryOperator<T>)` | `Optional<T>` | pas d'identité, flux vide possible |
| `reduce(U identity, BiFunction<U, ? super T, U>, BinaryOperator<U>)` | `U` | le type change ; le combiner fusionne deux résultats partiels |
| `collect(Supplier<R>, BiConsumer<R, ? super T>, BiConsumer<R, R>)` | `R` | réduction **mutable** |
| `collect(Collector)` | `R` | |
| `Collector.of(supplier, accumulator, combiner, [finisher], characteristics...)` | `Collector` | |

**Les règles d'une réduction correcte par morceaux** (indispensables au chapitre 13, avec les streams parallèles) :
- **identité :** `acc(identity, x) == x` ;
- **associativité :** `(a op b) op c == a op (b op c)` ;
- **combiner** compatible avec l'accumulateur.

Une identité non neutre est ajoutée **une fois par morceau**.

</details>
