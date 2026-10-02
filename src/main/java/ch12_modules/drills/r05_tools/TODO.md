# Drill de rappel 5 — Les outils : `--describe-module`, `jdeps`, `jlink`

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Dans `ch12_modules/drills/r05_tools/` :
  - `src/r.report/` : `module r.report` **requiert `java.sql`**. Son `main` affiche `"rapport du " + java.sql.Date.valueOf("2026-10-02") + " dans " + <nom du module>` ;
  - `internal/Peek.java` : **sans paquet**. Son `main` affiche `new sun.security.x509.X500Name("CN=Ada").getCommonName()` (une API **interne** du JDK : à ne jamais faire en vrai).
- Ton script `recall.sh` va dans ce dossier, avec `P=ch12_modules/drills/r05_tools` et `OUT=build/ch12/r05_tools`. Chaque défi commence par `echo "--- Dnn <titre>"`, comme dans la sortie attendue.

## Défis

- ☐ **D01.** `java --describe-module java.sql | sed 's/@[0-9.]*//' | sort`.
  → `requires java.logging transitive` … `uses java.sql.Driver`
- ☐ **D02.** `java --list-modules`, en ne gardant que les lignes qui commencent par `java.s`, puis `sed 's/@.*//'`.
  → `java.scripting` … `java.sql.rowset`
- ☐ **D03.** Compile et lance `r.report`. Puis :
  - `jdeps -s --module-path "$OUT/mods" -m r.report` ;
  - `jdeps --print-module-deps` sur le même module.
  → `r.report -> java.sql` puis `java.base,java.sql`
- ☐ **D04.** Compile `Peek.java` dans `$OUT/cp` avec `--add-exports java.base/sun.security.x509=ALL-UNNAMED` (redirige ses avertissements avec `2>/dev/null`). Puis `jdeps --jdk-internals "$OUT/cp" | grep -- "->" | tr -s " " | sed "s/^ //"`.
  → `Peek -> sun.security.x509.X500Name JDK internal API (java.base)`
- ☐ **D05.** `jlink` de `r.report` dans `$OUT/image`, avec `--strip-debug --compress=2 --no-header-files --no-man-pages`. Puis :
  - `"$OUT/image/bin/java" --list-modules | sed 's/@.*//'` ;
  - le lancement de `r.report/r.report.Main` avec le `java` de l'image.
  → `java.base` … `r.report`, puis `rapport du 2026-10-02 dans r.report`

## Expériences (hors sortie attendue)

1. Pourquoi l'image contient-elle `java.xml` et `java.transaction.xa`, que `r.report` ne requiert pas ?
2. `jdeps -summary` est-il la même chose que `jdeps -s` ?
3. Lance `Peek` avec le `java` de l'image : que se passe-t-il ?
4. Que veut dire `ALL-UNNAMED` dans `--add-exports` ?

## Sortie attendue complète

```
--- D01 describe-module java.sql
exports java.sql
exports javax.sql
java.sql
requires java.base mandated
requires java.logging transitive
requires java.transaction.xa transitive
requires java.xml transitive
uses java.sql.Driver
--- D02 list-modules
java.scripting
java.se
java.security.jgss
java.security.sasl
java.smartcardio
java.sql
java.sql.rowset
--- D03 jdeps
rapport du 2026-10-02 dans r.report
r.report -> java.base
r.report -> java.sql
java.base,java.sql
--- D04 jdk-internals
cp -> java.base
Peek -> sun.security.x509.X500Name JDK internal API (java.base)
--- D05 jlink
java.base
java.logging
java.sql
java.transaction.xa
java.xml
r.report
rapport du 2026-10-02 dans r.report
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Commande | Rôle |
|---|---|
| `java --list-modules` | les modules observables (avec leur version) |
| `java --describe-module m` (`-d m`) | le descripteur de m |
| `jar --describe-module --file f` (`-d -f f`) | le descripteur d'un jar (ou le nom automatique déduit) |
| `jdeps -s` (`-summary`) | les dépendances, en résumé ; `-R` pour le récursif |
| `jdeps --module-path p -m m` | analyser un module (attention : pour `jdeps`, `-p` = `--package`) |
| `jdeps --print-module-deps` | la liste à donner à `jlink --add-modules` |
| `jdeps --jdk-internals` | repère les API internes du JDK utilisées |
| `jlink --module-path … --add-modules … --output dir` | une image minimale |
| `jlink --strip-debug --compress=2 --no-header-files --no-man-pages --launcher nom=module` | des options pour réduire l'image, et ajouter un lanceur |

- `jlink` ajoute les modules **requis transitivement**, mais pas les fournisseurs de services (`--bind-services` pour cela).
- Il refuse les modules **automatiques**.

</details>
