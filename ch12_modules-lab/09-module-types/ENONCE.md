# Lab 09 - Les 3 types de modules : named, automatic, unnamed (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

## Le probleme, explique comme a un tout petit enfant

Un meme fichier `.jar`, SANS AUCUNE MODIFICATION, peut se comporter de
3 facons totalement differentes selon COMMENT tu le lances - un peu
comme la meme personne qui se comporte differemment selon qu'elle
rentre par la porte de service (`-cp`, le classpath) ou par la porte
d'honneur (`--module-path`).

| Type | Ou est le jar ? | module-info ? | Nom du module | Exporte quoi ? |
|---|---|---|---|---|
| **unnamed** | classpath (`-cp`) | peu importe (ignore) | aucun | tout, mais seul le code du classpath le lit |
| **automatic** | module-path (`-p`) | NON | derive du nom du fichier | TOUT, d'office |
| **named** | module-path (`-p`) | OUI | celui du module-info | seulement ses `exports` |

Regle du nom automatique : on retire `.jar`, puis la version finale
(`-1.0`, `-2.3.1`), puis tout caractere non alphanumerique devient un
point. `mathutils-1.0.jar` -> `mathutils` ; `my-lib_x-2.0.jar` -> `my.lib.x`.

## Ce qu'il y a dans ce lab

- `lib-src/` : le code de `Calc.square(n)`, SANS module-info.
- `classpath-app/AppMain.java` : une app SANS module-info (scenario classpath).
- `modpath-app-src/app/` : une app AVEC module-info (`requires mathutils;`), pour les 2 scenarios module-path.
- `exercise/mathutils-module-info.java` et `exercise/launch.sh` : **tes 5 TODO**.
- `run.sh` construit tout (jars, dossiers) puis appelle tes fonctions et verifie.

## A faire

### TODO 1 - `exercise/mathutils-module-info.java`

Le module nomme `mathutils` doit permettre au module `app` d'utiliser
`com.example.mathutils`. Lance `./run.sh` AVANT : lis l'erreur de
compilation du scenario named. Remarque que le scenario automatic, lui,
n'a besoin de rien : un automatic module exporte tout.

### TODO 2 a 4 - `exercise/launch.sh`

Ecris dans chaque fonction UNE commande `java` (les dossiers sont
decrits en tete du fichier ; `$SEP` est le separateur de chemins,
`:` sous Linux/Mac et `;` sous Windows) :

- `run_unnamed` : le jar ET `AppMain` sur le **classpath** -> `Resultat (classpath) : 36`.
- `run_automatic` : le jar SANS module-info ET le module `app` sur le **module-path**, lancer `app/com.example.app.Main` -> `Resultat (module) : 36`.
- `run_named` : pareil avec le jar AVEC module-info.

### TODO 5 - `exercise/launch.sh`

`AUTOMATIC_NAME` : le nom de module que Java donnera a un jar nomme
`string-tools-2.3.1.jar`. `run.sh` le compare a ce que dit VRAIMENT
`jar --describe-module --file string-tools-2.3.1.jar`.

## Ce qu'on remarque

`java --list-modules --module-path <dossier>` est l'outil le plus sur
pour DIAGNOSTIQUER le type d'un module : un module automatic apparait
avec le mot `automatic` accole a son nom, un module nomme JAMAIS.

## Indices techniques (a lire seulement si bloque)

- Classpath : `java -cp "a.jar${SEP}dossier" NomDeClasse`
- Module-path : `java --module-path "dossier1${SEP}dossier2" --module module/paquet.Classe`
  (formes courtes : `-p` et `-m`)
- `jar --describe-module --file x.jar` marche MEME sur un jar sans
  module-info : il affiche l'automatic module qu'il DEVIENDRAIT
  (`No module descriptor found. Derived automatic module.` puis `nom@version automatic`).
- Un module automatic exporte TOUJOURS TOUT, et lit tous les autres modules.
