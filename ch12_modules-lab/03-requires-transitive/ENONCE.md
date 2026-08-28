# Lab 03 - `requires transitive` (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

## Le probleme, explique comme a un tout petit enfant

Imagine une chaine de 3 amis : Alice (`geo.app`) fait confiance a Bob
(`geo.calculator`), et Bob fait confiance a Charlie (`geo.units`). Bob
utilise les outils de Charlie EN COULISSES pour rendre service a Alice -
mais si le SERVICE que Bob rend a Alice consiste a lui REMETTRE EN MAIN
PROPRE un objet fabrique par Charlie (`Distance`), Alice doit ELLE AUSSI
pouvoir reconnaitre et manipuler cet objet. Un simple `requires` de Bob
envers Charlie ne fait confiance qu'a BOB - Alice, elle, ne connait
toujours pas Charlie. `requires transitive` change ca : Bob dit
"quiconque me fait confiance (`requires geo.calculator`) doit AUSSI
automatiquement faire confiance a Charlie, je m'en porte garant."

## Ce qu'il y a dans ce lab

- `geo.units` exporte une classe `Distance`.
- `geo.calculator` a une methode `Calculator.sum(Distance, Distance)`
  qui RENVOIE un `Distance` - son API publique EXPOSE donc le type de
  `geo.units`, meme si `geo.calculator` ne fait que `requires geo.units`
  (pas encore transitive).
- `geo.app` fait `requires geo.calculator` UNIQUEMENT (pas
  `geo.units`), et essaie d'utiliser directement le `Distance` renvoye.

## A faire

1. Lance `./run.sh`. Lis le message d'erreur - il est LEGEREMENT
   different de Lab01/Lab02 : ce n'est plus "n'exporte pas", mais
   "le module geo.app NE LIT PAS geo.units" (`does not read it`).
2. Modifie `exercise/src/geo.calculator/module-info.java` pour que
   `requires geo.units;` devienne `requires transitive geo.units;`.
3. Relance `./run.sh` - `geo.app` doit maintenant compiler et afficher
   "Distance totale : 7.0 m", SANS jamais avoir ecrit lui-meme
   `requires geo.units;`.

## Ce qu'on remarque

`requires transitive X` cree une regle "si tu me requires, tu requires
AUSSI X automatiquement" - c'est exactement pour ca que le chapitre
insiste : "requires transitive doit etre utilise quand TOUS les
modules qui requierent un module doivent toujours requerir l'autre
aussi." C'est le cas ici : impossible d'utiliser `Calculator.sum()`
utilement sans jamais toucher a un `Distance`.

## Indices techniques (a lire seulement si bloque)

- Ligne corrigee dans `geo.calculator/module-info.java` :
  `requires transitive geo.units;`
- Message d'erreur exact AVANT correction :
  `package com.example.geo.units is not visible (package com.example.geo.units is declared in module geo.units, but module geo.app does not read it)`
  - remarque : ce n'est PAS "does not export it" comme dans Lab01/02
    (geo.units exporte bien son package a tout le monde), mais "geo.app
    does not read it" - geo.app n'a simplement AUCUN lien de lecture
    vers geo.units, ni direct ni transitif, tant que le TODO n'est pas
    fait.
