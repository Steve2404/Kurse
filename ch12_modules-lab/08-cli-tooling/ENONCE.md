# Lab 08 - Tour des outils en ligne de commande (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

Pas de TODO ici non plus : ce lab reutilise les modules `greeting.api`/
`greeting.app` (comme au Lab01, deja corriges) pour faire tourner, dans
l'ordre, les 4 commandes citees par les Exam Essentials du chapitre.
Essaie de PREDIRE ce que chaque commande va afficher AVANT de lancer
`./run.sh`.

## Les 4 commandes, en une phrase chacune

- `java --describe-module` : decrit un module DEJA COMPILE (requires,
  exports, packages contenus) - utile pour verifier ce qu'un module
  expose reellement.
- `jar --describe-module --file x.jar` : la MEME chose, mais lue
  directement DANS un fichier `.jar` (fonctionne meme sur un jar SANS
  module-info - voir Lab07, il derive alors un automatic module).
- `jdeps` : analyse les VRAIES dependances d'un module compile, package
  par package, jusqu'a citer les packages exacts du JDK utilises (tres
  utile pour preparer une migration : "de quoi ce code a-t-il
  reellement besoin ?").
- `jlink` : fabrique une image d'execution Java SUR MESURE, qui ne
  contient QUE les modules necessaires a ton application (pas tout le
  JDK) - plus petite, plus rapide a demarrer, ideale pour la
  distribution.

## A faire (predire AVANT de lancer)

1. `java --describe-module` sur `greeting.app` : va-t-il lister
   `greeting.api` ? Le module `java.base` va-t-il apparaitre, alors
   que personne ne l'a jamais ecrit dans aucun `module-info.java` de
   ce lab ?
2. `jdeps` sur `greeting.app` : d'apres toi, va-t-il seulement dire
   "greeting.app depend de greeting.api", ou va-t-il descendre jusqu'a
   citer des PACKAGES precis du JDK (comme `java.io`) ?
3. Une fois l'image `jlink` construite : d'apres toi, sera-t-elle plus
   grande ou plus petite qu'un JDK complet ? D'un facteur 2 ? 10 ?
   Lance `./run.sh` pour verifier (la difference est nette).

## Ce qu'on remarque

Ces 4 commandes repondent a des questions COMPLEMENTAIRES : "qu'est-ce
que CE module expose ?" (`--describe-module`), "de quoi ce module
depend-il VRAIMENT, jusqu'au package pres ?" (`jdeps`), et "comment
livrer SEULEMENT ce dont j'ai besoin ?" (`jlink`). Sur l'examen, il
faut surtout savoir QUELLE commande repond a QUELLE question, plus que
memoriser chaque option en detail.

## Indices techniques (a lire seulement si bloque)

- `java --module-path mods --describe-module greeting.app`
- `jar --describe-module --file jars/greeting.api.jar`
- `jdeps --module-path mods -m greeting.app`
- `jlink --module-path mods:$JAVA_HOME/jmods --add-modules greeting.app --output custom-runtime`
  (le `$JAVA_HOME/jmods` est indispensable : jlink a besoin des
  modules du JDK lui-meme, en plus des notres, pour construire une
  image complete et autonome)
