# Lab 07 - Forcer les portes depuis la ligne de commande : --add-exports, --add-opens, --add-modules (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.
Prerequis : Lab01 (exports) et Lab06 (opens).

## Le probleme, explique comme a un tout petit enfant

Parfois on ne PEUT PAS modifier un `module-info.java` (une bibliotheque
d'un autre, un vieux code qu'on migre...). On donne alors les
autorisations de l'EXTERIEUR, sur la ligne de commande :

| Dans module-info | Sur la ligne de commande | Pour qui |
|---|---|---|
| `exports p to m` | `--add-exports module/p=m` | `javac` ET `java` (a repeter pour les deux !) |
| `opens p to m` | `--add-opens module/p=m` | `java` seulement (reflexion a l'execution) |
| `requires m` (d'un module racine) | `--add-modules m` | `javac` et `java` : ajoute m au graphe |

Dans `src/` (a NE PAS modifier) :

- `vault.core` exporte `com.example.vault.api` (classe `Vault`, champ PRIVE `code`),
  mais PAS `com.example.vault.internal` (classe `Secrets`).
- `vault.audit` exporte `com.example.vault.audit`, mais **personne ne le requiert**.
- `vault.inspector` (`requires vault.core`) fait 3 choses interdites :
  appeler `Secrets.hint()`, lire `code` par reflexion, charger `Audit` par `Class.forName`.

## A faire : les 4 variables de `exercise/flags.sh`

Lance `./run.sh` apres chaque TODO : l'erreur suivante apparait, a toi
de lire le message et de trouver l'option. Messages REELS, dans l'ordre :

1. **TODO 1** (`COMPILE_FLAGS`) - a la compilation :
   `error: package com.example.vault.internal is not visible`
2. **TODO 2** (`RUN_EXPORTS`) - a l'execution, meme si ca a compile :
   `IllegalAccessError: class ...Main (in module vault.inspector) cannot access class ...Secrets (in module vault.core) because module vault.core does not export com.example.vault.internal to module vault.inspector`
3. **TODO 3** (`RUN_OPENS`) :
   `InaccessibleObjectException: Unable to make field private final java.lang.String com.example.vault.api.Vault.code accessible: module vault.core does not "opens com.example.vault.api" to module vault.inspector`
4. **TODO 4** (`RUN_ADD_MODULES`) :
   `ClassNotFoundException: com.example.vault.audit.Audit` (le module n'est pas dans le graphe)

Sortie attendue :

```
indice : 12**
code : 1234
audit OK
```

## Ce qu'on remarque

- `exports` (ou `--add-exports`) suffit pour les membres PUBLICS ; la
  reflexion sur un membre PRIVE demande `opens` (ou `--add-opens`),
  meme si le package est deja exporte.
- `javac` et `java` sont deux programmes : une option de compilation
  ne "reste" pas pour l'execution.
- Un module present sur le module-path n'est charge que si quelqu'un le
  requiert, ou s'il est ajoute comme racine avec `--add-modules`.

## Indices techniques (a lire seulement si bloque)

- Forme generale : `--add-exports <module-source>/<package>=<module-lecteur>` (idem `--add-opens`).
- `--add-exports vault.core/com.example.vault.internal=vault.inspector`
- `--add-opens vault.core/com.example.vault.api=vault.inspector`
- `--add-modules vault.audit`
- `ALL-UNNAMED` a la place du module lecteur ouvre l'acces au code du classpath.
