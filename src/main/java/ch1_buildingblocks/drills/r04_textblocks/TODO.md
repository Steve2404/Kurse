# Drill de rappel 4 — Les text blocks

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`**.
- **Chaque défi utilise un text block** (8 en tout).
- `Check` montre les espaces de tête sous forme de `·` quand une ligne diffère.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_textblocks` → **New** → **Java Class** → `Recall04`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher. Par exemple, pour un défi `D01` qui attend `D01 : 8 16`, écris un `System.out.println("D01 : " + … + " " + …);`, où les `…` sont les valeurs que **Java** calcule.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
