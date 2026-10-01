# Chapitre 12 (Modules / JPMS) — parcours, drills et plan de révision

Les **labs** (`ch12_modules-lab/01` → `11`) t'apprennent les notions avec le vrai outillage
(`javac`, `java`, `jar`, `jdeps`, `jlink`). Les **drills** (`ch12_modules-lab/drills`, 01 → 03)
te les font répéter jusqu'à ce qu'elles sortent toutes seules. Tous les drills utilisent les mêmes
données : **la bibliothèque du chapitre 10 découpée en 3 modules** (`drills/library/`) :
`library.model` (`Book`), `library.service` (`Catalog` et `internal.InMemoryCatalog`),
`library.app` (`Main`).

Les solutions (`solution/` de chaque lab et de chaque drill) sont **commentées** : chaque directive
ou commande explique pourquoi elle est là. Lis-les **après** avoir réussi.

⚠️ Sous Windows, lancer les scripts depuis **Git Bash** (`./run.sh`). Les messages de `java`
peuvent être dans la langue du système ; ceux de `javac` sont en anglais.

---

## Par quoi commencer : labs ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les labs du thème (comprendre).
Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — labs | Jour 2 — drills |
|---|---|---|---|
| 1 | Directives de base | 01 → 02 → 03 | Drill01 (TODO 1 à 4) |
| 2 | Services | 04 → 05 | Drill01 (TODO 7 et 8) |
| 3 | Réflexion et ligne de commande | 06 → 07 | Drill01 (TODO 5 et 6) |
| 4 | Cycles et types de modules | 08 → 09 | refaire Drill01 en entier |
| 5 | Outils | 10 | Drill02 |
| 6 | Synthèse | 11 (capstone : migration) | Drill03 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : un `module-info.java` qui utilise **toutes** les directives,
et le tableau des options longues / courtes (`-p`, `-m`, `-d`, `-c`, `-f`, `-e`, `-s`).

**Conseil propre à ce chapitre :** devant une erreur, lis QUI se plaint et QUAND :
`javac` (« not visible » : exports / requires), `java` au démarrage (« Module … not found »,
`FindException`), `java` pendant l'exécution (`IllegalAccessError`, `InaccessibleObjectException` :
exports / opens ; `ServiceConfigurationError` : uses).

| Drill | Contenu | TODO |
|---|---|---|
| 01 `Directives` | `exports`, `requires`, `requires transitive`, `exports … to`, `opens … to`, `open module`, `uses`, `provides … with` (un scénario par directive) | 8 |
| 02 `Commands` | `javac --module-source-path`, `java -p -m`, `-d`, `--list-modules`, `jar -c -f -e -C`, `jar -d`, `jdeps -s`, `jdeps --jdk-internals`, `--show-module-resolution` | 10 |
| 03 `MixedKata` | les 3 `module-info` de la bibliothèque et la commande de lancement, **sans indiquer la forme** | 4 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les trous **sans regarder la « CARTE MÉMOIRE »** (dans le `README.md` du drill).
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `./run.sh exercise 01` (ou `02`, `03`) depuis `drills/` jusqu'à 100 %.
   `./run.sh solution` vérifie les corrigés.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill03 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : les `ENONCE.md` des labs contiennent les **messages réels** de `javac` et de
  `java` (`package … is not visible`, `cyclic dependence involving …`, `does not declare uses`,
  `does not "opens …" to module …`, `Module … not found`…). Relis-les avant l'examen, puis fais les
  questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore ch12_modules-lab/drills/Drill01_Directives/exercise
git restore ch12_modules-lab/01-exports-requires/exercise
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 7/8`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Directives | | | | | | | |
| 02 Commandes | | | | | | | |
| 03 Kata mélangé | | | | | | | |
