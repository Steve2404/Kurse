# Drill 01 - Les directives de module-info.java

Un exercice (lab) t'APPREND une notion. Un drill te la fait REPETER
jusqu'a ce qu'elle sorte toute seule. Ici : 8 petits scenarios, chacun
avec UN trou dans UN `module-info.java`, sur la bibliotheque du
chapitre 10 decoupee en 3 modules (`library.model` -> `Book`,
`library.service` -> `Catalog` + `internal.InMemoryCatalog`, `library.app` -> `Main`).

1. Chronometre-toi, note ton temps et ton score dans `REVISION.md`.
2. Remplis les trous SANS regarder la carte memoire. Bloque plus d'une
   minute : regarde-la, cache-la, reecris.
3. Verifie : `./run.sh exercise 01` (depuis `drills/`).

## Les TODO (un dossier par TODO, dans `exercise/`)

| TODO | Dossier | Fichier | Forme visee |
|---|---|---|---|
| 1 | `01_exports` | `library.model/module-info.java` | `exports` |
| 2 | `02_requires` | `library.app/module-info.java` | `requires` |
| 3 | `03_requires_transitive` | `library.service/module-info.java` | `requires transitive` |
| 4 | `04_exports_to` | `library.service/module-info.java` | `exports ... to` (verifie dans `--describe-module`) |
| 5 | `05_opens` | `library.model/module-info.java` | `opens ... to` |
| 6 | `06_open_module` | `library.model/module-info.java` | `open module` (verifie dans `--describe-module`) |
| 7 | `07_uses` | `library.app/module-info.java` | `uses` |
| 8 | `08_provides` | `library.service/module-info.java` | `provides ... with` |

Remettre a zero : `git restore ch12_modules-lab/drills/Drill01_Directives/exercise`

---

## CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher)

```
[open] module nom.du.module {
    requires autre.module;                       // je lis ce module
    requires transitive autre.module;            // ... et mes lecteurs aussi
    exports mon.package;                         // public pour tous mes lecteurs
    exports mon.package to m1, m2;               // seulement pour m1 et m2
    opens mon.package;                           // reflexion profonde (execution)
    opens mon.package to m1;                     // ... pour m1 seulement
    uses mon.service.Interface;                  // je consomme (ServiceLoader)
    provides mon.service.Interface with ma.Impl; // je fournis
}
```

- Erreurs a reconnaitre : `package X is not visible` (exports ou requires manquant),
  `does not "opens X" to module Y` (opens), `does not declare 'uses'` (uses),
  aucun fournisseur trouve sans erreur (provides).
- `open module` : tout est ouvert, et on ne peut plus ecrire de `opens` dedans.
- Noms de modules : points autorises, comme des packages (`library.model`).
