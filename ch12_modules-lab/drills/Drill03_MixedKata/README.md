# Drill 03 - Kata melange : toute la bibliotheque, sans indice de forme

Mode d'emploi : voir `../Drill01_Directives/README.md`. Fais ce drill
seulement quand les drills 01 et 02 passent. Verifie : `./run.sh exercise 03`.

Les trois `module-info.java` de `exercise/` sont VIDES, et `kata.sh`
aussi. Lis `exercise/library.app/com/example/library/app/Main.java` :
il charge le catalogue par `ServiceLoader`, lit le titre d'un `Book`,
puis lit le champ PRIVE `author` par reflexion. A toi de deduire
chaque directive necessaire, et seulement celles-la.

| TODO | Fichier |
|---|---|
| 1 | `exercise/library.model/module-info.java` |
| 2 | `exercise/library.service/module-info.java` |
| 3 | `exercise/library.app/module-info.java` (une seule dependance directe !) |
| 4 | `exercise/kata.sh` : la commande qui lance l'application |

Sortie attendue :

```
Catalogue : 3 livres
Premier titre : Dune
Auteur lu par reflexion : Herbert
```

Remettre a zero : `git restore ch12_modules-lab/drills/Drill03_MixedKata/exercise`
