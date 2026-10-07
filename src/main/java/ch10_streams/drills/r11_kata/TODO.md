# Drill de rappel 11 — Kata mixte chronométré (tout le chapitre)

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 25 min, **sans carte mémoire**. C'est le test final de chaque cycle de révision.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall11`** et ton record pour `Data.BOOKS`.
- Chaque défi tient en **une** chaîne de stream, éventuellement suivie d'une 2e sur le résultat.
- Écris une ligne `Dxx : ` par défi.

**Les notions de ce drill ont été apprises dans :** projets 1 à 8 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r11_kata` → **New** → **Java Class** → `Recall11`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall11`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall11`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Le genre dont le prix **moyen** est le plus élevé, avec cette moyenne (arrondie à 2 décimales avec `Math.round`).
  → `D01 : Fantasy 9.95`
- ☐ **D02.** L'auteur qui totalise le plus de pages, via une `Map` auteur → pages construite par `toMap`.
  → `D02 : Zola`
- ☐ **D03.** Les titres groupés par **décennie** dans une `TreeMap`, joints par `|`. N'affiche que les décennies **avant 1960**, avec une vue de la `TreeMap`.
  → `D03 : {1870=L'Assommoir, 1880=Germinal, 1930=Le Hobbit, 1950=Fondation}`
- ☐ **D04.** Un auteur a-t-il écrit plusieurs livres ? Réponds en comparant deux comptes.
  → `D04 : true`
- ☐ **D05.** Le titre du **deuxième** livre le plus cher.
  → `D05 : Hyperion`
- ☐ **D06.** La longueur minimale, la longueur maximale et la somme des longueurs des titres, en un seul passage.
  → `D06 : 4 15 76`
- ☐ **D07.** Les pages moyennes des livres SF et celles des autres livres, en un seul passage (arrondies à 1 décimale).
  → `D07 : SF 355.0 / autres 444.8`
- ☐ **D08.** Les 2 mots les plus fréquents de `WORDS`. À égalité, l'ordre alphabétique décide. Affiche `mot=nombre`.
  → `D08 : java=2, stream=2`
- ☐ **D09.** L'initiale de l'auteur du livre SF le plus ancien. Utilise une chaîne `Optional` et un `?` par défaut.
  → `D09 : A`
- ☐ **D10.** La somme cumulée de `NUMBERS` (chaque position reçoit la somme de son préfixe), en `List<Integer>`.
  → `D10 : [5, 8, 16, 17, 26, 28, 36, 43]`

## Sortie attendue complète

```
D01 : Fantasy 9.95
D02 : Zola
D03 : {1870=L'Assommoir, 1880=Germinal, 1930=Le Hobbit, 1950=Fondation}
D04 : true
D05 : Hyperion
D06 : 4 15 76
D07 : SF 355.0 / autres 444.8
D08 : java=2, stream=2
D09 : A
D10 : [5, 8, 16, 17, 26, 28, 36, 43]
```

## Après le kata

- **Pour chaque défi raté ou lent (plus de 3 min) :** relis la carte du drill correspondant, puis refais **ce drill** le lendemain.
- **Note ton temps** dans le tableau de suivi de `drills/README.md`.
