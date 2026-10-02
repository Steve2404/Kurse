# Drill de rappel 4 — Les text blocks

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`**.
- **Chaque défi utilise un text block** (8 en tout).
- `Check` montre les espaces de tête sous forme de `·` quand une ligne diffère.

## Défis

- ☐ **D01.** Un text block d'une seule ligne, `bonjour`, **sans** saut de ligne final, affiché entre crochets.
  → `D01 : [bonjour]`
- ☐ **D02.** Un text block de deux lignes `a` et `b`, dont les `"""` fermants sont **sur leur propre ligne**. Affiche-le entre crochets avec `print`, suivi d'un `"\n"`. Où tombe le `]` ?
  → `D02 : [a` / `b` / `]` (sur 3 lignes)
- ☐ **D03.** Deux lignes `x` et `y`, chacune précédée de **2 espaces** à l'affichage, obtenus **par la position** des `"""` fermants.
  → `D03 :` / `  x` / `  y`
- ☐ **D04.** `un deux` sur **une** ligne à l'affichage, mais écrit sur **deux** lignes dans le code source.
  → `D04 : [un deux]`
- ☐ **D05.** `fin` suivi d'**un espace conservé**, entre crochets.
  → `D05 : [fin ]`
- ☐ **D06.** La ligne `"cite" et """triple""" fin`. Quels guillemets faut-il échapper, et lesquels non ?
  → `D06 : "cite" et """triple""" fin`
- ☐ **D07.** `col1`, une **tabulation**, `col2`, puis un **saut de ligne écrit en échappement**, puis `ligne2`, le tout sur **une** ligne de code source.
  → `D07 :` / `col1	col2` / `ligne2`
- ☐ **D08.** `haut`, une ligne vide, puis `bas`.
  → `D08 :` / `haut` / `` / `bas`

## Expériences (hors sortie attendue)

1. Écris `String t = """bonjour""";` sur **une** ligne. Que dit `javac` ?
2. Mets des espaces **à la fin** d'une ligne de text block (sans `\s`). Sont-ils conservés ?
3. Mets les `"""` fermants **plus à droite** que le texte. Change-t-il l'indentation ?

## Sortie attendue complète

```
D01 : [bonjour]
D02 : [a
b
]
D03 :
  x
  y
D04 : [un deux]
D05 : [fin ]
D06 : "cite" et """triple""" fin
D07 :
col1	col2
ligne2
D08 :
haut

bas
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Ouverture :** `"""` suivi d'un **saut de ligne** (rien d'autre sur la ligne, sauf des espaces). Le contenu commence à la ligne suivante.
- **Indentation accidentelle :** Java retire, de chaque ligne, le plus petit nombre d'espaces de tête. Ce calcul compte **aussi** la ligne des `"""` fermants quand ils sont seuls sur leur ligne. Reculer les `"""` à gauche ajoute donc de l'indentation.
- **Espaces de fin de ligne :** supprimés. `\s` force **un** espace, conservé.
- **`\` en fin de ligne :** supprime le saut de ligne (la ligne suivante est collée).
- **Fin du texte :** `"""` sur leur propre ligne → le texte finit par `\n`. `"""` collés au texte → pas de `\n` final.
- **Guillemets :** `"` et `""` sont libres. `"""` doit s'écrire `\"""` (au moins un des trois échappé).
- **Échappements :** `\n`, `\t`, `\"`, `\\` fonctionnent comme dans une chaîne normale.
- **Lignes vides :** elles sont conservées.

</details>
