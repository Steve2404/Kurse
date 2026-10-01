# Chapitre 14 (I/O) — parcours, drills et plan de révision

Les **exercices** (`ch14_io/exercises`, 01 → 20) t'apprennent les notions.
Les **drills** (`ch14_io/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : `drills/Workspace.java`
crée dans un dossier **temporaire** une petite arborescence (docs, logs, src : 11 entrées, 5 fichiers)
et la supprime ensuite. Rien n'est jamais écrit dans le dépôt.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

⚠️ Les chemins s'affichent avec `\` sous Windows : les tests comparent avec des `/`
(`Workspace.slash`). Les méthodes de `Files` lancent toutes `IOException` (checked).

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | `File` et `Path` | 01 → 02 → 03 | Drill01 |
| 2 | `Files` : opérations, options, attributs, exceptions | 04 → 05 → 06 → 07 → 08 | Drill02 |
| 3 | `Files` et les streams : `list`, `walk`, `find`, `lines` | 09 → 10 → 11 | Drill03 |
| 4 | `java.io` : octets, tampons, caractères, mise en forme | 12 → 13 → 14 → 15 → 16 | Drill04 (TODO 1 à 8, 10) |
| 5 | Sérialisation | 17 → 18 | Drill04 (TODO 9) |
| 6 | Flux système et `Console` | 19 | refaire Drill04 |
| 7 | Synthèse | 20 (capstone : copier une arborescence) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : le tableau des classes `java.io` (octets / caractères,
bas / haut niveau), les méthodes de `Path` qui ne touchent pas le disque, et ce que lance chaque
méthode de `Files` sur un fichier absent ou déjà présent.

**Conseil propre à ce chapitre :** devant chaque appel, demande-toi *est-ce que ça touche le
disque ?* (`Path` : non ; `Files` : oui, donc `IOException`) et *qui ferme le flux ?*
(try-with-resources, y compris pour les `Stream` de `Files`).

| Drill | Contenu | TODO |
|---|---|---|
| 01 `PathApi` | `Path.of`, `getFileName`, `getParent`, `getNameCount`, `getName`, `subpath`, `resolveSibling`, `resolve`, `relativize`, `normalize`, `isAbsolute`, `endsWith` | 12 |
| 02 `FilesOperations` | `exists`, `isDirectory`, `readAllLines`, `createDirectories` + `writeString`, `APPEND`, `copy`, `REPLACE_EXISTING`, `move`, `delete` / `deleteIfExists`, `size`, dates, `isSameFile` | 12 |
| 03 `FilesStreams` | `list`, `walk`, `walk(max)`, `find`, `lines`, `newBufferedReader`, `newBufferedWriter`, chemins relatifs, plus gros fichier | 10 |
| 04 `JavaIoStreams` | `FileOutputStream`, `FileInputStream`, `BufferedReader`, `BufferedWriter`, `FileWriter(…, true)`, `PrintWriter`, `InputStreamReader`, `ByteArrayOutputStream`, sérialisation, `mark` / `reset` | 10 |
| 05 `MixedKata` | 8 tâches sur l'arborescence, **sans indiquer la forme** | 8 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill05 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : l'exercice 03 compare ta version de `Path` au vrai `Path`
  (`Path.of("")` a 1 nom, `resolve` ne normalise pas…) ; l'exercice 07 compare tes règles aux
  exceptions réelles de `Files` (`FileAlreadyExistsException`, `NoSuchFileException`,
  `DirectoryNotEmptyException`, `NotDirectoryException`…) ; l'exercice 16 vérifie la carte `java.io`
  par réflexion ; l'exercice 18 la sérialisation d'un graphe. Relis leurs Javadoc avant l'examen,
  puis fais les questions de révision du livre.
- Le chapitre 10 (streams) s'applique tel quel à `Files.lines`, `list`, `walk`, `find`.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch14_io/drills/exercises/Drill01_PathApi.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 API Path | | | | | | | |
| 02 Opérations Files | | | | | | | |
| 03 Files et streams | | | | | | | |
| 04 java.io | | | | | | | |
| 05 Kata mélangé | | | | | | | |
