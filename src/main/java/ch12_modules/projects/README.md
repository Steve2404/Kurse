# Chapitre 12 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, la règle du crescendo, et **où mettre tes modules**) est décrit dans `../PARCOURS.md`.

Dans chaque dossier de projet, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Check.java` : le correcteur ;
- `solution/build.sh` : le script de la correction.

Les modules de la correction sont dans `ch12_modules/<projet>/solution/`, à la racine du dépôt.

**Tout le reste, c'est toi qui le crées :**
- tes modules, dans `ch12_modules/<projet>/` ;
- ton `build.sh`, à côté du `TODO.md`.

| ☐ | Projet | Notions | Ce qui est dur |
|---|---|---|---|
| ☐ | `p01_library` — bibliothèque en 3 modules | `exports`, `requires transitive`, export qualifié, `javac`/`java`/`jar`, `--describe-module`, `Module` à l'exécution | index trié par préfixe, erreur d'accès de l'intrus |
| ☐ | `p02_pricing` — caisse à remises | services : `uses`, `provides … with`, `provider()`, `ServiceLoader.stream()`, `--limit-modules`, `--show-module-resolution` | meilleure combinaison de remises par masque de bits |
| ☐ | `p03_config` — configuration par réflexion | `opens` qualifié, `open module`, `--add-exports`, `--add-opens`, les 3 erreurs d'accès | binder par réflexion, dump récursif |
| ☐ | `p04_migration` — migration | modules nommés, automatiques, sans nom, `Automatic-Module-Name`, cycles, `jdeps` | slugs uniques ; lire les noms déduits |
| ☐ | `p05_runtime` — image d'exécution | `jdeps -s -R`, `--print-module-deps`, `jlink` (lanceur), `--module-version`, `ModuleLayer` | sac à dos 0/1, ordre topologique des modules |
| ☐ | `p06_events` — **capstone** plateforme d'événements | tout : 8 modules, service, opens, export qualifié, module automatique, `jlink` qui refuse | allocation de salles à deux tas, haversine |
