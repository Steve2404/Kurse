# Chapitre 14 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, la règle du crescendo, et **les règles d'un programme qui touche au disque**) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les types, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_paths` — plan du site | `Path`, `Paths`, `File`, décomposer, `resolve`, `relativize`, `normalize` | `PathLab` | liens relatifs avec aller-retour, mini-shell qui ne sort pas de la racine, arborescence |
| ☐ | `p02_backup` — sauvegarde incrémentale | `Files` : créer, copier, déplacer, supprimer, `mismatch`, `walk`/`list`/`find`, exceptions | `BackupLab` | supprimer, copier et comparer des arbres |
| ☐ | `p03_rle` — compresseur | flux d'octets et de caractères, tampons, encodages, `mark`/`reset`, `Data*Stream` | `StreamLab` | compression RLE, Adler-32 à la main |
| ☐ | `p04_savegame` — sauvegarde de partie | sérialisation, `transient`, `static`, constructeurs à la relecture, records | `SaveGame` | pile d'annulation par copies profondes |
| ☐ | `p05_sort` — tri externe | `Files.lines`, `newBufferedReader`/`Writer`, `readAllLines`, options d'ouverture | `SortLab` | trier un fichier trop gros : paquets triés + fusion par tas |
| ☐ | `p06_duplicates` — chasseur de doublons | attributs, vue d'attributs, `walkFileTree` + `SimpleFileVisitor`, `walk` avec profondeur | `Dedup` | occupation par dossier, doublons groupés par taille puis par contenu |
| ☐ | `p07_vcs` — **capstone** gestionnaire de versions | tout le chapitre | `MiniGit` | stockage par empreinte, status, checkout, diff de lignes (LCS) |
