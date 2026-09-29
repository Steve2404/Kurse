# Chapitre 1 (Building Blocks) — parcours, drills et plan de révision

Les **exercices** (`ch1_buildingblocks/exercises`, 01 → 17) t'apprennent les notions.
Les **drills** (`ch1_buildingblocks/drills/exercises`, 01 → 05) te les font répéter
jusqu'à ce qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données :
la caisse de `drills/Shop.java` (arguments de ligne de commande, quantités tapées au
clavier, code produit). Lis ce fichier une fois et garde-le ouvert à côté.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi cet outil et quel piège il évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser). Jamais tous les exercices puis
tous les drills.

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | `main()` et arguments | 01 → 02 → 03 | (révision : refaire 03 de zéro) |
| 2 | Packages, imports, fichiers | 04 | (révision : relire les 5 règles de l'Exercise04) |
| 3 | Types, littéraux, identifiants, wrappers, blocs de texte | 05 → 06 → 07 → 08 → 09 | Drill01, Drill02, Drill03 |
| 4 | Variables : initialisation, `var`, portée | 10 → 11 → 12 → 13 | Drill04 |
| 5 | Objets : constructeurs, ordre d'initialisation, ramasse-miettes | 14 → 15 → 16 | (révision : refaire 15 et 16 de zéro) |
| 6 | Synthèse | 17 (capstone : la caisse) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues ce jour-là (10 – 20 min), 2) la
nouveauté, 3) 2 minutes de « carte vierge » (écrire de mémoire les règles vues).

**Pour un exercice :** lancer `main()` → lire jusqu'au premier TODO → remplir → relancer
jusqu'à 100 % → lire le corrigé commenté.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `WrapperApi` | `parseInt` (avec base), `valueOf`, constantes, `compare`, `toBinaryString`, `Boolean.parseBoolean`, `Character.*` | 21 |
| 02 `LiteralsAndPrimitives` | bases (0b, 0, 0x), `_`, suffixes L/f, `char` et unicode, limites, débordement, valeurs par défaut | 15 |
| 03 `TextBlocksAndStrings` | `"""` (saut final, marge, `\s`, `\`, guillemets), concaténation de gauche à droite | 11 |
| 04 `VariablesAndVar` | types choisis par `var`, déclarations groupées, `final`, portée de bloc, masquage, `static`, initialisation | 12 |
| 05 `MixedKata` | 13 questions sur la caisse, **sans indiquer la méthode** | 13 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris la ligne de
   mémoire**. Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois. Réussi à
  100 % du premier coup et vite → séance suivante ; sinon on le refait le lendemain.
- **Mélange** : le Drill05 chaque semaine pendant la révision de l'examen.
- **Carte vierge** : sur papier, les 3 signatures valides de `main()`, les 5 règles
  d'import et de fichier, les règles de `_`, des identifiants et de `var`.
- **Lecture de code** : l'examen demande aussi « ça compile ? ». Les Javadoc des exercices
  02, 04, 06, 07, 10 et 11 contiennent les versions **fausses** avec le vrai message de
  `javac` : relis-les avant l'examen, puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

Tant que tes réponses ne sont pas commitées :

```
git restore src/main/java/ch1_buildingblocks/drills/exercises/Drill01_WrapperApi.java
```

Si tu as commité tes réponses : `git restore --source=origin/main -- <chemin>`.
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `29/09 – 9 min – 18/21`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Wrappers | | | | | | | |
| 02 Littéraux | | | | | | | |
| 03 Texte | | | | | | | |
| 04 Variables | | | | | | | |
| 05 Kata mélangé | | | | | | | |
