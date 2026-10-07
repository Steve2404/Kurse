# Drill de rappel 6 — Kata mixte (options courtes)

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Dans ton script, **uniquement des options courtes** pour `java -d` et `jar` : `Check` refuse `--describe-module`, `--create`, `--main-class` et `--file`.
- Tes modules vont dans `ch12_modules/drills/r06_kata/src/`. Ton script `recall.sh` va dans ce dossier, avec `P=ch12_modules/drills/r06_kata`, `OUT=build/ch12/r06_kata`, et `mkdir -p "$OUT/jars"`.

**Les notions de ce drill ont été apprises dans :** projets 1 à 6 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée tes modules** dans `ch12_modules/drills/r06_kata/src/`, un dossier par module (comme au projet 1, en-tête : clic droit sur `Kurse` → **New** → **Directory**, puis **New** → **File** pour chaque `module-info.java` et chaque classe).
3. **Crée ton script** : clic droit sur le dossier `r06_kata` (celui de ce `TODO.md`) → **New** → **File** → `recall.sh`. Recopie l'en-tête donné dans les **Règles**.
4. **Lance-le** depuis le dossier `Kurse`, dans le terminal PowerShell :
   `& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch12_modules/drills/r06_kata/recall.sh`
5. **Bloqué plus de 3 minutes sur un défi ?** Écris `# D03 : ✗` dans ton script, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas, relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Deux modules :
  - **`k.core`** :
    - exporte `k.core` (`Api.version()` rend `"k.core v1"`) ;
    - exporte `k.core.util` **seulement** à `k.app` (`Text.reverse(String)`) ;
    - **ouvre** `k.core.model` à tous, **sans** l'exporter (`Point`, avec `private int x = 3` et `private int y = 4`) ;
  - **`k.app`** : requiert `k.core`. Son `main` charge `Point` par `Class.forName`, le crée, lit `x` par réflexion, et affiche :
    `Api.version() + " | " + Text.reverse("module") + " | x=" + x + " | k.core.model exporte " + <isExported> + ", ouvert " + <isOpen>`.
  
  Dans le script : `echo "--- D01"`, puis compile (`-m k.app,k.core`) et lance.
  → `k.core v1 | eludom | x=3 | k.core.model exporte true, ouvert true`
- ☐ **D02.** `echo "--- D02 java -d"`, puis le descripteur de `k.core` avec **`java -p … -d k.core`**, filtré comme au drill 1 (`sed`, `grep -v "java.base mandated"`, `sort`).
  → `opens k.core.model` et `qualified exports k.core.util to k.app`
- ☐ **D03.** `echo "--- D03 jar -c -f -e, puis jar -d -f"`. Puis :
  1. crée les deux jars avec **`jar -c -f`**, celui de l'application avec **`-e k.app.Main`** ;
  2. décris `k.app.jar` avec **`jar -d -f`** (filtres : `sed 's/ jar:.*//' | grep -v "java.base mandated" | sort`) ;
  3. lance `-m k.app` depuis les jars.
  → `main-class k.app.Main`

## Expériences (hors sortie attendue)

1. Pourquoi `isExported("k.core.model")` rend-il `true` à l'exécution, alors que le paquet est seulement ouvert ? (Lis la Javadoc de `Module.isExported` : « exports **or opens** ».)
2. Dans `k.app`, écris `import k.core.model.Point;` : compile-t-il ? Pourquoi ?
3. Que veut dire `-d` pour `javac` ? Et pour `java` ?

## Sortie attendue complète

```
--- D01
k.core v1 | eludom | x=3 | k.core.model exporte true, ouvert true
--- D02 java -d
exports k.core
k.core
opens k.core.model
qualified exports k.core.util to k.app
--- D03 jar -c -f -e, puis jar -d -f

contains k.app
k.app
main-class k.app.Main
requires k.core
k.core v1 | eludom | x=3 | k.core.model exporte true, ouvert true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Longue | Courte | Outil |
|---|---|---|
| `--module-path` | `-p` | `javac`, `java` (pas `jdeps` : là, `-p` = `--package`) |
| `--module` | `-m` | `javac`, `java` |
| `--describe-module` | `-d` | `java`, `jar` |
| `--create` / `--file` / `--list` / `--extract` | `-c` / `-f` / `-t` / `-x` | `jar` |
| `--main-class` | `-e` | `jar` |
| (destination) | `-d` | `javac` (ATTENTION : rien à voir avec `java -d`) |

- `opens` sans `exports` : invisible à la **compilation** (pas d'`import`), mais accessible par **réflexion** à l'exécution.

</details>
