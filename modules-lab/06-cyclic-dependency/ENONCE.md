# Lab 06 - Les cycles sont INTERDITS (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

## Le probleme, explique comme a un tout petit enfant

Imagine 2 personnes qui refusent chacune de commencer a parler tant que
l'AUTRE n'a pas fini de parler en premier - une attente infinie, sans
issue. C'est exactement ce qu'est un cycle de modules : le module A dit
"je requires B", et B dit "je requires A". Pour construire A, il
faudrait deja avoir fini B ; pour construire B, il faudrait deja avoir
fini A. Le Java Platform Module System REFUSE categoriquement de
compiler une telle situation - pas d'exception, pas de contournement,
c'est une regle stricte et definitive.

## Ce qu'il y a dans ce lab (contrairement aux autres, PAS de TODO a une
ligne)

`exercise/` contient 2 modules construits pour se marcher mutuellement
sur les pieds :

- `orders.processing` a besoin de `ShippingLabel` (de
  `orders.shipping`) pour produire un rapport complet.
- `orders.shipping` a besoin de `OrderStatus` (de
  `orders.processing`) pour la MEME raison, dans l'autre sens.

Chacun `requires` l'autre DIRECTEMENT : un cycle a 2 modules.

## A faire

1. Lance `./run.sh`. La compilation de `exercise/` echoue avec
   "cyclic dependence involving..." - CETTE FOIS, il n'y a PAS de
   ligne unique a corriger : le probleme est dans la CONCEPTION meme
   des 2 modules.
2. Reflechis (avant de regarder `solution/`) : pourquoi ces 2 modules
   avaient-ils VRAIMENT besoin l'un de l'autre ? Est-ce qu'ils
   n'auraient pas plutot besoin, tous les deux, d'un TROISIEME
   ingredient commun ?
3. Regarde `solution/` : le cycle est casse en extrayant un module
   `orders.common` (avec juste un `OrderId`, l'ingredient partage),
   dont `orders.processing` ET `orders.shipping` dependent DESORMAIS
   chacun independamment, SANS jamais dependre l'un de l'autre. Un
   4e module, `orders.app`, joue le role du COORDINATEUR qui, lui,
   requires les deux pour produire le rapport complet - au lieu que
   les deux modules metier se coordonnent entre eux directement.

## Ce qu'on remarque

Un cycle de modules est SOUVENT le signe que 2 modules partagent en
realite un INGREDIENT COMMUN qui merite sa propre place (un 3e module),
ou qu'il manque un COORDINATEUR au-dessus d'eux plutot que de les
laisser se referencer mutuellement. C'est une lecon d'architecture
autant qu'une regle du compilateur.

## Indices techniques (a lire seulement si bloque)

- Message d'erreur exact :
  `cyclic dependence involving orders.shipping` (et symetriquement
  pour orders.processing) - le compilateur refuse de choisir un ordre
  de compilation, puisqu'aucun ordre valide n'existe.
- La solution proposee n'est PAS la seule possible : fusionner les 2
  modules en un seul aurait aussi "casse" le cycle (il n'y a plus 2
  entites separees a faire pointer l'une vers l'autre) - mais ca
  perdrait la separation des responsabilites. Extraire un module
  commun est generalement le choix le plus propre.
