# Drill de rappel 4 — Modules nommés, automatiques et sans nom

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Dans `ch12_modules/drills/r04_types/` :
  - `legacy/t/util/Tools.java` : `static String shout(String s)` rend `s.toUpperCase() + "!"`. Pas de `module-info` ;
  - `manifest.txt` : `Automatic-Module-Name: org.acme.tools` ;
  - `src/t.app/`, le module nommé.
- Ton script `recall.sh` va dans ce dossier, avec `P=ch12_modules/drills/r04_types` et `OUT=build/ch12/r04_types`. Il commence par compiler `Tools.java` dans `$OUT/legacy`, puis crée `$OUT/names`, `$OUT/split` et `$OUT/lib`.

**Les notions de ce drill ont été apprises dans :** projet 4 (étapes 1 à 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée tes modules** dans `ch12_modules/drills/r04_types/src/`, un dossier par module (comme au projet 1, en-tête : clic droit sur `Kurse` → **New** → **Directory**, puis **New** → **File** pour chaque `module-info.java` et chaque classe).
3. **Crée ton script** : clic droit sur le dossier `r04_types` (celui de ce `TODO.md`) → **New** → **File** → `recall.sh`. Recopie l'en-tête donné dans les **Règles**.
4. **Lance-le** depuis le dossier `Kurse`, dans le terminal PowerShell :
   `& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch12_modules/drills/r04_types/recall.sh`
5. **Bloqué plus de 3 minutes sur un défi ?** Écris `# D03 : ✗` dans ton script, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas, relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `echo "--- D01 noms automatiques"`. Pour chacun des fichiers `math-utils-3.0.jar`, `string_tools.jar`, `parser2-1.0.0-SNAPSHOT.jar` et `my.cool.lib.jar` :
  1. crée-le dans `$OUT/names` avec `-C "$OUT/legacy" .` ;
  2. affiche `<fichier> -> $(jar --describe-module --file … | grep automatic | sed 's/ jar:.*//')`.
  → `math-utils-3.0.jar -> math.utils@3.0 automatic` …
- ☐ **D02.** `echo "--- D02 paquet partage"`.
  1. Copie `math-utils-3.0.jar` et `string_tools.jar` dans `$OUT/split` ;
  2. lance `java -p "$OUT/split" --add-modules ALL-MODULE-PATH -version` ;
  3. garde la ligne `ResolutionException: …`, puis réduis-la au paquet en cause (l'ordre des deux modules dans le message varie) :
     ```bash
     2>&1 | grep -o "ResolutionException: .*" | sed -E 's/.*contains package ([a-z.]+),.*/ResolutionException : paquet \1 dans deux modules/' || true
     ```
  → `ResolutionException : paquet t.util dans deux modules`
- ☐ **D03.** `echo "--- D03 module nomme -> module automatique"`.
  - `t.app` **requiert `math.utils`**. Son `main` affiche :
    `Tools.shout("auto") + " module " + <nom> + " automatique " + <isAutomatic> + ", exporte t.util " + <isExported("t.util")> + ", lit le module sans nom " + <canRead(unnamed)> + ", t.app le lit " + <canRead>` ;
  - **Le script :**
    1. copie `math-utils-3.0.jar` dans `$OUT/lib` ;
    2. compile avec `-p "$OUT/lib"` ;
    3. range `t.app.jar` dans `$OUT/lib` ;
    4. lance avec `-p "$OUT/lib" -m t.app/t.app.Main`.
  → `AUTO! module math.utils automatique true, exporte t.util true, lit le module sans nom true, t.app le lit true`
- ☐ **D04.** `echo "--- D04 Automatic-Module-Name"`. Crée `$OUT/names/tools-9.9.jar` avec `--manifest "$P/manifest.txt"`, puis `jar --describe-module … | grep automatic`.
  → `org.acme.tools@9.9 automatic`

## Expériences (hors sortie attendue)

1. Mets deux `-p` sur la même ligne de `java` : lequel gagne ?
2. Que donne le nom de fichier `1-utils.jar` ?
3. `t.app` peut-il faire `requires` vers du code du classpath ? Pourquoi ?
4. Pourquoi un paquet partagé (*split package*) est-il interdit entre deux modules ?

## Sortie attendue complète

```
--- D01 noms automatiques
math-utils-3.0.jar -> math.utils@3.0 automatic
string_tools.jar -> string.tools automatic
parser2-1.0.0-SNAPSHOT.jar -> parser2@1.0.0-SNAPSHOT automatic
my.cool.lib.jar -> my.cool.lib automatic
--- D02 paquet partage
ResolutionException : paquet t.util dans deux modules
--- D03 module nomme -> module automatique
AUTO! module math.utils automatique true, exporte t.util true, lit le module sans nom true, t.app le lit true
--- D04 Automatic-Module-Name
org.acme.tools@9.9 automatic
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| | Nommé | Automatique | Sans nom |
|---|---|---|---|
| Où ? | module path, **avec** `module-info` | module path, jar **sans** `module-info` | classpath |
| Nom | celui du `module-info` | `Automatic-Module-Name`, sinon déduit du fichier | aucun (`getName()` rend `null`) |
| Exporte | ce qu'il déclare | **tous** ses paquets | tous ses paquets, mais personne ne peut le `requires` |
| Lit | ce qu'il requiert | **tous** les modules, dont le sans nom | **tous** les modules résolus |

**Le nom déduit du fichier :**
1. on retire `.jar` ;
2. on retire la version (à partir de `-` suivi d'un chiffre) ;
3. chaque caractère non alphanumérique devient `.` ;
4. les `.` répétés sont fusionnés, et ceux du début et de la fin retirés.

**La migration :**
- **bottom-up** : on modularise d'abord les bibliothèques (les feuilles) ;
- **top-down** : on met tout sur le module path ; les jars deviennent automatiques, et l'application devient nommée d'abord.

</details>
