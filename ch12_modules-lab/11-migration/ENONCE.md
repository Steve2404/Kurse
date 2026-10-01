# Lab 11 (CAPSTONE) - Migrer un vrai projet : bottom-up, top-down, puis tout nomme (niveau : capstone)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.
Prerequis : tous les labs precedents, surtout Lab03 (transitive), Lab07 (--add-modules) et Lab09 (automatic).

## Le probleme, explique comme a un tout petit enfant

Un projet d'avant Java 9 : 3 jars SANS module-info, sur le classpath.

```
core-1.0.jar        (com.example.core.Money)         aucune dependance
text-utils-2.1.jar  (com.example.text.Formatter)     depend de core
app-3.0.jar         (com.example.app.Main)           depend de text-utils ET de core
```

On ne peut pas tout migrer d'un coup. Deux strategies de l'examen :

- **Bottom-up** : on commence par le BAS (le jar sans dependance, `core`).
  Il devient un module NOMME sur le module-path ; les autres restent sur
  le classpath (unnamed module), qui lit tous les modules... a condition
  qu'ils soient dans le graphe.
- **Top-down** : on commence par le HAUT (`app`). Il devient un module
  NOMME ; les jars pas encore migres sont mis sur le module-path tels
  quels et deviennent des modules AUTOMATIQUES (nom tire du fichier).

Et la fin de la migration : les 3 modules sont nommes.

`run.sh` construit les jars d'origine, puis joue les 3 etapes avec TES
fichiers et verifie `Total : 12,50 EUR` a chaque fois.

## A faire

1. **TODO 1** `exercise/core-module-info.java` : ce que `core` doit declarer pour que le code du classpath l'utilise.
2. **TODO 2** `exercise/flags.sh` : `BOTTOM_UP_FLAGS`, l'option qui fait entrer `core` dans le graphe
   alors qu'aucun module ne le requiert (sans elle : `package com.example.core does not exist`).
3. **TODO 3** `exercise/app-module-info.java` : les `requires` de `app`, avec les noms AUTOMATIQUES
   des jars (`core-1.0.jar` -> ?, `text-utils-2.1.jar` -> ?).
4. **TODO 4** `exercise/text-utils-module-info.java` : le module `text.utils` quand tout est nomme.
   Attention : `Formatter.euros(Money)` fait apparaitre un type de `core` dans son API publique.

## Ce qu'on remarque

- Bottom-up : chaque module migre ne depend que de modules DEJA migres.
  Il faut juste `--add-modules` pour que le classpath voie les modules nommes.
- Top-down : les automatic modules absorbent le retard ; mais leurs noms
  dependent des NOMS DE FICHIERS (renommer un jar casse les `requires` !).
- Verifie en direct : en top-down, `requires text.utils;` suffirait (un
  module automatique donne acces a tous les autres modules automatiques).
  Une fois tout nomme, cette aide disparait : `requires transitive core`
  dans `text.utils` (ou `requires core` dans `app`) devient necessaire.
- Sur cette machine, les messages de `java` (pas ceux de `javac`) sont
  dans la langue du systeme (ex. `Hauptklasse ... konnte nicht gefunden
  werden` = "main class not found") : c'est normal.

## Indices techniques (a lire seulement si bloque)

- `exports com.example.core;`
- `BOTTOM_UP_FLAGS="--add-modules core"`
- Noms automatiques : `core-1.0.jar` -> `core` ; `text-utils-2.1.jar` -> `text.utils`.
- `module text.utils { requires transitive core; exports com.example.text; }`
