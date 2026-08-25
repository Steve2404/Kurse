# Lab 05 - `opens` : autoriser la reflexion profonde (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

## Le probleme, explique comme a un tout petit enfant

`exports` ouvre la porte D'ENTREE d'une piece (tu peux utiliser les
classes PUBLIQUES normalement, comme d'habitude). Mais certains outils
(frameworks de serialisation type Gson/Jackson, frameworks d'injection
de dependances...) veulent aller BIEN PLUS LOIN : ouvrir un TIROIR
FERME A CLE a l'interieur meme de cette piece, et lire des choses
PRIVEES (`private String name`) que meme le proprietaire de la maison
n'exposait a personne. `exports` ne donne JAMAIS ce droit - il faut la
permission SUPPLEMENTAIRE et EXPLICITE `opens`.

## Ce qu'il y a dans ce lab

- `model.entities` exporte normalement `Person` (constructeur et
  `getName()` publics), mais son champ `name` est `private`.
- `reflect.tool` n'utilise PAS `getName()` - il simule un framework de
  serialisation et lit `name` directement par reflexion
  (`Field.setAccessible(true)` puis `field.get(person)`).

## A faire

1. Lance `./run.sh`. La compilation reussit (comme au Lab04) - c'est
   encore a L'EXECUTION que ca casse, avec une
   `InaccessibleObjectException` tres explicite.
2. Ajoute la ligne manquante dans
   `exercise/src/model.entities/module-info.java`, en l'ouvrant
   SPECIFIQUEMENT a `reflect.tool` (comme un export qualifie du Lab02,
   mais avec `opens ... to ...`).
3. Relance `./run.sh` - la lecture par reflexion doit maintenant
   reussir.

## Ce qu'on remarque

`exports` et `opens` repondent a 2 besoins differents et peuvent tous
les deux etre qualifies (`to module`) ou non :

- `exports pkg;` -> acces normal, COMPILE-TIME, aux membres PUBLICS
  uniquement.
- `opens pkg;` -> acces par REFLEXION PROFONDE (y compris aux membres
  PRIVES), verifie SEULEMENT a L'EXECUTION, jamais a la compilation.

Un module peut tres bien `opens` un package SANS l'`exports` (utile
pour un framework qui inspecte des classes que personne d'autre ne
doit utiliser normalement), ou l'inverse (ce lab).

## Indices techniques (a lire seulement si bloque)

- Ligne a ajouter : `opens com.example.model.entities to reflect.tool;`
- Message d'erreur exact AVANT correction :
  `InaccessibleObjectException: ... module model.entities does not "opens com.example.model.entities" to module reflect.tool`
- `module ... { opens pkg; }` (sans `to`) ouvrirait la reflexion sur ce
  package a TOUT LE MONDE, exactement comme `exports` sans `to`.
