# Lab 07 - Les 3 types de modules : named, automatic, unnamed (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

Pas de TODO a corriger ce coup-ci : ce lab fait tourner LA MEME petite
bibliotheque (`Calc.square(n)`) de 3 facons differentes, pour observer
comment Java la classe a chaque fois. Essaie de repondre AVANT de
lancer `./run.sh`.

## Le probleme, explique comme a un tout petit enfant

Un meme fichier `.jar`, SANS AUCUNE MODIFICATION, peut se comporter de
3 facons totalement differentes selon COMMENT tu le lances - un peu
comme la meme personne qui se comporte differemment selon qu'elle
rentre par la porte de service (`-cp`, le classpath) ou par la porte
d'honneur (`--module-path`).

- **Unnamed module** : le jar est mis sur le classpath (`-cp`
  /`-classpath`). Peu importe qu'il ait un `module-info.class` a
  l'interieur ou pas - sur le classpath, tout devient un gros paquet
  "sans nom", comme au bon vieux temps d'avant Java 9.
- **Automatic module** : le jar N'A PAS de `module-info.class`, mais il
  est place sur le `--module-path`. Java lui invente alors un nom de
  module automatiquement (a partir du nom du fichier .jar), et -
  detail important - lui accorde AUTOMATIQUEMENT `exports` sur TOUS
  ses packages (contrairement a un module nomme, qui doit exporter
  explicitement).
- **Named module** : le jar A un `module-info.class` (compile depuis un
  vrai `module-info.java`), et il est sur le `--module-path`. C'est le
  cas "normal" de tous les labs precedents.

## Ce qu'il y a dans ce lab

- `lib-src/` : le code source de `Calc`, SANS module-info.
- `lib-src-named/` : EXACTEMENT le meme code, mais avec un
  `module-info.java` cette fois.
- `classpath-app/AppMain.java` : une app SANS module-info, pour le
  scenario classpath.
- `modpath-app-src/app/` : une app AVEC module-info (`requires
  mathutils;`, rien d'autre), reutilisee pour les 2 scenarios
  module-path.

## A faire (predire AVANT de lancer)

1. Le jar `mathutils-1.0.jar` (SANS module-info) est place sur le
   `--module-path`. D'apres toi, quel NOM de module Java va-t-il lui
   donner automatiquement ? (indice : a partir du nom de fichier,
   sans l'extension `.jar` ni le numero de version)
2. Le module `app` (`requires mathutils;`, RIEN d'autre - pas de
   `exports`) va-t-il pouvoir utiliser `Calc` dans les 2 scenarios
   module-path (automatic ET named) ? Pourquoi le scenario "automatic"
   fonctionne-t-il MEME SANS export explicite cote bibliotheque ?
3. Lance `./run.sh` et compare avec tes reponses.

## Ce qu'on remarque

`java --list-modules --module-path <dossier>` est l'outil le plus sur
pour DIAGNOSTIQUER le type d'un module : un module automatic apparait
avec le mot `automatic` explicitement accole a son nom dans la sortie,
un module nomme normalement n'a JAMAIS ce mot.

## Indices techniques (a lire seulement si bloque)

- Nom automatique derive de `mathutils-1.0.jar` : `mathutils` (le
  numero de version `-1.0` est reconnu et retire ; un tiret restant
  `-` dans le nom serait converti en point `.`).
- `jar --describe-module --file un-jar-sans-module-info.jar` fonctionne
  MEME sur un jar non modulaire : il derive et affiche l'automatic
  module qu'il DEVIENDRAIT s'il etait place sur le module-path.
- Un module automatic exporte TOUJOURS TOUT (aucune notion de
  `exports` partiel possible pour lui) - c'est un choix delibere du
  JPMS pour rester compatible avec d'anciennes bibliotheques non
  modularisees.
