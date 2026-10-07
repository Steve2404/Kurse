# Drill de rappel 1 — Les directives de `module-info.java`

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Tes modules vont dans `ch12_modules/drills/r01_directives/src/` (un dossier par module).
- Ton script `recall.sh` va dans **ce** dossier, avec :
  - `P=ch12_modules/drills/r01_directives` et `OUT=build/ch12/r01_directives` ;
  - `set -e` et `rm -rf "$OUT"` ;
  - `javac -d "$OUT/mods" --module-source-path "$P/src" -m d.app,d.plugin,d.open,d.extra` ;
  - `java -p "$OUT/mods" -m d.app/d.app.Main` ;
  - pour chaque module (`d.base d.mid d.friend d.plugin d.app d.open d.extra`) : `echo "--- <m>"`, puis `java -p "$OUT/mods" --describe-module <m> | sed 's/ file:.*//' | grep -v "java.base mandated" | sort`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 1), projet 2 (étapes 1 et 2) et projet 3 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée tes modules** dans `ch12_modules/drills/r01_directives/src/`, un dossier par module (comme au projet 1, en-tête : clic droit sur `Kurse` → **New** → **Directory**, puis **New** → **File** pour chaque `module-info.java` et chaque classe).
3. **Crée ton script** : clic droit sur le dossier `r01_directives` (celui de ce `TODO.md`) → **New** → **File** → `recall.sh`. Recopie l'en-tête donné dans les **Règles**.
4. **Lance-le** depuis le dossier `Kurse`, dans le terminal PowerShell :
   `& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch12_modules/drills/r01_directives/recall.sh`
5. **Bloqué plus de 3 minutes sur un défi ?** Écris `# D03 : ✗` dans ton script, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas, relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

Chaque module a **une** petite classe ; seuls les `module-info` sont l'objet du drill.

- ☐ **D01. `d.base`** : exporte `d.base` (l'interface `Greeter`, avec `String greet(String name)`), et exporte `d.base.hidden` **seulement** à `d.friend` (la classe `Secret`, avec `static String code()` qui rend `"42"`).
  → `qualified exports d.base.hidden to d.friend`
- ☐ **D02. `d.mid`** : lit `d.base` **et le transmet** à ses lecteurs ; exporte `d.mid`. La classe `Polite` a `static String wrap(Greeter g, String name)`, qui rend `"[" + g.greet(name) + "]"`.
  → `requires d.base transitive`
- ☐ **D03. `d.friend`** : requiert `d.mid`, exporte `d.friend`, et **ouvre** `d.friend` à **tous**. La classe `Friend` a `static String secret()`, qui rend `"secret " + Secret.code()`.
  → `opens d.friend`
- ☐ **D04. `d.plugin`** : requiert `d.base`. Il **fournit** `Greeter` avec `d.plugin.Hello` (`"Bonjour " + name`), et n'exporte rien.
  → `provides d.base.Greeter with d.plugin.Hello`
- ☐ **D05. `d.app`** : requiert `d.friend` et `d.mid`, et **utilise** le service `Greeter`. Le `main` affiche `Polite.wrap(<premier Greeter de ServiceLoader>, "Ada") + " " + Friend.secret()`.
  → `[Bonjour Ada] secret 42`
- ☐ **D06. `d.open`** : un **module ouvert**, qui exporte `d.open` (la classe `Box`, avec `private int value`).
  → `exports d.open`
- ☐ **D07. `d.extra`** : dépend de `d.open` **seulement à la compilation** (dépendance optionnelle). Son `main` affiche `"d.open present " + ModuleLayer.boot().findModule("d.open").isPresent()`. À la fin du script :
  1. `echo "--- D07 requires static"` ;
  2. lance `d.extra/d.extra.Main` ;
  3. relance-le avec `--add-modules d.open`.
  → `requires d.open static`, puis `d.open present false`, puis `d.open present true`

## Expériences (hors sortie attendue)

1. Dans `d.open`, ajoute `opens d.open;` : quelle erreur ?
2. Retire `transitive` dans `d.mid` : quel module ne compile plus, et pourquoi ?
3. Exporte un paquet qui n'existe pas (`exports d.base.nope;`) : quelle erreur ?
4. Écris deux fois `requires d.mid;` : quelle erreur ? Et `requires d.mid;` avec `requires transitive d.mid;` ?
5. Les noms de modules : lesquels compilent ? `module 2fast { }`, `module my-app { }`, `module my_app { }`, `module com.Example.App { }`, `module java.mine { }`.
6. Dans D07, pourquoi `d.open` est-il absent au premier lancement, alors qu'il est sur le module path ?

## Sortie attendue complète

```
[Bonjour Ada] secret 42
--- d.base
d.base
exports d.base
qualified exports d.base.hidden to d.friend
--- d.mid
d.mid
exports d.mid
requires d.base transitive
--- d.friend
d.friend
exports d.friend
opens d.friend
requires d.mid
--- d.plugin
contains d.plugin
d.plugin
provides d.base.Greeter with d.plugin.Hello
requires d.base
--- d.app
contains d.app
d.app
requires d.friend
requires d.mid
uses d.base.Greeter
--- d.open
d.open
exports d.open
--- d.extra
contains d.extra
d.extra
requires d.open static
--- D07 requires static
d.open present false
d.open present true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Directive | Effet |
|---|---|
| `exports p;` | les types publics de p sont accessibles à tous (compilation et exécution) |
| `exports p to m1, m2;` | idem, mais seulement pour m1 et m2 |
| `requires m;` | ce module lit m |
| `requires transitive m;` | et tous ceux qui me lisent lisent aussi m |
| `requires static m;` | m est requis à la **compilation**, mais facultatif à l'exécution (pas résolu, sauf si un autre module le requiert, ou `--add-modules`) |
| `opens p;` / `opens p to m;` | réflexion profonde (membres privés) à l'exécution seulement |
| `open module x { }` | tous les paquets sont ouverts (`opens` y est alors interdit) |
| `uses I;` | ce module recherche des fournisseurs de I (`ServiceLoader`) |
| `provides I with C1, C2;` | ce module fournit I |

- `java.base` est toujours lu : `requires java.base mandated`.
- **Nom de module :** des identifiants Java séparés par des points (pas de `-`, pas de chiffre en tête de segment), souvent le nom du paquet principal. Les noms `java.*` sont réservés au JDK.
- **Un module ne peut pas requérir deux fois le même module** (même avec et sans `transitive`).
- `--describe-module` affiche `contains p` pour un paquet ni exporté ni ouvert.
- **L'ordre des lignes** de `--describe-module` n'est pas garanti, d'où le `sort`.

</details>
