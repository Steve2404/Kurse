# Lab 10 - Les outils en ligne de commande : java, jar, jdeps, jlink (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

## Le probleme, explique comme a un tout petit enfant

Les modules `greeting.api` / `greeting.app` (ceux du Lab01, deja
corriges, dans `src/`) sont compiles par `run.sh` dans `build/.../mods`.
Toi, tu es le technicien : pour chaque question, tu dois savoir QUEL
outil repondre et avec QUELLES options.

| Question | Outil |
|---|---|
| Qu'est-ce que ce module compile declare ? | `java --describe-module` (`-d`) |
| Qu'est-ce que ce jar contient comme module ? | `jar --describe-module --file` (`-d -f`) |
| De quoi ce module depend-il VRAIMENT, jusqu'au package ? | `jdeps` |
| Comment livrer SEULEMENT ce dont j'ai besoin ? | `jlink` |
| Quels modules ont ete charges, et a cause de qui ? | `java --show-module-resolution` |
| Quels modules existent ? | `java --list-modules` |

## A faire : les 10 TODO de `exercise/tools.sh`

Chaque fonction contient UNE commande. `run.sh` les appelle dans
l'ordre et verifie leur sortie (les fonctions suivantes utilisent les
jars et l'image fabriques par les precedentes).

1. `describe_app` : decrire `greeting.app` (on doit voir `requires greeting.api` et `requires java.base mandated`).
2. `package_api` : jar `$B/jars/greeting.api.jar`, version de module `1.0`.
3. `describe_jar` : decrire le module de ce jar (on doit voir `greeting.api@1.0`).
4. `package_app` : jar `$B/jars/greeting.app.jar` avec la classe principale inscrite.
5. `run_from_jars` : lancer depuis `$B/jars` avec SEULEMENT le nom du module.
6. `deps` : `jdeps` sur `greeting.app`.
7. `build_runtime` : `jlink` -> `$B/custom-runtime` (penser aux modules du JDK : `$JMODS`).
8. `run_runtime` : lancer le `java` de l'image, sans module-path.
9. `show_resolution` : lancer en affichant la resolution des modules.
10. `runtime_modules` : lister les modules de l'image (3 seulement !).

## Ce qu'on remarque

- `java.base` apparait partout (`mandated`) alors que personne ne l'a ecrit.
- Grace a `--main-class`, `java -p jars -m greeting.app` suffit.
- L'image jlink ne contient que 3 modules et pese plusieurs fois moins
  qu'un JDK complet ; elle n'a plus besoin de `--module-path`.
- Sur l'examen, il faut surtout savoir QUELLE commande repond a QUELLE
  question, et les formes courtes : `-p` (module-path), `-m` (module),
  `-d` (describe-module), `-e` (main-class pour `jar`), `-c -f` (create, file).

## Indices techniques (a lire seulement si bloque)

- `java --module-path "$B/mods" --describe-module greeting.app`
- `jar --create --file "$B/jars/x.jar" --module-version 1.0 -C "$B/mods/greeting.api" .`
- `jar --describe-module --file "$B/jars/x.jar"`
- `jar --create --file ... --main-class com.example.greeting.app.Main -C ... .`
- `java -p "$B/jars" -m greeting.app`
- `jdeps --module-path "$B/mods" -m greeting.app`
- `jlink --module-path "$B/mods${SEP}$JMODS" --add-modules greeting.app --output "$B/custom-runtime"`
- `"$B/custom-runtime/bin/java" -m greeting.app/com.example.greeting.app.Main`
- `java -p "$B/mods" --show-module-resolution -m greeting.app/com.example.greeting.app.Main`
- `"$B/custom-runtime/bin/java" --list-modules`
